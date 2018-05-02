package sourceforge.net.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import sourceforge.net.myview.R;

/**
 * Created：2018/4/11 on 16:58
 * Author:gaideng on dg
 * Description:
 */

public class MyCircle extends View {
    private final Object lock = new Object();
    private int mFirstColor;
    private int mSecondColor;
    private int mSleep;
    private int mCircleWidth;
    private Paint mPaint;
    private int mProgress;
    private RectF mRectF;
    private Thread mThread;
    private boolean isStop = false;
    public MyCircle(Context context) {
        this(context,null);
    }

    public MyCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCircle, defStyleAttr, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int arr = a.getIndex(i);
            switch (arr){
                case R.styleable.MyCircle_firstColor:
                    mFirstColor = a.getColor(arr, Color.RED);
                    break;
                case R.styleable.MyCircle_secondColor:
                    mSecondColor = a.getColor(arr,Color.YELLOW);
                    break;
                case R.styleable.MyCircle_sleep:
                    mSleep = a.getInt(arr,20);
                    break;
                case R.styleable.MyCircle_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(arr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        //消除锯齿
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
        mThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    while (true){
                        mProgress++;
                        if (mProgress >= 360){
                            mProgress = 0;
                            int temp = mSecondColor;
                            mSecondColor = mFirstColor;
                            mFirstColor = temp;
                        }
                        if (isStop){
                            onPause();
                        }
                        postInvalidate();
                        sleep(mSleep);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }

    /**
     * 只能run中调用
     */
    private void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread(){
        isStop = true;
    }
    public void startThread(){
        isStop = false;
        synchronized (lock){
            lock.notifyAll();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取圆形
        int circleX = getWidth()/2;
        int circleY = getHeight()/2;
        //获取半径
        int radiu = Math.min(circleX,circleY) - mCircleWidth/2;
        //第一圈
        mPaint.setColor(mFirstColor);
        canvas.drawCircle(circleX,circleY,radiu,mPaint);
        //画进度
        mRectF.left = circleX - radiu;
        mRectF.top = circleY - radiu;
        mRectF.right = circleX + radiu;
        mRectF.bottom  = circleY + radiu;
        mPaint.setColor(mSecondColor);
        canvas.drawArc(mRectF,-90,mProgress,false,mPaint);
    }
}
