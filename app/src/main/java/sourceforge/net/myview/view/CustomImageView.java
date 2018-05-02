package sourceforge.net.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import sourceforge.net.myview.R;

/**
 * Created：2018/4/11 on 9:59
 * Author:gaideng on dg
 * Description:
 */

public class CustomImageView extends View {
    private final int IMAGE_SCALE_FILL = 0;
    private final int IMAGE_SCALE_CENTER = 1;
    private String mTilte = "";
    private int mTitleSize;
    private int mTitleColor;
    private Bitmap mImage;
    private int mImageType;
    private Rect mRect;
    private Paint mPaint;
    private Rect mBound;
    public CustomImageView(Context context) {
        this(context,null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null){
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
            int count = a.getIndexCount();
            for (int i = 0; i < count; i++) {
                int arr = a.getIndex(i);
                switch (arr){
                    case R.styleable.CustomImageView_titleText:
                        mTilte = a.getString(arr);
                        break;
                    case R.styleable.CustomImageView_titleTextSize:
                        mTitleSize = a.getDimensionPixelSize(arr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,getResources().getDisplayMetrics()));
                        break;
                    case R.styleable.CustomImageView_titleColor:
                        mTitleColor = a.getColor(arr, Color.YELLOW);
                        break;
                    case R.styleable.CustomImageView_image:
                        mImage = BitmapFactory.decodeResource(getResources(),a.getResourceId(arr,0));
                        break;
                    case R.styleable.CustomImageView_imageScaleType:
                        mImageType = a.getInt(arr,1);
                        break;
                }
            }
            a.recycle();
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setTextSize(mTitleSize);
            mBound = new Rect();
            mPaint.getTextBounds(mTilte,0,mTilte.length(),mBound);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if ( widthMode == MeasureSpec.AT_MOST){//wrap_content
            //文本宽度
            int desireByText = getPaddingLeft() + mBound.width() + getPaddingRight();
            //图片决定宽度
            int desireByImg = getPaddingLeft() + mImage.getWidth() + getPaddingRight();
            //不能超过容器宽度
            int desire = Math.max(desireByImg,desireByText);
            width = Math.min(desire,width);
        }
        //设置高度
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST){
            int desire = getPaddingTop() + mImage.getHeight() + mBound.height() + getPaddingBottom();
            //不能超过容器高度
            height = Math.min(desire,height);
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //设置边框
        mPaint.setColor(Color.CYAN);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mRect.left = getPaddingLeft();
        mRect.top = getPaddingTop();
        mRect.right = getMeasuredWidth() - getPaddingRight();
        mRect.bottom = getMeasuredHeight() - getPaddingBottom();

        mPaint.setColor(mTitleColor);
        mPaint.setStyle(Paint.Style.FILL);
        //当字体宽度大于设置宽度
        if (mBound.width() > getMeasuredWidth()){
            String msg = TextUtils.ellipsize(mTilte,new TextPaint(mPaint),getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),getMeasuredHeight()-getPaddingBottom(),mPaint);
        }else {//居中
            canvas.drawText(mTilte,getMeasuredWidth()/2 - mBound.width()/2,getMeasuredHeight()-getPaddingBottom(),mPaint);
        }

        mRect.bottom -= mBound.height();

        //设置图片
        if(mImageType == IMAGE_SCALE_FILL){
            canvas.drawBitmap(mImage,null,mRect,mPaint);
        }else {
            mRect.left = getMeasuredWidth()/2 - mImage.getWidth()/2;
            mRect.right = getMeasuredWidth()/2 + mImage.getWidth()/2;
            mRect.top = (getMeasuredHeight() - mBound.height())/2 - mImage.getHeight()/2;
            mRect.bottom = (getMeasuredHeight() - mBound.height())/2 + mImage.getHeight()/2;
            canvas.drawBitmap(mImage,null,mRect,mPaint);
        }
    }
}
