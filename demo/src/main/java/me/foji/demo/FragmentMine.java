package me.foji.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by scott on 2016/11/6.
 */
public class FragmentMine extends BaseFragment {
    @Nullable
    @Override
    public View onBindView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    protected boolean openSlideToClose() {
        return false;
    }
}
