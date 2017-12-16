package me.foji.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.youngfeng.Snake;
import com.youngfeng.annotations.EnableDragToClose;

/**
 * Created by scott on 2016/11/6.
 */
@EnableDragToClose()
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Snake.host(this);
        // 仅仅使用这一句代码，就可以开启滑动关闭功能
//        Snake.init(this);
//        com.youngfeng.Snake.openDragToCloseForActivity(this);
    }
}
