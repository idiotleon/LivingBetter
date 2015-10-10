package tek.first.livingbetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Leon on 10/9/2015.
 */
public class CustomDrawerListViewAdapter extends BaseAdapter {

    private Context context;

    public CustomDrawerListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String[] options = context.getResources().getStringArray(R.array.components);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageview_drawer_list_row);
        TextView textView = (TextView) rowView.findViewById(R.id.textview_drawer_list_row);
        textView.setText(options[position]);
//        imageView.setImageResource(LanguageInfo.nationalFlagImageUrls[position]);
        return rowView;
    }
}
