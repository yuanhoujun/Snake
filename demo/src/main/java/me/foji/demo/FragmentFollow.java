package me.foji.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.foji.snake.annotations.SlideToClose;

/**
 * Created by scott on 2016/11/6.
 */
@SlideToClose(enable = false)
public class FragmentFollow extends BaseFragment {
    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follow, container, false);
    }

    @Override
    protected boolean openSlideToClose() {
        return false;
    }
}
