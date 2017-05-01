package me.foji.snake.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 滑动关闭基础注解
 *
 * @author Scott Smith 2017-03-21 16:47
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlideToClose {
    boolean enable() default true;
    int minVelocity() default 2000;
    int shadowStartColor() default 0x00000000;
    int shadowEndColor() default 0x50000000;
}
