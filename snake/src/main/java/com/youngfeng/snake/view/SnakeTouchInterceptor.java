package com.youngfeng.snake.view;

import android.view.MotionEvent;

/**
 * Snake touch intercetptor.
 *
 * @author Scott Smith 2017-12-29 14:17
 */
public interface SnakeTouchInterceptor {
    /**
     * Like view#onInterceptTouchEvent
     *
     * @param event motion event
     *
     * @return the same to {@link android.view.ViewGroup#onInterceptHoverEvent(MotionEvent)}
     */
    boolean onInterceptTouchEvent(MotionEvent event);

    /**
     * Like view#onTouch
     *
     * @param event motion event
     *
     * @return the same to {@link android.view.View#onTouchEvent(MotionEvent)}
     */
    boolean onTouchEvent(MotionEvent event);
}
