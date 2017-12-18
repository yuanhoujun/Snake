package me.foji.snake.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.youngfeng.snake.util.Utils;

/**
 * 用于控制页面滑动的布局文件
 *
 * @author Scott Smith  @Date 2016年10月2016/10/18日 12:08
 */
public class SnakeFrameLayout extends FrameLayout {
    public View mContentView;
    private ViewDragHelper mViewDragHelper;
    private OnHorizontalScrollListener onHorizontalScrollListener;
    private boolean isTouchEdge = false;
    private int mXRange = getResources().getDisplayMetrics().widthPixels;

    // 阴影Drawable
    private GradientDrawable mShadowDrawable;

    // 阴影边缘默认起始颜色
    public static final int DEFAULT_SHADOW_START_COLOR = Color.parseColor("#00000000");
    // 阴影边缘默认结束颜色
    public static final int DEFAULT_SHADOW_END_COLOR = Color.parseColor("#50000000");

    private int mShadowStartColor = DEFAULT_SHADOW_START_COLOR;
    private int mShadowEndColor = DEFAULT_SHADOW_END_COLOR;
    private int mShadowWidth = (int) Utils.dp2px(getContext(),15f);

    public SnakeFrameLayout(Context context, View decorChildView) {
        super(context);
        mContentView = decorChildView;
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                isTouchEdge = true;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mXRange;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if(left < 0) left = 0;
                left = isTouchEdge? left : 0;

                if(null != onHorizontalScrollListener && left > 0) {
                    onHorizontalScrollListener.onScroll(left);
                }

                return left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    int shadowStartColor = Utils.changeAlpha(mShadowStartColor, (int) (Color.alpha(mShadowStartColor) * (1 - (float)left / (float) mXRange)));
                    int shadowEndColor = Utils.changeAlpha(mShadowEndColor, (int) (Color.alpha(mShadowEndColor) * (1 - (float)left / (float) mXRange)));

                    mShadowDrawable.mutate();
                    mShadowDrawable.setColors(new int[] {shadowStartColor , shadowEndColor});
                }
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if(null != onHorizontalScrollListener) {
                    boolean isClose = xvel > mViewDragHelper.getMinVelocity();
                    if(!isClose) {
                        isClose = releasedChild.getLeft() > mXRange / 3;
                    }
                    onHorizontalScrollListener.onRelease(releasedChild.getLeft(),isClose, isTouchEdge);
                }
            }
        });
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        init(context);
    }

    public void setMinVelocity(float minVelocity) {
        mViewDragHelper.setMinVelocity(minVelocity);
    }

    public void setShadowStartColor(@ColorInt int shadowStartColor) {
        mShadowStartColor = shadowStartColor;
    }

    public void setShadowEndColor(@ColorInt int shadowEndColor) {
        mShadowEndColor = shadowEndColor;
    }

    public void setShadowWidth(int shadowWidth) {
        mShadowWidth = shadowWidth;
    }

    public void reset() {
        isTouchEdge = false;
        mContentView.setLeft(0);
        mContentView.invalidate();
    }

    private void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mContentView);

        mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,new int[] {mShadowStartColor,mShadowEndColor});
    }

    public void setOnHorizontalScrollListener(OnHorizontalScrollListener onHorizontalScrollListener) {
        this.onHorizontalScrollListener = onHorizontalScrollListener;
    }

     public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        canvas.save();
        int left = mContentView.getLeft() - mShadowWidth;
        int top = 0;
        int right = left + mShadowWidth;
        int bottom = getResources().getDisplayMetrics().heightPixels;

        mShadowDrawable.setBounds(left,top,right,bottom);
        mShadowDrawable.draw(canvas);

        canvas.restore();
    }

    public interface OnHorizontalScrollListener {
        void onScroll(int left);
        void onRelease(int left, boolean close, boolean isTouchEdge);
    }
}
