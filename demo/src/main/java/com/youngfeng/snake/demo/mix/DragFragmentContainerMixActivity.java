package com.youngfeng.snake.demo.mix;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.ui.FragmentContainerActivity;

/**
 * Drag fragment container mix activity.
 *
 * @author Scott Smith 2018-01-04 12:58
 */
@EnableDragToClose()
public class DragFragmentContainerMixActivity extends FragmentContainerActivity {

    @Override
    protected void onInitView() {
        super.onInitView();
        setToolbarVisible(false);

        Snake.host(this);
        supportSwitchTo(FirstMixFragment.class);
    }
}
