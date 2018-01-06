package com.youngfeng.snake.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.Animation;

/**
 * Animation factory.
 *
 * @author Scott Smith 2018-01-05 18:08
 */
public class AnimationFactory {

    public static Animation emptyAmiation() {
        Animation animation = new Animation() {};
        animation.setDuration(0);
        return animation;
    }

    public static Animator emptyAnimator() {
        Animator animator = ObjectAnimator.ofFloat(null, "alpha", 255, 255);
        animator.setDuration(0);
        return animator;
    }
}
