package me.foji.snake.manager;

import android.animation.ObjectAnimator;

/**
 * Created by scott on 2016/10/21.
 */

public class SnakeAnimator {
    public ObjectAnimator closeAnimator;
    public ObjectAnimator restoreAnimator;

    public SnakeAnimator(ObjectAnimator closeAnimator, ObjectAnimator restoreAnimator) {
        this.closeAnimator = closeAnimator;
        this.restoreAnimator = restoreAnimator;
    }
}
