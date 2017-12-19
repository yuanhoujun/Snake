package com.youngfeng.snake.config;

import android.util.AndroidRuntimeException;

/**
 * Snake config exception.
 *
 * @author Scott Smith 2017-12-19 14:09
 */
public class SnakeConfigException extends AndroidRuntimeException {

    public SnakeConfigException() {
        super();
    }

    public SnakeConfigException(String name) {
        super(name);
    }

    public SnakeConfigException(String name, Throwable cause) {
        super(name, cause);
    }

    public SnakeConfigException(Exception cause) {
        super(cause);
    }
}
