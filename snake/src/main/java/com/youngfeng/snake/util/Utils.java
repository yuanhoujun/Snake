package com.youngfeng.snake.util;

import android.content.Context;
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

    // 获取屏幕宽度
    public static int screenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
