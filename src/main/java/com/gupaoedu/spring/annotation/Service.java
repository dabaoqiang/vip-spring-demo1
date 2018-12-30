package com.gupaoedu.spring.annotation;

import java.lang.annotation.*;

/**
 * @author xiaoqiang
 * @Title: Service
 * @ProjectName vip-spring-demo1
 * @Description: TODO
 * @date 2018-12-30 22:41
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
