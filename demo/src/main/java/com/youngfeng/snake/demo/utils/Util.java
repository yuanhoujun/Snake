package com.youngfeng.snake.demo.utils;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.youngfeng.snake.demo.R;

/**
 * Utilities class.
 *
 * @author Scott Smith 2019-11-25 10:12
 */
public class Util {

    public static void push(Fragment current, Fragment next, @IdRes int containerId) {
        FragmentTransaction ft = current.requireActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.snake_slide_in_right, R.anim.snake_slide_out_left,
                R.anim.snake_slide_in_left, R.anim.snake_slide_out_right);
        ft.add(containerId, next, next.getClass().getCanonicalName()).hide(current)
                .addToBackStack(current.getClass().getCanonicalName());
        ft.commit();
    }

    public static void switchTo(AppCompatActivity activity, Fragment target, @IdRes int containerId) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(containerId, target, target.getClass().getCanonicalName());
        ft.setCustomAnimations(0, 0, 0, 0);
        ft.commit();
    }
}
