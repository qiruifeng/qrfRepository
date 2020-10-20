package com.lee.demo.easyexcel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.lee.demo.easyexcel.utils.SexConverter;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * @ClassName: com.lee.demo.easyexcel.entity.User
 * @Author: Jimmy
 * @Date: 2020/1/11 23:32
 * @Description: 用户实体类
 * 1.@Data是Lombok的Jar包内的，Jar包安装成功仍然报错的话是因为IDE需要安装Lombok相关插件
 * 2.@ExcelProperty(index=0)注解用来指定哪些字段和excel列对应，其中index表示在excel中第几列
 */
@Data
public class User implements Serializable {
    private String id;
    @ExcelProperty(index=1)
    private String username;
    @ExcelProperty(index=2)
    private String nickname;
    @ExcelProperty(index=3)
    private String password;
    @ExcelProperty(index=4)
    private String identitynum;
    @ExcelProperty(index=5, converter = SexConverter.class)
    private String sex;
    @ExcelProperty(index=6)
    private String age;
    @ExcelProperty(index=7)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private Date birthday;
    @ExcelProperty(index=8)
    @NumberFormat("#.##厘米")
    private Double height;
    @ExcelProperty(index=9)
    @NumberFormat("#.##斤")
    private Double weight;
    @ExcelProperty(index=10)
    private String telephone;
    @ExcelProperty(index=11)
    private String email;
    @ExcelProperty(index=12)
    private String address;
}
