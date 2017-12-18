package me.foji.snake.engine;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import me.foji.snake.manager.SnakeAnimator;
import me.foji.snake.manager.SnakeManager;
import com.youngfeng.snake.util.Utils;
import me.foji.snake.widget.SnakeFrameLayout;

/**
 * 滑动关闭功能引擎实现
 *
 * @author Scott Smith  @Date 2016年10月2016/10/19日 14:34
 */
public class SnakeEngineImpl extends SnakeEngine {
    // 是否开启滑动关闭功能 (可以根据情况灵活配置)
    private boolean mEnable = true;
    // 最小的滑动关闭速度 (默认2000)
    private int minVelocity = 2000;
    // 阴影边缘起始颜色
    private int mShadowStartColor = SnakeFrameLayout.DEFAULT_SHADOW_START_COLOR;
    // 阴影边缘结束颜色
    private int mShadowEndColor = SnakeFrameLayout.DEFAULT_SHADOW_END_COLOR;
    // 动画总执行时间
    private final int AINMATOR_DURATION = 300;
    // 记录当前进入的Activity
    private AppCompatActivity currentActivity;

    public SnakeEngineImpl() {
    }

    @Override
    public SnakeEngine activity(AppCompatActivity activity) {
        currentActivity = activity;
        SnakeManager.get().insert(activity);
        return this;
    }

    @Override
    public SnakeEngine enable(boolean enable) {
        SnakeManager.get().put(currentActivity,enable);
        return this;
    }

    @Override
    public SnakeEngine minVelocity(int minVelocity) {
        this.minVelocity = minVelocity;
        return this;
    }

    @Override
    public SnakeEngine shadowStartColor(int color) {
        mShadowStartColor = color;
        return this;
    }

    @Override
    public SnakeEngine shadowEndColor(int color) {
        mShadowEndColor = color;
        return this;
    }

    @Override
    public void start() {
        final AppCompatActivity currentActivity = SnakeManager.get().currentActivity();
        activityDestroyAsset(currentActivity);

        // 检查当前Activity滑动关闭功能是否开启 (默认开启)
        // 栈底的Activity没必要开启滑动关闭功能
        boolean openStatus = SnakeManager.get().getOpenStatus(currentActivity) && !SnakeManager.get().isFirst(currentActivity);
        // 开启状态才处理滑动关闭逻辑
        if(openStatus) {
            ViewGroup currentDecorView = (ViewGroup) currentActivity.getWindow().getDecorView();
            final View currentContentView0 = currentDecorView.getChildAt(0);
            currentDecorView.removeView(currentContentView0);

            // 使用SnakeFrameLayout替换ContentView (注: 这里的ContentView并非真正的ContentView，而是ContentView的父控件)
            final SnakeFrameLayout newContentView = new SnakeFrameLayout(currentActivity,currentContentView0);
            newContentView.setMinVelocity(minVelocity);
            newContentView.setShadowStartColor(mShadowStartColor);
            newContentView.setShadowEndColor(mShadowEndColor);
            currentDecorView.addView(newContentView);

            AppCompatActivity lastActivity = SnakeManager.get().lastActivity();

            final View lastContentView0 = ((ViewGroup)lastActivity.getWindow().getDecorView()).getChildAt(0);
            final int screenW = Utils.screenWidth(currentActivity);
            newContentView.setOnHorizontalScrollListener(new SnakeFrameLayout.OnHorizontalScrollListener() {
                @Override
                public void onScroll(int left) {
                    Log.e("onScroll", "left = " + left);

                    final float ratio = (float)left / screenW - 1;
                    lastContentView0.setLeft((int) (ratio * Utils.dp2px(currentActivity,100f)));
                    lastContentView0.invalidate();
                }

                @Override
                public void onRelease(int left, boolean close, boolean isTouchEdge) {
                    Log.e("onRelease", "left = " + left + ", close = " + close + ", isTouchEdge = " + isTouchEdge);

                    // 关闭当前页面
                    if(isTouchEdge && close) {
                        SnakeAnimator animator = SnakeManager.get().getAnimator(currentActivity);
                        ObjectAnimator closeAnimator = null;
                        if(null != animator) {
                            closeAnimator = animator.closeAnimator;
                        } else {
                            animator = new SnakeAnimator(null,null);
                        }

                        if(null == closeAnimator) {
                            closeAnimator = ObjectAnimator.ofInt(currentContentView0,"left",left,screenW);
                        }

                        closeAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                currentActivity.finish();
                                currentActivity.overridePendingTransition(0,0);
                                lastContentView0.setLeft(0);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        closeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int) animation.getAnimatedValue();
                                float ratio =  1.0f * value / screenW - 1;
                                lastContentView0.setLeft((int) (ratio * Utils.dp2px(currentActivity,100f)));
                            }
                        });
                        // 注意：这里时间偶尔会出现负值，必须注意
                        closeAnimator.setDuration((long) (AINMATOR_DURATION * (1 - (float)left / (float)screenW)));
                        closeAnimator.setTarget(currentContentView0);
                        closeAnimator.setIntValues(left,screenW);
                        if(closeAnimator.isStarted()) {
                            closeAnimator.end();
                        }
                        closeAnimator.start();

                        animator.closeAnimator = closeAnimator;
                        SnakeManager.get().put(currentActivity,animator);
                    }

                    // 页面状态恢复
                    if(isTouchEdge && !close) {
                        SnakeAnimator animator = SnakeManager.get().getAnimator(currentActivity);
                        ObjectAnimator restoreAnimator = null;
                        if(null != animator) {
                            restoreAnimator = animator.restoreAnimator;
                        } else {
                            animator = new SnakeAnimator(null,null);
                        }


                        if(null == restoreAnimator ) {
                            restoreAnimator = ObjectAnimator.ofInt(currentContentView0,"left",left,0);
                            restoreAnimator.setDuration(300);
                        }

                        restoreAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                lastContentView0.setLeft(0);
                                newContentView.reset();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        restoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int) animation.getAnimatedValue();
                                float ratio =  (float)value/screenW - 1;
                                lastContentView0.setLeft((int) (ratio * Utils.dp2px(currentActivity,100f)));
                            }
                        });

                        restoreAnimator.setTarget(currentContentView0);
                        restoreAnimator.setIntValues(left,0);
                        if(restoreAnimator.isStarted()) {
                            restoreAnimator.end();
                        }
                        restoreAnimator.start();

                        animator.restoreAnimator = restoreAnimator;
                        SnakeManager.get().put(currentActivity,animator);
                    }
                }
            });
        }
    }

    @Override
    public void recycle() {
        currentActivity = null;
    }

    private void activityDestroyAsset(AppCompatActivity appCompatActivity) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && appCompatActivity.isDestroyed()) {
            throw new RuntimeException("不能在已经销毁的Activity: " + appCompatActivity + "上面启用滑动关闭功能");
        }
    }
}
