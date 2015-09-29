package tek.first.livingbetter.todolist;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tek.first.livingbetter.R;
import tek.first.livingbetter.helper.DatabaseHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class TodolistFragment extends Fragment implements
        CustomAdapter.Callback {
    private Button create;
    private ListView incomplete;
    private ListView completed;
    private DatabaseHelper databaseHelper;
    private ArrayList<toDoItem> incom = new ArrayList<>();
    private ArrayList<toDoItem> comed = new ArrayList<>();
    private CustomAdapter ada_incom;
    private CustomAdapter ada_comped;
     private Comparator<toDoItem> comparator = new Comparator<toDoItem>() {
        public int compare(toDoItem s1, toDoItem s2) {

            if (s1.getPriority() - s2.getPriority() > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    public TodolistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todolist, container, false);

        create = (Button) view.findViewById(R.id.todolist_create);
        incomplete = (ListView) view.findViewById(R.id.todolist_incomplete);
        completed = (ListView) view.findViewById(R.id.todolist_completed);
        databaseHelper = new DatabaseHelper(getActivity());
        incom.clear();
        comed.clear();
        incom.addAll(databaseHelper.getincomplete());
        comed.addAll(databaseHelper.getcompleted());
        Collections.sort(incom, comparator);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                final String str = format.format(new Date());

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.dialog_create, null);
                builder.setIcon(R.drawable.plus_math_50);
                builder.setTitle("Please Input your Activity Info");
                builder.setView(textEntryView);
                final EditText title = (EditText) textEntryView.findViewById(R.id.dia_et_title);
                title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View arg0, boolean arg1) {
                        InputMethodManager inputMgr = (InputMethodManager) arg0.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
                final EditText priority = (EditText) textEntryView.findViewById(R.id.dia_et_priority);
                priority.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View arg0, boolean arg1) {
                        InputMethodManager inputMgr = (InputMethodManager) arg0.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });

                final Spinner spinner = (Spinner)textEntryView.findViewById(R.id.spinner_category);
                String[] spinner_item = new String[]{"Food","Entertainment","Shopping","Unknown"};
                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String title_res =title .getText().toString().trim();
                        String cate_res = spinner.getSelectedItem().toString();
                        int priority_res = Integer.parseInt(priority.getText().toString().trim());

                        if (title_res.isEmpty()) {
                            Crouton.makeText( getActivity(), "Please input your Title", Style.ALERT).show();
                        }
                        else {
                            toDoItem item = new toDoItem(0,title_res,str,priority_res,cate_res);
                            incom.add(item);
                            databaseHelper.insertToDoListItem(item);
                            ada_incom.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog.show();

            }
        });
        ada_incom = new CustomAdapter(getActivity(), "incomplete", incom, this);
        ada_comped = new CustomAdapter(getActivity(), "completed", comed, this);
        incomplete.setAdapter(ada_incom);
        completed.setAdapter(ada_comped);
        return view;
    }

    @Override
    public void click(View v) {
        final View x = v;
        switch (v.getId()) {
            case R.id.todolist_button_complete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.dialog, null);
                builder.setIcon(R.drawable.coins_50);
                builder.setTitle("Please Input your expense");
                builder.setView(textEntryView);
                final EditText expanse = (EditText) textEntryView.findViewById(R.id.et_expanse);
                expanse.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View arg0, boolean arg1) {
                        InputMethodManager inputMgr = (InputMethodManager) arg0.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String ex = expanse.getText().toString().trim();
                        if (ex.isEmpty()) {
                            Crouton.makeText(getActivity(), "Please input your expense", Style.CONFIRM ).show();
                        } else {
                            int position = (int) x.getTag();
                            toDoItem item = incom.get(position);
                            incom.remove(position);
                            comed.add(item);
                            databaseHelper.updateToDoListItem(item, Float.parseFloat(ex));
                            ada_incom.notifyDataSetChanged();
                            ada_comped.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog.show();
                break;
            case R.id.todolist_button_delete:
                final View x1 = v;
                final AlertDialog.Builder builder_delete = new AlertDialog.Builder(getActivity());
                LayoutInflater factory_delete = LayoutInflater.from(getActivity());
                final View textEntryView_delete = factory_delete.inflate(R.layout.dialog, null);
                builder_delete.setTitle("Sure to delete this ?");
                builder_delete.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int position = (int) x1.getTag();
                        toDoItem item = incom.get(position);
                        incom.remove(position);
                        databaseHelper.deleteToDoItem(item);
                        ada_incom.notifyDataSetChanged();
                    }
                });
                builder_delete.setIcon(R.drawable.delete_50);
                builder_delete.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                builder_delete.show();
                break;
            case R.id.todolist_button_update:
                final View x2 = v;
                final int pos = (int) x2.getTag();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                final String str_time = format.format(new Date());
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                LayoutInflater factory1 = LayoutInflater.from(getActivity());
                final View textEntryView1 = factory1.inflate(R.layout.dialog_create, null);
                builder1.setIcon(R.drawable.edit_502);
                builder1.setTitle("Please Input your Activity Info");
                builder1.setView(textEntryView1);
                final EditText title = (EditText) textEntryView1.findViewById(R.id.dia_et_title);
                title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View arg0, boolean arg1) {
                        InputMethodManager inputMgr = (InputMethodManager) arg0.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });
                final EditText priority = (EditText) textEntryView1.findViewById(R.id.dia_et_priority);
                priority.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View arg0, boolean arg1) {
                        InputMethodManager inputMgr = (InputMethodManager) arg0.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                });

                final Spinner spinner = (Spinner)textEntryView1.findViewById(R.id.spinner_category);
                String[] spinner_item = new String[]{"Food","Entertainment","Shopping","Unknown"};
                ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                builder1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String title_res =title .getText().toString().trim();
                        String cate_res = spinner.getSelectedItem().toString();
                        int priority_res = Integer.parseInt(priority.getText().toString().trim());

                        if (title_res.isEmpty()) {
                            Crouton.makeText(getActivity(),"Please input your title.", Style.ALERT).show();
                        }
                        else {
                            toDoItem item = new toDoItem(0,title_res,str_time,priority_res,cate_res);
                            databaseHelper.updateToDoListItem(incom.get(pos),item);
                            incom.remove(pos);
                            incom.add(item);

                            ada_incom.notifyDataSetChanged();
                        }
                    }
                });
                builder1.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alertDialog1.show();
                break;
        }


    }


    public static Fragment newInstance(int position) {
        Fragment fragment = new TodolistFragment();
        Bundle args = new Bundle();
        args.putInt("selection", position);
        fragment.setArguments(args);
        return fragment;
    }
}