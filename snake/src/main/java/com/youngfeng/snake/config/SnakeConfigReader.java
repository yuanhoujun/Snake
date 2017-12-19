package com.youngfeng.snake.config;

import android.app.Application;

/**
 * Snake config reader.
 *
 * @author Scott Smith 2017-12-19 08:41
 */
public class SnakeConfigReader {
    private Application mApplication;
    private static SnakeConfigReader instance;
    private SnakeConfig mSnakeConfig = new SnakeConfig();

    private SnakeConfigReader() {
    }

    public static synchronized SnakeConfigReader get() {
        if(null == instance) {
            instance = new SnakeConfigReader();
        }

        return instance;
    }

    public void init(Application application) {
        mApplication = application;


    }

    public SnakeConfig config() {
        return mSnakeConfig;
    }
}
