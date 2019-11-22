package com.youngfeng.snake.demo.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.main.MainActivity;
import com.youngfeng.snake.demo.ui.BaseActivity;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Splash activity.
 *
 * @author Scott Smith 2018-01-17 16:19
 */
@BindView(layoutId = R.layout.activity_splash)
public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private Handler mHandler = new Handler();
    private final int RC_WRITE_EXTERNAL_STORAGE = 0x1;
    private AlertDialog mDialog;

    @Override
    protected void onInitView() {
        super.onInitView();
        permissionCheck();
    }

    private void permissionCheck() {
        if(!EasyPermissions.hasPermissions(this , Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this , getString(R.string.tip_grant_permissions),
                                RC_WRITE_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            next();
        }
    }

    private void next() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start(MainActivity.class);
                finish();
            }
        }, 2000);
    }

    @Nullable
    @Override
    public Toolbar toolbar() {
        return null;
    }

    private void showGrantPermissionDialog() {
        if(null == mDialog) {
            mDialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.tip_grant_permissions)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openSystemSetting();
                            finish();
                        }
                    })
                    .create();
        }

        if(!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    private void openSystemSetting() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS ,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {}
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        next();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this , perms)) {
            showGrantPermissionDialog();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
