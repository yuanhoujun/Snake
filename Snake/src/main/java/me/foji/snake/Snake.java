package me.foji.snake;

import android.support.v7.app.AppCompatActivity;

import me.foji.lifecyclebinder.LifeCycleBinder;
import me.foji.lifecyclebinder.OnLifeCycleChangedListener;
import me.foji.snake.annotations.SlideToClose;
import me.foji.snake.engine.SnakeEngine;
import me.foji.snake.manager.SnakeManager;
/**
 * 使用该类开启滑动关闭功能
 *
 * @author Scott Smith  @Date 2016年10月2016/10/18日 11:53
 */
public class Snake {
    private static SnakeEngine mSnakeEngine;

    public static void init(AppCompatActivity activity) {
        if(null == mSnakeEngine) {
            mSnakeEngine = SnakeEngine.get();
        }
        mSnakeEngine.activity(activity);
        bindLifeCycle(activity);
        processAnnotations(mSnakeEngine , activity);
    }

    private static void bindLifeCycle(final AppCompatActivity appCompatActivity) {
        LifeCycleBinder.bind(appCompatActivity, new OnLifeCycleChangedListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {
                Snake.onDestroy(appCompatActivity);
            }
        });
    }

    private static void processAnnotations(SnakeEngine snakeEngine , AppCompatActivity activity) {
        SlideToClose slideToClose = activity.getClass().getAnnotation(SlideToClose.class);
        if(null != slideToClose) {
            snakeEngine.enable(slideToClose.enable());
            snakeEngine.minVelocity(slideToClose.minVelocity());
            snakeEngine.shadowStartColor(slideToClose.shadowStartColor());
            snakeEngine.shadowEndColor(slideToClose.shadowEndColor());
        }
        snakeEngine.start();
    }

    private static void processAnnotations(me.foji.snake.v4.app.Fragment fragment) {
        SlideToClose slideToClose = fragment.getClass().getAnnotation(SlideToClose.class);
        if(null != slideToClose) {
            SlideToCloseBuilder.get().enable(slideToClose.enable());
            SlideToCloseBuilder.get().shadowStartColor(slideToClose.shadowStartColor());
            SlideToCloseBuilder.get().shadowEndColor(slideToClose.shadowEndColor());
            SlideToCloseBuilder.get().minVelocity(slideToClose.minVelocity());
        }
        SlideToCloseBuilder.get().start();
    }

    public static void init(me.foji.snake.v4.app.Fragment fragment) {
        SlideToCloseBuilder.get().fragment(fragment);
        bindLifeCycle(fragment);
        processAnnotations(fragment);
    }

    private static void bindLifeCycle(final me.foji.snake.v4.app.Fragment fragment) {
        LifeCycleBinder.bind(fragment, new OnLifeCycleChangedListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {
                Snake.onDestroy(fragment);
            }
        });
    }

    private static void processAnnotations(me.foji.snake.app.Fragment fragment) {
        SlideToClose slideToClose = fragment.getClass().getAnnotation(SlideToClose.class);
        if(null != slideToClose) {
            SlideToCloseBuilder.get().enable(slideToClose.enable());
            SlideToCloseBuilder.get().shadowStartColor(slideToClose.shadowStartColor());
            SlideToCloseBuilder.get().shadowEndColor(slideToClose.shadowEndColor());
            SlideToCloseBuilder.get().minVelocity(slideToClose.minVelocity());
        }
        SlideToCloseBuilder.get().start();
    }

    private static void bindLifeCycle(final me.foji.snake.app.Fragment fragment) {
        LifeCycleBinder.bind(fragment, new OnLifeCycleChangedListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDestroy() {
                Snake.onDestroy(fragment);
            }
        });
    }

    public static void init(me.foji.snake.app.Fragment fragment) {
        SlideToCloseBuilder.get().fragment(fragment);
        processAnnotations(fragment);
        bindLifeCycle(fragment);
    }

    @Deprecated
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
