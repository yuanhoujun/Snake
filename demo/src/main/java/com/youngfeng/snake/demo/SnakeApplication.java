package com.youngfeng.snake.demo;

import android.app.Application;

import com.tencent.bugly.Bugly;
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
        Snake.setDebug(BuildConfig.DEBUG);
        Snake.init(this);
        Bugly.init(this, Constant.BUGLY_APP_ID, BuildConfig.DEBUG);
    }
}
