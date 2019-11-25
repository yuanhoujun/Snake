package com.youngfeng.snake.demo.androidx;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.ActivityFragmentSampleBinding;
import com.youngfeng.snake.demo.utils.Util;

/**
 * Fragment sample activity.
 *
 * @author Scott Smith 2019-11-23 12:31
 */
@EnableDragToClose()
public class FragmentSampleActivity extends AppCompatActivity {
    private FragmentViewModel viewModel = new FragmentViewModel();

    private ActivityFragmentSampleBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_fragment_sample);
        dataBinding.setOnNavigateUpListener(() -> {
            if (!getSupportFragmentManager().popBackStackImmediate()) {
                finish();
            }
        });
        dataBinding.setVm(viewModel);
        dataBinding.setLifecycleOwner(this);

        FirstFragment fragment = Snake.newProxySupport(FirstFragment.class);
        Util.switchTo(this, fragment, R.id.container);
    }

    public void setTitle(String title) {
        viewModel.setTitle(title);
    }
}
