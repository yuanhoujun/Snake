package me.foji.demo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youngfeng.annotations.EnableDragToClose;

/**
 * 请描述使用该类使用方法！！！
 *
 * @author Scott Smith 2017-12-14 12:44
 */
@EnableDragToClose()
public class SnakeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
