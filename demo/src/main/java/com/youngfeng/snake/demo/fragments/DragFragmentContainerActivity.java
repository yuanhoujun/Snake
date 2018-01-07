package com.youngfeng.snake.demo.fragments;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.ui.FragmentContainerActivity;

/**
 * Drag fragment container activity.
 *
 * @author Scott Smith 2017-12-24 21:45
 */
@EnableDragToClose()
public class DragFragmentContainerActivity extends FragmentContainerActivity {

    @Override
    protected void onInitView() {
        super.onInitView();
        setToolbarVisible(false);

        Snake.host(this);
        switchTo(FirstFragment.class);
    }
}
