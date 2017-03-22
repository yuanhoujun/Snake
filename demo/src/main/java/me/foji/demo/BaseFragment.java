package me.foji.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.foji.snake.Snake;
import me.foji.snake.v4.app.Fragment;

/**
 * Created by scott on 2016/11/6.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用方式同Activity完全一样, 记得也要在onDestory里面调用回收方法
        Snake.init(this);
    }

    protected boolean openSlideToClose() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
