package com.youngfeng.snake.demo.androidx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.FragmentFirstBinding;
import com.youngfeng.snake.demo.utils.EventObserver;
import com.youngfeng.snake.demo.utils.Util;

/**
 * The first support fragment.
 *
 * @author Scott Smith 2017-12-26 14:38
 */
@EnableDragToClose(false)
public class FirstFragment extends Fragment {
    private FragmentViewModel viewModel = new FragmentViewModel();
    private FragmentFirstBinding viewDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = FragmentFirstBinding.inflate(inflater, container, false);
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
            SecondFragment fragment = Snake.newProxySupport(SecondFragment.class);
            Util.push(FirstFragment.this, fragment, R.id.container);
        }));
    }

    private void setupTitle() {
        ((FragmentSampleActivity)requireActivity()).setTitle(FirstFragment.class.getSimpleName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setupTitle();
        }
    }
}
