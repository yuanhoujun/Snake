package com.youngfeng.snake.demo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
    public void goToThirdFragment(View view) {
        push(Snake.newProxy(ThirdFragment.class));
//        push(new ThirdFragment());
    }
}
