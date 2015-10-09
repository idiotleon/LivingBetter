package tek.first.livingbetter.wallet;

import android.app.Fragment;
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
import tek.first.livingbetter.provider.DatabaseHelper;
import tek.first.livingbetter.wallet.model.ItemModel;


public class HistoryFragment extends Fragment {
    private TabHost tabHost;
    private DatabaseHelper dbHelper;
    private ListView weekListViewHistory;
    private ListView monthListViewHistory;
    private ArrayList<ItemModel> weekList;
    private ArrayList<ItemModel> monthList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallet_fragment_history, container, false);
        tabHost = (TabHost) view.findViewById(R.id.tabhost);
        tabHost.setup();
        dbHelper = new DatabaseHelper(getActivity());
        TabWidget tabWidget = tabHost.getTabWidget();
        tabHost.addTab(tabHost.newTabSpec("tab1").setContent(R.id.tab1).setIndicator("Week", null));
        tabHost.addTab(tabHost.newTabSpec("tab2").setContent(R.id.tab2).setIndicator("Month", null));
        weekListViewHistory = (ListView) view.findViewById(R.id.week_lv_history);
        monthListViewHistory = (ListView) view.findViewById(R.id.month_lv_history);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        Date dateStart = null;
        dateStart = new Date(date.getYear(), date.getMonth(), date.getDay() - calendar.get(Calendar.DAY_OF_WEEK) + 1);
        String str = "";
        str += String.valueOf(dateStart.getYear() + 1900)
                + ((dateStart.getMonth() + 1) < 10 ? "0" + String.valueOf((dateStart.getMonth() + 1)) : String.valueOf((dateStart.getMonth() + 1)))
                + (dateStart.getDay() < 10 ? "0" + String.valueOf(dateStart.getDay()) : String.valueOf(dateStart.getDay()))
                + "000000";
        String longDateStart = str;
        str = "";
        str += String.valueOf(date.getYear() + 1900)
                + ((date.getMonth() + 1) < 10 ? "0" + String.valueOf((date.getMonth() + 1)) : String.valueOf((date.getMonth() + 1)))
                + (date.getDay() < 10 ? "0" + String.valueOf(date.getDay()) : String.valueOf(date.getDay()))
                + "235959";
        String dateCurrent = str;
        dbHelper = new DatabaseHelper(getActivity());
        weekList = dbHelper.getItemFilteredDate(longDateStart, dateCurrent);
        String startDate = dateCurrent.substring(0,6)+"01";
        monthList = dbHelper.getMonthData(startDate,dateCurrent);
        monthListViewHistory.setAdapter(new OneWeekAdapter(HistoryFragment.this, monthList));
        weekListViewHistory.setAdapter(new OneWeekAdapter(HistoryFragment.this, weekList));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
