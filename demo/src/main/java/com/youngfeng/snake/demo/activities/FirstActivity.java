package com.youngfeng.snake.demo.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.ActivityFirstBinding;
import com.youngfeng.snake.demo.utils.EventObserver;

/**
 * The first activity.
 *
 * @author Scott Smith 2017-12-24 17:01
 */
@EnableDragToClose()
public class FirstActivity extends AppCompatActivity {
    private ActivityViewModel viewModel = new ActivityViewModel();

    private ActivityFirstBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_first);
        dataBinding.setLifecycleOwner(this);
        dataBinding.setVm(viewModel);
        dataBinding.setOnNavigateUpListener(() -> {
            finish();
        });
        dataBinding.setTitle(FirstActivity.class.getSimpleName());

        setupDragToCloseStatus();
        setupNavigation();
    }

    private void setupDragToCloseStatus() {
        viewModel.dragToCloseEnable.observe(this, enable -> {
            Snake.enableDragToClose(FirstActivity.this, enable);

            viewModel.setDragToCloseStatusText(getString(R.string.ph_status_of_enable_drag_to_close)
                            .replace("#status", enable ? "开启" : "关闭"));
        });

        // Get enabled status of current activity.
        boolean isEnabled = Snake.dragToCloseEnabled(this);
        viewModel.enableDragToClose(isEnabled);
    }

    private void setupNavigation() {
        viewModel.goToNextEvent.observe(this, new EventObserver<>(content -> {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left);
        }));;
    }

    public void setTitle(String title) {
        viewModel.setTitle(title);
    }
}
