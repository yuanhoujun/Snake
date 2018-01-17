package com.youngfeng.snake.view;

/**
 * Snake UI configurations, use with SnakeHackLayout.
 *
 * @author Scott Smith 2018-01-17 13:55
 */
public class SnakeUIConfig {
    public boolean allowPageLinkage = true;

    private SnakeUIConfig() {}

    public static SnakeUIConfig get() {
        return new SnakeUIConfig();
    }
}
