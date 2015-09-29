package tek.first.livingbetter.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/31.
 */
public class MyDemoChart_BarChart extends AbstractDemoChart{
    private Context context;
    public MyDemoChart_BarChart(Context context,ArrayList<Item> res) {
        super(context);
        this.context=context;
    }
    public String getName() {
        return "Sales horizontal bar chart";
    }

    public String getDesc() {
        return "The monthly sales for the last 2 years (horizontal bar chart)";
    }

    public Intent execute(Context context,ArrayList<Item> res) {
        String[] titles = new String[] { "2007", "2008" };
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[] { 5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, 9500, 10500,
                11600, 13500 });
        values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200, 22030, 21200, 19500, 15500,
                12600, 14000 });

        int[] colors = new int[] { Color.CYAN, Color.BLUE};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
        renderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        setChartSettings(renderer, "Monthly sales in the last 2 years", "Month", "Units sold", 0.5,
                12.5, 0, 24000, Color.GRAY, Color.LTGRAY);
        renderer.setXLabels(1);
        renderer.setYLabels(10);
        renderer.addTextLabel(1, "Jan");
        renderer.addTextLabel(3, "Mar");
        renderer.addTextLabel(5, "May");
        renderer.addTextLabel(7, "Jul");
        renderer.addTextLabel(10, "Oct");
        renderer.addTextLabel(12, "Dec");
        renderer.setDisplayChartValues(true);
        return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
                BarChart.Type.DEFAULT);
    }

}

