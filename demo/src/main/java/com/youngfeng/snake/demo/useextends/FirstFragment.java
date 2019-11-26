package com.youngfeng.snake.demo.useextends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.FragmentFirst2Binding;
import com.youngfeng.snake.demo.utils.EventObserver;
import com.youngfeng.snake.demo.utils.Util;

/**
 * The first support fragment.
 *
 * @author Scott Smith 2017-12-26 14:38
 */
//@EnableDragToClose(false)
public class FirstFragment extends BaseFragment {
    private FragmentViewModel viewModel = new FragmentViewModel();
    private FragmentFirst2Binding viewDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = FragmentFirst2Binding.inflate(inflater, container, false);
        viewDataBinding.setVm(viewModel);
        viewDataBinding.setLifecycleOwner(getViewLifecycleOwner());

        return viewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupNavigation();
        setupTitle();
    }

    private void setupNavigation() {
        viewModel.goToSecondFrgEvent.observe(this, new EventObserver<>(content -> {
            SecondFragment fragment = new SecondFragment();
            Util.push(FirstFragment.this, fragment, R.id.container);
        }));
    }

    private void setupTitle() {
        ((FragmentSample2Activity)requireActivity()).setTitle(FirstFragment.class.getSimpleName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setupTitle();
        }
    }
}
