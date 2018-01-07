package com.youngfeng.snake.demo.main;

import android.content.pm.PackageManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.activities.FirstActivity;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.fragments.DragFragmentContainerActivity;
import com.youngfeng.snake.demo.mix.MixActivity;
import com.youngfeng.snake.demo.other.StarGuideActivity;
import com.youngfeng.snake.demo.support.SupportDragFragmentContainerActivity;
import com.youngfeng.snake.demo.ui.BaseActivity;

import butterknife.OnClick;

/**
 * Main activity.
 *
 * @author Scott Smith 2017-12-24 16:23
 */
@BindView(layoutId = R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @butterknife.BindView(R.id.text_version) TextView mTextVersion;

    @Override
    protected void onInitView() {
        super.onInitView();
        setTitle(R.string.demo_drag_to_close);
        setReturnBackVisible(false);
        setVersion();
    }

    private void setVersion() {
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mTextVersion.setText(Html.fromHtml(getString(R.string.ph_version).replace("#version", versionName)));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_use_in_activity)
    public void useInActivity(View view) {
        start(FirstActivity.class);
    }

    @OnClick(R.id.btn_use_in_fragment)
    public void useInFragment(View view) {
        start(DragFragmentContainerActivity.class);
    }

    @OnClick(R.id.btn_use_in_support_fragment)
    public void useInSupportFragment(View view) {
        start(SupportDragFragmentContainerActivity.class);
    }

    @OnClick(R.id.btn_mix_use)
    public void mixUse(View view) {
        start(MixActivity.class);
    }

    @OnClick(R.id.btn_check_update)
    public void checkUpdate() {
        Beta.checkUpgrade();
    }

    @OnClick(R.id.btn_share_to_wechat)
    public void shareToWechat() {

    }

    @OnClick(R.id.btn_cheer_for_the_author)
    public void cheerFor() {
        start(StarGuideActivity.class);
    }

    @Override
    protected boolean needAutoUpdateTitle() {
        return false;
    }
}
