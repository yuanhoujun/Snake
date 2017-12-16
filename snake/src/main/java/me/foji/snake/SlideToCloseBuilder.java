package me.foji.snake;

import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;

import java.util.HashMap;

import me.foji.snake.widget.SnakeFrameLayout;

/**
 * 使用该类构建SnakeEngine，SnakeEngine用于完成滑动关闭逻辑
 *
 * @author Scott Smith
 */
public class SlideToCloseBuilder {
    private Fragment mSupportFragment;
    private android.app.Fragment mFragment;
    private int mShadowStartColor = SnakeFrameLayout.DEFAULT_SHADOW_START_COLOR;
    private int mShadowEndColor = SnakeFrameLayout.DEFAULT_SHADOW_END_COLOR;
    private int minVelocity;

    private HashMap<android.app.Fragment,Boolean> mOpenStatuses = new HashMap<>();
    private HashMap<android.app.Fragment,Boolean> mRunStatuses = new HashMap<>();

    private HashMap<Fragment,Boolean> mSupportOpenStatuses = new HashMap<>();
    private HashMap<Fragment,Boolean> mSupportRunStatuses = new HashMap<>();

    private static SlideToCloseBuilder instanse;

    private SlideToCloseBuilder() {}

    public static SlideToCloseBuilder get() {
        if(null == instanse) instanse = new SlideToCloseBuilder();
        return instanse;
    }

    public SlideToCloseBuilder fragment(Fragment fragment) {
        mSupportFragment = fragment;
        return this;
    }

    public SlideToCloseBuilder fragment(android.app.Fragment fragment) {
        mFragment = fragment;
        return this;
    }

    public SlideToCloseBuilder shadowStartColor(@ColorInt int color) {
        mShadowStartColor = color;
        return this;
    }

    public SlideToCloseBuilder shadowEndColor(@ColorInt int color) {
        mShadowEndColor = color;
        return this;
    }

    public SlideToCloseBuilder minVelocity(int minVelocity) {
        this.minVelocity = minVelocity;
        return this;
    }

    public SlideToCloseBuilder enable(boolean enable) {
        if(null != mSupportFragment) {
            mSupportOpenStatuses.put(mSupportFragment,enable);
        } else {
            mOpenStatuses.put(mFragment,enable);
        }
        return this;
    }

    public SlideToCloseBuilder enable(me.foji.snake.app.Fragment fragment, boolean enable) {
        mOpenStatuses.put(fragment,enable);
        return this;
    }

    public SlideToCloseBuilder enable(me.foji.snake.v4.app.Fragment fragment, boolean enable) {
        mSupportOpenStatuses.put(fragment,enable);
        return this;
    }

    public void start() {
        if(null != mSupportFragment) {
            mSupportRunStatuses.put(mSupportFragment,true);
        } else {
            mRunStatuses.put(mFragment,true);
        }
    }

    public void start(me.foji.snake.app.Fragment fragment) {
        mRunStatuses.put(fragment, true);
    }

    public void start(Fragment fragment) {
        mSupportRunStatuses.put(fragment, true);
    }

    public boolean getOpenStatus(Fragment fragment) {
        Boolean enable = mSupportOpenStatuses.get(fragment);
        return enable == null ? true : enable;
    }

    public boolean getOpenStatus(android.app.Fragment fragment) {
        Boolean enable = mOpenStatuses.get(fragment);
        return enable == null ? true : enable;
    }

    public boolean isStart(Fragment fragment) {
        Boolean start = mSupportRunStatuses.get(fragment);
        return start == null ? false : start;
    }

    public boolean isStart(android.app.Fragment fragment) {
        Boolean start = mRunStatuses.get(fragment);
        return start == null ? false : start;
    }

    public int shadowStartColor() {
        return mShadowStartColor;
    }

    public int shadowEndColor() {
        return mShadowEndColor;
    }

    public int minVelocity() {
        return minVelocity;
    }

    public void clear(Fragment fragment) {
        mSupportOpenStatuses.remove(fragment);
        mSupportRunStatuses.remove(fragment);
        mSupportFragment = null;
    }

    public void clear(me.foji.snake.app.Fragment fragment) {
        mOpenStatuses.remove(fragment);
        mRunStatuses.remove(fragment);
        mFragment = null;
    }
}
