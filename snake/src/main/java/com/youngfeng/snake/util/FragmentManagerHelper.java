package com.youngfeng.snake.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.view.View;

/**
 * Fragment manager utils
 *
 * @author Scott Smith 2017-12-16 17:34
 */
public class FragmentManagerHelper {
    private FragmentManager mFragmentManager;
    private androidx.fragment.app.FragmentManager mAndroidXFragmentManager;

    private FragmentManagerHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    private FragmentManagerHelper(androidx.fragment.app.FragmentManager fragmentManager) {
        mAndroidXFragmentManager = fragmentManager;
    }

    public static FragmentManagerHelper get(FragmentManager fragmentManager) {
        return new FragmentManagerHelper(fragmentManager);
    }

    public static FragmentManagerHelper get(androidx.fragment.app.FragmentManager fragmentManager) {
        return new FragmentManagerHelper(fragmentManager);
    }

    public Fragment getLastFragment() {
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        if (backStackCount <= 0) return null;

        FragmentManager.BackStackEntry backStackEntry = mFragmentManager.getBackStackEntryAt(backStackCount - 1);
        String fragmentTag = backStackEntry.getName();
        return mFragmentManager.findFragmentByTag(fragmentTag);
    }

    public androidx.fragment.app.Fragment getLastAndroidXFragment() {
        int backStackCount = mAndroidXFragmentManager.getBackStackEntryCount();
        if (backStackCount <= 0) return null;

        androidx.fragment.app.FragmentManager.BackStackEntry backStackEntry = mAndroidXFragmentManager.getBackStackEntryAt(backStackCount - 1);
        String fragmentTag = backStackEntry.getName();
        return mAndroidXFragmentManager.findFragmentByTag(fragmentTag);
    }

    public View getViewOfLastFragment() {
        if(null == getLastFragment()) return null;
        return getLastFragment().getView();
    }

    public View getViewOfLastAndroidXFragment() {
        if (null == getLastAndroidXFragment()) return null;

        return getLastAndroidXFragment().getView();
    }

    public boolean backToLastFragment() {
        return mFragmentManager.popBackStackImmediate();
    }

    public boolean backToAndroidXFragment() {
        return mAndroidXFragmentManager.popBackStackImmediate();
    }

    public boolean backStackEmpty() {
        return mFragmentManager.getBackStackEntryCount() <= 0;
    }

    public boolean androidXBackStackEmpty() {
        return mAndroidXFragmentManager.getBackStackEntryCount() <= 0;
    }
}
