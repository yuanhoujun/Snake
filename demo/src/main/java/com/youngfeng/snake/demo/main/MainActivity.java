package com.youngfeng.snake.demo.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.activities.FirstActivity;
import com.youngfeng.snake.demo.androidx.FragmentSampleActivity;
import com.youngfeng.snake.demo.databinding.ActivityMainBinding;
import com.youngfeng.snake.demo.utils.EventObserver;

/**
 * Main activity.
 *
 * @author Scott Smith 2017-12-24 16:23
 */
@EnableDragToClose()
public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel = new MainViewModel();
    private ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dataBinding.setLifecycleOwner(this);
        dataBinding.setVm(viewModel);

        setupNavigation();
    }

    private void setupNavigation() {
        viewModel.openUseCaseInActivityEvent.observe(this, new EventObserver<>(content -> {
            Intent intent = new Intent(MainActivity.this, FirstActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left);
        }));

        viewModel.openUseCaseInAndroidXFrgEvent.observe(this, new EventObserver<>(content -> {
            Intent intent = new Intent(MainActivity.this, FragmentSampleActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left);
        }));
    }
}
