package tek.first.livingbetter.wallet;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import tek.first.livingbetter.R;
import tek.first.livingbetter.helper.DatabaseHelper;


public class HistoryFragment extends Fragment {
    private TabHost tabHost;
    DatabaseHelper helper;
    ListView week_lv_history;
    ListView month_lv_history;
    ArrayList<Item> week_list;
    ArrayList<Item> month_list;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        tabHost = (TabHost) view.findViewById(R.id.tabHost);
        tabHost.setup();
        helper = new DatabaseHelper(getActivity());
        TabWidget tabWidget = tabHost.getTabWidget();
        tabHost.addTab(tabHost.newTabSpec("tab1").setContent(R.id.tab1).setIndicator("Week", null));
        tabHost.addTab(tabHost.newTabSpec("tab2").setContent(R.id.tab2).setIndicator("Month", null));
        week_lv_history = (ListView) view.findViewById(R.id.week_lv_history);
        month_lv_history = (ListView) view.findViewById(R.id.month_lv_history);
        context = getActivity();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        Date Date_start = null;
        Date_start = new Date(date.getYear(), date.getMonth(), date.getDay() - calendar.get(Calendar.DAY_OF_WEEK) + 1);
        String str = "";
        str += String.valueOf(Date_start.getYear() + 1900)
                + ((Date_start.getMonth() + 1) < 10 ? "0" + String.valueOf((Date_start.getMonth() + 1)) : String.valueOf((Date_start.getMonth() + 1)))
                + (Date_start.getDay() < 10 ? "0" + String.valueOf(Date_start.getDay()) : String.valueOf(Date_start.getDay()))
                + "000000";
        String long_Date_start = str;
        str = "";
        str += String.valueOf(date.getYear() + 1900)
                + ((date.getMonth() + 1) < 10 ? "0" + String.valueOf((date.getMonth() + 1)) : String.valueOf((date.getMonth() + 1)))
                + (date.getDay() < 10 ? "0" + String.valueOf(date.getDay()) : String.valueOf(date.getDay()))
                + "235959";
        String date_current = str;
        helper = new DatabaseHelper(context);
        week_list = helper.getItemfiltedDate(long_Date_start, date_current);
        String startDate = date_current.substring(0,6)+"01";
        month_list = helper.getMonthData(startDate,date_current);
        month_lv_history.setAdapter(new OneWeekAdapter(HistoryFragment.this,month_list));
        week_lv_history.setAdapter(new OneWeekAdapter(HistoryFragment.this, week_list));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.close();
        }
    }
}
