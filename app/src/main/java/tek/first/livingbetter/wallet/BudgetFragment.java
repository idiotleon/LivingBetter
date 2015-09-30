package tek.first.livingbetter.wallet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import tek.first.livingbetter.R;
import tek.first.livingbetter.helper.DatabaseHelper;


public class BudgetFragment extends Fragment {
    private EditText entertainment_et_fragment_budget;
    private EditText food_et_fragment_budget;
    private EditText shopping_et_fragment_budget;
    private TextView totalnumber_tv_fragment_budget;
    private int value_entertainment;
    private int value_food;
    private int value_shopping;
    private int value_total;
    private Button save_btn_budget;
    private TextView activity_tv_fragment_budget;
    private EditText activity_et_fragment_budget;
    private Button edit_btn_budget;
    private MyProgressBar progressBar;
    private DatabaseHelper dataHelper;
    float total;
    Date date;
    int year;
    int month;
    float current;
    String total_budget;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        shopping_et_fragment_budget = (EditText) view.findViewById(R.id.shopping_et_fragment_budget);
        entertainment_et_fragment_budget = (EditText) view.findViewById(R.id.entertainment_et_fragment_budget);
        food_et_fragment_budget = (EditText) view.findViewById(R.id.food_et_fragment_budget);
        progressBar = (MyProgressBar) view.findViewById(R.id.budget_pb);
        dataHelper = new DatabaseHelper(getActivity());

        save_btn_budget = (Button) view.findViewById(R.id.save_btn_budget);
        edit_btn_budget = (Button) view.findViewById(R.id.edit_btn_budget);

        loadSavedPreferences();
        initialProgressBar();

        edit_btn_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unlock();
                save_btn_budget.setVisibility(View.VISIBLE);
                edit_btn_budget.setVisibility(View.GONE);
                return;
            }
        });

        if (shopping_et_fragment_budget.getText().toString().trim().equals("") && entertainment_et_fragment_budget.getText().toString().trim().equals("") && food_et_fragment_budget.getText().toString().trim().equals("")) {
            unlock();
            edit_btn_budget.setVisibility(View.GONE);
            save_btn_budget.setVisibility(View.VISIBLE);
        } else {
            lock();
        }

        save_btn_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (entertainment_et_fragment_budget.getText().toString().trim().equals(""))
                        value_entertainment = 0;
                    else
                        value_entertainment = Integer.parseInt(entertainment_et_fragment_budget.getText().toString());
                    if (food_et_fragment_budget.getText().toString().trim().equals(""))
                        value_food = 0;
                    else
                        value_food = Integer.parseInt(food_et_fragment_budget.getText().toString());
                    if (shopping_et_fragment_budget.getText().toString().trim().equals(""))
                        value_shopping = 0;
                    else
                        value_shopping = Integer.parseInt(shopping_et_fragment_budget.getText().toString());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                value_total = value_entertainment + value_food + value_shopping;
                total_budget = String.valueOf(value_total);

                savePreferences("storeShopping", shopping_et_fragment_budget.getText().toString());
                savePreferences("storeEntertainment", entertainment_et_fragment_budget.getText().toString());
                savePreferences("storeFood", food_et_fragment_budget.getText().toString());
                savePreferences("storeTotal", total_budget);
                savePreferences("saveBudget", save_btn_budget.getVisibility());
                savePreferences("editBudget", edit_btn_budget.getVisibility());

                lock();
                total = value_total;
                int process = (int) ((current / total) * 100);
                if (process > 70 && process < 100) {
                    progressBar.warningText(Color.YELLOW);
                } else if (process > 100) {
                    progressBar.warningText(Color.RED);
                } else {
                    progressBar.warningText(Color.GREEN);
                }
                progressBar.setProgress(process, current, total);

                edit_btn_budget.setVisibility(View.VISIBLE);
                save_btn_budget.setVisibility(View.GONE);
                Fragment fragment;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                fragment = WalletFragment.newInstance(2);
                ft.replace(R.id.container, fragment);
                ft.commit();
            }
        });

        return view;
    }


    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name_shopping = sharedPreferences.getString("storeShopping", "");
        String name_entertainment = sharedPreferences.getString("storeEntertainment", "");
        String name_food = sharedPreferences.getString("storeFood", "");
        String name_total = sharedPreferences.getString("storeTotal", "");
        shopping_et_fragment_budget.setText(name_shopping);
        entertainment_et_fragment_budget.setText(name_entertainment);
        food_et_fragment_budget.setText(name_food);

        save_btn_budget.setVisibility(View.GONE);
        edit_btn_budget.setVisibility(View.VISIBLE);
    }

    private void savePreferences(String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void savePreferences(String key, int visibility) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, visibility);
        editor.commit();
    }

    private void unlock() {
        food_et_fragment_budget.setFocusableInTouchMode(true);
        entertainment_et_fragment_budget.setFocusableInTouchMode(true);
        shopping_et_fragment_budget.setFocusableInTouchMode(true);

    }

    public void lock() {
        food_et_fragment_budget.setFocusableInTouchMode(false);
        entertainment_et_fragment_budget.setFocusableInTouchMode(false);
        shopping_et_fragment_budget.setFocusableInTouchMode(false);
        food_et_fragment_budget.clearFocus();
        entertainment_et_fragment_budget.clearFocus();
        shopping_et_fragment_budget.clearFocus();
    }

    public void initialProgressBar() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String name_total = sharedPreferences.getString("storeTotal", "1");
        if (name_total.equals(""))
            name_total += "1";
        total = Float.parseFloat(name_total);
        date = new Date();
        year = date.getYear() + 1900;
        month = date.getMonth();

        current = dataHelper.getCurrentCost(year, month);
        int process = (int) ((current / total) * 100);
        if (process > 70 && process < 100) {
            progressBar.warningText(Color.YELLOW);
        } else if (process > 100) {
            progressBar.warningText(Color.RED);
        } else {
            progressBar.warningText(Color.GREEN);
        }
        progressBar.setProgress(process, current, total);
    }


}
