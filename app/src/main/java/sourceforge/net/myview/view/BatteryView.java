package sourceforge.net.myview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import sourceforge.net.myview.R;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * Created：2018/3/15 on 17:00
 * Author:gaideng on dg
 * Description:
 */

public class BatteryView extends View {
    /**
     * 整体布局中心坐标
     */
    private float centerX,centerY;
    /**
     * 电池内容宽度、高度
     */
    private float bWidth,bHeight;
    /**
     * 背景颜色和电池颜色
     */
    private int bBackGround,bColor,bOutColor;
    /**
     * 电池小节数/边角弧度
     */
    private int bItem,bCorner;
    /**
     * 外边框宽度、水平内边距、垂直边距、上部跨度和高度
     */
    private float bBorderWidth,bHPadding,bVPadding,bSmallW,bSmallH;
    /**
     * 电池当前进度
     */
    private float bProgress;
    private RectF rfBig,rfSmall,rfProgress;
    private Paint paintBig,paintSmall;

    private float mItemHeight;
    public BatteryView(Context context) {
        super(context);
        init(null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.BatteryView);
            if (arr != null) {
                bWidth = arr.getDimension(R.styleable.BatteryView_bWidth, 0);
                bHeight = arr.getDimension(R.styleable.BatteryView_bHeight, 0);
                bBackGround = arr.getColor(R.styleable.BatteryView_bBackGround, Color.WHITE);
                bColor = arr.getColor(R.styleable.BatteryView_bColor, Color.GREEN);
                bOutColor = arr.getColor(R.styleable.BatteryView_bOutColor, Color.GREEN);
                bItem = arr.getInteger(R.styleable.BatteryView_bItem, 5);
                bCorner = arr.getInteger(R.styleable.BatteryView_bCorner, 5);
                bBorderWidth = arr.getDimension(R.styleable.BatteryView_bBorderWidth, 5);
                bHPadding = arr.getDimension(R.styleable.BatteryView_bHPadding, 5);
                bVPadding = arr.getDimension(R.styleable.BatteryView_bVPadding, 5);
                bSmallW = arr.getDimension(R.styleable.BatteryView_bSmallW, 0);
                bSmallH = arr.getDimension(R.styleable.BatteryView_bSmallH, 5);
                bProgress = arr.getFloat(R.styleable.BatteryView_bProgress,0);
                arr.recycle();
            }
        }
        initPaint();
    }

    private void initPaint() {
        rfBig = new RectF();
        rfSmall = new RectF();
        rfProgress = new RectF();

        paintBig = new Paint();
        paintBig.setColor(bColor);
        paintBig.setStyle(Paint.Style.STROKE);
        paintBig.setStrokeWidth(bBorderWidth);

        paintSmall = new Paint();
        paintSmall.setColor(bColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED){
            sizeWidth  = (int) Math.ceil(TypedValue.applyDimension(COMPLEX_UNIT_DIP,20,getContext().getResources().getDisplayMetrics()));
            sizeWidth+=getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){
            sizeHeight = (int)Math.ceil(TypedValue.applyDimension(COMPLEX_UNIT_DIP,100,getContext().getResources().getDisplayMetrics()));
            sizeHeight += getPaddingTop() + getPaddingBottom();
        }
        if (bWidth==0){
            bWidth = sizeWidth;
        }
        if (bHeight == 0){
            bHeight = sizeHeight;
        }
        if (bSmallW == 0){
            bSmallW = bWidth/2;
        }
        setMeasuredDimension(sizeWidth,sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getMeasuredWidth()/2;
        centerY = getMeasuredHeight()/2;
        mItemHeight = (bHeight - bSmallH - bVPadding*(bItem +1)-bBorderWidth*2)*1.0f/bItem;
        rfBig.left = centerX - bWidth/2;
        rfBig.right = rfBig.left + bWidth;
        rfBig.top = centerY - bHeight/2 + bSmallH;
        rfBig.bottom = centerY + bHeight/2;

        rfSmall.left = centerX - bSmallW/2;
        rfSmall.right = rfSmall.left + bSmallW;
        rfSmall.bottom =  centerY - bHeight/2 + bSmallH;
        rfSmall.top = centerY - bHeight/2;

        rfProgress.left = rfBig.left + bBorderWidth + bHPadding;
        rfProgress.right = rfBig.right - bBorderWidth - bHPadding;
        rfProgress.bottom = rfBig.bottom - bVPadding - bBorderWidth;
        rfProgress.top = rfProgress.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(bBackGround);
        drawView(bProgress,canvas);
    }

    private void drawView(float progress, Canvas canvas) {
        int color = bColor;
        if (progress <= 14){
            color = bOutColor;
        }
        paintBig.setColor(color);
        paintSmall.setColor(color);

        canvas.drawRoundRect(rfBig,bCorner,bCorner,paintBig);
        canvas.drawRect(rfSmall,paintSmall);

        //计算需要画的个数
//        int count = (int) Math.ceil(progress*bItem/100);
        int count = 0;
        if (progress < 15){
            count = 0;
        }else if (progress <= 25){
            count = 1;
        }else if (progress <= 45){
            count = 2;
        }else if (progress <= 65){
            count = 3;
        }else if (progress <= 85){
            count = 4;
        }else{
            count = 5;
        }
        for (int i = 0; i <count ; i++) {
            RectF rf = new RectF();
            rf.left = rfProgress.left;
            rf.right = rfProgress.right;
            rf.bottom =  rfProgress.bottom- i*(bVPadding + mItemHeight);
            rf.top =  rf.bottom - mItemHeight;
            canvas.drawRect(rf,paintSmall);
        }
    }

    public void setbProgress(float bProgress) {
        this.bProgress = bProgress;
        this.invalidate();
    }
}
