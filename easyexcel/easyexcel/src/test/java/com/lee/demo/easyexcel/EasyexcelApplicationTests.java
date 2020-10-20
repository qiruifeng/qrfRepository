package com.lee.demo.easyexcel;

import com.lee.demo.easyexcel.entity.User;
import com.lee.demo.easyexcel.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class EasyexcelApplicationTests {

    @Autowired
    private static UserService userService;
    @Autowired
    public EasyexcelApplicationTests(UserService userService) {
        this.userService = userService;
    }

    //指定每批次新增数量
    private static final int BATCH_COUNT = 3000;
    //标记执行到了第几批次
    private static int count = 1;

    /**
     * 初始化测试数据
     */
    @Test
    void initializeDatas() {
        long startTime=System.currentTimeMillis();   //获取开始时间

        //模拟初始化100万条数据
        List<User> userlist = new ArrayList<User>();
        for (int i = 1; i <= 1000000; i++) {
            User user = new User();
            user.setId(String.valueOf(i));
            user.setUsername("user" + i);
            user.setNickname("nick" + i);
            user.setPassword("pwd" + i);
            user.setIdentitynum("421355");
            user.setSex(String.valueOf(Math.random() * 1));
            user.setAge(String.valueOf(Math.random() * 70 + 10));
            //由于Easyexcel不支持jdk8的LocalDate类型，所以得把它改成jdk8支持的Date类型
            user.setBirthday(localDate2Date(LocalDate.now()));
            //生成的随机数得保留小数点后两位，不然超出了数据库设置的字段限制（身高、体重数据库设置的小数点后两位）
            user.setHeight(new BigDecimal(Math.random() * 90 + 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            user.setWeight(new BigDecimal(Math.random() * 100 +80).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            user.setTelephone("13534568989");
            user.setEmail("696859483@qq.com");
            user.setAddress("湖北武汉");

            userlist.add(user);

            if (userlist.size() >= BATCH_COUNT) {
                userService.saveBatch(userlist,BATCH_COUNT);
                // 存储完成清理 list
                userlist.clear();
                System.out.println("当前新插入第" + count + "批数据成功！");
                count++;
            }
        }
        //确保批量新增最后一批遗留的数据也存储到数据库
        userService.saveBatch(userlist,BATCH_COUNT);
        userlist.clear();
        System.out.println("当前新插入第" + count + "批数据成功！");

        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： "+(endTime-startTime)/1000+"秒");

    }

    public static Date localDate2Date(LocalDate localDate) {
        if(null == localDate) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }


}
