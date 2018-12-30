package com.gupaoedu.spring.servlet;

import com.gupaoedu.demo.mvc.action.DemoAction;
import com.gupaoedu.spring.annotation.Autowried;
import com.gupaoedu.spring.annotation.Controller;
import com.gupaoedu.spring.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaoqiang
 * @Title: DispatcherServlet
 * @ProjectName vip-spring-demo1
 * @Description: TODO
 * @date 2018-12-30 21:05
 */
public class DispatcherServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private Map<String, Object> beanMap = new ConcurrentHashMap<String, Object>();

    private List<String> classNames = new ArrayList<String>();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("正在调用doPost");
    }

    /**
     * 进行初始化
     *
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 定位
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        // 加载
        doScanner(contextConfig.getProperty("scanPackage"));

        // 注册
        doRegistry();

        // 注入
        doAutowired();

        //测试
        DemoAction demoAction = (DemoAction) beanMap.get("demoAction");
        demoAction.query(null,null,"xiaoqiang");
    }

    /**
     * 自动装配bean
     */
    private void doAutowired() {
     if (beanMap.isEmpty()) {return;}

        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            System.out.println("封装bean :" + entry.getValue());
            // 之前只是初始化bean实例，现在给bean进行bean与bean之间组装
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field filed : fields) {
                if (!filed.isAnnotationPresent(Autowried.class)){continue;}

                Autowried autowried = filed.getAnnotation(Autowried.class);

                System.out.println("起名称：" + autowried.value());
                String beanName = autowried.value().trim();

                if ("".equals(beanName)){
                    // 注入这个字段
                    System.out.println(filed.getType().getName());
                    System.out.println("注入bean名称为：" + filed.getType().getName());
                    beanName = filed.getType().getName();
                }
                filed.setAccessible(true);

                try {
                    filed.set(entry.getValue(), beanMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 进行bean的处理
     */
    private void doRegistry() {
        // 类文件是否加载完成
        if (classNames.isEmpty()) {
            return;
        }
        // 循环所有类文件,生成实例

        try {
            for (String className : classNames) {
                // 静态获取类的字节码对象class对象
                Class<?> clazz = Class.forName(className);
                // 判断该字节码对象是否打了标志，即加了注解
                if (clazz.isAnnotationPresent(Controller.class)) {
                    // 使用字节码newInstance实例
                    String benaName = lowerFirstCase(clazz.getSimpleName());
                    beanMap.put(benaName, clazz.newInstance());
                    // 如果是service bean，
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    // 方法返回该元素的指定类型的注释，如果是这样的注释，否则返回null。
                    Service service = clazz.getAnnotation(Service.class);
                    // 获取注解，然后获取注解bean名称，eg:@servcie("123")
                    String beanName = service.value();
                    System.out.println("service 注入的bean：" + beanName);
                    if ("".equals(beanName.trim())) {
                        beanName = lowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    beanMap.put(beanName, instance);
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i : interfaces) {
                        System.out.println("我是接口 ：" + i.getName());
                        beanMap.put(i.getName(), instance);
                    }
                } else {
                    continue;
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }


    /**
     * 第一个名称小写，存入map
     *
     * @param str
     * @return
     */
    private String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 扫描文件
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        // 扫描包下面的所有类
        // 根据包名获取物理地址 将.替换/成物理地址,加载这个路径下的所有文件
        // classPath 获取是null,当前是/ 为null  linux mac环境下
        System.out.println(this.getClass().getClassLoader().getResource(""));
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());
        // 根据类型获取文件目标
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                // 循环子集
                System.out.println(file.getName());
                doScanner(scanPackage + "." + file.getName());
            } else {
                classNames.add(scanPackage + "." + file.getName().replaceAll(".class", ""));
            }
        }
    }

    /**
     * 跟现实世界桥梁进行一个连接配置
     *
     * @param location
     */
    private void doLoadConfig(String location) {
        // 加载contextConfigLocation 配置文件信息
        // 读取xml里面配置信息注入到properties，从而将resource加入资源,桥梁架设成功
        System.out.println("打印文件读取地址：" + location);
        InputStream is = this.getClass().getClassLoader().
                getResourceAsStream(location.replace("classpath:", "").trim());
        try {
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
