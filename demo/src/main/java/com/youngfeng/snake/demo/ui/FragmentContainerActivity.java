package com.youngfeng.snake.demo.ui;

import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;

/**
 * Fragment container activity.
 *
 * @author Scott Smith 2017-12-24 17:15
 */
@BindView(layoutId = R.layout.activity_fragment_container)
public class FragmentContainerActivity extends BaseActivity {

    @Override
    public int containerId() {
        return R.id.container;
    }
}
