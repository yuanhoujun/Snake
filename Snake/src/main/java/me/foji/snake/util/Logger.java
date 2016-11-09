package me.foji.snake.util;

import android.util.Log;

/**
 * @author Scott Smith  @Date 2016年08月16/8/8日 19:08
 */
public class Logger {
    private static final String TAG = "Snake";

    public static void v(String message) {
        Log.v(TAG,message);
    }

    public static void e(String message) {
        Log.e(TAG,message);
    }
}
