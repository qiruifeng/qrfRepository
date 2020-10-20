package com.lee.demo.easyexcel.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lee.demo.easyexcel.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserDao
 * @Author: Jimmy
 * @Date: 2020/1/12 14:03
 * @Description: TODO
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}