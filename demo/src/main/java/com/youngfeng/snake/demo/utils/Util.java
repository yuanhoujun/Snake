package com.youngfeng.snake.demo.utils;

import android.content.*;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.youngfeng.snake.demo.Constant;
import com.youngfeng.snake.demo.R;

/**
 * Utilities class.
 *
 * @author Scott Smith 2019-11-25 10:12
 */
public class Util {

    public static void push(Fragment current, Fragment next, @IdRes int containerId) {
        FragmentTransaction ft = current.requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left,
                R.anim.snake_slide_in_left, R.anim.snake_slide_out_right);
        ft.add(containerId, next, next.getClass().getCanonicalName()).hide(current)
                .addToBackStack(current.getClass().getCanonicalName());
        ft.commit();
    }

    public static void switchTo(AppCompatActivity activity, Fragment target, @IdRes int containerId) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(containerId, target, target.getClass().getCanonicalName());
        ft.setCustomAnimations(0, 0, 0, 0);
        ft.commit();
    }

    public static void startGitRepo(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constant.URL_GIT_REPO));
        context.startActivity(intent);
    }

    public static void startWechat(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "检测到你的手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
        }
    }

    public static void copy(Context context, String label, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText(label, text);
        cm.setPrimaryClip(mClipData);
    }

    public static int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(dp * density + 0.5f);
    }
}
