package me.foji.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.foji.snake.Snake;

public class TestFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        Snake.init(this);
    }
}
