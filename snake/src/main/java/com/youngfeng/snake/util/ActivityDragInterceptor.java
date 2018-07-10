package com.youngfeng.snake.util;

import android.app.Activity;
import android.view.View;
import com.youngfeng.snake.R;
import com.youngfeng.snake.config.SnakeConfigReader;
import com.youngfeng.snake.view.SnakeHackLayout;

/**
 * Activity drag interceptor.
 *
 * @author Scott Smith 2018-01-16 20:47
 */
public class ActivityDragInterceptor extends SnakeHackLayout.DragInterceptor {
    public static final int INTERCEPT_SCENE_ROOT_ACTIVITY = 1;
    public static final int INTERCEPT_SCENE_TRANSLUCENT_CONVERSION = 2;

    private Activity mActivity;
    private boolean isTranslucent = false;

    private ActivityDragInterceptor(Activity activity) {
        mActivity = activity;
        convertToTranslucent(mActivity, new TranslucentConversionListener() {
            @Override
            public void onTranslucentConversionComplete(boolean drawComplete) {
                isTranslucent = true;
                convertFromTranslucent(mActivity);
                isTranslucent = false;
            }
        });
    }

    public static ActivityDragInterceptor get(Activity activity) {
        return new ActivityDragInterceptor(activity);
    }

    public void attachToLayout(SnakeHackLayout snakeLayout) {
        snakeLayout.setOnEdgeDragListener(new SnakeHackLayout.OnEdgeDragListener() {

            @Override
            public void onDragStart(SnakeHackLayout parent) {
                SoftKeyboardHelper.hideKeyboard(mActivity);

                if (parent.onlyListenToFastSwipe()) {
                    isTranslucent = true;
                } else {
                    convertToTranslucent(mActivity, new TranslucentConversionListener() {
                        @Override
                        public void onTranslucentConversionComplete(boolean drawComplete) {
                            isTranslucent = true;
                        }
                    });
                }

                Logger.d("ActivityDragInterceptor: onDragStart...");
            }

            @Override
            public void onDrag(SnakeHackLayout parent, View view, int left) {
                Logger.d("ActivityDragInterceptor: onDrag: left = " + left);

                if(parent.onlyListenToFastSwipe() || !parent.getUIConfig().allowPageLinkage) return;

                View viewOfLastActivity = ActivityManager.get().getViewOfLastActivity(mActivity);
                if(null != viewOfLastActivity && left > 0) {
                    float ratio = (left * 1.0f) / parent.getWidth();

                    viewOfLastActivity.setX((ratio - 1) * Utils.dp2px(mActivity, 100f));
                }
            }

            @Override
            public void onRelease(SnakeHackLayout parent, View view, int left, boolean shouldClose, int interceptScene) {
                Logger.d("ActivityDragInterceptor: onRelease -> shouldClose = " + shouldClose
                        + ", interceptScene = " + interceptScene + "，left = " + left);

                if(INTERCEPT_SCENE_ROOT_ACTIVITY == interceptScene || parent.ignoredDragEvent()) {
                    parent.smoothScrollToStart(view);
                    return;
                }

                if(parent.onlyListenToFastSwipe() || INTERCEPT_SCENE_TRANSLUCENT_CONVERSION == interceptScene
                        || left <= 0) {
                    resetLastActivityUI();

                    if(shouldClose) {
                        mActivity.finish();
                        mActivity.overridePendingTransition(R.anim.snake_slide_in_left, R.anim.snake_slide_out_right);
                    } else {
                        parent.smoothScrollToStart(view);
                        if(!parent.onlyListenToFastSwipe()) {
                            convertFromTranslucent(mActivity);
                            isTranslucent = false;
                        }
                    }
                    return;
                }


                if(shouldClose) {
                    parent.smoothScrollToLeave(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            resetLastActivityUI();

                            mActivity.finish();
                            mActivity.overridePendingTransition(0, 0);
                        }
                    });
                } else {
                    parent.smoothScrollToStart(view, new SnakeHackLayout.OnReleaseStateListener() {
                        @Override
                        public void onReleaseCompleted(SnakeHackLayout parent, View view) {
                            resetLastActivityUI();

                            convertFromTranslucent(mActivity);
                            isTranslucent = false;
                        }
                    });
                }
            }
        });
        snakeLayout.setDragInterceptor(this);
    }

    private void resetLastActivityUI() {
        View viewOfLastActivity = ActivityManager.get().getViewOfLastActivity(mActivity);
        if(null != viewOfLastActivity) {
            viewOfLastActivity.setX(0f);
        }
    }

    private void convertToTranslucent(Activity activity, TranslucentConversionListener listener) {
        if(needConvertToTranslucent(activity)) {
            ActivityHelper.convertToTranslucent(activity, listener);
        } else {
            if(null != listener) listener.onTranslucentConversionComplete(true);
        }
    }

    private boolean needConvertToTranslucent(Activity activity) {
        return !ActivityHelper.isTranslucent(activity);
    }

    private void convertFromTranslucent(Activity activity) {
        if(needConvertToTranslucent(activity)) {
            ActivityHelper.convertFromTranslucent(activity);
        }
    }

    @Override
    public int intercept(SnakeHackLayout parent, View view, int pointerId) {
        if(ActivityManager.get().isRootActivity(mActivity) && !SnakeConfigReader.get().enableForRootActivity()) {
            return INTERCEPT_SCENE_ROOT_ACTIVITY;
        }

        if(!isTranslucent) return INTERCEPT_SCENE_TRANSLUCENT_CONVERSION;

        return -1;
    }
}
