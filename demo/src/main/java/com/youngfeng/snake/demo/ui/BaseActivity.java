package com.youngfeng.snake.demo.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimatorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.youngfeng.snake.demo.R;
import com.youngfeng.snake.demo.annotations.BindView;
import com.youngfeng.snake.demo.ui.exception.NoContainerIdException;

import butterknife.ButterKnife;

/**
 * Base class of all activities
 *
 * @author Scott Smith 2017-12-23 22:32
 */
public class BaseActivity extends AppCompatActivity {
    private String mCurrentFragmentTag;
    private LinearLayout mContentView;
    private final String KEY_CURRENT_FRAGMENT_TAG = "com.youngfeng:android.current.fragment";
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepare();
        processAnotations();

        onInitView();
    }

    private void prepare() {
        mContentView = new LinearLayout(this);
        mContentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mContentView.setOrientation(LinearLayout.VERTICAL);
        addToolbarToContentView();
        super.setContentView(mContentView);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getFragmentManager().findFragmentById(containerId());
                if(null != fragment) {
                    mCurrentFragmentTag = fragment.getTag();
                }
            }
        });
    }

    private void processAnotations() {
        BindView bindView = getClass().getAnnotation(BindView.class);
        if(null != bindView) {
            int layoutId = bindView.layoutId();
            setContentView(layoutId);
            if(bindView.bindToButterKnife()) {
                ButterKnife.bind(this);
            }
        }
    }

    private void addToolbarToContentView() {
        mToolbar = toolbar();

        if (null != mToolbar) {
            ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
            if (null == layoutParams) {
                layoutParams = generateDefaultLayoutParams();
            }

            mContentView.addView(mToolbar, 0, layoutParams);
        }
    }

    private ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    public @Nullable Toolbar toolbar() {
        return null;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View contentView = getLayoutInflater().inflate(layoutResID, mContentView, false);
        setContentView(contentView);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if(null != mContentView) {
            if (null == params) params = generateDefaultLayoutParams();
            mContentView.removeAllViews();
            addToolbarToContentView();
            mContentView.addView(view, params);
        } else {
            super.setContentView(view, params);
        }
    }


    public void push(@AnimatorRes int enter, @AnimatorRes int exit, @AnimatorRes int popEnter,
                     @AnimatorRes int popExit, @NonNull Class<? extends BaseFragment> fragment, boolean addToBackStack) {
        try {
            if (fragment.getName().equals(mCurrentFragmentTag)) return;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(enter, exit, popEnter, popExit);

            if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
                Fragment currentFragment = fragmentManager.findFragmentByTag(mCurrentFragmentTag);

                if (null != currentFragment) {
                    transaction.hide(currentFragment);

                    if(addToBackStack) {
                        transaction.addToBackStack(mCurrentFragmentTag);
                    }
                }
            }

            Fragment targetFragment = fragmentManager.findFragmentByTag(fragment.getName());

            if (null != targetFragment && isActive((BaseFragment) targetFragment)) {
                transaction.show(targetFragment);
            } else {
                targetFragment = Fragment.instantiate(this, fragment.getName());
                if (null != targetFragment) {
                    transaction.add(containerId(), targetFragment, fragment.getName());
                }
            }
            transaction.commitAllowingStateLoss();

            mCurrentFragmentTag = fragment.getName();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void push(Class<? extends BaseFragment> fragment, boolean addToBackStack) {
        push(R.animator.fragment_enter, R.animator.fragment_exit,
                R.animator.fragment_pop_enter, R.animator.fragment_pop_exit, fragment, addToBackStack);
    }

    public void push(Class<? extends BaseFragment> fragment) {
        push(fragment, true);
    }

    public void switchTo(Class<? extends BaseFragment> fragment) {
        push(0, 0, 0, 0, fragment, true);
    }

    public void push(@AnimatorRes int enter, @AnimatorRes int exit, @AnimatorRes int popEnter,
                     @AnimatorRes int popExit, @NonNull Fragment fragment, boolean addToBackStack) {
        try {
            if (fragment.getClass().getName().equals(mCurrentFragmentTag)) return;

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(enter, exit, popEnter, popExit);

            if (!TextUtils.isEmpty(mCurrentFragmentTag)) {
                Fragment currentFragment = fragmentManager.findFragmentByTag(mCurrentFragmentTag);

                if (null != currentFragment) {
                    transaction.hide(currentFragment);

                    if(addToBackStack) {
                        transaction.addToBackStack(mCurrentFragmentTag);
                    }
                }
            }

            if (fragment.isAdded()) {
                transaction.show(fragment);
            } else {
                transaction.add(containerId(), fragment, fragment.getClass().getName());
            }
            transaction.commitAllowingStateLoss();

            mCurrentFragmentTag = fragment.getClass().getName();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void push(BaseFragment fragment, boolean addToBackStack) {
        push(R.animator.fragment_enter, R.animator.fragment_exit,
                R.animator.fragment_pop_enter, R.animator.fragment_pop_exit, fragment, addToBackStack);
    }

    public void push(BaseFragment fragment) {
        push(fragment, true);
    }


    public boolean popFragment() {
        try {
            FragmentManager fragmentManager = getFragmentManager();

            String fragmentTag = null;
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentTag = fragmentManager
                        .getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
            }

            Fragment lastFragment = fragmentManager.findFragmentByTag(mCurrentFragmentTag);

            if (fragmentManager.popBackStackImmediate()) {
                mCurrentFragmentTag = fragmentTag;

                /**
                 * 假设A->B->C, B->C时B没有加入回退栈，回退到A将导致C没有正常退出，这里强制隐藏当前页面
                 */
                if (null != lastFragment) {
                    getFragmentManager().beginTransaction().hide(lastFragment).commitAllowingStateLoss();
                }

                return true;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }

        finish();
        return false;
    }

    public void start(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    private boolean isActive(@NonNull BaseFragment fragment) {
        return fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving();
    }

    public int containerId() {
        throw new NoContainerIdException("Override this method, return fragment container layout resource id please.");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!TextUtils.isEmpty(mCurrentFragmentTag)) {
            outState.putString(KEY_CURRENT_FRAGMENT_TAG, mCurrentFragmentTag);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRAGMENT_TAG);
    }

    @Override
    public FragmentManager getFragmentManager() {
        return super.getFragmentManager();
    }

    protected void onInitView() {}

    @Override
    public void onBackPressed() {
        popFragment();
    }
}