package com.youngfeng.snake.demo.activities;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseActivity;
import com.youngfeng.snake.util.ActivityHelper;
import com.youngfeng.snake.view.SnakeHackLayout;

import butterknife.OnClick;

/**
 * The first activity.
 *
 * @author Scott Smith 2017-12-24 17:01
 */
@BindView(layoutId = R.layout.activity_first)
@EnableDragToClose()
public class FirstActivity extends BaseActivity {
    @butterknife.BindView(R.id.text_open_status) TextView mTextOpenStatus;

    @Override
    protected void onInitView() {
        Snake.host(this);
        updateOpenStatus();
    }

    private void updateOpenStatus() {
        String status = Snake.dragToCloseEnabled(this) ? "已开启" : "已禁用";
        mTextOpenStatus.setText(Html.fromHtml(getString(R.string.ph_status_of_enable_drag_to_close)
                .replace("#status", status)));
    }

    @OnClick(R.id.btn_second_activity)
    public void goToSecondActivity(View view) {
        start(SecondActivity.class);
        overridePendingTransition(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left);
    }

    @OnClick(R.id.btn_disable_drag_to_close)
    public void disableDragToClose(View view) {
        if(Snake.dragToCloseEnabled(this)) {
            Snake.enableDragToClose(this, false);
            updateOpenStatus();
            toast("滑动关闭功能已禁用");
        } else {
            toast("滑动关闭功能已禁用，无需重复调用");
        }
    }

    @OnClick(R.id.btn_enable_drag_to_close)
    public void enableDragToClose(View view) {
        if(!Snake.dragToCloseEnabled(this)) {
            Snake.enableDragToClose(this, true);
            updateOpenStatus();
            toast("滑动关闭功能已开启");
        } else {
            toast("滑动关闭功能已开启，无需重复调用");
        }
    }
}
