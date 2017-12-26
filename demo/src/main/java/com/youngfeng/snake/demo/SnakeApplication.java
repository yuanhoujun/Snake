package com.youngfeng.snake.demo;

import android.app.Application;

import com.youngfeng.snake.Snake;

/**
 * Snake application class.
 *
 * @author Scott Smith 2017-12-24 21:29
 */
public class SnakeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Snake.init(this);
    }
}
