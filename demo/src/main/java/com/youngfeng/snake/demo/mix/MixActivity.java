package com.youngfeng.snake.demo.mix;

import android.view.View;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseActivity;

import butterknife.OnClick;

/**
 * Mix activity
 *
 * @author Scott Smith 2018-01-04 12:01
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.activity_mix)
public class MixActivity extends BaseActivity {

    @Override
    protected void onInitView() {
        super.onInitView();
        Snake.host(this);
    }

    @OnClick(R.id.btn_next)
    public void goToNextActivity(View view) {
        start(SecondMixActivity.class);
    }
}
