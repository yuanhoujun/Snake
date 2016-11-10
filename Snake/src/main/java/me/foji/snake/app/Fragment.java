package me.foji.snake.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.foji.snake.SlideToCloseBuilder;
import me.foji.snake.Snake;
import me.foji.snake.util.Utils;
import me.foji.snake.widget.SnakeFrameLayout;

/**
 * 要实现Fragment滑动关闭功能，需要继承该Fragment 或 me.foji.snake.v4.app.Fragment
 *
 * @author Scott Smith  @Date 2016年10月2016/10/18日 11:19
 */
public class Fragment extends android.app.Fragment {
    private ObjectAnimator mSlideToCloseAnimator;
    private ObjectAnimator mSlideToReleaseAnimator;
    private int mLastVisibility ;
    private boolean mOverrideCheck = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snake.init(this).start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = onBindView(inflater,container,savedInstanceState);
        mOverrideCheck = false;
        if(null == view) return null;
        return buildSnakeView(view);
    }

    private View buildSnakeView(final View view) {
        if(needBuildSnakeView()) {
            final SnakeFrameLayout snakeFrameLayout = new SnakeFrameLayout(getActivity(), view);
            snakeFrameLayout.setShadowStartColor(SlideToCloseBuilder.get().shadowStartColor());
            snakeFrameLayout.setShadowEndColor(SlideToCloseBuilder.get().shadowEndColor());
            snakeFrameLayout.setMinVelocity(SlideToCloseBuilder.get().minVelocity());
            snakeFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            snakeFrameLayout.setOnHorizontalScrollListener(new SnakeFrameLayout.OnHorizontalScrollListener() {
                @Override
                public void onScroll(int left) {
                    android.app.Fragment fragment = lastFragment();
                    if(null != fragment) {
                        View lastView = fragment.getView();
                        if(null != lastView) {
                            mLastVisibility = lastView.getVisibility();
                            lastView.setVisibility(View.VISIBLE);
                            final float ratio = (float)left / Utils.screenWidth(getActivity()) - 1;
                            lastView.setLeft((int) (ratio * Utils.dp2px(getActivity(), 100f)));
                            lastView.invalidate();
                        }
                    }
                }

                @Override
                public void onRelease(int left, boolean close, boolean isTouchEdge) {
                    android.app.Fragment fragment = lastFragment();
                    View lastView = null;
                    if(null != fragment) {
                        lastView = fragment.getView();
                    }

                    if(null == lastView) return;

                    final int screenW = Utils.screenWidth(getActivity());

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
                                getActivity().getFragmentManager().popBackStackImmediate();
                                finalLastView.setLeft(0);
                                finalLastView.setVisibility(mLastVisibility);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        mSlideToCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int value = (int) animation.getAnimatedValue();
                                float ratio =  1.0f * value / screenW - 1;
                                finalLastView.setLeft((int) (ratio * Utils.dp2px(getActivity(),100f)));
                            }
                        });

                        mSlideToCloseAnimator.setDuration((long) (300f * left / screenW));

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
                        mSlideToReleaseAnimator.setDuration((long) (1000f * left / screenW));

                        final View finalLastView2 = lastView;
                        mSlideToReleaseAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                finalLastView2.setLeft(0);
                                snakeFrameLayout.reset();
                                finalLastView2.setVisibility(mLastVisibility);
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
                                finalLastView2.setLeft((int) (ratio * Utils.dp2px(getActivity(),100f)));
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
        return enable && isStart && getFragmentManager().getBackStackEntryCount() > 0;
    }

    private android.app.Fragment lastFragment() {
        FragmentManager manager = getActivity().getFragmentManager();
        int count = manager.getBackStackEntryCount();

        String name = null;
        if(count > 0) {
            FragmentManager.BackStackEntry stackEntry = manager.getBackStackEntryAt(count - 1);
            name = stackEntry.getName();
        }

        android.app.Fragment fragment = null;
        if(!TextUtils.isEmpty(name)) {
            fragment = manager.findFragmentByTag(name);
        }

        return fragment;
    }

    @Nullable
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mOverrideCheck) {
            throw new RuntimeException("不能重写onCreateView方法，请使用onBindView方法代替");
        }
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
