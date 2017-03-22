package me.foji.demo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.foji.snake.Snake;

/**
 * Created by scott on 2016/11/6.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 仅仅使用这一句代码，就可以开启滑动关闭功能
        Snake.init(this);
    }
}
