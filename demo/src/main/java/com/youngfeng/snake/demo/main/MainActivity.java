package com.youngfeng.snake.demo.main;

import android.view.View;

import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.activities.FirstActivity;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.fragments.DragFragmentContainerActivity;
import com.youngfeng.snake.demo.ui.BaseActivity;

import butterknife.OnClick;

/**
 * Main activity.
 *
 * @author Scott Smith 2017-12-24 16:23
 */
@BindView(layoutId = R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @OnClick(R.id.btn_use_in_activity)
    public void useInActivity(View view) {
        start(FirstActivity.class);
    }

    @OnClick(R.id.btn_use_in_fragment)
    public void useInFragment(View view) {
        start(DragFragmentContainerActivity.class);
    }

    @OnClick(R.id.btn_use_in_support_fragment)
    public void useInSupportFragment(View view) {

    }

    @OnClick(R.id.btn_nest_scroll_widget)
    public void nestScrollWidget(View view) {

    }
}
