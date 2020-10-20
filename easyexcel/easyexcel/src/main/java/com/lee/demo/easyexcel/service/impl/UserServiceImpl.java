package com.lee.demo.easyexcel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lee.demo.easyexcel.dao.UserDao;
import com.lee.demo.easyexcel.entity.User;
import com.lee.demo.easyexcel.service.UserService;
import com.lee.demo.easyexcel.utils.ExcelListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserServiceImpl
 * @Author: Jimmy
 * @Date: 2020/1/12 13:59
 * @Description: TODO
 */
@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void importExcel(MultipartFile excel) throws IOException {

        long startTime=System.currentTimeMillis();   //获取开始时间

        /**
         * 写法一：
         * 这里需要指定用哪个class去读，读取第一个sheet后文件流会自动关闭
         */
        EasyExcel.read(excel.getInputStream(), User.class, new ExcelListener()).sheet().doRead();

        /**
         * 写法二：
         * 该写法要记得手动关闭reader，读的时候会创建临时文件，到时磁盘会崩的
         */
        /*
        ExcelReader excelReader = EasyExcel.read(excel.getInputStream(), User.class, new ExcelListener()).build();
        ReadSheet readSheet = EasyExcel.readSheet(0).build(); //readSheet(sheetNo)表示读取第几个sheet，0表示第一个sheet
        excelReader.read(readSheet);
        excelReader.finish();
        */

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)/1000+"秒");

    }

    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {

        // 这里注意 有同学反映使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            long startTime=System.currentTimeMillis();   //获取开始时间

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            /**
             * 写法一：
             * 这里需要指定用哪个class去写，写入第一个sheet后文件流会自动关闭
             */
            //EasyExcel.write(response.getOutputStream(), User.class).sheet("模板").doWrite(data());

            /**
             * 写法二：
             * 该写法要记得手动关闭writer，不然会报异常
             */
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), User.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(data(), writeSheet);
            // 千万别忘记finish 会帮忙关闭流
            excelWriter.finish();

            long endTime=System.currentTimeMillis(); //获取结束时间
            System.out.println("程序运行时间： "+(endTime-startTime)/1000+"秒");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 使用MybatisPlus的条件查询方法查询所有数据
     * @return
     */
    private List<User> data() {
        QueryWrapper<User> queryWrapper = Wrappers.query();
        List<User> userList = userDao.selectList(queryWrapper);
        return userList;
    }

}
