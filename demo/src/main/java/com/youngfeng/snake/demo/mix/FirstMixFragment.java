package com.youngfeng.snake.demo.mix;

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

    @OnClick(R.id.btn_next)
    public void nextFragment() {
        push(Snake.newProxySupport(SecondMixFragment.class));
    }
}
