package tek.first.livingbetter.todolist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import tek.first.livingbetter.todolist.model.SimpleToDoItem;

/**
 * Created by Leon on 10/7/2015.
 */
public class IncompleteSimpleToDoItemsDisplayFragment extends ListFragment {

    private static final String LOG_TAG = IncompleteSimpleToDoItemsDisplayFragment.class.getSimpleName();

    private ArrayList<SimpleToDoItem> simpleToDoItemModelArrayList;

    private ToDoItemStatusChangeListener toDoItemStatusChangeListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            toDoItemStatusChangeListener = (ToDoItemStatusChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewItemAddedListener.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        simpleToDoItemModelArrayList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                simpleToDoItemModelArrayList
                        = GeneralHelper.getSortedIncompleteSimpleToDoItemsAsArrayList(getActivity());
                Log.v(LOG_TAG, "onItemLongClick(), IncompleteDetailedItemsDisplayFragment executed");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.do_you_want_to))
                        .setItems(R.array.simple_todoitem_operation, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final SimpleToDoItem simpleToDoItemModel = simpleToDoItemModelArrayList.get(position);
                                switch (which) {
                                    case 0:
                                        simpleToDoItemModel.setCompletionStatus(GeneralHelper.CompletionStatus.COMPLETED);
                                        GeneralHelper.updateToDoListItem(getActivity(), simpleToDoItemModel);
                                        Toast.makeText(getActivity(), "DetailedToDoItem: " + simpleToDoItemModel.getTitle() + " is marked as complete.", Toast.LENGTH_SHORT).show();
                                        toDoItemStatusChangeListener.onStatusChanged();
                                        break;
                                    case 1:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(R.string.delete_confirmation).setPositiveButton(R.string.todolist_confirm_text, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                GeneralHelper.deleteToDoItem(getActivity(), simpleToDoItemModel);
                                                toDoItemStatusChangeListener.onStatusChanged();
                                                Toast.makeText(getActivity(), "SimpleToDoItem: " + simpleToDoItemModel.getTitle() + " deleted.", Toast.LENGTH_SHORT).show();
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
        simpleToDoItemModelArrayList = GeneralHelper.getSortedIncompleteSimpleToDoItemsAsArrayList(getActivity());

        Intent simpleToDoItemIntent = new Intent(getActivity(), ToDoItemDetailsActivity.class);
        simpleToDoItemIntent.putExtra(GeneralConstants.SIMPLE_TO_DO_ITEM_IDENTIFIER, simpleToDoItemModelArrayList);
        startActivity(simpleToDoItemIntent);
    }

}
