package com.feilong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author FeiLong
 * @version 1.8
 * @date 2020/11/7 13:01
 */
@Controller
@RequestMapping("/tes")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
