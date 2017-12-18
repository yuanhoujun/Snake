package com.youngfeng.snake.util;

import android.app.Activity;
import android.app.ActivityOptions;

/**
 * Interface for informing a translucent {@link Activity} once all visible activities below it
 * have completed drawing. This is necessary only after an {@link Activity} has been made
 * opaque using {@link Activity#convertFromTranslucent()} and before it has been drawn
 * translucent again following a call to {@link
 * Activity#convertToTranslucent(android.app.Activity.TranslucentConversionListener,
 * ActivityOptions)}
 */
public interface TranslucentConversionListener {
    /**
     * Callback made following {@link Activity#convertToTranslucent} once all visible Activities
     * below the top one have been redrawn. Following this callback it is safe to make the top
     * Activity translucent because the underlying Activity has been drawn.
     *
     * @param drawComplete True if the background Activity has drawn itself. False if a timeout
     * occurred waiting for the Activity to complete drawing.
     *
     * @see Activity#convertFromTranslucent()
     * @see Activity#convertToTranslucent(TranslucentConversionListener, ActivityOptions)
     */
     void onTranslucentConversionComplete(boolean drawComplete);
}
