package me.foji.demo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.foji.snake.Snake;

/**
 * Created by scott on 2016/11/6.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用方式同Activity完全一样, 记得也要在onDestory里面调用回收方法
//        Snake.init(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return onBindView(inflater, container, savedInstanceState);
    }

    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    protected boolean openSlideToClose() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
