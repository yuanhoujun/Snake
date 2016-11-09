package me.foji.snake;

import android.support.v7.app.AppCompatActivity;

import me.foji.snake.engine.SnakeEngine;
import me.foji.snake.manager.SnakeManager;
/**
 * 使用该类开启滑动关闭功能
 *
 * @author Scott Smith  @Date 2016年10月2016/10/18日 11:53
 */
public class Snake {
    private static SnakeEngine mSnakeEngine;

    public static SnakeEngine init(AppCompatActivity activity) {
        if(null == mSnakeEngine) {
            mSnakeEngine = SnakeEngine.get();
        }
        mSnakeEngine.activity(activity);
        return mSnakeEngine;
    }

    public static SlideToCloseBuilder init(me.foji.snake.v4.app.Fragment fragment) {
        return SlideToCloseBuilder.get().fragment(fragment);
    }

    public static SlideToCloseBuilder init(me.foji.snake.app.Fragment fragment) {
        return SlideToCloseBuilder.get().fragment(fragment);
    }

    public static void onDestroy(AppCompatActivity activity) {
        SnakeManager.get().remove(activity);
        SnakeManager.get().removeAnimator(activity);
        SnakeManager.get().removeOpenStatus(activity);
    }

    public static void onDestroy(me.foji.snake.v4.app.Fragment fragment) {
        SlideToCloseBuilder.get().clear(fragment);
    }

    public static void onDestroy(me.foji.snake.app.Fragment fragment) {
        SlideToCloseBuilder.get().clear(fragment);
    }
}
