package com.youngfeng.snake;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.annotations.PrimaryConstructor;
import com.youngfeng.snake.annotations.SetDragParameter;
import com.youngfeng.snake.config.SnakeConfigException;
import com.youngfeng.snake.config.SnakeConfigReader;
import com.youngfeng.snake.util.ActivityHelper;
import com.youngfeng.snake.util.ActivityManager;
import com.youngfeng.snake.util.FragmentManagerHelper;
import com.youngfeng.snake.util.SoftKeyboardHelper;
import com.youngfeng.snake.util.TranslucentConversionListener;
import com.youngfeng.snake.view.SnakeHackLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.youngfeng.snake.util.Utils;

/**
 * 框架入口，用于集成滑动关闭功能
 *
 * @author Scott Smith 2017-12-11 12:17
 */
public class Snake {

    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityManager.get().put(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityManager.get().remove(activity);
            }
        });
        SnakeConfigReader.get().init(application);
    }

    public static <T extends Fragment> T newProxy(Class<? extends T> fragment, Object... args) {
        checkAnnotationNotEmpty(fragment);

        try {
            Class<?> snakeProxyClass = Class.forName(fragment.getName() + "_SnakeProxy");
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

    private static void checkAnnotationNotEmpty(Class<?> clazz) {
        if(clazz.getAnnotation(EnableDragToClose.class) == null) {
            throw new IllegalStateException(String.format("Please add %s annotation to class %s first,  eg: @%s.",
                    EnableDragToClose.class.getName(), clazz.getName(), EnableDragToClose.class.getSimpleName()));
        }
    }

    public static void openDragToCloseForFragment(@NonNull SnakeHackLayout snakeHackLayout, @NonNull final Fragment fragment) {
        assertFragmentActive(fragment);

        setDragParameter(fragment.getClass().getAnnotation(SetDragParameter.class), snakeHackLayout);

        final FragmentManagerHelper fragmentManagerHelper = FragmentManagerHelper.get(fragment.getFragmentManager());
        snakeHackLayout.setOnEdgeDragListener(new SnakeHackLayout.OnEdgeDragListener() {
            private boolean keyboardHideSuccess = false;

            @Override
            public void onDragStart(SnakeHackLayout parent) {
                keyboardHideSuccess = SoftKeyboardHelper.hideKeyboard(fragment);
            }

            @Override
            public void onDrag(SnakeHackLayout parent, View view, int left) {
                if(!keyboardHideSuccess) return;

                View viewOfLastFragment = fragmentManagerHelper.getViewOfLastFragment();
                if(null != viewOfLastFragment) {
                    float ratio = (left * 1.0f) / parent.getWidth();
                    viewOfLastFragment.setLeft((int) ((ratio - 1) * Utils.dp2px(fragment.getActivity(), 100f)));
                }
            }

            @Override
            public void onRelease(SnakeHackLayout parent, View view, int left, boolean shouldClose, boolean ignoreDragEvent) {
                if(shouldClose) {
                    parent.smoothScrollToLeave(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            if (!fragmentManagerHelper.backStackEmpty()) {
                                fragmentManagerHelper.backToLastFragment();
                            }
                        }
                    });

                } else {
                    parent.smoothScrollToStart(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {

                        }
                    });
                }
            }
        });
    }

    private static void assertFragmentActive(Fragment fragment) {
        if(fragment.isDetached() || fragment.isRemoving()) {
            throw new IllegalStateException("You can't add this feature to a detached or removing fragment");
        }
    }

    private static void setDragParameter(@Nullable SetDragParameter dragParameter, SnakeHackLayout snakeHackLayout) {
        if(null != dragParameter) {
            snakeHackLayout.hideShadowOfEdge(dragParameter.hideShadowOfEdge());
            snakeHackLayout.setMinVelocity(dragParameter.minVelocity());
            snakeHackLayout.setOnlyListenToFastSwipe(dragParameter.onlyListenToFastSwipe());

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
        }
    }

    public static void openDragToCloseForActivity(@NonNull final Activity activity) {
        assertActivityDestroyed(activity);
        checkAnnotationNotEmpty(activity.getClass());

        EnableDragToClose enableDragToClose = activity.getClass().getAnnotation(EnableDragToClose.class);
        if(!enableDragToClose.value()) return;

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View topWindowView = decorView.getChildAt(0);

        // Just return if top window view was SnakeHackLayout.
        if(topWindowView instanceof SnakeHackLayout) return;

        decorView.removeView(topWindowView);

        final SnakeHackLayout snakeHackLayout = SnakeHackLayout.getLayout(activity, topWindowView, false);
        decorView.addView(snakeHackLayout);

        SetDragParameter dragParameter = activity.getClass().getAnnotation(SetDragParameter.class);
        setDragParameter(dragParameter, snakeHackLayout);

        snakeHackLayout.setOnEdgeDragListener(new SnakeHackLayout.OnEdgeDragListener() {
            @Override
            public void onDragStart(SnakeHackLayout parent) {
                SoftKeyboardHelper.hideKeyboard(activity);

                ActivityHelper.setWindowTranslucent(activity, true, new TranslucentConversionListener() {
                    @Override
                    public void onTranslucentConversionComplete(boolean drawComplete) {
                        snakeHackLayout.setAllowDragChildView(drawComplete);
                    }
                });
            }

            @Override
            public void onDrag(SnakeHackLayout parent, View view, int left) {

            }

            @Override
            public void onRelease(SnakeHackLayout parent, View view, int left, boolean shouldClose, boolean ignoreDragEvent) {
                // Just return if snakeHackLayout set canDragToClose == false
                if(!parent.canDragToClose()) return;

                if(shouldClose) {
                    // Just return if ignore the drag event
                    if(ignoreDragEvent) return;

                    activity.finish();
                } else {
                    parent.smoothScrollToStart(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            ActivityHelper.setWindowTranslucent(activity, false, null);
                            snakeHackLayout.setAllowDragChildView(false);
                        }
                    });
                }
            }

            @Override
            public boolean canDragToClose() {
                return !ActivityManager.get().isRootActivity(activity) || SnakeConfigReader.get().enableForRootActivity();
            }
        });
    }

    private static void assertActivityDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalStateException("You cannot add this feature to a destroyed activity");
        }
    }

    public static void host(@NonNull Activity activity) {
        openDragToCloseForActivity(activity);
    }

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

        // Do nothing if top window view is not SnakeHackLayout.
        if(!(topWindowView instanceof SnakeHackLayout)) return;

        ((SnakeHackLayout) topWindowView).ignoreDragEvent(!enable);
    }

    public static void enableDragToClose(@NonNull Fragment fragment, boolean enable) {
        try {
            Method method = fragment.getClass().getMethod("enableDragToClose", Boolean.class);
            method.invoke(fragment, enable);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
