package com.youngfeng.snake.demo.support;

import android.view.View;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseSupportFragment;

import butterknife.OnClick;

/**
 * The first support fragment.
 *
 * @author Scott Smith 2017-12-26 14:38
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.fragment_first_support)
public class FirstSupportFragment extends BaseSupportFragment {

    @OnClick(R.id.btn_second_fragment)
    public void goToSecondFragment(View view) {
        push(Snake.newSupportProxy(SecondSupportFragment.class));
    }
}
