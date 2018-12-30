package com.gupaoedu.demo.mvc.action;

import com.gupaoedu.demo.service.IDemoService;
import com.gupaoedu.spring.annotation.Autowried;
import com.gupaoedu.spring.annotation.Controller;
import com.gupaoedu.spring.annotation.RequestMapping;
import com.gupaoedu.spring.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaoqiang
 * @Title: DemoAction
 * @ProjectName vip-spring-demo1
 * @Description: TODO
 * @date 2018-12-30 23:11
 */
@Controller
@RequestMapping("/demo")
public class DemoAction {

    @Autowried
    private IDemoService iDemoService;

    @RequestMapping(value = "/query.json")
    public void query(HttpServletRequest req, HttpServletResponse res,
                      @RequestParam("name") String name) {
        String result = iDemoService.get(name);
        System.out.println(result);
    }

}
