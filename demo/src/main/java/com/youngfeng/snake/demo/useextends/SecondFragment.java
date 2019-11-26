package com.youngfeng.snake.demo.useextends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.youngfeng.snake.Snake;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.FragmentSecond2Binding;
import com.youngfeng.snake.demo.utils.EventObserver;
import com.youngfeng.snake.demo.utils.Util;

/**
 * The first support fragment.
 *
 * @author Scott Smith 2017-12-26 14:38
 */
//@EnableDragToClose()
public class SecondFragment extends BaseFragment {
    private FragmentViewModel viewModel = new FragmentViewModel();
    private FragmentSecond2Binding viewDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = FragmentSecond2Binding.inflate(inflater, container, false);
        viewDataBinding.setVm(viewModel);
        viewDataBinding.setLifecycleOwner(getViewLifecycleOwner());

        return viewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupNavigation();
        setupDragToCloseStatus();
        setupTitle();
    }

    private void setupNavigation() {
        viewModel.goToThirdFrgEvent.observe(this, new EventObserver<>(content -> {
            ThirdFragment fragment = new ThirdFragment();
            Util.push(SecondFragment.this, fragment, R.id.container);
        }));
    }

    private void setupDragToCloseStatus() {
        viewModel.dragToCloseEnable.observe(this, enable -> {
            Snake.enableDragToClose(SecondFragment.this, enable);

            viewModel.setDragToCloseStatusText(getString(R.string.ph_status_of_enable_drag_to_close)
                    .replace("#status", enable ? "开启" : "关闭"));
        });

        // Get enabled status of current activity.
        boolean isEnabled = Snake.dragToCloseEnabled(this);
        viewModel.enableDragToClose(isEnabled);
    }

    private void setupTitle() {
        ((FragmentSample2Activity)requireActivity()).setTitle(SecondFragment.class.getSimpleName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setupTitle();
        }
    }
}
