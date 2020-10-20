package com.lee.demo.easyexcel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lee.demo.easyexcel.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: UserService
 * @Author: Jimmy
 * @Date: 2020/1/11 23:53
 * @Description: TODO
 */
public interface UserService extends IService<User> {

    public void importExcel(MultipartFile excel) throws IOException;

    public void exportExcel(HttpServletResponse response) throws IOException;
}
