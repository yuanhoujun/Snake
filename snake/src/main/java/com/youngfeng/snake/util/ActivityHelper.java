package com.youngfeng.snake.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Activity utils
 *
 * @author Scott Smith 2017-12-17 17:00
 */
public class ActivityHelper {

    public static void convertFromTranslucent(Activity activity) {
        try {
            @SuppressLint("PrivateApi")
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable e) {
        }
    }

    public static void convertToTranslucent(@NonNull Activity activity, TranslucentConversionListener listener) {
        if (activity.isFinishing()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertToTranslucentAfterLollipop(activity, listener);
        } else {
            if(null != listener) {
                listener.onTranslucentConversionComplete(true);
            }
        }
    }

    public static void convertToTranslucent(@NonNull Activity activity) {
        convertToTranslucent(activity, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void convertToTranslucentAfterLollipop(@NonNull Activity activity, TranslucentConversionListener conversionListener) {
        try {
            @SuppressLint("PrivateApi")
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                    break;
                }
            }

            ConversionInvocationHandler invocationHandler = new ConversionInvocationHandler(new WeakReference<TranslucentConversionListener>(conversionListener));
            Object newProxy = Proxy.newProxyInstance(Activity.class.getClassLoader(), new Class<?>[]{translucentConversionListenerClazz}, invocationHandler);

            @SuppressLint("PrivateApi")
            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz, ActivityOptions.class);
            convertToTranslucent.setAccessible(true);
            long start = System.currentTimeMillis();
            convertToTranslucent.invoke(activity, newProxy, options);
            long end = System.currentTimeMillis();
            Logger.d("convertToTranslucentAfterLollipop, time = " + (end - start));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static int getWindowBackgroundResourceId(@NonNull Activity activity) {
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        final int backgroundResourceId = a.getResourceId(0, 0);
        a.recycle();

        return backgroundResourceId;
    }

    public static boolean isTranslucent(@NonNull Activity activity) {
        try {
            TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                    android.R.attr.windowIsTranslucent
            });
            boolean isTranslucent = a.getBoolean(0, false);
            a.recycle();

            return isTranslucent;
        } catch (Throwable e) {
            return false;
        }
    }
}
