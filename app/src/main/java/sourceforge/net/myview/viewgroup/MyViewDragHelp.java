package sourceforge.net.myview.viewgroup;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created：2018/4/12 on 19:29
 * Author:gaideng on dg
 * Description:自定义拖拽类
 */

public class MyViewDragHelp extends LinearLayout{
    private ViewDragHelper mHelper;
    private static final String TAG = "MyViewDragHelp";
    private View mDragView;
    private View mAutoBack;
    /**
     * 记录自动返回时的点
     */
    private Point mAutoBackPrePoint = new Point();
    /**
     * 边界捕获，禁止直接移动
     */
    private View mEdgeTracker;
    public MyViewDragHelp(Context context) {
        this(context,null);
    }

    public MyViewDragHelp(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewDragHelp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView || child == mAutoBack;
            }

            /**
             * 手指释放时回调
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
//                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild == mAutoBack){
                    mHelper.settleCapturedViewAt(mAutoBackPrePoint.x,mAutoBackPrePoint.y);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                mHelper.captureChildView(mEdgeTracker,pointerId);

            }

            /**
             * 当子view消耗事件
             * @param child
             * @return
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - leftBound;

                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound = getPaddingTop();
                int bottomBound = getHeight() - child.getWidth() - topBound;
                int newTop = Math.min(Math.max(top,topBound),bottomBound);
                Log.i(TAG, "clampViewPositionVertical: \ntopBound:" + topBound + "\ntop:" + top + "\nbottomBound:"+ bottomBound + "\nnewTop:" + newTop);
                return newTop;
            }
        });
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.i(TAG, "computeScroll: ");
        if (mHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //记录移动时位置
        mAutoBackPrePoint.x = mAutoBack.getLeft();
        mAutoBackPrePoint.y = mAutoBack.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = getChildAt(0);
        mAutoBack = getChildAt(1);
        mEdgeTracker = getChildAt(2);
    }
}
