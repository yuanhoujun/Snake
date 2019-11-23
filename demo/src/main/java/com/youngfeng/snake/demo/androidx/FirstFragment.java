package com.youngfeng.snake.demo.androidx;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.FragmentFirstBinding;
import com.youngfeng.snake.demo.utils.EventObserver;

/**
 * The first support fragment.
 *
 * @author Scott Smith 2017-12-26 14:38
 */
@EnableDragToClose()
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
    }

    private void setupNavigation() {
        viewModel.goToSecondFrgEvent.observe(this, new EventObserver<>(content -> {
            NavController navController = NavHostFragment.findNavController(FirstFragment.this);
            navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
        }));
    }
}
