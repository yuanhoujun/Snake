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
}
