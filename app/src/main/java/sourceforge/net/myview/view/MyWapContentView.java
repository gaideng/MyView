package sourceforge.net.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import sourceforge.net.myview.R;

/**
 * Created：2018/3/15 on 20:17
 * Author:gaideng on dg
 * Description:
 */

public class MyWapContentView extends View {
    private int backgroud,textColor;
    private float textSize;
    private String text;
    private Rect mBound;
    private Paint mPaint;
    public MyWapContentView(Context context) {
        super(context);
        init(null);
    }

    public MyWapContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }


    public MyWapContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyWapContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null){
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.MyWapContentView);
            if (arr != null){
                backgroud = arr.getColor(R.styleable.MyWapContentView_mBackground, Color.WHITE);
                textColor = arr.getColor(R.styleable.MyWapContentView_mTextColor,Color.GRAY);
                textSize = arr.getDimension(R.styleable.MyWapContentView_mTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,getContext().getResources().getDisplayMetrics()));
                text = arr.getString(R.styleable.MyWapContentView_mText);
                arr.recycle();
            }
        }
        /**
         * 获得绘制文本宽高
         */
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mBound = new Rect();
        mPaint.getTextBounds(text,0,text.length(),mBound);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text = postRandom();
                postInvalidate();
            }
        });
    }

    private String postRandom() {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4){
            int item = random.nextInt(10);
            set.add(item);
        }
        StringBuilder sb = new StringBuilder();
        for (Integer i : set){
            sb.append(String.valueOf(i));
        }
        return String.valueOf(sb);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        if (modeWidth != MeasureSpec.EXACTLY){
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(text,0,text.length(),mBound);
            sizeWidth = mBound.width() + getPaddingLeft() +getPaddingRight();
        }
        if (modeHeight != MeasureSpec.EXACTLY){
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(text,0,text.length(),mBound);
            sizeHeight = mBound.height() + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(sizeWidth,sizeHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(backgroud);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        mPaint.setColor(textColor);
        canvas.drawText(text,getWidth()/2-mBound.width()/2,getHeight()/2+mBound.height()/2,mPaint);
    }
}
