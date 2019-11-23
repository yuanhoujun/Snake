package com.youngfeng.snake.demo.splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.youngfeng.snake.demo.main.MainActivity;


/**
 * Splash activity.
 *
 * @author Scott Smith 2018-01-17 16:19
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
