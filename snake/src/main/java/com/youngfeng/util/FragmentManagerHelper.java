package com.youngfeng.util;

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

    public FragmentManagerHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public static FragmentManagerHelper get(FragmentManager fragmentManager) {
        return new FragmentManagerHelper(fragmentManager);
    }

    public Fragment getLastFragment() {
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        if (backStackCount <= 0) return null;

        FragmentManager.BackStackEntry backStackEntry = mFragmentManager.getBackStackEntryAt(backStackCount - 1);
        String fragmentTag = backStackEntry.getName();
        return mFragmentManager.findFragmentByTag(fragmentTag);
    }

    public View getViewOfLastFragment() {
        return getLastFragment().getView();
    }

    public boolean backToLastFragment() {
        return mFragmentManager.popBackStackImmediate();
    }

    public boolean backStackEmpty() {
        return mFragmentManager.getBackStackEntryCount() <= 0;
    }
}
