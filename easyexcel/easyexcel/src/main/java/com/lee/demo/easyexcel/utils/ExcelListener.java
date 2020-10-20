package com.lee.demo.easyexcel.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.lee.demo.easyexcel.entity.User;
import com.lee.demo.easyexcel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ExcelListener
 * @Author: Jimmy
 * @Date: 2020/1/12 14:08
 * @Description: 模板读取类
 */
@Component
public class ExcelListener extends AnalysisEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelListener.class);

    @Autowired
    private static UserService userService;

    public ExcelListener() {
    }

    /**
     * 此处必须使用构造器来注入spring管理的类，不然使用@Autowired会注入失败，会报userService类NullPointerException
     * @param userService
     */
    @Autowired
    public ExcelListener(UserService userService) {
        this.userService = userService;
    }


    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<User> list = new ArrayList<User>();

    /**
     * 这个每一条数据解析都会来调用
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(object));
        list.add((User)object);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM（Out Of Memory）
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //这里也要保存数据，确保批量新增最后一批遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成并存储成功！");
    }

    /**
     * 使用MybatisPlus的批量新增方法插入数据
     */
    public void saveData() {
        userService.saveBatch(this.list,BATCH_COUNT);
    }

    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
}
