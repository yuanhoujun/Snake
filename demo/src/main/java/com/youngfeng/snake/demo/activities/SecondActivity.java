package com.youngfeng.snake.demo.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.ActivitySecondBinding;

/**
 * The first activity.
 *
 * @author Scott Smith 2017-12-24 17:01
 */
@EnableDragToClose()
public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_second);
        dataBinding.setOnNavigateUpListener(() -> finish());
        dataBinding.setLifecycleOwner(this);
        dataBinding.setTitle(SecondActivity.class.getSimpleName());
    }
}
