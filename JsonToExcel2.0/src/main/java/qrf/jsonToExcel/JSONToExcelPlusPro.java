package qrf.jsonToExcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;

import java.io.File;
import java.util.List;

import static qrf.util.util.*;

public class JSONToExcelPlusPro {
    static String PATH = "F:\\test\\res\\";

    public static void main(String[] args) {
        List<File> files = getFiles("F:\\temp2");
        System.out.println(files.size());
        for (int fileIndex = 0; fileIndex < 10; fileIndex++) {
            int fileNum=fileIndex+1;
            System.out.println("开始处理第" + fileNum + "个文件");


            try {

                File jsonFile = files.get(fileIndex);
                String jsonStr = getJSONString(jsonFile);
                JSONArray jsonArray = JSON.parseArray(jsonStr);
                if (jsonArray.size() == 1){
                    System.out.println("第" + fileNum + "个文件是标准文件");
                    JSONObject jsonObject=jsonArray.getJSONObject(0);

                    String 电子病历菜单及详情字符串json="";
                    try {
                        JSONArray 电子病历菜单及详情_jsonArray = jsonObject.getJSONArray("电子病历菜单及详情");
                        String 电子病历菜单及详情字符串 = 电子病历菜单及详情_jsonArray.toJSONString();
                        电子病历菜单及详情字符串json=modifyString(电子病历菜单及详情字符串);
                        System.out.println("电子病历菜单及详情字符串json为："+电子病历菜单及详情字符串json);
                    }catch (Exception e){
                        System.out.println("获取电子病历菜单及详情字符串异常： "+e.getMessage());
                    }
                    JSONObject jsonObject1=JSON.parseObject(电子病历菜单及详情字符串json);
                    System.out.println("jsonObject1为："+jsonObject1.toJSONString());


                }else {
                    System.out.println("第" + fileNum + "个文件不是标准文件");
                }


            } catch (Exception e) {
                System.out.println("第" + fileNum + "个文件处理异常");
                System.out.println("异常信息为" + e.getMessage());
            }
        }

    }
}
