package com.youngfeng.snake.view;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Snake WebView.
 *
 * @author Scott Smith 2018-01-12 10:17
 */
public class SnakeWebView extends WebView {
    private GestureDetector mGestureDetector;
    private DragMode mDragMode = DragMode.BOTH;
    private OnDragListener onDragListener;
    private float mMinVelocity = 2000;
    private boolean isPause;

    public SnakeWebView(Context context) {
        super(context);
        init(context);
    }

    public SnakeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SnakeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SnakeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public SnakeWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init(context);
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(needIntercept(velocityX, velocityY)) {
                    if(null != onDragListener) {
                        onDragListener.onFling(velocityX, mDragMode);
                    }

                    if(velocityX > 0) {
                        if(canGoBack()) {
                            goBack();
                        }
                    } else {
                        if(canGoForward()) {
                            goForward();
                        }
                    }

                    return true;
                }
                return false;
            }
        });
    }

    private boolean needIntercept(float velocityX, float velocityY) {
        if(Math.abs(velocityX) > Math.abs(velocityY) && Math.abs(velocityX) > mMinVelocity && !isPause) {
            if(DragMode.BOTH == mDragMode) return true;
            if(DragMode.LEFT == mDragMode && velocityX < 0) return true;
            if(DragMode.RIGHT == mDragMode && velocityX > 0) return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mGestureDetector.onTouchEvent(event)) {
            return super.onTouchEvent(event);
        } else {
            return true;
        }
    }

    public void setMinVelocity(float minVelocity) {
        minVelocity = minVelocity;
    }

    public void setDragMode(DragMode mode) {
        mDragMode = mode;
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }

    public void pauseDragging() {
        isPause = true;
    }

    public void resumeDragging() {
        isPause = false;
    }

    public enum DragMode {
        LEFT, RIGHT, BOTH, NONE
    }

    public interface OnDragListener {
        void onFling(float velocityX, DragMode mode);
    }
}
