package com.youngfeng.snake.demo.fragments;

import android.view.View;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseFragment;

import butterknife.OnClick;

/**
 * The second fragment.
 *
 * @author Scott Smith 2017-12-24 17:03
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.fragment_second)
public class SecondFragment extends BaseFragment {

    @OnClick(R.id.btn_next)
    public void goToNextFragment(View view) {
        ThirdFragment thirdFragment = Snake.newProxy(ThirdFragment.class);
        push(thirdFragment);
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
