package com.youngfeng.snake.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

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

    /**
     * Get navigation bar height.
     *
     * @param context Context
     * @return navigation bar height
     */
    public static int getNavigationHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");

        int navigationBarHeight = 0;
        if(resourceId > 0) {
            navigationBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return navigationBarHeight;
    }

    /**
     * Get whether navigation bar is visible.
     *
     * @param context Context
     * @return true: visible, false: invisible
     */
    public static boolean navigationBarVisible(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            Point realSize = new Point();
            display.getRealSize(realSize);

            return realSize.y != size.y;
        }else {
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

            if(hasMenuKey || hasBackKey) {
                return false;
            } else {
                return true;
            }
        }
    }
}
