package com.lee.demo.easyexcel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: IndexController
 * @Author: Jimmy
 * @Date: 2020/1/11 23:37
 * @Description: 首页Controller
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

}
