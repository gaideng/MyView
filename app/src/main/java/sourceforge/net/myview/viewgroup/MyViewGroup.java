package sourceforge.net.myview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created：2018/4/12 on 15:59
 * Author:gaideng on dg
 * Description:
 */

public class MyViewGroup extends ViewGroup {

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量子控件
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //wrap_content情况分析：计算子view宽度和高度计算出最大值
        int cTWidth = 0;
        int cBWidth = 0;
        int cLHeight = 0;
        int cRHeight = 0;
        int bWidth = 0;
        int bHeigh = 0;
        int count = getChildCount();
        MarginLayoutParams params;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            params = (MarginLayoutParams) child.getLayoutParams();
            if (i == 0 || i == 1){
                cTWidth += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            if (i == 2 || i == 3){
                cBWidth += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            }
            if (i == 0 || i == 2){
                cLHeight += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }
            if (i == 1 || i == 3){
                cRHeight += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            }
        }
        bWidth = Math.max(cTWidth,cBWidth);
        bHeigh = Math.max(cLHeight,cRHeight);

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? width:bWidth , heightMode == MeasureSpec.EXACTLY ? height : bHeigh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int cWidth = 0;
        int cHeigh = 0;
        MarginLayoutParams params = null;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            cWidth = child.getMeasuredWidth();
            cHeigh = child.getMeasuredHeight();
            params = (MarginLayoutParams) child.getLayoutParams();
            int cl = 0,ct = 0,cr = 0,cb = 0;
            switch (i){
                case 0:
                    cl = params.leftMargin;
                    ct = params.topMargin;
                    break;
                case 1:
                    cl = getWidth() - params.rightMargin - cWidth;
//                    cl = getWidth() - params.rightMargin - cWidth - params.leftMargin;
                    ct = params.topMargin;
                    break;
                case 2:
                    cl = params.leftMargin;
                    ct = getHeight() - params.bottomMargin - cHeigh;
                    break;
                case 3:
                    cl = getWidth() - params.rightMargin - cWidth;
//                    cl = getWidth() - params.rightMargin - cWidth - params.leftMargin;
                    ct = getHeight() - params.bottomMargin - cHeigh;
                    break;
            }
            cr = cl + cWidth;
            cb = ct + cHeigh;
            child.layout(cl,ct,cr,cb);
        }
    }
}
