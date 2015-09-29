package tek.first.livingbetter.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;

import java.util.ArrayList;

import tek.first.livingbetter.helper.DatabaseHelper;

/**
 * Created by shipeng on 2015/8/27.
 */
public class MyDemoChart extends AbstractDemoChart {
    private DatabaseHelper helper;
    private Context context;

    public MyDemoChart(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public String getName() {
        return "Budget chart";
    }

    @Override
    public String getDesc() {
        return "The budget per project for this year (pie chart)";
    }

    @Override
    public Intent execute(Context context, ArrayList<Item> res) {
        Double sum_shopping = 0.0;
        Double sum_entertainment = 0.0;
        Double sum_food = 0.0;
        Double sum_unknown = 0.0;
        for (Item item : res) {
            if (item.getCate().equals("Shopping")) {
                sum_shopping += Double.parseDouble(item.getExpense());
            } else if (item.getCate().equals("Entertainment")) {
                sum_entertainment += Double.parseDouble(item.getExpense());
            } else if (item.getCate().equals("Food")) {
                sum_food += Double.parseDouble(item.getExpense());
            } else if (item.getCate().equals("Unknown")) {
                sum_unknown += Double.parseDouble(item.getExpense());
            }
        }
        double total = sum_unknown + sum_shopping + sum_entertainment + sum_food;

        if( total == 0.0 )
            return  null;


        double[] values = new double[]{sum_shopping, sum_entertainment, sum_food, sum_unknown};
        int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.MAGENTA, Color.RED};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        renderer.setLabelsTextSize(50);
        return ChartFactory.getPieChartIntent(context, buildCategoryDataset("Project budget", values, total),
                renderer, "Budget");


    }
}
