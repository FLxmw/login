package com.feilong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author FeiLong
 * @version 1.8
 * @date 2020/10/27 9:35
 */

/**
 * //使用WebMvcConfigurerAdapter可以来扩展SpringMVC的功能
 * //@EnableWebMvc   不要接管SpringMVC
 */
@SuppressWarnings("ALL")
@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {

    //所有的WebMvcConfigurerAdapter组件都会一起起作用
    //将自定义的组件注册进容器中
    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
//            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
//                registry.addViewController("/").setViewName("login.html");
//                registry.addViewController("/index.html").setViewName("login.html");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //静态资源；  *.css , *.js
                //SpringBoot已经做好了静态资源映射
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/index.html")
                        .excludePathPatterns("/user/login");
            }
        };
        return adapter;
    }

}
