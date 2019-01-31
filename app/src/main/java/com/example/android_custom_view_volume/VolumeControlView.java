package com.example.android_custom_view_volume;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class VolumeControlView extends View {


    private static final float CONVERT_DEGREE_TO_INT_FACTOR = 2.55f;
    private static final int minRotation = 0, maxRotation = 255, EDGE_OFFSET = 15, SLOW_ROTATION_FACTOR =120;

    protected int circleRotation = 0, distanceTraveled = 0, circleEndPoint = 0, circleStartPoint = 0, currentVolume;
    protected Paint paintOuterCircle, paintInnerCircle, paintKnob;
    boolean mAnimateOnDisplay;


    public VolumeControlView(Context context) {
        super(context);
        init(null);
    }

    public VolumeControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public VolumeControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Rect rectangle1 = new Rect ();
     * rectangle1.top = 50;
     * rectangle1.bottom = 250;
     * rectangle1.left= 25;
     * rectangle1.right = 325;
     *
     * Paint paint1 = new Paint (Paint.ANTI_ALIAS_FLAG);
     * paint1.setColor(Color.BLUE);
     *
     * canvas.drawRect(rectangle1,paint1);
     *
     *
     * **/


    public void setCurrentVolume(int currentVolume) {
        this.currentVolume =  currentVolume;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth() / 2f;
        float height = getHeight() / 2f;

        canvas.rotate(circleRotation, width, height);

        int radius = 100;
        if(width < height){
            radius = (int)(width) - EDGE_OFFSET;
        }else if (height < width){
            radius = (int)(height) - EDGE_OFFSET;
        }

      int innerCircleRadius = (int) (radius * .92f);
        int smallInnerCircleRadius = (int) (radius * .08f);

        canvas.drawCircle(width, height, radius, paintOuterCircle);
        canvas.drawCircle(width, height, innerCircleRadius, paintInnerCircle);
        canvas.drawCircle(
                width - (innerCircleRadius - (smallInnerCircleRadius * 2) - 80),
                height + (innerCircleRadius * .5f ),
                smallInnerCircleRadius,
                paintKnob);



    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                circleStartPoint = (int) event.getX();
                Log.d(TAG, "Action was DOWN");

                break;
            case MotionEvent.ACTION_MOVE:
                circleEndPoint = (int) event.getX();


                distanceTraveled = (circleEndPoint - circleStartPoint) / SLOW_ROTATION_FACTOR;
                circleRotation = circleRotation + (distanceTraveled);
                if(circleRotation < minRotation){
                    circleRotation = minRotation;
                }
                if(circleRotation > maxRotation){
                    circleRotation = maxRotation;
                }
                setCurrentVolume((int) (circleRotation / CONVERT_DEGREE_TO_INT_FACTOR));
                invalidate();

                break;
            case MotionEvent.ACTION_UP:
                Toast.makeText(getContext(), "Volume " + currentVolume + "%", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Action was UP");
                break;
        }
        return true;
    }


    protected void init(AttributeSet attrs){
        paintOuterCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOuterCircle.setStyle(Paint.Style.FILL);

        paintInnerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintInnerCircle.setStyle(Paint.Style.STROKE);
        paintInnerCircle.setStrokeWidth(15);

        paintKnob = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintKnob.setStyle(Paint.Style.FILL);
        paintInnerCircle.setColor(Color.BLUE);
        paintKnob.setColor(Color.GREEN);
        paintOuterCircle.setColor(Color.YELLOW);


    }

}


