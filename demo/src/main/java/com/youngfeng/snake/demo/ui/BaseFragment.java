package com.youngfeng.snake.demo.ui;

import android.animation.Animator;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.youngfeng.snake.Snake;
import com.youngfeng.snake.animation.AnimationController;
import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.widget.TranslateLinearLayout;

import butterknife.ButterKnife;

/**
 * Base class of all fragments.
 *
 * @author Scott Smith 2017-12-24 10:29
 */
public class BaseFragment extends Fragment implements AnimationController {
    private Toolbar mToolbar;
    private TranslateLinearLayout mContentView;
    private final String KEY_STATE_HIDDEN = "com.youngfeng:fragment.state.hidden";
    private boolean mDisableAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(null != savedInstanceState) {
            restore(savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInitView();
    }

    private void restore(@NonNull Bundle savedInstanceState) {
        boolean isHidden = savedInstanceState.getBoolean(KEY_STATE_HIDDEN);
        if(isHidden) {
            getFragmentManager().beginTransaction().hide(this).commitAllowingStateLoss();
        } else {
            getFragmentManager().beginTransaction().show(this).commitAllowingStateLoss();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContentView = new TranslateLinearLayout(getActivity());
        mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setOrientation(LinearLayout.VERTICAL);
        addToolbarToContentView();
        bindViewToContentView(inflater, container);

        return mContentView;
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return Snake.wrap(super.onCreateAnimator(transit, enter, nextAnim), this);
    }

    private void addToolbarToContentView() {
        mToolbar = toolbar();

        if(null != mToolbar && null != mContentView) {
            mContentView.removeView(mToolbar);
            mContentView.addView(mToolbar, 0);
        }
    }

    private void bindViewToContentView(LayoutInflater inflater, ViewGroup container) {
        BindView bindView = getClass().getAnnotation(BindView.class);
        if(null != bindView) {
            View view = inflater.inflate(bindView.layoutId(), container, false);
            mContentView.addView(view);

            if(bindView.bindToButterKnife()) {
                ButterKnife.bind(this, mContentView);
            }
        }
    }

    protected @Nullable Toolbar toolbar() {
        return null;
    }

    public final void push(Class<? extends BaseFragment> fragment, boolean addToBackStack) {
        if(!(getActivity() instanceof BaseActivity) || getActivity().isFinishing()) return;

        ((BaseActivity) getActivity()).push(fragment, addToBackStack);
    }

    public final void push(Class<? extends BaseFragment> fragment) {
        push(fragment, true);
    }

    public final void push(BaseFragment fragment, boolean addToBackStack) {
        if(!(getActivity() instanceof BaseActivity) || getActivity().isFinishing()) return;

        ((BaseActivity) getActivity()).push(fragment, addToBackStack);
    }

    public final void push(BaseFragment fragment) {
        push(fragment, true);
    }

    public final void pop() {

    }

    public void toast(String msg) {
        if(getActivity() instanceof BaseActivity) {
            ((BaseActivity)getActivity()).toast(msg);
        }
    }

    @Override
    public void disableAnimation(boolean disable) {
        mDisableAnimation = disable;
    }

    @Override
    public boolean animationDisabled() {
        return mDisableAnimation;
    }

    protected void onInitView() {}
}
