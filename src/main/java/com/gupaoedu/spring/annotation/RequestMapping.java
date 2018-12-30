package com.gupaoedu.spring.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoqiang
 * @Title: RequestMapping
 * @ProjectName vip-spring-demo1
 * @Description: TODO
 * @date 2018-12-30 22:41
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
