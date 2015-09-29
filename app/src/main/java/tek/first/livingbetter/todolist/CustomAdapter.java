package tek.first.livingbetter.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import tek.first.livingbetter.R;

/**
 * Created by HAN KE on 2015/9/3.
 */
public class CustomAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<toDoItem> list;
    Context context;
    private String name;
    private static LayoutInflater inflater;
    private Callback mCallback;

    CustomAdapter(Context context, String name, ArrayList<toDoItem> todolist, Callback callback) {
        this.context = context;
        this.name = name;
        this.list = todolist;
        mCallback=callback;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface  Callback{
        public void click(View v);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder {
        TextView title;
        TextView time;
        TextView category;
        TextView priority;
        Button update;
        Button delete;
        Button complete;
    }

    @Override
    public View getView(int i, View rowView, ViewGroup viewGroup) {
        final int pos = i;
        Holder holder;
        if (rowView == null) {
            holder = new Holder();
            rowView = inflater.inflate(R.layout.todolist_item, null);
            holder.complete = (Button) rowView.findViewById(R.id.todolist_button_complete);
            holder.title = (TextView) rowView.findViewById(R.id.todolist_title);
            holder.category = (TextView) rowView.findViewById(R.id.todolist_cate);
            holder.time = (TextView) rowView.findViewById(R.id.todolist_time);
            holder.priority = (TextView) rowView.findViewById(R.id.todolist_proirity);
            holder.update = (Button) rowView.findViewById(R.id.todolist_button_update);
            holder.delete = (Button) rowView.findViewById(R.id.todolist_button_delete);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        if (name.equals("incomplete")) {
            holder.title.setText(list.get(i).getTitle());
            holder.time.setText(handledate(list.get(i).getCreatetime()));
            holder.priority.setText(String.valueOf(list.get(i).getPriority()));
            holder.category.setText(list.get(i).getCategory());
            holder.delete.setOnClickListener(this);
            holder.update.setOnClickListener(this);
            holder.complete.setOnClickListener(this);
            holder.delete.setTag(pos);
            holder.update.setTag(pos);
            holder.complete.setTag(pos);
        } else if (name.equals("completed")) {
            holder.delete.setVisibility(View.INVISIBLE);
            holder.complete.setVisibility(View.INVISIBLE);
            holder.update.setVisibility(View.INVISIBLE);
            holder.title.setText(list.get(i).getTitle());
            holder.time.setText(handledate(list.get(i).getCreatetime()));
            holder.priority.setText(String.valueOf(list.get(i).getPriority()));
            holder.category.setText(list.get(i).getCategory());
        }
        return rowView;
    }
    @Override
         public void onClick(View v) {
                mCallback.click(v);
             }


    public static String handledate(String date){
        StringBuffer sb = new StringBuffer();
        if( date == null )
            return "";
        sb.append(date.substring(4,6));
        sb.append("/");
        sb.append(date.substring(6,8));
        sb.append("/");
        sb.append(date.substring(2,4));
        sb.append(" ");
        sb.append(date.substring(8,10));
        sb.append(":");
        sb.append(date.substring(10,12));
        //2012 01 02 00 00 00
        return  sb.toString();
    }
}
