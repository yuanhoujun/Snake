package com.youngfeng.snake.demo.activities;

import android.view.View;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseActivity;

import butterknife.OnClick;

/**
 * The first activity.
 *
 * @author Scott Smith 2017-12-24 17:01
 */
@BindView(layoutId = R.layout.activity_first)
@EnableDragToClose()
public class FirstActivity extends BaseActivity {

    @Override
    protected void onInitView() {
        Snake.host(this);
    }

    @OnClick(R.id.btn_second_activity)
    public void goToSecondActivity(View view) {
        start(SecondActivity.class);
    }

    @OnClick(R.id.btn_disable_drag_to_close)
    public void disableDragToClose(View view) {
        Snake.enableDragToClose(this, false);
        toast("滑动关闭功能已禁用");
    }

    @OnClick(R.id.btn_enable_drag_to_close)
    public void enableDragToClose(View view) {
        Snake.enableDragToClose(this, true);
        toast("滑动关闭功能已开启");
    }
}
