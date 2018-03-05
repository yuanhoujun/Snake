package com.youngfeng.snake.demo.inherit;

import com.youngfeng.snake.annotations.EnableDragToClose;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.BaseSupportFragment2;
import com.youngfeng.snake.demo.utils.Util;

import butterknife.OnClick;

/**
 * The third fragment.
 *
 * @author Scott Smith 2017-12-24 17:03
 */
@EnableDragToClose()
@BindView(layoutId = R.layout.fragment_third_support)
public class ThirdSupportFragment extends BaseSupportFragment2 {

    @OnClick(R.id.btn_star_this_repo)
    public void starRepo() {
        Util.openGitRepo(getActivity());
    }

    @OnClick(R.id.btn_follow_the_authors_article)
    public void openAuthorsJianshu() {
        Util.openJianshu(getActivity());
    }
}
