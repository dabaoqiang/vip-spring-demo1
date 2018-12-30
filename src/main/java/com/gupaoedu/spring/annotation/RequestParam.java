package com.gupaoedu.spring.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoqiang
 * @Title: RequestParam
 * @ProjectName vip-spring-demo1
 * @Description: TODO
 * @date 2018-12-30 22:41
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
