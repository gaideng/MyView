package sourceforge.net.myview.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created：2018/4/11 on 15:59
 * Author:gaideng on dg
 * Description:
 */

public class MyViewGroup extends ViewGroup {
    private Context mContext;
    private int screenHeight;
    public MyViewGroup(Context context) {
        this(context,null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        screenHeight = getScreenSize((Activity)context)[1];
    }

    private int[] getScreenSize(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels,metrics.heightPixels};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int mLeft = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int widthChild = child.getMeasuredWidth();
            int heightChild = child.getMeasuredHeight();
            //垂直方向居中
            int top = (getMeasuredHeight() - heightChild)/2;
            child.layout(mLeft,top,mLeft + widthChild,top + heightChild);
            //设置下一个left
            mLeft += widthChild;
        }
    }
}
