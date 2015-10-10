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

import java.util.Date;

import tek.first.livingbetter.R;
import tek.first.livingbetter.provider.DatabaseHelper;

public class BudgetFragment extends Fragment {
    private EditText entertainmentEtFragmentBudget;
    private EditText foodEtFragmentBudget;
    private EditText shoppingEtFragmentBudget;
    private int valueEntertainment;
    private int valueFood;
    private int valueShopping;
    private int valueTotal;
    private Button saveBtnBudget;
    private Button editBtnBudget;
    private MyProgressBar progressBar;
    private DatabaseHelper dataHelper;
    private float total;
    private Date date;
    private int year;
    private int month;
    private float current;
    private String totalBudget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wallet_fragment_budget, container, false);

        shoppingEtFragmentBudget = (EditText) view.findViewById(R.id.wallet_shopping_et_fragment_budget);
        entertainmentEtFragmentBudget = (EditText) view.findViewById(R.id.wallet_entertainment_et_fragment_budget);
        foodEtFragmentBudget = (EditText) view.findViewById(R.id.wallet_food_et_fragment_budget);
        progressBar = (MyProgressBar) view.findViewById(R.id.wallet_budget_pb);
        dataHelper = new DatabaseHelper(getActivity());

        saveBtnBudget = (Button) view.findViewById(R.id.wallet_save_btn_budget);
        editBtnBudget = (Button) view.findViewById(R.id.wallet_edit_btn_budget);

        loadSavedPreferences();
        initialProgressBar();

        editBtnBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unlock();
                saveBtnBudget.setVisibility(View.VISIBLE);
                editBtnBudget.setVisibility(View.GONE);
                return;
            }
        });

        if (shoppingEtFragmentBudget.getText().toString().trim().equals("")
                && entertainmentEtFragmentBudget.getText().toString().trim().equals("")
                && foodEtFragmentBudget.getText().toString().trim().equals("")) {
            unlock();
            editBtnBudget.setVisibility(View.GONE);
            saveBtnBudget.setVisibility(View.VISIBLE);
        } else {
            lock();
        }

        saveBtnBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (entertainmentEtFragmentBudget.getText().toString().trim().equals(""))
                        valueEntertainment = 0;
                    else
                        valueEntertainment = Integer.parseInt(entertainmentEtFragmentBudget.getText().toString());
                    if (foodEtFragmentBudget.getText().toString().trim().equals(""))
                        valueFood = 0;
                    else
                        valueFood = Integer.parseInt(foodEtFragmentBudget.getText().toString());
                    if (shoppingEtFragmentBudget.getText().toString().trim().equals(""))
                        valueShopping = 0;
                    else
                        valueShopping = Integer.parseInt(shoppingEtFragmentBudget.getText().toString());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                valueTotal = valueEntertainment + valueFood + valueShopping;
                totalBudget = String.valueOf(valueTotal);

                savePreferences("storeShopping", shoppingEtFragmentBudget.getText().toString());
                savePreferences("storeEntertainment", entertainmentEtFragmentBudget.getText().toString());
                savePreferences("storeFood", foodEtFragmentBudget.getText().toString());
                savePreferences("storeTotal", totalBudget);
                savePreferences("saveBudget", saveBtnBudget.getVisibility());
                savePreferences("editBudget", editBtnBudget.getVisibility());

                lock();
                total = valueTotal;
                int process = (int) ((current / total) * 100);
                if (process > 70 && process < 100) {
                    progressBar.warningText(Color.YELLOW);
                } else if (process > 100) {
                    progressBar.warningText(Color.RED);
                } else {
                    progressBar.warningText(Color.GREEN);
                }
                progressBar.setProgress(process, current, total);

                editBtnBudget.setVisibility(View.VISIBLE);
                saveBtnBudget.setVisibility(View.GONE);
                Fragment fragment;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
/*                fragment = WalletActivity.newInstance(2);
                ft.replace(R.id.container, fragment);
                ft.commit();*/
            }
        });

        return view;
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String nameShopping = sharedPreferences.getString("storeShopping", "");
        String nameEntertainment = sharedPreferences.getString("storeEntertainment", "");
        String nameFood = sharedPreferences.getString("storeFood", "");
        String nameTotal = sharedPreferences.getString("storeTotal", "");
        shoppingEtFragmentBudget.setText(nameShopping);
        entertainmentEtFragmentBudget.setText(nameEntertainment);
        foodEtFragmentBudget.setText(nameFood);

        saveBtnBudget.setVisibility(View.GONE);
        editBtnBudget.setVisibility(View.VISIBLE);
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
        foodEtFragmentBudget.setFocusableInTouchMode(true);
        entertainmentEtFragmentBudget.setFocusableInTouchMode(true);
        shoppingEtFragmentBudget.setFocusableInTouchMode(true);

    }

    public void lock() {
        foodEtFragmentBudget.setFocusableInTouchMode(false);
        entertainmentEtFragmentBudget.setFocusableInTouchMode(false);
        shoppingEtFragmentBudget.setFocusableInTouchMode(false);
        foodEtFragmentBudget.clearFocus();
        entertainmentEtFragmentBudget.clearFocus();
        shoppingEtFragmentBudget.clearFocus();
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
