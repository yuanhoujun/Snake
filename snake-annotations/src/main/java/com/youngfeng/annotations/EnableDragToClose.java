package com.youngfeng.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启或关闭【滑动关闭】功能
 *
 * @author Scott Smith 2017-12-14 16:26
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableDragToClose {
    boolean value() default true;
}
