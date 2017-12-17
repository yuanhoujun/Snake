package com.youngfeng.snake.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

/**
 * Soft keyboard utils
 *
 * @author Scott Smith 2017-12-17 10:28
 */
public class SoftKeyboardHelper {
    private static final int MESSAGE_WHAT_HIDE_KEYBOARD = 1;
    private static Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_WHAT_HIDE_KEYBOARD: {
                    if(msg.obj instanceof Fragment) {
                        hideKeyboard((Fragment) msg.obj);
                    }
                    if(msg.obj instanceof Activity) {
                        hideKeyboard((Activity) msg.obj);
                    }
                    break;
                }
            }
        }
    };

    public static boolean hideKeyboard(@NonNull Fragment fragment) {
        if(fragment.isDetached() || fragment.isRemoving() || null == fragment.getActivity()) return true;

        InputMethodManager inputMethodManager = (InputMethodManager) fragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View rootView = fragment.getView();
        if(rootView instanceof ViewGroup) {
            View focusedChild = ((ViewGroup)rootView).getFocusedChild();
            if(null != focusedChild) {
                return inputMethodManager.hideSoftInputFromWindow(focusedChild.getWindowToken(), 0);
            }
        }

        return true;
    }

    public static void hideKeyboardDelayed(@NonNull final Fragment fragment, int delayMillis) {
        Message message = mMainHandler.obtainMessage(MESSAGE_WHAT_HIDE_KEYBOARD, fragment);
        mMainHandler.sendMessageDelayed(message, delayMillis);
    }

    public static boolean hideKeyboard(@NonNull Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedChild = activity.getCurrentFocus();
        if(null != focusedChild) {
            return inputMethodManager.hideSoftInputFromWindow(focusedChild.getWindowToken(), 0);
        }

        return true;
    }

    public static void hideKeyboardDelayed(@NonNull final Activity activity, int delayMillis) {
       Message message = mMainHandler.obtainMessage(MESSAGE_WHAT_HIDE_KEYBOARD, activity);
       mMainHandler.sendMessageDelayed(message, delayMillis);
    }
}
