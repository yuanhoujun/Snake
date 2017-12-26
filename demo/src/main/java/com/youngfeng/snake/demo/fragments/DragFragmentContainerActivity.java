package com.youngfeng.snake.demo.fragments;

import com.youngfeng.snake.demo.ui.FragmentContainerActivity;

/**
 * Drag fragment container activity.
 *
 * @author Scott Smith 2017-12-24 21:45
 */
public class DragFragmentContainerActivity extends FragmentContainerActivity {

    @Override
    protected void onInitView() {
        super.onInitView();
        switchTo(FirstFragment.class);
    }
}
