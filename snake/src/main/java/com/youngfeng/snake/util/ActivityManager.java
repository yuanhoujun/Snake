package com.youngfeng.snake.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Activity back stack manager.
 *
 * @author Scott Smith 2017-12-18 14:35
 */
public class ActivityManager {
    private static ActivityManager instance;
    private List<ActivityInstance> mActivityStack = new LinkedList<>();

    private ActivityManager() {}

    public static synchronized ActivityManager get() {
        if(null == instance) instance = new ActivityManager();
        return instance;
    }

    /**
     * Put an activity instance into Activity Stack.
     *
     * @param activity Activity instance
     */
    public void put(Activity activity) {
        ActivityInstance instance = new ActivityInstance();
        instance.activity = activity;
        instance.originBackgroundResourceId = ActivityHelper.getWindowBackgroundResourceId(activity);

        if(!mActivityStack.contains(instance)) {
            mActivityStack.add(0, instance);
        }
    }

    /**
     * Remove an Activity instance from activity stack
     *
     * @param activity Activity instance
     */
    public void remove(Activity activity) {
        Iterator<ActivityInstance> it = mActivityStack.iterator();
        while (it.hasNext()) {
            if(it.next().activity == activity) {
                it.remove();
                break;
            }
        }
    }

    /**
     * Get last Activity instance of current Activity.
     *
     * @param activity activity instance
     * @return the last activity of current activity stack.
     */
    public Activity getLastActivity(@NonNull Activity activity) {
        int index = indexOf(activity);

        if(index >= 0 && mActivityStack.size() > index + 1) {
            return mActivityStack.get(index + 1).activity;
        }

        return null;
    }

    public int indexOf(@NonNull Activity activity) {
        for(int i = 0;i < mActivityStack.size(); i++) {
            if(mActivityStack.get(i).activity == activity) return i;
        }

        return -1;
    }

    public boolean isRootActivity(@NonNull Activity activity) {
        return mActivityStack.size() > 0 && indexOf(activity) >= mActivityStack.size() - 1;
    }

    public void setWindowTranslucent(@NonNull Activity activity, boolean translucent) {
        int index = indexOf(activity);
        if(index >= 0) {
            ActivityInstance activityInstance = mActivityStack.remove(index);
            activityInstance.isTranlucent = translucent;
            mActivityStack.add(index, activityInstance);
        }
    }

    public ActivityInstance getActivityInstance(int index) {
        if(index < 0 || mActivityStack.isEmpty()) return null;

        return mActivityStack.get(index);
    }

    public boolean isTranslucent(@NonNull Activity activity) {
        ActivityInstance activityInstance = getActivityInstance(indexOf(activity));
        if(null != activityInstance) return activityInstance.isTranlucent;
        return false;
    }

    public ActivityInstance convertToActivityInstance(@NonNull Activity activity) {
        int index = indexOf(activity);
        if(index >= 0) return mActivityStack.get(index);

        return null;
    }

    public int getBackgroundResourceId(@NonNull Activity activity) {
        ActivityInstance activityInstance = convertToActivityInstance(activity);
        if(null != activityInstance) return activityInstance.originBackgroundResourceId;

        return -1;
    }

    public View getViewOfLastActivity(@NonNull Activity activity) {
        Activity lastActivity = getLastActivity(activity);
        if(null == lastActivity) return null;

        return ((ViewGroup)lastActivity.getWindow().getDecorView()).getChildAt(0);
    }
}
