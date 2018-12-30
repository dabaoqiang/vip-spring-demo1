package com.gupaoedu.spring.annotation;

import javax.xml.bind.Element;
import java.lang.annotation.*;

/**
 * @author xiaoqiang
 * @Title: Autowried
 * @ProjectName vip-spring-demo1
 * @Description: TODO
 * @date 2018-12-30 22:41
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowried {
    String value() default  "";
}
