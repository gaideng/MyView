package sourceforge.net.myview.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import sourceforge.net.myview.R;

/**
 * Created：2018/4/13 on 10:52
 * Author:gaideng on dg
 * Description:自定义手势锁
 */

public class GestureLockGroup extends RelativeLayout{
    private static final String TAG = "GestureLockGroup";
    private int mColorNoFingerInner = 0xff939090;
    private int mColorNoFingerOuter = 0xFFE0DBDB;
    private int mColorFingerOn = 0xFF378FC9;
    private int mColorFingerUp = 0xFFFF0000;
    private int mCount = 4;
    private int mTryTimes;
    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;
    /**
     * 每个GestureLockView宽度
     */
    private int mGestureLockViewWidth;
    private int mMarginGestureLockView;
    /**
     * GestureLockView容器
     */
    private GestureLockView[] mGestureLockViews;
    /**
     * 保存用户选中的GestureLockView的id
     */
    private List<Integer> mChoose = new ArrayList<>();

    /**
     * 回调接口
     */
    private OnGestureLockViewListener mOnGestureLockViewListener;
    /**
     * 指引线的开始位置
     */
    private int mLastPathX;
    private int mLastPathY;
    /**
     * 指引结束的结束位置
     */
    private Point mTmpTarget = new Point();
    private int[] answer = {1,2,3,5,8};
    public GestureLockGroup(Context context) {
        this(context,null);
    }

