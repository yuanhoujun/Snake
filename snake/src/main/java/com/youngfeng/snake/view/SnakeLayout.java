package com.youngfeng.snake.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import me.foji.snake.util.Utils;

/**
 * 使用该布局使Activity、Fragment根布局具备监听边缘滑动功能
 *
 * @author Scott Smith 2017-12-11 14:05
 */
public class SnakeLayout extends FrameLayout {
    private View mContentView;
    private ViewDragHelper mViewDragHelper;
    // 阴影Drawable
    private GradientDrawable mShadowDrawable;
    // 阴影边缘默认起始颜色
    public static final int DEFAULT_SHADOW_START_COLOR = Color.parseColor("#00000000");
    // 阴影边缘默认结束颜色
    public static final int DEFAULT_SHADOW_END_COLOR = Color.parseColor("#50000000");

    private int mShadowStartColor = DEFAULT_SHADOW_START_COLOR;
    private int mShadowEndColor = DEFAULT_SHADOW_END_COLOR;
    private int mShadowWidth = (int) Utils.dp2px(getContext(),15f);

    public SnakeLayout(@NonNull Context context, @NonNull View contentView) {
        super(context);
        mContentView = contentView;
        mShadowDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {mShadowStartColor, mShadowEndColor});

        init();
    }

    private void init() {
        addView(mContentView);

        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }
        });
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
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
