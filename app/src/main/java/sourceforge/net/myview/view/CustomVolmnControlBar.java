package sourceforge.net.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import sourceforge.net.myview.R;

/**
 * Created：2018/4/11 on 19:56
 * Author:gaideng on dg
 * Description:
 */

public class CustomVolmnControlBar extends View {

    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    /**
     * 总个数
     */
    private int mDotCount = 10;
    /**
     * 间距
     */
    private int mSplitSize;
    /**
     * 当前进度
     */
    private int current = 3;
    /**
     * 中间图片
     */
    private Bitmap mImage;
    private Paint mPaint;
    private Rect mRect;
    private RectF mRectF;
    private float itemSize;
    public CustomVolmnControlBar(Context context) {
        this(context,null);
    }

    public CustomVolmnControlBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomVolmnControlBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolmnControlBar,defStyleAttr,0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int arr = a.getIndex(i);
            switch (arr){
                case R.styleable.CustomVolmnControlBar_firstColor:
                    mFirstColor = a.getColor(arr, Color.GRAY);
                    break;
                case R.styleable.CustomVolmnControlBar_secondColor:
                    mSecondColor = a.getColor(arr,Color.BLACK);
                    break;
                case R.styleable.CustomVolmnControlBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(arr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,20,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolmnControlBar_dotCount:
                    mDotCount = a.getInt(arr,10);
                    break;
                case R.styleable.CustomVolmnControlBar_bg:
                    mImage = BitmapFactory.decodeResource(getResources(),a.getResourceId(arr,0));
                    break;
                case R.styleable.CustomVolmnControlBar_splitSize:
                    mSplitSize = a.getInt(arr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,10,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
        mRectF = new RectF();
//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setCurrent(new Random().nextInt(mDotCount));
//            }
//        });
    }
 public void up(){
     current++;
     postInvalidate();
 }
 public void down(){
     current--;
     postInvalidate();
 }
    private int yDown,yUp;
    private float mUpSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,10,getResources().getDisplayMetrics());
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                yDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int yMove = (int) event.getY();
                if (yMove - yDown < -mUpSize){
                    yDown = yMove;
                    up();
                }
                if (yMove - yDown > mUpSize){
                    yDown = yMove;
                    down();
                }
                break;
            case MotionEvent.ACTION_UP:
//                yUp = (int) event.getY();
//                if (yUp < yDown){
//                    up();
//                }
//                if (yUp > yDown){
//                    down();
//                }
                break;
        }
        return true;
    }

    /**
     * 设置当前进度个数
     * @param current
     */
    public void setCurrent(int current) {
        if (this.current != current && current >=0) {
            this.current = current;
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画笔
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //比较打下
        float cx1 = (getWidth() - getPaddingLeft() - getPaddingRight()) * 1.0f/2;
        float cy1 = (getHeight() - getPaddingTop() - getPaddingBottom()) * 1.0f/2;
        float radiu = Math.min(cx1,cy1) - mCircleWidth * 1.0f/2;
        //获取圆心坐标和半径
        float cx = cx1 + getPaddingLeft();
        float cy = cy1 + getPaddingTop();
        //画块状
        drawAval(canvas,cx,cy,radiu);
        //画中间图片
        //当图片宽度或高度大雨内切正方形时；图片最大宽度的一半imageMax
        //内切圆半径
        float innerR = radiu - mCircleWidth * 1.0f/2;
       double maxWidth = (Math.sqrt(2) * (innerR))/2;

        if (mImage.getWidth() > maxWidth * 2 || mImage.getHeight() > maxWidth * 2) {
            mRect.left = (int) Math.ceil(cx - maxWidth);
            mRect.top = (int) Math.ceil(cy - maxWidth );
            mRect.right = (int) Math.floor(cx + maxWidth );
            mRect.bottom = (int) Math.floor(cy + maxWidth );
        }else {
            mRect.left = (int) (cx - mImage.getWidth()/2);
            mRect.top = (int) (cy - mImage.getHeight()/2 );
            mRect.right = mRect.left + mImage.getWidth();
            mRect.bottom = mRect.top + mImage.getHeight();
        }
        canvas.drawBitmap(mImage,null,mRect,mPaint);
    }

    private void drawAval(Canvas canvas, float cx, float cy, float radiu) {
        //画所有的块
        //计算单个块状角度
        itemSize = (250 * 1.0f - mSplitSize * (mDotCount-1))/mDotCount;
        //规定圆环画的区域
        mRectF.left = cx - radiu;
        mRectF.top = cy - radiu;
        mRectF.right = cx + radiu;
        mRectF.bottom = cy + radiu;
        //画第一种颜色
        mPaint.setColor(mFirstColor);
        for (int i = 0; i < mDotCount; i++) {
            float start = 145 + (itemSize + mSplitSize) *i;
            canvas.drawArc(mRectF,start,itemSize,false,mPaint);
        }
        //画第二种
        mPaint.setColor(mSecondColor);
        if (current > mDotCount){
            current = mDotCount;
        }
        if (current < 0){
            current = 0;
        }
        for (int i = 0; i < current; i++) {
            float start = 145 + (itemSize + mSplitSize) *i;
            canvas.drawArc(mRectF,start,itemSize,false,mPaint);
        }
    }
}
