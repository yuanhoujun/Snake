package me.foji.snake.v4.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.foji.snake.SlideToCloseBuilder;
import me.foji.snake.Snake;
import me.foji.snake.util.Logger;
import me.foji.snake.util.Utils;
import me.foji.snake.widget.SnakeFrameLayout;

/**
 * 扩展Fragment,增加addLifecycleListener、push、pop等接口
 *
 * @author Scott Smith  @Date 2016年10月2016/10/18日 11:19
 */
public class Fragment extends android.support.v4.app.Fragment {
    private ObjectAnimator mSlideToCloseAnimator;
    private ObjectAnimator mSlideToReleaseAnimator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snake.init(this).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = onBindView(inflater,container,savedInstanceState);
        if(null == view) return null;
        return buildSnakeView(view);
    }

    private View buildSnakeView(final View view) {
        if(needBuildSnakeView()) {
            final SnakeFrameLayout snakeFrameLayout = new SnakeFrameLayout(getContext(),view);
            snakeFrameLayout.setShadowStartColor(SlideToCloseBuilder.get().shadowStartColor());
            snakeFrameLayout.setShadowEndColor(SlideToCloseBuilder.get().shadowEndColor());
            snakeFrameLayout.setMinVelocity(SlideToCloseBuilder.get().minVelocity());
            snakeFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            snakeFrameLayout.setOnHorizontalScrollListener(new SnakeFrameLayout.OnHorizontalScrollListener() {
                @Override
                public void onScroll(int left) {
                    android.support.v4.app.Fragment fragment = lastFragment();
                    if(null != fragment) {
                        View lastView = fragment.getView();
                        if(null != lastView) {
                            lastView.setVisibility(View.VISIBLE);
                            final float ratio = (float)left / getContext().getResources().getDisplayMetrics().widthPixels - 1;
                            lastView.setLeft((int) (ratio * Utils.dp2px(getContext(),100f)));
                            lastView.invalidate();                        }
                    }
                }

                @Override
                public void onRelease(int left, boolean close, boolean isTouchEdge) {
                    android.support.v4.app.Fragment fragment = lastFragment();
                    View lastView = null;
                    if(null != fragment) {
                        lastView = fragment.getView();
                    }

                    if(null == lastView) return;

                    final int screenW = getContext().getResources().getDisplayMetrics().widthPixels;

                    // 关闭当前页面
                    if(isTouchEdge && close) {
                        if(null == mSlideToCloseAnimator) {
                            mSlideToCloseAnimator = ObjectAnimator.ofInt(view, "left", left, screenW);
                        }
                        final View finalLastView = lastView;
                        mSlideToCloseAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                getActivity().getSupportFragmentManager().popBackStackImmediate();
                                finalLastView.setLeft(0);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        final View finalLastView1 = lastView;
                        mSlideToCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int) animation.getAnimatedValue();
                                float ratio =  (float)value/screenW - 1;
                                finalLastView.setLeft((int) (ratio * Utils.dp2px(getContext(),100f)));
                            }
                        });

                        mSlideToCloseAnimator.setDuration((long) (300 * (float)left / screenW));

                        mSlideToCloseAnimator.setTarget(view);
                        mSlideToCloseAnimator.setIntValues(left,screenW);
                        if(mSlideToCloseAnimator.isStarted()) {
                            mSlideToCloseAnimator.end();
                        }
                        mSlideToCloseAnimator.start();
                    }

                    // 页面状态恢复
                    if(isTouchEdge && !close) {
                        if(null == mSlideToReleaseAnimator) {
                            mSlideToReleaseAnimator = ObjectAnimator.ofInt(view, "left", left, 0);
                        }
                        mSlideToReleaseAnimator.setDuration((long) (1000 * (float)left / screenW));

                        final View finalLastView2 = lastView;
                        mSlideToReleaseAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                finalLastView2.setLeft(0);
                                snakeFrameLayout.reset();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        mSlideToReleaseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int) animation.getAnimatedValue();
                                float ratio =  (float)value/screenW - 1;
                                finalLastView2.setLeft((int) (ratio * Utils.dp2px(getContext(),100f)));
                            }
                        });

                        mSlideToReleaseAnimator.setTarget(view);
                        mSlideToReleaseAnimator.setIntValues(left,0);
                        if(mSlideToReleaseAnimator.isStarted()) {
                            mSlideToReleaseAnimator.end();
                        }
                        mSlideToReleaseAnimator.start();
                    }

                }
            });
            return snakeFrameLayout;
        }
        return view;
    }

    /**
     * 只有当前Fragment滑动关闭功能打开，并且当前Fragment不是第一个Fragment才需要构建SnakeView
     * 如果是第一个Fragment，则使用Activity的滑动关闭功能
     *
     * @return true 需要构建 反之不需要
     */
    private boolean needBuildSnakeView() {
        boolean enable = SlideToCloseBuilder.get().getOpenStatus(this);
        boolean isStart = SlideToCloseBuilder.get().isStart(this);
        return enable && isStart && getActivity().getSupportFragmentManager().getFragments() != null && getActivity().getSupportFragmentManager().getFragments().size() > 1;
    }

    private android.support.v4.app.Fragment lastFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        int count = manager.getBackStackEntryCount();

        String name = null;
        if(count > 0) {
            FragmentManager.BackStackEntry stackEntry = manager.getBackStackEntryAt(count - 1);
            name = stackEntry.getName();
        }

        android.support.v4.app.Fragment fragment = null;
        if(!TextUtils.isEmpty(name)) {
            fragment = manager.findFragmentByTag(name);
        }

        // 待定
        if(null == fragment) {
            List<android.support.v4.app.Fragment> fragments = manager.getFragments();
            if(null != fragments && fragments.size() > 1) {
                fragment = fragments.get(fragments.size() - 2);
            }
        }

        return fragment;
    }

    @Nullable
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(null != mSlideToCloseAnimator) {
            mSlideToCloseAnimator.removeAllListeners();
            mSlideToCloseAnimator.cancel();
            mSlideToCloseAnimator = null;
        }

        if(null != mSlideToReleaseAnimator) {
            mSlideToReleaseAnimator.removeAllListeners();
            mSlideToReleaseAnimator.cancel();
            mSlideToReleaseAnimator = null;
        }
    }
}
