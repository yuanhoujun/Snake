package com.youngfeng.snake.demo.fragments;

import android.animation.Animator;
import android.view.View;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.animation.AnimationFactory;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
