package com.youngfeng.snake.demo.ui.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;

/**
 * Abs toolbar.
 *
 * @author Scott Smith 2017-12-24 16:25
 */
public abstract class AbsToolbar extends Toolbar {

    public AbsToolbar(Context context) {
        super(context);
    }

    public AbsToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
