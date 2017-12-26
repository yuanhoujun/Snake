package com.youngfeng.snake.demo.annotations;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bind content view for activity and android
 *
 * @author Scott Smith 2017-12-23 22:54
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindView {
    /**
     * Layout resource id
     */
    @LayoutRes int layoutId();

    /**
     * Bind to butterknife inject framework.
     */
    boolean bindToButterKnife() default true;
}
