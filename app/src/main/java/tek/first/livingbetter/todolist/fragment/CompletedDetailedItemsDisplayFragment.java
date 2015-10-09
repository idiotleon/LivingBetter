package tek.first.livingbetter.todolist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import tek.first.livingbetter.R;
import tek.first.livingbetter.todolist.activity.ToDoItemDetailsActivity;
import tek.first.livingbetter.todolist.helper.GeneralConstants;
import tek.first.livingbetter.todolist.helper.GeneralHelper;
import tek.first.livingbetter.todolist.helper.GeneralHelper.ToDoItemStatusChangeListener;
import tek.first.livingbetter.todolist.model.DetailedToDoItem;

/**
 * Created by Leon on 10/7/2015.
 */
public class CompletedDetailedItemsDisplayFragment extends ListFragment{

    private static final String LOG_TAG = CompletedDetailedItemsDisplayFragment.class.getSimpleName();

    private ArrayList<DetailedToDoItem> completedToDoItemsArrayList;
    private GeneralHelper.ToDoItemStatusChangeListener toDoItemStatusChangeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            toDoItemStatusChangeListener = (ToDoItemStatusChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement ToDoItemStatusChangeListener.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        completedToDoItemsArrayList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                completedToDoItemsArrayList
                        = GeneralHelper.getSortedCompletedDetailedToDoItemsAsArrayList(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.do_you_want_to))
                        .setItems(R.array.complete_todoitem_operation, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DetailedToDoItem toDoItem = completedToDoItemsArrayList.get(position);
                                switch (which) {
                                    case 0:
                                        toDoItem.setCompletionStatus(GeneralHelper.CompletionStatus.INCOMPLETE);
                                        GeneralHelper.updateToDoListItem(getActivity(), toDoItem);
                                        Toast.makeText(getActivity(), "DetailedToDoItem: " + toDoItem.getTitle() + " is marked as complete.", Toast.LENGTH_SHORT).show();
                                        toDoItemStatusChangeListener.onStatusChanged();
                                        break;
                                    case 1:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(R.string.delete_confirmation).setPositiveButton(R.string.todolist_confirm_text, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                GeneralHelper.deleteToDoItem(getActivity(), toDoItem);
                                                toDoItemStatusChangeListener.onStatusChanged();
                                                Toast.makeText(getActivity(), "DetailedToDoItem: " + toDoItem.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
                                            }
                                        }).setNegativeButton(R.string.todolist_cancel_text, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        (builder.create()).show();
                                        break;
                                }
                            }
                        });
                (builder.create()).show();
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        completedToDoItemsArrayList = GeneralHelper.getSortedCompletedDetailedToDoItemsAsArrayList(getActivity());

        Intent intent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        intent.putExtra(GeneralConstants.DETAILED_TO_DO_ITEM_IDENTIFIER, completedToDoItemsArrayList.get(position));
        startActivity(intent);
    }
}
