package com.youngfeng.snake.demo.fragments;

import android.view.View;
import android.widget.TextView;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseFragment;

import butterknife.OnClick;

/**
 * The first fragment.
 *
 * @author Scott Smith 2017-12-24 17:03
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.fragment_first)
public class FirstFragment extends BaseFragment {


    @OnClick(R.id.btn_second_fragment)
    public void goToSecondFragment(View view) {
        SecondFragment secondFragment = Snake.newProxy(SecondFragment.class);
        push(secondFragment);
//        push(SecondFragment.class);
    }

}
