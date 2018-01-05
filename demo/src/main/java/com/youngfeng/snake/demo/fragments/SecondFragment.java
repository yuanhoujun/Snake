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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Snake.addDragListener(this, new Snake.OnDragListener() {
            @Override
            public void onDragStart(View view) {
                super.onDragStart(view);
                Log.e("FirstFragment", "拖拽开始");
            }

            @Override
            public void onDrag(View view, int left) {
                super.onDrag(view, left);
                Log.e("FirstFragment", "拖拽中");
            }

            @Override
            public void onRelease(View view, float xVelocity) {
                super.onRelease(view, xVelocity);
                Log.e("FirstFragment", "拖拽释放: " + view);
            }
        });
    }

    @OnClick(R.id.btn_next)
    public void goToThirdFragment(View view) {
        push(Snake.newProxy(ThirdFragment.class));
    }
}
