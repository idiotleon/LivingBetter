package tek.first.livingbetter.wallet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by shipeng on 2015/9/2.
 */
public class MyProgressBar extends ProgressBar {
    String text;
    Paint mPaint;
    public MyProgressBar(Context context) {
        super(context);
        initText();
    }
    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        System.out.println("2");
        initText();
    }


    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("3");
        initText();
    }

    public synchronized void setProgress(int progress,float current,float total) {
        setText(progress, current, total);
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(this.text, x, y, this.mPaint);
    }


    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLUE);
        this.mPaint.setTextSize(60);

    }
    public void warningText(int color){
        this.mPaint.setColor(color);
    }
    private void setText(){
        setText(this.getProgress(),0,0);
    }

    private void setText(int progress,float current,float total){
        int i = (progress * 100)/this.getMax();
        this.text = current+"/"+total+"   "+ String.valueOf(i) + "%";
    }


}