package com.youngfeng.snake.demo.mix;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseSupportFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import me.foji.widget.QuickAutoScrollViewPager;
import me.foji.widget.QuickScrollPagerAdapter;

/**
 * Second mix fragment.
 *
 * @author Scott Smith 2018-01-04 12:37
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.fragment_second_mix)
public class SecondMixFragment extends BaseSupportFragment {
    @butterknife.BindView(R.id.view_pager) QuickAutoScrollViewPager mViewPager;
    @butterknife.BindView(R.id.text_open_status) TextView mTextOpenStatus;

    @Override
    protected void onInitView() {
        super.onInitView();
        updateOpenStatus();

        List<Integer> data = new ArrayList<>();
        data.add(R.mipmap.t1);
        data.add(R.mipmap.t2);
        data.add(R.mipmap.t3);

        mViewPager.setQuickAdapter(new QuickScrollPagerAdapter<Integer>(data) {
            @Override
            public void convert(ImageView imageView, int position, Integer resourceId) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(resourceId);
            }
        });
        mViewPager.autoScroll();

        Snake.addDragListener(this, new Snake.OnDragListener() {
            @Override
            public void onDragStart(View view) {
                super.onDragStart(view);
                mViewPager.stopAutoScroll();
            }

            @Override
            public void onBackToStartCompleted(View view) {
                super.onBackToStartCompleted(view);
                mViewPager.autoScroll();
            }
        });
    }


    private void updateOpenStatus() {
        String status = Snake.dragToCloseEnabled(this) ? "已开启" : "已禁用";
        mTextOpenStatus.setText(Html.fromHtml(getString(R.string.ph_status_of_enable_drag_to_close)
                .replace("#status", status)));
    }

    @OnClick(R.id.btn_disable_drag_to_close)
    public void disableDragToClose(View view) {
        if(Snake.dragToCloseEnabled(this)) {
            Snake.enableDragToClose(this, false);
            updateOpenStatus();
            toast("滑动关闭功能已禁用");
        } else {
            toast("滑动关闭功能已禁用，无需重复调用");
        }
    }

    @OnClick(R.id.btn_enable_drag_to_close)
    public void enableDragToClose(View view) {
        if(!Snake.dragToCloseEnabled(this)) {
            Snake.enableDragToClose(this, true);
            updateOpenStatus();
            toast("滑动关闭功能已开启");
        } else {
            toast("滑动关闭功能已开启，无需重复调用");
        }
    }
}