    public GestureLockGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GestureLockGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GestureLockGroup,defStyleAttr,0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int arr = a.getIndex(i);
            switch (arr){
                case R.styleable.GestureLockGroup_color_no_finger_inner_circle:
                    mColorNoFingerInner = a.getColor(arr, Color.GRAY);
                    break;
                case R.styleable.GestureLockGroup_color_no_finger_outer_circle:
                    mColorNoFingerOuter = a.getColor(arr, Color.GRAY);
                    break;
                case R.styleable.GestureLockGroup_color_finger_on:
                    mColorFingerOn = a.getColor(arr, Color.GRAY);
                    break;
                case R.styleable.GestureLockGroup_color_finger_up:
                    mColorFingerUp = a.getColor(arr, Color.GRAY);
                    break;
                case R.styleable.GestureLockGroup_count:
                    mCount = a.getInteger(arr, 4);
                    break;
                case R.styleable.GestureLockGroup_tryTimes:
                    mTryTimes = a.getInteger(arr, 3);
                    break;
            }

        }
        a.recycle();
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = mHeight = Math.min(mWidth,mHeight);
        if (mGestureLockViews == null){
            mGestureLockViews = new GestureLockView[mCount * mCount];
            //每个Gesture宽度
            mGestureLockViewWidth = (int) ((4 * mWidth * 1.0f)/(5 * mCount + 1));
            //间距
            mMarginGestureLockView = (int) (mGestureLockViewWidth * 0.25f);
            //画笔宽度
            mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);
            for (int i = 0; i < mGestureLockViews.length; i++) {
                mGestureLockViews[i] = new GestureLockView(getContext(),mColorNoFingerInner,mColorNoFingerOuter,mColorFingerOn,mColorFingerUp);
                mGestureLockViews[i].setId(i+1);
                //设置位置，定位
                RelativeLayout.LayoutParams lockerParams = new RelativeLayout.LayoutParams(mGestureLockViewWidth,mGestureLockViewWidth);
                int rightMargin = mMarginGestureLockView;
                int bottomMargin = mMarginGestureLockView;
                int topMargin = 0;
                int leftMargin = 0;
                //第一行
                if (i>=0 && i < mCount){
                    topMargin = mMarginGestureLockView;
                }
                //第一列
                if (i % mCount == 0){
                    leftMargin = mMarginGestureLockView;
                }

                //不是每行第一个放在前一个右边
                if (i % mCount != 0){
                    lockerParams.addRule(RelativeLayout.RIGHT_OF,mGestureLockViews[i-1].getId());
                }
                //第二行开始，设置为上一行同一位置view下面
                if (i > mCount-1){
                    lockerParams.addRule(RelativeLayout.BELOW,mGestureLockViews[i-mCount].getId());
                }
                lockerParams.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);
                mGestureLockViews[i].setCurrentMode(GestureLockView.Mode.STATUS_NO_FINGER);
                addView(mGestureLockViews[i],lockerParams);
                Log.e(TAG, "mWidth = " + mWidth + " ,  mGestureViewWidth = "
                        + mGestureLockViewWidth + " , mMarginBetweenLockView = "
                        + mMarginGestureLockView);

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                //重置
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                mPaint.setColor(mColorFingerOn);
                mPaint.setAlpha(50);
                GestureLockView child = getChildIdByPos(x,y);
                if (child != null){
                    int cid = child.getId();
                    if (!mChoose.contains(cid)){
                        mChoose.add(cid);
                        child.setCurrentMode(GestureLockView.Mode.STATUS_FINGER_ON);
                        if (mOnGestureLockViewListener != null){
                            mOnGestureLockViewListener.onBlockSelected(cid);
                        }
                        mLastPathX = (child.getLeft() + child.getRight())/2;
                        mLastPathY = (child.getTop() + child.getBottom())/2;
                        //第一个点
                        if (mChoose.size() == 1){
                            mPath.moveTo(mLastPathX,mLastPathY);
                        }else {
                            mPath.lineTo(mLastPathX,mLastPathY);
                        }
                    }
                }
                //指引线终点
                mTmpTarget.x = x;
                mTmpTarget.y = y ;
                break;
            case MotionEvent.ACTION_UP:
                mPaint.setColor(mColorFingerUp);
                mPaint.setAlpha(50);
                this.mTryTimes--;
                //回调是否成功
                if (mOnGestureLockViewListener != null && mChoose.size() > 0){
                    mOnGestureLockViewListener.onGestureEvent(checkAnswer());
                    if (this.mTryTimes == 0){
                        mOnGestureLockViewListener.onUnmatchedExceedBoundary();
                    }
                }
                //将终点设置位置为起点，即取消指引线
                mTmpTarget.x = mLastPathX;
                mTmpTarget.y = mLastPathY;
                
                //改变子元素的状态为up
                changeItemMode();

                //计算每个元素箭头需要旋转的角度
                for (int i = 0; i  < mChoose.size() -1; i++) {
                    //获取id
                    int childId = mChoose.get(i);
                    int nextChildId = mChoose.get(i+1);
                    //获取view
                    GestureLockView startChild = (GestureLockView) findViewById(childId);
                    GestureLockView endChild = (GestureLockView) findViewById(nextChildId);
                    int dx = endChild.getLeft() - startChild.getLeft();
                    int dy = endChild.getTop() - startChild.getTop();
                    //计算角度
                    int angle = (int) (Math.toDegrees(Math.atan2(dy,dx)) + 90);
                    startChild.setArrowDegree(angle);
                }
                break;
        }
        invalidate();
        return true;
    }

    private void changeItemMode() {
        for (GestureLockView view :
                mGestureLockViews) {
            if (mChoose.contains(view.getId())){
                view.setCurrentMode(GestureLockView.Mode.STATUS_FINGER_UP);
            }
        }
    }

    /**
     * 检查绘制是否正确
     * @return
     */
    private boolean checkAnswer() {
        if (answer.length != mChoose.size()){
            return false;
        }
        for (int i = 0; i < answer.length; i++) {
            if (answer[i] != mChoose.get(i)){
                return false;
            }
        }
        return true;
    }

    /**
     * 根据点击位置获取GestureView Id
     * @param x
     * @param y
     */
    private GestureLockView getChildIdByPos(int x, int y) {
        for (GestureLockView view:
             mGestureLockViews) {
            if (checkPostionInChild(view,x,y)){
                return view;
            }
        }
        return null;
    }

    /**
     * 判断当前位置是否在View上
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean checkPostionInChild(GestureLockView view, int x, int y) {
        int padding = (int) (mGestureLockViewWidth * 0.15f);
        if (x >= view.getLeft() + padding && y >= view.getTop() + padding && x <= view.getRight() -padding && y <= view.getBottom() - padding){
            return true;
        }
        return false;
    }

    /**
     * 重置
     */
    private void reset() {
        mChoose.clear();
        mPath.reset();
        for (GestureLockView gestureVeiw :
                mGestureLockViews) {
            gestureVeiw.setCurrentMode(GestureLockView.Mode.STATUS_NO_FINGER);
            gestureVeiw.setArrowDegree(-1);
        }
    }
    /**
     * 设置回调接口
     *
     * @param listener
     */
    public void setOnGestureLockViewListener(OnGestureLockViewListener listener)
    {
        this.mOnGestureLockViewListener = listener;
    }
    /**
     * 对外公布设置答案的方法
     *
     * @param answer
     */
    public void setAnswer(int[] answer)
    {
        this.answer = answer;
    }


    /**
     * 设置最大实验次数
     *
     * @param boundary
     */
    public void setUnMatchExceedBoundary(int boundary)
    {
        this.mTryTimes = boundary;
    }
    @Override
    public void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);
        //绘制GestureLockView间的连线
        if (mPath != null)
        {
            canvas.drawPath(mPath, mPaint);
        }
        //绘制指引线
        if (mChoose.size() > 0)
        {
            if (mLastPathX != 0 && mLastPathY != 0)
                canvas.drawLine(mLastPathX, mLastPathY, mTmpTarget.x,
                        mTmpTarget.y, mPaint);
        }

    }
    public interface OnGestureLockViewListener
    {
        /**
         * 单独选中元素的Id
         *
         */
        public void onBlockSelected(int cId);

        /**
         * 是否匹配
         *
         * @param matched
         */
        public void onGestureEvent(boolean matched);

        /**
         * 超过尝试次数
         */
        public void onUnmatchedExceedBoundary();
    }
}
