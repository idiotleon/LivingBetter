package tek.first.livingbetter.wallet;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import tek.first.livingbetter.R;
import tek.first.livingbetter.helper.DatabaseHelper;


public class AnalysisFragment extends Fragment {
    private Button btn_show;
    private DatePicker dp_startDate_picker;
    private DatePicker dp_endDate_picker;
    private int status = -1;
    private RadioGroup radioGroup;
    private DatabaseHelper helper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        dp_endDate_picker = (DatePicker) view.findViewById(R.id.dp_endDate_picker);
        dp_startDate_picker = (DatePicker) view.findViewById(R.id.dp_startDate_picker);
        dp_endDate_picker.updateDate(2015, 0, 1);
        dp_startDate_picker.updateDate(2015, 0, 1);
        radioGroup = (RadioGroup) view.findViewById(R.id.chart_rg_analysis);
        helper = new DatabaseHelper(getActivity());

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rb1 = (RadioButton) view.findViewById(R.id.pieChart_rb_analysis);
                RadioButton rb2 = (RadioButton) view.findViewById(R.id.barChart_rb_analysis);
                if (rb1.isChecked()) {
                    status = 0;
                } else if (rb2.isChecked()) {
                    status = 1;
                }
            }
        });

        btn_show = (Button) view.findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String str = "";
                long startDateLong, endDateLong;
                str += String.valueOf(dp_startDate_picker.getYear())
                        + ((dp_startDate_picker.getMonth() + 1) < 10 ? "0" + String.valueOf((dp_startDate_picker.getMonth() + 1)) : String.valueOf((dp_startDate_picker.getMonth() + 1)))
                        + (dp_startDate_picker.getDayOfMonth() < 10 ? "0" + String.valueOf(dp_startDate_picker.getDayOfMonth()) : String.valueOf(dp_startDate_picker.getDayOfMonth()))
                        + "000000";
                startDateLong = Long.parseLong(str);
                str = "";
                str += String.valueOf(dp_endDate_picker.getYear())
                        + ((dp_endDate_picker.getMonth() + 1) < 10 ? "0" + String.valueOf((dp_endDate_picker.getMonth() + 1)) : String.valueOf((dp_endDate_picker.getMonth() + 1)))
                        + (dp_endDate_picker.getDayOfMonth() < 10 ? "0" + String.valueOf(dp_endDate_picker.getDayOfMonth()) : String.valueOf(dp_endDate_picker.getDayOfMonth()))
                        + "235959";
                endDateLong = Long.parseLong(str);
                if (endDateLong < startDateLong) {
                    Crouton.makeText(getActivity(), " Please select a ending date after the starting date", Style.ALERT).show();
                    return;
                }
                ArrayList<Item> res = helper.getItemfiltedDate(startDateLong, endDateLong);
                for (Item item : res)
                    Log.e("date: ", item.getDate().toString());
                if (status == 0) {
                    if (res.size() != 0) {
                        MyDemoChart myDemoChart = new MyDemoChart(getActivity());
                        Intent intent = myDemoChart.execute(getActivity(), res);
                        if (intent == null)
                            Crouton.makeText(getActivity(), "There is no expense in this period.", Style.ALERT).show();
                        else
                            getActivity().startActivity(intent);
                    } else {
                        Crouton.makeText(getActivity(), "No data found", Style.ALERT).show();
                        return;
                    }
                } else if (status == 1) {
                    Crouton.makeText(getActivity(), "Please wait the next version.", Style.INFO).show();
//                    MyDemoChart_BarChart myDemoChart_barChart = new MyDemoChart_BarChart(getActivity(), res);
//                    Intent intent1 = myDemoChart_barChart.execute(getActivity(), res);
//                    getActivity().startActivity(intent1);
                } else {
                    Crouton.makeText(getActivity(), "You have to choose one chart", Style.ALERT).show();
                    return;
                }
            }
        });
        return view;
    }
}
