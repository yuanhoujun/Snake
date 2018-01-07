package com.youngfeng.snake.demo.other;

import android.net.Uri;

import com.github.chrisbanes.photoview.PhotoView;
import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseActivity;

/**
 * Image browser.
 *
 * @author Scott Smith 2018-01-07 15:51
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.activity_image_browser)
public class ImageBrowserActivity extends BaseActivity {
    @butterknife.BindView(R.id.photoView) PhotoView mPhotoView;

    public static final String KEY_IMAGE_URI = "image_uri";

    @Override
    protected void onInitView() {
        super.onInitView();
        Snake.host(this);

        Uri uri = getIntent().getParcelableExtra(KEY_IMAGE_URI);
        if(null != uri) {
            mPhotoView.setImageURI(uri);
        }
    }
}
