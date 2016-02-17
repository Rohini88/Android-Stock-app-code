package com.example.avinash.listview;

/**
 * Created by rshinde on 10/20/15.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Region;


public class SubGraphView extends View{


    Path cv_path;
    Paint cv_paint;
    Boolean cv_flag = true;

    public SubGraphView(Context c, AttributeSet attrs) {

        super(c, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        if (cv_flag) {
            int x = getWidth();
            int y = getHeight();


            Paint paint = new Paint();
            paint.setAntiAlias(true);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(4.5f);
            paint.setAlpha(0*80);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors

            paint.setColor(Color.parseColor("#CD5C5C"));

        } else {
            cv_path = new Path();
            cv_paint = new Paint();
            cv_paint.setColor(Color.GRAY);//line color
            cv_paint.setStyle(Paint.Style.FILL_AND_STROKE);
            cv_paint.setStrokeWidth(3);
            //opacity
            cv_paint.setAlpha(100);

            //drawLine (float startX, float startY, float stopX, float stopY, Paint paint)
            //canvas.drawLine(120,400,140,30,cv_paint);

            for (int x = 0; x < 500; x+=15) {
                canvas.drawLine(0,x, 1020,x, cv_paint);
            }

            for (int y = 0; y < 5000; y+=15) {
                canvas.drawLine(y,0,y,500, cv_paint);
            }



            cv_paint.setStrokeWidth(8);
            cv_paint.setColor(Color.RED);
            canvas.drawLine(0,30,150,100,cv_paint);
            canvas.drawLine(150,100,300,330,cv_paint);
            canvas.drawLine(300,330,550,250,cv_paint);
            canvas.drawLine(550,250,750,250,cv_paint);
            canvas.drawLine(750,250,1020,400,cv_paint);


            //cv_paint.setPathEffect(new DashPathEffect(new float[]{4, 2, 4, 2}, 0));
            //cv_paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 5));
            //canvas.drawRect(300,300,800,800,cv_paint);



        }
    }

    public void cp_toggleFlag() {
        cv_flag = !cv_flag;
        invalidate();
    }


}
