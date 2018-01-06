package com.youngfeng.snake.demo.support;

import android.view.View;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.fragments.ThirdFragment;
import com.youngfeng.snake.demo.ui.BaseSupportFragment;

import butterknife.OnClick;

/**
 * The second support fragment.
 *
 * @author Scott Smith 2017-12-26 14:38
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.fragment_second_support)
public class SecondSupportFragment extends BaseSupportFragment {
    @OnClick(R.id.btn_next)
    public void goToNextFragment(View view) {
        ThirdSupportFragment thirdFragment = Snake.newProxySupport(ThirdSupportFragment.class);
        push(thirdFragment);

//        disableAnimation(true);
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
