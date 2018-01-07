package com.youngfeng.snake.demo.other;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.Constant;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseActivity;
import com.youngfeng.snake.demo.utils.Util;

import butterknife.OnClick;

/**
 * Star guide activity.
 *
 * @author Scott Smith 2018-01-07 15:20
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.activity_star_guide)
public class StarGuideActivity extends BaseActivity {

    @Override
    protected void onInitView() {
        super.onInitView();
        Snake.host(this);
    }

    @OnClick(R.id.btn_copy)
    public void copyRepoUrl() {
        copyToClipboard(Constant.URL_GIT_REPO);
        toast("版本库链接已复制");
    }

    @OnClick(R.id.btn_open)
    public void useComputerBrowser() {
        toast("使用电脑浏览器输入复制的链接打开，点击右侧star即可");
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Repo url", text);
        clipboardManager.setPrimaryClip(clipData);
    }

    @OnClick(R.id.image_star_repo)
    public void browseImage() {
        Util.browseImage(this, Uri.parse("android.resource://" + getPackageName()
                + "/" + R.mipmap.star_repo));
    }
}
