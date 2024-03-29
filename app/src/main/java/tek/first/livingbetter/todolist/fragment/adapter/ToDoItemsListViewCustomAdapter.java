package tek.first.livingbetter.todolist.fragment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tek.first.livingbetter.R;
import tek.first.livingbetter.todolist.helper.GeneralHelper;
import tek.first.livingbetter.todolist.model.DetailedToDoItem;

/**
 * Created by Leon on 10/7/2015.
 */
public class ToDoItemsListViewCustomAdapter extends BaseAdapter {

    private static String LOG_TAG = ToDoItemsListViewCustomAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<DetailedToDoItem> toDoListItemsArrayList;

    public ToDoItemsListViewCustomAdapter(Context context, ArrayList<DetailedToDoItem> toDoListItemsArrayList) {
        this.context = context;
        this.toDoListItemsArrayList = toDoListItemsArrayList;
    }

    @Override
    public int getCount() {
        return toDoListItemsArrayList.size();
    }

    @Override
    public DetailedToDoItem getItem(int position) {
        return toDoListItemsArrayList.get(position);
    }

    public int getPriority(int position) {
        return (getItem(position)).getPriority();
    }

    public GeneralHelper.CompletionStatus getCompletionStatus(int position) {
        return getItem(position).getCompletionStatus();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toDoItemRootView = inflater.inflate(R.layout.todoitem_custom_textview, null);
        TextView textViewNumbering = (TextView) toDoItemRootView.findViewById(R.id.todolist_row_numbering);
        Log.v(LOG_TAG, "getPriority(position): " + Integer.toString(getPriority(position)));
        TextView textViewDeadline = (TextView) toDoItemRootView.findViewById(R.id.todolist_row_deadline);
        TextView textViewTitle = (TextView) toDoItemRootView.findViewById(R.id.todolist_row_title);

        textViewTitle.setText(toDoListItemsArrayList.get(position).getTitle());
        textViewNumbering.setText(Integer.toString(position + 1));
        textViewDeadline.setText(GeneralHelper.parseDateAndTimeToString(getItem(position).getToDoItemDeadline()));

        return toDoItemRootView;
    }

}
