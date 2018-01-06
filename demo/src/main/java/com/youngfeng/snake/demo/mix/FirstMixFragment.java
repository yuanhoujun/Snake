package com.youngfeng.snake.demo.mix;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseSupportFragment;

import butterknife.OnClick;

/**
 * First mix fragment.
 *
 * @author Scott Smith 2018-01-04 12:37
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.fragment_first_mix)
public class FirstMixFragment extends BaseSupportFragment {
    @butterknife.BindView(R.id.text_open_status) TextView mTextOpenStatus;

    @Override
    protected void onInitView() {
        super.onInitView();
        updateOpenStatus();
    }

    @OnClick(R.id.btn_next)
    public void nextFragment() {
        push(Snake.newProxySupport(SecondMixFragment.class));
    }

    private void updateOpenStatus() {
        String status = Snake.dragToCloseEnabled(this) ? "已开启" : "已禁用";
        mTextOpenStatus.setText(Html.fromHtml(getString(R.string.ph_status_of_enable_drag_to_close)
                .replace("#status", status)));
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
