package tek.first.livingbetter.habit.leftdrawer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import tek.first.livingbetter.todolist.activity.ToDoListDisplayActivity;
import tek.first.livingbetter.wallet.WalletActivity;

/**
 * Created by Leon on 10/9/2015.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    private Context context;

    public DrawerItemClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(context, ToDoListDisplayActivity.class);
                break;
            default:
                intent = new Intent(context, WalletActivity.class);
                break;
        }
        context.startActivity(intent);
    }
}
