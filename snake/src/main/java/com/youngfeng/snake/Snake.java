package com.youngfeng.snake;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.annotations.PrimaryConstructor;
import com.youngfeng.snake.util.ActivityHelper;
import com.youngfeng.snake.util.ActivityManager;
import com.youngfeng.snake.util.FragmentManagerHelper;
import com.youngfeng.snake.util.SoftKeyboardHelper;
import com.youngfeng.snake.view.SnakeHackLayout;

import java.lang.reflect.Constructor;

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
            public void onRelease(SnakeHackLayout parent, View view, int left, boolean shouldClose) {
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

        SnakeHackLayout snakeHackLayout = SnakeHackLayout.getLayout(activity, topWindowView, true);
        decorView.addView(snakeHackLayout);

        snakeHackLayout.setOnEdgeDragListener(new SnakeHackLayout.OnEdgeDragListener() {
            @Override
            public void onDragStart(SnakeHackLayout parent) {
                SoftKeyboardHelper.hideKeyboard(activity);
                ActivityHelper.setWindowTranslucent(activity, true);
            }

            @Override
            public void onDrag(SnakeHackLayout parent, View view, int left) {

            }

            @Override
            public void onRelease(SnakeHackLayout parent, View view, int left, boolean shouldClose) {
                if(shouldClose) {
                    activity.finish();
                } else {
                    parent.smoothScrollToStart(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            ActivityHelper.setWindowTranslucent(activity, false);
                        }
                    });
                }
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
}
