package com.gupaoedu.demo.service.impl;

import com.gupaoedu.demo.service.IDemoService;
import com.gupaoedu.spring.annotation.Service;

/**
 * @author xiaoqiang
 * @Title: DemoServiceImpl
 * @ProjectName vip-spring-demo1
 * @Description: TODO
 * @date 2018-12-30 23:15
 */
@Service
public class DemoServiceImpl implements IDemoService {

    @Override
    public String get(String name) {
        return "My name is " + name;
    }
}
