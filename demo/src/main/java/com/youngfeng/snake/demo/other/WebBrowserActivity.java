package com.youngfeng.snake.demo.other;

import android.os.Handler;
import android.util.Log;
import android.webkit.WebViewClient;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.Constant;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseActivity;
import com.youngfeng.snake.view.SnakeWebView;

/**
 * Web browser activity.
 *
 * @author Scott Smith 2018-01-05 16:01
 */
@EnableDragToClose
@BindView(layoutId = R.layout.activity_web_browser)
public class WebBrowserActivity extends BaseActivity {
    @butterknife.BindView(R.id.webView) SnakeWebView mWebView;

    @Override
    protected void onInitView() {
        super.onInitView();
        Snake.host(this);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(Constant.URL_JIANSHU);

        mWebView.setOnDragListener(new SnakeWebView.OnDragListener() {
            @Override
            public void onFling(float velocityX, SnakeWebView.DragMode mode) {
                Log.e(getClass().getSimpleName(), velocityX + ": " + mode);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.pauseDragging();
                Log.e(getClass().getSimpleName(), "拖拽已经暂停");

            }
        }, 5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.resumeDragging();
                Log.e(getClass().getSimpleName(), "拖拽已经恢复");
            }
        }, 10000);
    }
}
