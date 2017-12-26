package com.youngfeng.snake.demo.ui.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

/**
 * Translate linearlayout, support property animations.
 *
 * @author Scott Smith 2017-12-24 16:13
 */
public class TranslateLinearLayout extends LinearLayout {
    private float fractionX = 0f;
    private ViewTreeObserver.OnPreDrawListener mPreDrawListener = null;

    public TranslateLinearLayout(Context context) {
        super(context);
    }

    public TranslateLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TranslateLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TranslateLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setFractionX(final float fractionX) {
        this.fractionX = fractionX;
//
//        if(null == mPreDrawListener) {
//            mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
//                @Override
//                public boolean onPreDraw() {
//                    getViewTreeObserver().removeOnPreDrawListener(mPreDrawListener);
//                    setTranslateX(fractionX);
//                    return true;
//                }
//            };
//            getViewTreeObserver().addOnPreDrawListener(mPreDrawListener);
//        }
//
//        setTranslateX(fractionX);
    }

//    private void setTranslateX(float fractionX) {
//        int width = getWidth();
//        if(width <= 0f) return;
//
//        setTranslationX(width * fractionX);
//    }

    public float getFractionX() {
        return fractionX;
    }
}
