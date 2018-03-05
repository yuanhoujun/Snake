package com.youngfeng.snake.support.v4.app;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.animation.SnakeAnimationController;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.config.SnakeConfigException;
import com.youngfeng.snake.util.FragmentManagerHelper;
import com.youngfeng.snake.view.SnakeHackLayout;
import com.youngfeng.snake.view.SnakeTouchInterceptor;

import java.lang.reflect.Field;

/**
 * Fragment
 *
 * @author Scott Smith 2018-03-04 14:45
 */
public class Fragment extends android.support.v4.app.Fragment implements SnakeAnimationController {
    private SnakeHackLayout mSnakeLayout;
    private boolean mDisableAnimation;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        replaceWithSnakeLayout(view);
    }

    private void replaceWithSnakeLayout(View view) {
        FragmentManagerHelper fragmentManagerHelper = FragmentManagerHelper.get(getActivity().getSupportFragmentManager());

        if(null == view || fragmentManagerHelper.supportBackStackEmpty()) return;

        EnableDragToClose enableDragToClose = getClass().getAnnotation(EnableDragToClose.class);
        if(null != enableDragToClose && !enableDragToClose.value()) return;

        mSnakeLayout = SnakeHackLayout.getLayout(getActivity());

        if(view.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
            mSnakeLayout.addView(view);
            parent.addView(mSnakeLayout);
        }

        try {
            Field mView = android.support.v4.app.Fragment.class.getDeclaredField("mView");
            mView.setAccessible(true);
            mView.set(this, mSnakeLayout);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Snake.openDragToCloseForFragment(mSnakeLayout, this);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return Snake.wrap(super.onCreateAnimation(transit, enter, nextAnim), this);
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return Snake.wrap(super.onCreateAnimator(transit, enter, nextAnim), this);
    }

    /**
     * Turn the slide-off function on or off
     *
     * @param enable true: on, false: off
     */
    public void enableDragToClose(Boolean enable) {
        EnableDragToClose enableDragToClose = getClass().getAnnotation(EnableDragToClose.class);
        if(enable) {
            if(null == enableDragToClose || !enableDragToClose.value()) {
                throw new SnakeConfigException("If you want to dynamically turn the slide-off feature on or off, add the EnableDragToClose annotation to " + getClass().getName() + " and set to true");
            }
        }

        if(null != mSnakeLayout) {
            mSnakeLayout.ignoreDragEvent(!enable);
        }
    }

    /**
     * Add OnDragListener for drag event.
     *
     * @param onDragListener {@link com.youngfeng.snake.Snake.OnDragListener}
     */
    public void addOnDragListener(Snake.OnDragListener onDragListener) {
        if(null != mSnakeLayout && null != onDragListener) {
            mSnakeLayout.addOnDragListener(onDragListener);
        }
    }

    /**
     * Set custom touch interceptor.
     *
     * @param interceptor the touch interceptor
     */
    public void setCustomTouchInterceptor(SnakeTouchInterceptor interceptor) {
        if(null != mSnakeLayout && null != interceptor) {
            mSnakeLayout.setCustomTouchInterceptor(interceptor);
        }
    }

    /**
     * Turn the slide back to home function on or off
     *
     * @param enable true: on, false: off
     */
    public void enableSwipeUpToHome(Boolean enable) {
        if(null != mSnakeLayout) {
            mSnakeLayout.enableSwipeUpToHome(enable);
        }
    }

    @Override
    public void disableAnimation(boolean disable) {
        mDisableAnimation = disable;
    }

    @Override
    public boolean animationDisabled() {
        return mDisableAnimation;
    }
}
