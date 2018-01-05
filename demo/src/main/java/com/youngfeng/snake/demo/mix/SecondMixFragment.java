package com.youngfeng.snake.demo.mix;

import android.view.View;
import android.widget.ImageView;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseSupportFragment;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onInitView() {
        super.onInitView();

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
            public void onDrag(View view, int left) {
                super.onDrag(view, left);
            }

            @Override
            public void onRelease(View view, float xVelocity) {
                super.onRelease(view, xVelocity);
                mViewPager.autoScroll();
            }
        });
    }
}
