package com.youngfeng.snake.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.youngfeng.snake.demo.Constant;
import com.youngfeng.snake.demo.other.ImageBrowserActivity;

/**
 * Util
 *
 * @author Scott Smith 2018-01-07 14:59
 */
public class Util {

    public static void openGitRepo(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Constant.URL_GIT_REPO));
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public static void openJianshu(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constant.URL_JIANSHU));
        context.startActivity(intent);
    }

    public static void browseImage(Context context, Uri uri) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtra(ImageBrowserActivity.KEY_IMAGE_URI, uri);
        context.startActivity(intent);
    }
}
