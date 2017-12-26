package com.youngfeng.snake.demo.ui.exception;

import android.util.AndroidRuntimeException;

/**
 * 在Activity中切换Fragment，如果没有指定容器ID，将抛出该异常
 *
 * @author Scott Smith 2017-07-06 11:41
 */
public class NoContainerIdException extends AndroidRuntimeException {

    public NoContainerIdException() {
    }

    public NoContainerIdException(String name) {
        super(name);
    }

    public NoContainerIdException(String name, Throwable cause) {
        super(name, cause);
    }

    public NoContainerIdException(Exception cause) {
        super(cause);
    }
}