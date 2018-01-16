package com.youngfeng.snake.util;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

/**
 * It keep a Activity instance and its info
 *
 * @author Scott Smith 2017-12-18 15:00
 */
public class ActivityInstance {
    public @IdRes int originBackgroundResourceId;
    public @NonNull Activity activity;
    public boolean isTranlucent = false;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ActivityInstance) {
            return ((ActivityInstance) obj).activity == activity;
        }
        return super.equals(obj);
    }
}
