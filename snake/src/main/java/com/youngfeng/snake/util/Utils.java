package com.youngfeng.snake.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * 实用类
 *
 * @author Scott Smith
 */
public class Utils {
    public static float dp2px(Context context,float dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

    public static int changeAlpha(@ColorInt int color,int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb(alpha,red,green,blue);
    }

    /**
     * Return back to home.
     */
    public static void backToHome(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
