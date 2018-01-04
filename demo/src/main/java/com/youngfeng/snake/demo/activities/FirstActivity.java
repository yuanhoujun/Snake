package com.youngfeng.snake.demo.activities;

import android.util.Log;
import android.view.View;

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

    @Override
    protected void onInitView() {
        Snake.host(this);

        Log.e("isTranslucent", "This is translucent: " + ActivityHelper.isTranslucent(this) + "");

        Snake.addDragListener(this, new Snake.OnDragListener() {
            @Override
            public void onDragStart(View view) {
                super.onDragStart(view);

                Log.e("DragListener", "拖拽开始 <<<");
            }

            @Override
            public void onDrag(View view, int left) {
                super.onDrag(view, left);

                Log.e("DragListener", "拖拽中 <<< " + left);
            }

            @Override
            public void onRelease(View view, float xVelocity) {
                super.onRelease(view, xVelocity);

                Log.e("DragListener", "拖拽释放 <<< " + xVelocity + "@@@" + view);
            }
        });

    }

    @OnClick(R.id.btn_second_activity)
    public void goToSecondActivity(View view) {
        start(SecondActivity.class);
        overridePendingTransition(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left);
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
