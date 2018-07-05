package com.youngfeng.snake;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import com.youngfeng.snake.animation.AnimationFactory;
import com.youngfeng.snake.animation.SnakeAnimationController;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.annotations.PrimaryConstructor;
import com.youngfeng.snake.annotations.SetDragParameter;
import com.youngfeng.snake.app.Fragment;
import com.youngfeng.snake.config.SnakeConfigException;
import com.youngfeng.snake.config.SnakeConfigReader;
import com.youngfeng.snake.util.*;
import com.youngfeng.snake.view.SnakeHackLayout;
import com.youngfeng.snake.view.SnakeTouchInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 框架入口，用于集成滑动关闭功能
 *
 * @author Scott Smith 2017-12-11 12:17
 */
public class Snake {
    private static Application mContext;

    /**
     * Initializes snake with default configurations, it will find configuration file in asset/snake.xml,
     * Snake will use default configruation if not found snake.xml.
     *
     * @param application application instance
     */
    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new GlobalActivityLifecycleDelegate() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManager.get().put(activity);
                openDragToCloseForActivity(activity);
                Logger.d(activity.getClass() + " onCreate completed...");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManager.get().remove(activity);
                Logger.d(activity.getClass() + " destoryed completed...");
            }
        });
        SnakeConfigReader.get().init(application);
        mContext = application;
    }

    /**
     * Create a fragment proxy object using the specified constructor.
     * It will be use the empty parameter constructor if not specify the primaryconstructor annotation
     * in constructor.
     *
     * @param fragment specified fragment class
     * @param args specified constructor parameters
     * @param <T> the child classes of android.app.fragment.
     *
     * @return fragment proxy object
     */
    public static <T extends android.app.Fragment> T newProxy(Class<? extends T> fragment, Object... args) {
        checkAnnotationNotEmpty(fragment);

        try {
            String className = fragment.getName() + "_SnakeProxy";
            if(com.youngfeng.snake.app.Fragment.class.isAssignableFrom(fragment)) {
                className = fragment.getName();
            }
            Class<?> snakeProxyClass = Class.forName(className);

            Constructor<?>[] constructors = snakeProxyClass.getConstructors();

            Constructor<?> primaryConstructor = null;
            if(null != constructors) {
                for (Constructor<?> constructor : constructors) {
                    PrimaryConstructor primaryConstructorAnno = constructor.getAnnotation(PrimaryConstructor.class);
                    if(null != primaryConstructorAnno) {
                        primaryConstructor = constructor;
                        break;
                    }
                }
            }

            T result = null;
            if(null != primaryConstructor) {
                result = (T) primaryConstructor.newInstance(args);
            } else {
                result = (T) snakeProxyClass.newInstance();
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Create a support fragment proxy object using the specified constructor.
     * It will be use the empty parameter constructor if not specify the primaryconstructor annotation
     * in constructor.
     *
     * @param fragment specified support fragment class
     * @param args specified constructor parameters
     * @param <T> the child classes of android.support.v4.app.fragment.
     *
     * @return support fragment proxy object
     */
    public static <T extends android.support.v4.app.Fragment> T newProxySupport(Class<? extends T> fragment, Object... args) {
        checkAnnotationNotEmpty(fragment);

        try {
            String className = fragment.getName() + "_SnakeProxy";
            if(com.youngfeng.snake.support.v4.app.Fragment.class.isAssignableFrom(fragment)) {
                className = fragment.getName();
            }
            Class<?> snakeProxyClass = Class.forName(className);
            Constructor<?>[] constructors = snakeProxyClass.getConstructors();

            Constructor<?> primaryConstructor = null;
            if(null != constructors) {
                for (Constructor<?> constructor : constructors) {
                    PrimaryConstructor primaryConstructorAnno = constructor.getAnnotation(PrimaryConstructor.class);
                    if(null != primaryConstructorAnno) {
                        primaryConstructor = constructor;
                        break;
                    }
                }
            }

            T result = null;
            if(null != primaryConstructor) {
                result = (T) primaryConstructor.newInstance(args);
            } else {
                result = (T) snakeProxyClass.newInstance();
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Asset annotation EnableDragToClose not empty
    private static void checkAnnotationNotEmpty(Class<?> clazz) {
        if(clazz.getAnnotation(EnableDragToClose.class) == null) {
            throw new IllegalStateException(String.format("Please add %s annotation to class %s first,  eg: @%s.",
                    EnableDragToClose.class.getName(), clazz.getName(), EnableDragToClose.class.getSimpleName()));
        }
    }

    /**
     * Open DragToClose for fragment, just for internal using.
     *
     * @param snakeHackLayout SnakeHackLayout
     * @param fragment the current fragment
     */
    public static void openDragToCloseForFragment(@NonNull SnakeHackLayout snakeHackLayout, @NonNull final android.app.Fragment fragment) {
        assertFragmentActive(fragment);

        setDragParameter(fragment.getClass().getAnnotation(SetDragParameter.class), snakeHackLayout);

        final FragmentManagerHelper fragmentManagerHelper = FragmentManagerHelper.get(fragment.getFragmentManager());
        snakeHackLayout.setOnEdgeDragListener(new SnakeHackLayout.OnEdgeDragListener() {
            private int mVisibility = -1;

            @Override
            public void onDragStart(SnakeHackLayout parent) {
               SoftKeyboardHelper.hideKeyboard(fragment);
            }

            @SuppressLint("WrongConstant")
            @Override
            public void onDrag(SnakeHackLayout parent, View view, int left) {
                View viewOfLastFragment = fragmentManagerHelper.getViewOfLastFragment();
                if (null != viewOfLastFragment) {
                    if (View.VISIBLE != viewOfLastFragment.getVisibility()) {
                        mVisibility = viewOfLastFragment.getVisibility();
                        viewOfLastFragment.setVisibility(View.VISIBLE);
                        viewOfLastFragment.setX(0f);
                    }

                    if(parent.getUIConfig().allowPageLinkage) {
                        float ratio = (left * 1.0f) / parent.getWidth();
                        viewOfLastFragment.setX((ratio - 1) * Utils.dp2px(fragment.getActivity(), 100f));
                    }
                }
            }

            @Override
            public void onRelease(SnakeHackLayout parent, View view, int left, boolean shouldClose, int interceptScene) {
                if(shouldClose) {
                    parent.smoothScrollToLeave(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            View viewOfLastFragment = fragmentManagerHelper.getViewOfLastFragment();
                            if(null != viewOfLastFragment) {
                                viewOfLastFragment.setX(0f);
                            }

                            android.app.Fragment lastFragment = fragmentManagerHelper.getLastFragment();
                            disableAnimation(lastFragment, true);
                            if (fragmentManagerHelper.backToLastFragment()) {
                                disableAnimation(lastFragment, false);
                            } else {
                                disableAnimation(lastFragment, false);
                            }
                        }
                    });
                } else {
                    parent.smoothScrollToStart(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            View viewOfLastFragment = fragmentManagerHelper.getViewOfLastFragment();
                            if(null != viewOfLastFragment) {
                                if(mVisibility >= 0) {
                                    viewOfLastFragment.setVisibility(mVisibility);
                                }
                                viewOfLastFragment.setX(0f);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * Open DragToClose for support fragment, just for internal using.
     *
     * @param snakeHackLayout SnakeHackLayout
     * @param fragment the current fragment
     */
    public static void openDragToCloseForFragment(@NonNull SnakeHackLayout snakeHackLayout, @NonNull final android.support.v4.app.Fragment fragment) {
        assertFragmentActive(fragment);

        setDragParameter(fragment.getClass().getAnnotation(SetDragParameter.class), snakeHackLayout);

        final FragmentManagerHelper fragmentManagerHelper = FragmentManagerHelper.get(fragment.getFragmentManager());
        snakeHackLayout.setOnEdgeDragListener(new SnakeHackLayout.OnEdgeDragListener() {
            private int mVisibility = -1;

            @Override
            public void onDragStart(SnakeHackLayout parent) {
                SoftKeyboardHelper.hideKeyboard(fragment);
            }

            @SuppressLint("WrongConstant")
            @Override
            public void onDrag(SnakeHackLayout parent, View view, int left) {
                View viewOfLastFragment = fragmentManagerHelper.getViewOfLastSupportFragment();
                if (null != viewOfLastFragment) {
                    if (View.VISIBLE != viewOfLastFragment.getVisibility()) {
                        mVisibility = viewOfLastFragment.getVisibility();
                        viewOfLastFragment.setVisibility(View.VISIBLE);
                        viewOfLastFragment.setX(0f);
                    }

                    if(parent.getUIConfig().allowPageLinkage) {
                        float ratio = (left * 1.0f) / parent.getWidth();
                        viewOfLastFragment.setX((ratio - 1) * Utils.dp2px(fragment.getActivity(), 100f));
                    }
                }
            }

            @Override
            public void onRelease(SnakeHackLayout parent, View view, int left, boolean shouldClose, int interceptScene) {
                if(shouldClose) {
                    parent.smoothScrollToLeave(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            View viewOfLastFragment = fragmentManagerHelper.getViewOfLastSupportFragment();
                            if(null != viewOfLastFragment) {
                                viewOfLastFragment.setX(0f);
                            }

                            android.support.v4.app.Fragment lastFragment = fragmentManagerHelper.getLastSupportFragment();
                            disableAnimation(lastFragment, true);
                            if (fragmentManagerHelper.backToSupportFragment()) {
                                disableAnimation(lastFragment, false);
                            } else {
                                disableAnimation(lastFragment, false);
                            }
                        }
                    });
                } else {
                    parent.smoothScrollToStart(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            View viewOfLastFragment = fragmentManagerHelper.getViewOfLastSupportFragment();
                            if(null != viewOfLastFragment) {
                                if(mVisibility >= 0) {
                                    viewOfLastFragment.setVisibility(mVisibility);
                                }
                                viewOfLastFragment.setX(0f);
                            }
                        }
                    });
                }
            }
        });
    }

    private static void assertFragmentActive(android.app.Fragment fragment) {
        if(fragment.isDetached() || fragment.isRemoving()) {
            throw new IllegalStateException("You can't add this feature to a detached or removing fragment");
        }
    }

    private static void assertFragmentActive(android.support.v4.app.Fragment fragment) {
        if(fragment.isDetached() || fragment.isRemoving()) {
            throw new IllegalStateException("You can't add this feature to a detached or removing fragment");
        }
    }

    private static void setDragParameter(@Nullable SetDragParameter dragParameter, SnakeHackLayout snakeHackLayout) {
        if(null != dragParameter) {
            snakeHackLayout.hideShadowOfEdge(dragParameter.hideShadowOfEdge());
            snakeHackLayout.setMinVelocity(dragParameter.minVelocity());
            snakeHackLayout.setOnlyListenToFastSwipe(dragParameter.onlyListenToFastSwipe());
            snakeHackLayout.enableSwipeUpToHome(dragParameter.enableSwipeUpToHome());
            snakeHackLayout.setAllowPageLinkageOfUIConfig(dragParameter.allowPageLinkage());

            if(!dragParameter.hideShadowOfEdge()) {
                try {
                    snakeHackLayout.setShadowStartColor(Color.parseColor(dragParameter.shadowStartColor()));
                } catch (IllegalArgumentException e) {
                    throw new SnakeConfigException(
                            String.format("The shadow start color string of  %s annotation is set error, eg: #ff0000, current value: %s",
                                    SetDragParameter.class.getSimpleName(), dragParameter.shadowStartColor()));
                }

                try {
                    snakeHackLayout.setShadowEndColor(Color.parseColor(dragParameter.shadowEndColor()));
                } catch (IllegalArgumentException e) {
                    throw new SnakeConfigException(
                            String.format("The shadow end color string of  %s annotation is set error, eg: #ff0000, current value: %s",
                                    SetDragParameter.class.getSimpleName(), dragParameter.shadowEndColor()));
                }
            }
        } else {
            snakeHackLayout.hideShadowOfEdge(SnakeConfigReader.get().hideShadowOfEdge());
            snakeHackLayout.setMinVelocity(SnakeConfigReader.get().minVelocity());
            snakeHackLayout.setOnlyListenToFastSwipe(SnakeConfigReader.get().onlyListenToFastSwipe());
            snakeHackLayout.setShadowStartColor(SnakeConfigReader.get().shadowStartColor());
            snakeHackLayout.setShadowEndColor(SnakeConfigReader.get().shadowEndColor());
            snakeHackLayout.enableSwipeUpToHome(SnakeConfigReader.get().swipeUpToHomeEnabled());
            snakeHackLayout.setAllowPageLinkageOfUIConfig(SnakeConfigReader.get().allowPageLinkage());
        }
    }

    /**
     * Open DragToClose for activity, just for internal using.
     *
     * @param activity the specified activity
     */
    private static void openDragToCloseForActivity(@NonNull final Activity activity) {
        assertActivityDestroyed(activity);
        checkAnnotationNotEmpty(activity.getClass());

        EnableDragToClose enableDragToClose = activity.getClass().getAnnotation(EnableDragToClose.class);
        if(!enableDragToClose.value()) return;

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View topWindowView = decorView.getChildAt(0);

        // Just return if top window view was SnakeHackLayout.
        if(topWindowView instanceof SnakeHackLayout) return;

        // Set transparent background to avoid flashing.
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        activity.getWindow().getDecorView().setBackgroundDrawable(null);

        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[] { android.R.attr.windowBackground });
        int background = a.getResourceId(0, 0);
        a.recycle();
        topWindowView.setBackgroundResource(background);

        decorView.removeView(topWindowView);

        final SnakeHackLayout snakeHackLayout = SnakeHackLayout.getLayout(activity, topWindowView, true);
        decorView.addView(snakeHackLayout);

        SetDragParameter dragParameter = activity.getClass().getAnnotation(SetDragParameter.class);
        setDragParameter(dragParameter, snakeHackLayout);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            snakeHackLayout.setOnlyListenToFastSwipe(true);
        }

        ActivityDragInterceptor.get(activity).attachToLayout(snakeHackLayout);
    }

    private static void assertActivityDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalStateException("You cannot add this feature to a destroyed activity");
        }
    }

    /**
     * Let snake host the current activity.
     *
     * @param activity the specified activity instance
     * @deprecated this method will not be called in {@link Activity#onCreate(Bundle)}
     */
    @Deprecated
    public static void host(@NonNull Activity activity) {
    }

    /**
     * Turn the slide-off function on or off for activity.
     *
     * @param activity the specified activity
     * @param enable true: turn on, false: turn off
     */
    public static void enableDragToClose(@NonNull Activity activity, boolean enable) {
        if(activity.isFinishing()) return;

        if(enable) {
            EnableDragToClose enableDragToClose = activity.getClass().getAnnotation(EnableDragToClose.class);
            if (null == enableDragToClose || !enableDragToClose.value()) {
                throw new SnakeConfigException("If you want to dynamically turn the slide-off feature on or off, add the EnableDragToClose annotation to "
                        + activity.getClass().getName() + " and set to true");
            }
        }

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View topWindowView = decorView.getChildAt(0);

        if(!(topWindowView instanceof SnakeHackLayout)) {
            throw new SnakeConfigException("Add Snake.host(this) to onCreate method first.");
        }

        ((SnakeHackLayout) topWindowView).ignoreDragEvent(!enable);
    }

    /**
     * Turn the slide-off function on or off for fragment.
     *
     * @param fragment the specified fragment
     * @param enable true: turn on, false: turn off
     */
    public static void enableDragToClose(@NonNull android.app.Fragment fragment, boolean enable) {
        if(fragment instanceof com.youngfeng.snake.app.Fragment) {
            ((Fragment) fragment).enableDragToClose(enable);
        } else {
            try {
                Method method = fragment.getClass().getMethod("enableDragToClose", Boolean.class);
                method.invoke(fragment, enable);
            } catch (Throwable e) {
                if (e instanceof NoSuchMethodException) {
                    throw new SnakeConfigException("Plase use Snake.newProxy create a Fragment instance");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Turn the slide-off function on or off for support fragment.
     *
     * @param fragment the specified support fragment
     * @param enable true: turn on, false: turn off
     */
    public static void enableDragToClose(@NonNull android.support.v4.app.Fragment fragment, boolean enable) {
        if(fragment instanceof com.youngfeng.snake.support.v4.app.Fragment) {
            ((com.youngfeng.snake.support.v4.app.Fragment) fragment).enableDragToClose(enable);
        } else {
            try {
                Method method = fragment.getClass().getMethod("enableDragToClose", Boolean.class);
                method.invoke(fragment, enable);
            } catch (Throwable e) {
                if (e instanceof NoSuchMethodException) {
                    throw new SnakeConfigException("Plase use Snake.newProxySupport create a Fragment instance");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Add OnDragListener for drag event.
     *
     * @param activity the current activity.
     * @param onDragListener onDragListener
     */
    public static void addDragListener(@NonNull Activity activity, Snake.OnDragListener onDragListener) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if(!(decorView.getChildAt(0) instanceof SnakeHackLayout) || null == onDragListener) return;

        ((SnakeHackLayout)decorView.getChildAt(0)).addOnDragListener(onDragListener);
    }

    /**
     * Add OnDragListener for drag event.
     *
     * @param fragment the current fragment.
     * @param onDragListener onDragListener
     */
    public static void addDragListener(@NonNull android.app.Fragment fragment, Snake.OnDragListener onDragListener) {
        if(fragment instanceof com.youngfeng.snake.app.Fragment) {
            ((Fragment) fragment).addOnDragListener(onDragListener);
        } else {
            try {
                Method method = fragment.getClass().getMethod("addOnDragListener", OnDragListener.class);
                method.invoke(fragment, onDragListener);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add OnDragListener for drag event.
     *
     * @param fragment the current fragment.
     * @param onDragListener onDragListener
     */
    public static void addDragListener(@NonNull android.support.v4.app.Fragment fragment, Snake.OnDragListener onDragListener) {
        if(fragment instanceof com.youngfeng.snake.support.v4.app.Fragment) {
            ((com.youngfeng.snake.support.v4.app.Fragment) fragment).addOnDragListener(onDragListener);
        } else {
            try {
                Method method = fragment.getClass().getMethod("addOnDragListener", OnDragListener.class);
                method.invoke(fragment, onDragListener);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set custom touch interceptor for activity.
     *
     * @param activity the specified activity.
     * @param interceptor the custom touch interceptor.
     */
    public static void setCustomTouchInterceptor(@NonNull Activity activity, SnakeTouchInterceptor interceptor) {
        if(activity.isFinishing() || null == interceptor) return;

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if(!(decorView.getChildAt(0) instanceof SnakeHackLayout)) return;

        SnakeHackLayout snakeHackLayout = (SnakeHackLayout) decorView.getChildAt(0);
        snakeHackLayout.setCustomTouchInterceptor(interceptor);
    }

    /**
     * Set custom touch interceptor for fragment.
     *
     * @param fragment the specified fragment.
     * @param interceptor the custom touch interceptor.
     */
    public static void setCustomTouchInterceptor(@NonNull android.app.Fragment fragment, SnakeTouchInterceptor interceptor) {
        if(fragment instanceof com.youngfeng.snake.app.Fragment) {
            ((Fragment) fragment).setCustomTouchInterceptor(interceptor);
        } else {
            try {
                Method method = fragment.getClass().getMethod("setCustomTouchInterceptor", SnakeTouchInterceptor.class);
                method.invoke(fragment, interceptor);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set custom touch interceptor for fragment.
     *
     * @param fragment the specified fragment.
     * @param interceptor the custom touch interceptor.
     */
    public static void setCustomTouchInterceptor(@NonNull android.support.v4.app.Fragment fragment, SnakeTouchInterceptor interceptor) {
        if(fragment instanceof com.youngfeng.snake.support.v4.app.Fragment) {
            ((com.youngfeng.snake.support.v4.app.Fragment) fragment).setCustomTouchInterceptor(interceptor);
        } else {
            try {
                Method method = fragment.getClass().getMethod("setCustomTouchInterceptor", SnakeTouchInterceptor.class);
                method.invoke(fragment, interceptor);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Wrap fragment animator to simplify use.
     *
     * @param superAnimator the result of call super onCreateAnimator
     * @param animationController the interface of control fragment's animator
     *
     * @return the fragment animator
     */
    public static Animator wrap(@Nullable Animator superAnimator, @NonNull SnakeAnimationController animationController) {
        if(animationController.animationDisabled()) return AnimationFactory.emptyAnimator();
        return superAnimator;
    }

    /**
     * Wrap fragment animation to simplify use.
     *
     * @param superAnimation the result of call super onCreateAnimator
     * @param animationController the interface of control fragment's animation
     *
     * @return the fragment animation
     */
    public static Animation wrap(@Nullable Animation superAnimation, @NonNull SnakeAnimationController animationController) {
        if(animationController.animationDisabled()) return AnimationFactory.emptyAmiation();
        return superAnimation;
    }

    /**
     * Disable fragment's pop animation.
     *
     * @param fragment the specified support fragment
     * @param disable disable or enable animation
     */
    public static void disableAnimation(android.support.v4.app.Fragment fragment, boolean disable) {
        if(fragment instanceof SnakeAnimationController) {
            ((SnakeAnimationController) fragment).disableAnimation(disable);
        }
    }

    /**
     * Disable fragment's pop animation.
     *
     * @param fragment the specified fragment
     * @param disable disable or enable animation
     */
    public static void disableAnimation(android.app.Fragment fragment, boolean disable) {
        if(fragment instanceof SnakeAnimationController) {
            ((SnakeAnimationController) fragment).disableAnimation(disable);
        }
    }

    /**
     * Get enable status of the activity.
     *
     * @param activity the specified activity
     * @return true: enabled false: disabled
     */
    public static boolean dragToCloseEnabled(@NonNull Activity activity) {
        assertActivityDestroyed(activity);

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View topWindowView = decorView.getChildAt(0);
        if(topWindowView instanceof SnakeHackLayout) {
            return !((SnakeHackLayout) topWindowView).ignoredDragEvent();
        }

        return false;
    }

    /**
     * Get enable status of the fragment.
     *
     * @param fragment the specified fragment
     * @return true: enabled false: disabled
     */
    public static boolean dragToCloseEnabled(@NonNull android.app.Fragment fragment) {
        View contentView = fragment.getView();

        if(contentView instanceof SnakeHackLayout) {
            return !((SnakeHackLayout) contentView).ignoredDragEvent();
        }

        return false;
    }

    /**
     * Get enable status of the fragment.
     *
     * @param fragment the specified fragment
     * @return true: enabled false: disabled
     */
    public static boolean dragToCloseEnabled(@NonNull android.support.v4.app.Fragment fragment) {
        View contentView = fragment.getView();

        if(contentView instanceof SnakeHackLayout) {
            return !((SnakeHackLayout) contentView).ignoredDragEvent();
        }

        return false;
    }

    /**
     * Enable or disable swipe up to home for the specified activity.
     *
     * @param activity the specified activity
     * @param enable true: enable, false: disable
     */
    public static void enableSwipeUpToHome(@NonNull Activity activity, boolean enable) {
        if(activity.isFinishing()) return;

        if(enable) {
            EnableDragToClose enableDragToClose = activity.getClass().getAnnotation(EnableDragToClose.class);
            if (null == enableDragToClose || !enableDragToClose.value()) {
                throw new SnakeConfigException("If you want to dynamically turn the swipe up to home feature on or off, add the EnableDragToClose annotation to "
                        + activity.getClass().getName() + " and set true.");
            }
        }

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View topWindowView = decorView.getChildAt(0);

        if(!(topWindowView instanceof SnakeHackLayout)) {
            throw new SnakeConfigException("Add Snake.host(this) to onCreate method first.");
        }

        ((SnakeHackLayout) topWindowView).enableSwipeUpToHome(enable);
    }

    /**
     * Enable or disable swipe up to home for the specified fragment.
     *
     * @param fragment the specified fragment
     * @param enable true: enable, false: disable
     */
    public static void enableSwipeUpToHome(@NonNull android.app.Fragment fragment, boolean enable) {
        if(fragment instanceof com.youngfeng.snake.app.Fragment) {
            ((Fragment) fragment).enableSwipeUpToHome(enable);
        } else {
            try {
                Method method = fragment.getClass().getMethod("enableSwipeUpToHome", Boolean.class);
                method.invoke(fragment, enable);
            } catch (Throwable e) {
                if (e instanceof NoSuchMethodException) {
                    throw new SnakeConfigException("Plase use Snake.newProxy create a Fragment instance");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Enable or disable swipe up to home for the specified support fragment.
     *
     * @param fragment the specified support fragment
     * @param enable true: enable, false: disable
     */
    public static void enableSwipeUpToHome(@NonNull android.support.v4.app.Fragment fragment, boolean enable) {
        if(fragment instanceof com.youngfeng.snake.support.v4.app.Fragment) {
            ((com.youngfeng.snake.support.v4.app.Fragment) fragment).enableSwipeUpToHome(enable);
        } else {
            try {
                Method method = fragment.getClass().getMethod("enableSwipeUpToHome", Boolean.class);
                method.invoke(fragment, enable);
            } catch (Throwable e) {
                if (e instanceof NoSuchMethodException) {
                    throw new SnakeConfigException("Plase use Snake.newProxySupport create a Fragment instance");
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get enable state of the swipe up to home feature.
     *
     * @param activity the specified activity
     *
     * @return true: enable, false: disable
     */
    public static boolean swipeUpToHomeEnabled(@NonNull Activity activity) {
        assertActivityDestroyed(activity);

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View topWindowView = decorView.getChildAt(0);
        if(topWindowView instanceof SnakeHackLayout) {
            return ((SnakeHackLayout) topWindowView).swipeUpToHomeEnabled();
        }

        return false;
    }

    /**
     * Get enable state of the swipe up to home feature.
     *
     * @param fragment the specified fragment
     *
     * @return true: enable, false: disable
     */
    public static boolean swipeUpToHomeEnabled(@NonNull android.app.Fragment fragment) {
        View contentView = fragment.getView();

        if(contentView instanceof SnakeHackLayout) {
            return ((SnakeHackLayout) contentView).swipeUpToHomeEnabled();
        }

        return false;
    }

    /**
     * Get enable state of the swipe up to home feature.
     *
     * @param fragment the specified fragment
     *
     * @return true: enable, false: disable
     */
    public static boolean swipeUpToHomeEnabled(@NonNull android.support.v4.app.Fragment fragment) {
        View contentView = fragment.getView();

        if(contentView instanceof SnakeHackLayout) {
            return ((SnakeHackLayout) contentView).swipeUpToHomeEnabled();
        }

        return false;
    }

    /** Control whether debug logging is enabled. */
    public static void setDebug(boolean debug) {
        Logger.debug = debug;
    }

    public static Context getContext() {
        return mContext;
    }

    public static abstract class OnDragListener {
        public void onDragStart(View view) {}

        public void onDrag(View view, int left, boolean isSetlling) {}

        public void onRelease(View view, float xVelocity) {}

        public void onBackToStartCompleted(View view) {}
    }
}
