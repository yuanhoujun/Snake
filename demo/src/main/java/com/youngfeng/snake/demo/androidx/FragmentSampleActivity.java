package com.youngfeng.snake.demo.androidx;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;

/**
 * Fragment sample activity.
 *
 * @author Scott Smith 2019-11-23 12:31
 */
@EnableDragToClose()
public class FragmentSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sample);
    }
}
