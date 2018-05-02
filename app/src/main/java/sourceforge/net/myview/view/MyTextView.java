package sourceforge.net.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import sourceforge.net.myview.R;

/**
 * Created：2018/4/10 on 10:02
 * Author:gaideng on dg
 * Description:
 */

public class MyTextView extends View {
    private static final String TAG = "MyTextView";
    private String text;
    private int textColor;
    private int textSize;
    private Paint mPaint;
    private Rect mBound;
    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init( attrs);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text = randow();
                postInvalidate();
            }
        });
//        init2(attrs);
    }

    /**
     * 随机数
     * @return
     */
    private String randow() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4){
            set.add(random.nextInt(10));
        }
        for (Integer i :
                set) {
            sb.append(i);
        }
        return String.valueOf(sb);
    }

    private void init( @Nullable AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.test);
//            int count = typedArray.getIndexCount();
//            int textAttr = typedArray.getInteger(R.styleable.test_textAttr, 1);
//            String text = typedArray.getString(R.styleable.test_android_text);
//            Log.i(TAG, "MyTextView: textAttr:" + textAttr + ";text:" + text);
//            typedArray.recycle();
//        }
        if (attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.test);
            int count = a.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = a.getIndex(i);
                switch (attr){
                    case R.styleable.test_android_text:
                        text = a.getString(attr);
                        break;
                    case R.styleable.test_android_textSize:
                        textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.test_android_textColor:
                        textColor = a.getColor(attr, Color.BLACK);
                        break;
                    case R.styleable.test_textAttr:
                        break;
                }
            }
            a.recycle();
        }
/**
 * 获得文本的宽高
 */
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mBound = new Rect();
        mPaint.getTextBounds(text,0,text.length(),mBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY){
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(text,0,text.length(),mBound);
            width = getPaddingLeft() + mBound.width() + getPaddingRight();
        }
        if (heightMode != MeasureSpec.EXACTLY){
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(text,0,text.length(),mBound);
            height = getPaddingTop() + mBound.height() + getPaddingBottom();
        }

        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mPaint.setColor(textColor);
        //字体宽度大于设定宽度
        if (mBound.width() > getWidth()){
            TextPaint textPaint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(text,textPaint,getWidth()-getPaddingLeft()-getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),getHeight() - getPaddingBottom(),mPaint);
        }else {
            canvas.drawText(text, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        }

    }

    private void init2(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            int count = attrs.getAttributeCount();
            for (int i = 0; i < count; i++) {
                Log.i(TAG, "name: " + attrs.getAttributeName(i) + ";Value:" + attrs.getAttributeValue(i));
            }
        }
    }

}
