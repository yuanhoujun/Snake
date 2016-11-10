package me.foji.snake.manager;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 负责数据管理，例如：Activity的保存，移除等，动画数据的保存等
 *
 * @author Scott
 */
public class SnakeManager {
    private HashMap<AppCompatActivity,SnakeAnimator> animators = new HashMap<>();
    private HashMap<AppCompatActivity,Boolean> openStatuses = new HashMap<>();
    private ArrayList<AppCompatActivity> activities = new ArrayList<>();

    private static SnakeManager instance;

    private SnakeManager() {}

    public static SnakeManager get() {
        if(null == instance) {
            instance = new SnakeManager();
        }
        return instance;
    }

    public AppCompatActivity currentActivity() {
        if(activities.size() > 0) return activities.get(0);
        return null;
    }

    public AppCompatActivity lastActivity() {
        if(activities.size() > 1) return activities.get(1);
        return null;
    }

    public boolean isFirst(AppCompatActivity activity) {
        return activities.size() > 0 && activities.indexOf(activity) == activities.size() - 1;
    }

    public void insert(AppCompatActivity appCompatActivity) {
        if(!activities.contains(appCompatActivity)) {
            activities.add(0,appCompatActivity);
        }
    }

    public void remove(AppCompatActivity appCompatActivity) {
        activities.remove(appCompatActivity);
    }

    public void put(AppCompatActivity activity, SnakeAnimator animator) {
        animators.put(activity,animator);
    }

    public SnakeAnimator getAnimator(AppCompatActivity appCompatActivity) {
        return animators.get(appCompatActivity);
    }

    public void put(AppCompatActivity appCompatActivity,Boolean status) {
        openStatuses.put(appCompatActivity,status);
    }

    public boolean getOpenStatus(AppCompatActivity appCompatActivity) {
        Boolean status = openStatuses.get(appCompatActivity);
        if(null == status) {
            return true;
        }
        return status;
    }

    public void removeAnimator(AppCompatActivity appCompatActivity) {
        animators.remove(appCompatActivity);
    }

    public void removeOpenStatus(AppCompatActivity appCompatActivity) {
        openStatuses.remove(appCompatActivity);
    }
}
