package sourceforge.net.myview.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created：2018/4/13 on 13:54
 * Author:gaideng on dg
 * Description:
 */

public class GestureLockView extends View {
    /**
     * 三种状态
     */
    enum Mode{
        STATUS_NO_FINGER,STATUS_FINGER_ON,STATUS_FINGER_UP;
    }

    /**
     * 四种颜色,用户可以自定义，初始由Group传入
     */
    private int mColorNoFingerInner;
    private int mColorNoFingerOuter;
    private int mColorFingerOn;
    private int mColorFingerUp;

    /**
     * GestureLockView当前状态
     */
    private Mode mCurrentMode = Mode.STATUS_NO_FINGER;

    private int mWidth;
    private int mHeight;
    private int mRadius;
    private int mStrokeWidth = 2;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;

    /**
     * 箭头
     */
    private float mArrowRate = 0.333f;
    private int mArrowDegree = -1;
    private Path mArrowPath;
    private float mInnerRadiusRate = 0.3f;




    public GestureLockView(Context context,int colorNoFingerInner,int colorNoFingerOuter,int colorFingerOn,int colorFingerUp) {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOuter = colorNoFingerOuter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUp = colorFingerUp;
        mPaint = new Paint();
        mArrowPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(widthMeasureSpec);
        mWidth = Math.min(mWidth,mHeight);
        mRadius = mCenterX = mCenterY = mWidth/2;
        mRadius -= mStrokeWidth/2;

        //绘制三角形
        float mArrowLength = mWidth/2 * mArrowRate;
        mArrowPath.moveTo(mWidth/2,mStrokeWidth + 2);
        mArrowPath.lineTo(mWidth/2-mArrowLength,mStrokeWidth + 2 + mArrowLength);
        mArrowPath.lineTo(mWidth/2 + mArrowLength,mStrokeWidth + 2 + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentMode){
            case STATUS_FINGER_ON:
                //绘制外圆
                mPaint.setColor(mColorFingerOn);
                mPaint.setStrokeWidth(mStrokeWidth);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
                //内圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX,mCenterY,mRadius * mInnerRadiusRate,mPaint);
                break;
            case STATUS_FINGER_UP:
                //绘制外圆
                mPaint.setColor(mColorFingerUp);
                mPaint.setStrokeWidth(mStrokeWidth);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
                //内圆
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mCenterX,mCenterY,mRadius * mInnerRadiusRate,mPaint);
                //画箭头
                drawArrow(canvas);
                break;
            case STATUS_NO_FINGER:
                //绘制外圆
                mPaint.setColor(mColorNoFingerInner);
                mPaint.setStrokeWidth(mStrokeWidth);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(mCenterX,mCenterY,mRadius,mPaint);
                //内圆
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mColorNoFingerOuter);
                canvas.drawCircle(mCenterX,mCenterY,mRadius * mInnerRadiusRate,mPaint);
                break;
        }

    }

    /**
     * 绘制箭头
     * @param canvas
     */
    private void drawArrow(Canvas canvas) {
        if (mArrowDegree != -1){
            mPaint.setStyle(Paint.Style.FILL);
            canvas.save();
            canvas.rotate(mArrowDegree,mCenterX,mCenterY);
            canvas.drawPath(mArrowPath,mPaint);
            canvas.restore();
        }
    }

    public Mode getCurrentMode() {
        return mCurrentMode;
    }

    /**
     * 设置当前模式，并重新绘制
     * @param currentMode
     */
    public void setCurrentMode(Mode currentMode) {
        mCurrentMode = currentMode;
        invalidate();
    }

    public int getArrowDegree() {
        return mArrowDegree;
    }

    public void setArrowDegree(int arrowDegree) {
        mArrowDegree = arrowDegree;
    }
}
