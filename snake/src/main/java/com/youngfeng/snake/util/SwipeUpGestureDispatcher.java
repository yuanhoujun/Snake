package com.youngfeng.snake.util;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Just for detecting gesture of swipe up from edge bottom .
 *
 * @author Scott Smith 2018-01-15 10:18
 */
public class SwipeUpGestureDispatcher {
    // Unit: dp
    private static final int EDGE_SIZE = 20;

    private int mEdgeSize;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private boolean isEdgeBottomTouched;
    private int mTouchSlop;

    private View mTargetView;
    private VelocityTracker mVelocityTracker;
    private OnSwipeUpListener onSwipeUpListener;

    public static abstract class OnSwipeUpListener {
        public void onSwipeUp(float velocityY, boolean isEdgeBottomTouched) {}
    }

    private SwipeUpGestureDispatcher(View view, OnSwipeUpListener onSwipeUpListener, int minVelocity, int edgeSize) {
        mTargetView = view;
        this.onSwipeUpListener = onSwipeUpListener;
        mEdgeSize = edgeSize;

        ViewConfiguration configuration = ViewConfiguration.get(view.getContext());
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumFlingVelocity = minVelocity;
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    public static SwipeUpGestureDispatcher create(@NonNull View view,
                                                  @NonNull OnSwipeUpListener onGestureListener) {
        ViewConfiguration configuration = ViewConfiguration.get(view.getContext());
        int minVelocity = configuration.getScaledMinimumFlingVelocity();
        int edgeSize = (int) (EDGE_SIZE * view.getContext().getResources().getDisplayMetrics().density + 0.5f);

        return create(view, minVelocity, edgeSize, onGestureListener);
    }

    public static SwipeUpGestureDispatcher create(@NonNull View view,
                                                  int minVelocity,
                                                  @NonNull OnSwipeUpListener onGestureListener) {
        int edgeSize = (int) (EDGE_SIZE * view.getContext().getResources().getDisplayMetrics().density + 0.5f);
        return create(view, minVelocity, edgeSize, onGestureListener);
    }

    public static SwipeUpGestureDispatcher create(@NonNull View view,
                                                  int minVelocity,
                                                  int edgSize,
                                                  @NonNull OnSwipeUpListener onGestureListener) {
        return new SwipeUpGestureDispatcher(view, onGestureListener, minVelocity, edgSize);
    }

    public void dispatch(MotionEvent event) {
        if(null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = event.getX();
                mInitialMotionY = event.getY();

                if(isEdgeBottomTouched(mInitialMotionY)) {
                    isEdgeBottomTouched = true;
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                float dx = event.getX() - mInitialMotionX;
                float dy = event.getY() - mInitialMotionY;
                if(dx * dx + dy * dy > mTouchSlop * mTouchSlop) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    float velocityX = mVelocityTracker.getXVelocity();
                    float velocityY = mVelocityTracker.getYVelocity();

                    if (Math.abs(velocityY) > Math.abs(velocityX)
                            && Math.abs(velocityY) > mMinimumFlingVelocity
                            && velocityY < 0) {
                        if (null != onSwipeUpListener) {
                            onSwipeUpListener.onSwipeUp(velocityY, isEdgeBottomTouched);
                        }
                    }
                }
                cancel();
                break;
            }
        }
    }

    private boolean isEdgeBottomTouched(float y) {
        return y > mTargetView.getBottom() - mEdgeSize;
    }

    public void setMinVelocity(int minVelocity) {
        mMinimumFlingVelocity = minVelocity;
    }

    public void cancel() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;

        isEdgeBottomTouched = false;
    }
}
