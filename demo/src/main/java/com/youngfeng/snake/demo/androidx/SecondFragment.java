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
import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.FragmentSecondBinding;
import com.youngfeng.snake.demo.utils.EventObserver;

/**
 * The first support fragment.
 *
 * @author Scott Smith 2017-12-26 14:38
 */
@EnableDragToClose()
public class SecondFragment extends Fragment {
    private FragmentViewModel viewModel = new FragmentViewModel();
    private FragmentSecondBinding viewDataBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = FragmentSecondBinding.inflate(inflater, container, false);
        viewDataBinding.setVm(viewModel);
        viewDataBinding.setLifecycleOwner(getViewLifecycleOwner());

        return viewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupNavigation();
        setupDragToCloseStatus();
    }

    private void setupNavigation() {
        viewModel.goToThirdFrgEvent.observe(this, new EventObserver<>(content -> {
            NavController navController = NavHostFragment.findNavController(SecondFragment.this);
            navController.navigate(R.id.action_SecondFragment_to_ThirdFragment);
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
}
