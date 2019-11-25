package com.youngfeng.snake.demo.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.databinding.SimpleDialogBinding;

/**
 * This is a short description.
 *
 * @author Scott Smith 2019-11-25 21:53
 */
public class SimpleDialog extends Dialog {
    private SimpleDialogBinding dataBinding;

    public SimpleDialog(@NonNull Context context) {
        this(context, R.style.DialogStyle);
    }

    public SimpleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        dataBinding = SimpleDialogBinding.inflate(LayoutInflater.from(getContext()));

        if (context instanceof LifecycleOwner) {
            dataBinding.setLifecycleOwner((LifecycleOwner) context);
        }
        setContentView(dataBinding.getRoot());
    }

    public void setTitle(String title) {
        dataBinding.setTitle(title);
    }

    public void setContent(String content) {
        dataBinding.setContent(content);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String content;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public SimpleDialog build() {
            SimpleDialog dialog = new SimpleDialog(context);

            dialog.setTitle(title);
            dialog.setContent(content);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

//            Window window = dialog.getWindow();
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            lp.gravity = Gravity.CENTER;
//            window.setAttributes(lp);

            return dialog;
        }
    }
}
