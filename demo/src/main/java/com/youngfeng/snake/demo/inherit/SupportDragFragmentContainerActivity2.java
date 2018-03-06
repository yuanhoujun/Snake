package com.youngfeng.snake.demo.inherit;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.ui.FragmentContainerActivity;

/**
 * Support drag fragment container activity.
 *
 * @author Scott Smith 2017-12-26 14:37
 */
@EnableDragToClose()
public class SupportDragFragmentContainerActivity2 extends FragmentContainerActivity {

    @Override
    protected void onInitView() {
        super.onInitView();
        setToolbarVisible(false);

        Snake.host(this);
        supportSwitchTo(FirstSupportFragment.class);
    }
}
