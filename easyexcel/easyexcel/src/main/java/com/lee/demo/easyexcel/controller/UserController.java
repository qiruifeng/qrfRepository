package com.lee.demo.easyexcel.controller;

import com.lee.demo.easyexcel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: UserController
 * @Author: Jimmy
 * @Date: 2020/1/11 23:44
 * @Description: TODO
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 文件上传解析并导入数据库
     * @param excel
     * @throws IOException
     */
    @PostMapping("import")
    public void importExcel(MultipartFile excel) throws IOException {
        try {
            userService.importExcel(excel);
            LOGGER.info("excel导入数据库成功!");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("excel导入数据库失败!");
        }
    }

    /**
     * 查询数据库数据并导出Excel
     * @param response
     * @throws IOException
     */
    @GetMapping("export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        try {
            userService.exportExcel(response);
            LOGGER.info("导出Excel成功!");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("导出Excel失败!");
        }
    }

}
