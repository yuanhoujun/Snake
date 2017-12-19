package com.youngfeng.snake.config;

import android.support.annotation.ColorInt;

/**
 * Snake basic parameter configuration
 *
 * @author Scott Smith 2017-12-19 08:07
 */
public class SnakeConfig {
    /**
     * The root activity can be draging to close if you set this variable to true.
     */
    public boolean enableForRootActivity = false;

    /**
     * Only close the current page by fast swiping if you set this variable to true.
     */
    public boolean onlyListenToFastSwipe = false;

    /**
     * The minimum listening speed of rapid sliding.
     */
    public int minVelocity;

    /**
     * The will not draw the edge of page if you set this variable to true.
     */
    public boolean hideShadowOfEdge = false;

    /**
     * The start color of the edge shadows, direction: left -> right .
     */
    public @ColorInt int shadowStartColor;

    /**
     * The end color of the edge shadows, direction: left -> right .
     */
    public @ColorInt int shadowEndColor;
}
