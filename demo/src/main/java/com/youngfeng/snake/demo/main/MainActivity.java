package com.youngfeng.snake.demo.main;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.activities.FirstActivity;
import com.youngfeng.snake.demo.androidx.FragmentSampleActivity;
import com.youngfeng.snake.demo.databinding.ActivityMainBinding;
import com.youngfeng.snake.demo.useextends.FragmentSample2Activity;
import com.youngfeng.snake.demo.utils.EventObserver;
import com.youngfeng.snake.demo.utils.Util;
import com.youngfeng.snake.demo.widget.SimpleDialog;

/**
 * Main activity.
 *
 * @author Scott Smith 2017-12-24 16:23
 */
@EnableDragToClose()
public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel = new MainViewModel();
    private ActivityMainBinding dataBinding;
    private SimpleDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dataBinding.setLifecycleOwner(this);
        dataBinding.setVm(viewModel);

        setupViews();
        setupNavigation();
        setupListeners();
    }

    private void setupViews() {
        int oldFlag = dataBinding.btnUseInFragment.getPaintFlags();
        dataBinding.btnUseInFragment.setPaintFlags(oldFlag | Paint.STRIKE_THRU_TEXT_FLAG);
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

        viewModel.startGitRepoEvent.observe(this, new EventObserver<>(content -> {
            Util.startGitRepo(MainActivity.this);
        }));

        viewModel.openUseInheritEvent.observe(this, new EventObserver<>(content -> {
            Intent intent = new Intent(MainActivity.this, FragmentSample2Activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left);
        }));
    }

    private void setupListeners() {
        dataBinding.btnAboutAuthor.setOnClickListener(v -> {
            Util.copy(MainActivity.this, "Wechat", "欧阳锋工作室");
            Toast.makeText(MainActivity.this, "公众号已复制，请粘贴到微信搜索栏", Toast.LENGTH_LONG)
                    .show();
            Util.startWechat(MainActivity.this);
        });

        dataBinding.btnUpdateLog.setOnClickListener(v -> {
            showDialog(getString(R.string.update_log), getString(R.string.update_log_content));
        });

        dataBinding.btnUseInFragment.setOnClickListener(v -> {
            showDialog(getString(R.string.prompt_tips), getString(R.string.tips_app_fragment));
        });

        viewModel.copyQQNumberEvent.observe(this, new EventObserver<>(content -> {
            Util.copy(MainActivity.this, "QQ", "288177681");
            Toast.makeText(MainActivity.this, "QQ号已复制，请粘贴到QQ搜索栏", Toast.LENGTH_LONG)
                    .show();
        }));
    }

    private void showDialog(String title, String content) {
        if (null == dialog) {
            dialog = new SimpleDialog.Builder(this).title(title).content(content).build();
        }

        dialog.setTitle(title);
        dialog.setContent(content);

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
