package me.foji.snake.engine;

import android.support.v7.app.AppCompatActivity;

/**
 * 滑动关闭功能启动引擎
 *
 * @author Scott Smith  @Date 2016年10月2016/10/19日 14:31
 */
public abstract class  SnakeEngine {

    public static SnakeEngine get() {
        return new SnakeEngineImpl();
    }

    /**
     * 当前Activity
     * @param activity
     * @return
     */
    public abstract SnakeEngine activity(AppCompatActivity activity);
    /**
     * 开启或关闭滑动关闭功能
     *
     * @param enable true 开启 false 关闭
     * @return
     */
    public abstract SnakeEngine enable(boolean enable);

    /**
     * 最小滑动关闭速度, 默认2000
     *
     * @param minVelocity 滑动关闭速度,单位: px/s
     * @return
     */
    public abstract SnakeEngine minVelocity(int minVelocity);

    /**
     * 设置阴影部分起始颜色, 推荐使用默认设置
     *
     * @param color 左边缘阴影部分起始颜色
     */
    public abstract SnakeEngine shadowStartColor(int color);

    /**
     * 设置阴影部分结束颜色，推荐使用默认设置
     *
     * @param color 左边缘阴影部分结束颜色
     * @return
     */
    public abstract SnakeEngine shadowEndColor(int color);

    /**
     * 开启滑动关闭功能,必须调用该方法才能生效
     */
    public abstract void start();
}
