package qrf.jsonToExcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static qrf.util.util.*;
import static qrf.util.util.getSmallInBigIndex;

public class JsonToExcelPlus {

    static String PATH = "F:\\test\\res\\";

    public static void main(String[] args) throws Exception {

        //创建一个工作簿以及基本的表头
        Workbook workbook = new XSSFWorkbook();
        Sheet sheetInfo = workbook.createSheet("住院患者病历详细信息");

        //*******大表头*******
        Row rowBigTableHeadInfo = sheetInfo.createRow(0);
        Cell cell0_1 = rowBigTableHeadInfo.createCell(0);
        cell0_1.setCellValue("基本信息");

        Cell cell0_2 = rowBigTableHeadInfo.createCell(16);
        cell0_2.setCellValue("既往史");

        Cell cell0_3 = rowBigTableHeadInfo.createCell(30);
        cell0_3.setCellValue("查体一般信息");

        Cell cell0_4 = rowBigTableHeadInfo.createCell(35);
        cell0_4.setCellValue("住院信息");

        Cell cell0_5 = rowBigTableHeadInfo.createCell(43);
        cell0_5.setCellValue("出院记录");

        //*******基本信息******
        Row rowTableHeadInfo = sheetInfo.createRow(1);


        Cell cell1_15 = rowTableHeadInfo.createCell(0);
        cell1_15.setCellValue("就诊ID");

        Cell cell1_16 = rowTableHeadInfo.createCell(1);
        cell1_16.setCellValue("姓名");

        Cell cell1_1 = rowTableHeadInfo.createCell(2);
        cell1_1.setCellValue("住院流水号");

        Cell cell1_2 = rowTableHeadInfo.createCell(3);
        cell1_2.setCellValue("科室");

        Cell cell1_3 = rowTableHeadInfo.createCell(4);
        cell1_3.setCellValue("类型");

        Cell cell1_4 = rowTableHeadInfo.createCell(5);
        cell1_4.setCellValue("诊断");
//        sheetInfo.setColumnWidth(0, 5500);
        Cell cell1_5 = rowTableHeadInfo.createCell(6);
        cell1_5.setCellValue("性别");
//        sheetInfo.setColumnWidth(1, 6000);
        Cell cell1_6 = rowTableHeadInfo.createCell(7);
        cell1_6.setCellValue("年龄");
//        sheetInfo.setColumnWidth(2, 2500);
        Cell cell1_7 = rowTableHeadInfo.createCell(8);
        cell1_7.setCellValue("身高");
//        sheetInfo.setColumnWidth(3, 6000);
        Cell cell1_8 = rowTableHeadInfo.createCell(9);
        cell1_8.setCellValue("电话");
//        sheetInfo.setColumnWidth(4, 2500);
        Cell cell1_9 = rowTableHeadInfo.createCell(10);
        cell1_9.setCellValue("婚姻");
//        sheetInfo.setColumnWidth(5, 3500);
        Cell cell1_10 = rowTableHeadInfo.createCell(11);
        cell1_10.setCellValue("联系人电话");
//        sheetInfo.setColumnWidth(6, 2500);
        Cell cell1_11 = rowTableHeadInfo.createCell(12);
        cell1_11.setCellValue("现住址市");
//        sheetInfo.setColumnWidth(7, 3500);
        Cell cell1_12 = rowTableHeadInfo.createCell(13);
        cell1_12.setCellValue("现住址县");

        Cell cell1_13 = rowTableHeadInfo.createCell(14);
        cell1_13.setCellValue("BMI");

        Cell cell1_14 = rowTableHeadInfo.createCell(15);
        cell1_14.setCellValue("住址");

        //****既往史****
        Cell cell2_1 = rowTableHeadInfo.createCell(16);
        cell2_1.setCellValue("传染病史");

        Cell cell2_2 = rowTableHeadInfo.createCell(17);
        cell2_2.setCellValue("预防接种史");

        Cell cell2_3 = rowTableHeadInfo.createCell(18);
        cell2_3.setCellValue("外伤史");

        Cell cell2_4 = rowTableHeadInfo.createCell(19);
        cell2_4.setCellValue("手术史");

        Cell cell2_5 = rowTableHeadInfo.createCell(20);
        cell2_5.setCellValue("平素健康状况");

        Cell cell2_6 = rowTableHeadInfo.createCell(21);
        cell2_6.setCellValue("过敏史");

        Cell cell2_7 = rowTableHeadInfo.createCell(22);
        cell2_7.setCellValue("输血史");

        Cell cell2_8 = rowTableHeadInfo.createCell(23);
        cell2_8.setCellValue("疾病史");

        Cell cell2_9 = rowTableHeadInfo.createCell(24);
        cell2_9.setCellValue("产伤史");

        Cell cell2_10 = rowTableHeadInfo.createCell(25);
        cell2_10.setCellValue("过敏源");

        Cell cell2_11 = rowTableHeadInfo.createCell(26);
        cell2_11.setCellValue("药物过敏");

        Cell cell2_12 = rowTableHeadInfo.createCell(27);
        cell2_12.setCellValue("过敏药物");

        Cell cell2_13 = rowTableHeadInfo.createCell(28);
        cell2_13.setCellValue("其他");

        Cell cell2_14 = rowTableHeadInfo.createCell(29);
        cell2_14.setCellValue("嗜烟嗜酒史");

        //*******查体一般信息*****
        Cell cell3_1 = rowTableHeadInfo.createCell(30);
        cell3_1.setCellValue("体重");

        Cell cell3_2 = rowTableHeadInfo.createCell(31);
        cell3_2.setCellValue("血压");

        Cell cell3_3 = rowTableHeadInfo.createCell(32);
        cell3_3.setCellValue("呼吸");

        Cell cell3_4 = rowTableHeadInfo.createCell(33);
        cell3_4.setCellValue("脉搏");

        Cell cell3_5 = rowTableHeadInfo.createCell(34);
        cell3_5.setCellValue("体温");

        //*******住院信息******
        Cell cell4_1 = rowTableHeadInfo.createCell(35);
        cell4_1.setCellValue("入院日期");

        Cell cell4_2 = rowTableHeadInfo.createCell(36);
        cell4_2.setCellValue("出院日期");

        Cell cell4_3 = rowTableHeadInfo.createCell(37);
        cell4_3.setCellValue("实际住院天数");

        Cell cell4_4 = rowTableHeadInfo.createCell(38);
        cell4_4.setCellValue("病理诊断");

        Cell cell4_5 = rowTableHeadInfo.createCell(39);
        cell4_5.setCellValue("病理号");

        Cell cell4_6 = rowTableHeadInfo.createCell(40);
        cell4_6.setCellValue("手术名称");

        Cell cell4_7 = rowTableHeadInfo.createCell(41);
        cell4_7.setCellValue("手术日期");

        Cell cell4_8 = rowTableHeadInfo.createCell(42);
        cell4_8.setCellValue("术后并发症");

        //*******出院记录******
        Cell cell5_1 = rowTableHeadInfo.createCell(43);
        cell5_1.setCellValue("病历号");

        Cell cell5_2 = rowTableHeadInfo.createCell(44);
        cell5_2.setCellValue("出院诊断");

        Cell cell5_3 = rowTableHeadInfo.createCell(45);
        cell5_3.setCellValue("出院建议");

        Cell cell5_4 = rowTableHeadInfo.createCell(46);
        cell5_4.setCellValue("病理检查结果");

        //创建门诊住院病人id表
        Workbook workbook1 = new XSSFWorkbook();
        Sheet sheetInfo1 = workbook1.createSheet("门诊住院病人混合的id");
        int num = 0;

        //获取所有json文件
        List<File> files = getFiles("F:\\temp2");

        for (int i = 0; i < 1000; i++) {
            System.out.println("开始处理第" + i + "个文件");
            try {
                File jsonFile = files.get(i);
                String jsonStr = getJSONString(jsonFile);
                JSONArray jsonArray = JSON.parseArray(jsonStr);
                if (jsonArray.size() == 1) {
                    System.out.println("第" + i + "个文件为标准格式");
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    Row rowInfo = sheetInfo.createRow(i + 2);

                    //初始化0-46个单元格
                    List<Cell> cellList = new ArrayList<>();
                    for (int j = 0; j < 47; j++) {
                        Cell temp = rowInfo.createCell(j);
                        cellList.add(temp);
                    }


                    cellList.get(0).setCellValue(jsonObject.getLong("就诊ID"));
                    cellList.get(5).setCellValue(jsonObject.getString("诊断"));

                    //*****获取字段 电子病历菜单及详情******
//                    String 电子病历菜单及详情 = "电子病历菜单及详情";
//                    String 授权委托书 = "授权委托书";
//                    int l = getSmallInBigIndex(jsonStr, 电子病历菜单及详情);
//                    int r = getSmallInBigIndex(jsonStr, 授权委托书);
//                    String 电子病历菜单及详情字符串 = jsonStr.substring(l + 12, r - 3);
//                    System.out.println(电子病历菜单及详情字符串);
                    JSONArray 电子病历菜单及详情_jsonArray = jsonObject.getJSONArray("电子病历菜单及详情");
                    String 电子病历菜单及详情字符串 = 电子病历菜单及详情_jsonArray.toJSONString();
                    //*****获取字段 电子病历菜单及详情******

                    //*****获取字段 个人基本信息******
                    String 个人基本信息json = getSpecialStr(电子病历菜单及详情字符串, "个人基本信息", "住院基本信息");
                    System.out.println("个人基本信息json");
                    System.out.println(个人基本信息json);
                    JSONObject jsonObject_个人基本信息 = JSON.parseObject(个人基本信息json);
                    cellList.get(1).setCellValue(jsonObject_个人基本信息.getString("姓名"));
                    cellList.get(2).setCellValue(jsonObject.getString("住院流水号"));
                    cellList.get(6).setCellValue(jsonObject_个人基本信息.getString("性别"));
                    cellList.get(7).setCellValue(jsonObject_个人基本信息.getString("年龄"));
                    cellList.get(4).setCellValue(jsonObject.getString("类型"));
                    cellList.get(9).setCellValue(jsonObject_个人基本信息.getString("电话"));
                    cellList.get(10).setCellValue(jsonObject_个人基本信息.getString("婚姻"));
                    cellList.get(11).setCellValue(jsonObject_个人基本信息.getString("联系人电话"));
//            cellList.get(6).setCellValue(jsonObject_个人基本信息.getString("性别"));


                    //*****获取字段 个人基本信息******

                    //*****获取字段 住院基本信息******
                    String 住院基本信息json = getSpecialStr(电子病历菜单及详情字符串, "住院基本信息", "疾病诊断信息及病理诊断信息");
                    System.out.println("住院基本信息json");
                    System.out.println(住院基本信息json);
                    JSONObject jsonObject_住院基本信息 = JSON.parseObject(住院基本信息json);
                    cellList.get(35).setCellValue(jsonObject_住院基本信息.getString("入院日期"));
                    cellList.get(36).setCellValue(jsonObject_住院基本信息.getString("出院日期"));
                    cellList.get(37).setCellValue(jsonObject_个人基本信息.getString("实际住院天数"));
                    //*****获取字段 住院基本信息******

                    //*****获取字段 疾病诊断信息及病理诊断信息******
                    String 疾病诊断信息及病理诊断信息json = getSpecialStr(电子病历菜单及详情字符串, "疾病诊断信息及病理诊断信息", "手术及操作信息");
                    System.out.println("疾病诊断信息及病理诊断信息json");
                    System.out.println(疾病诊断信息及病理诊断信息json);
                    JSONObject jsonObject_疾病诊断信息及病理诊断信息 = JSON.parseObject(疾病诊断信息及病理诊断信息json);
                    cellList.get(38).setCellValue(jsonObject_住院基本信息.getString("病理诊断1"));
                    cellList.get(39).setCellValue(jsonObject_住院基本信息.getString("病理号1"));
                    //*****获取字段 疾病诊断信息及病理诊断信息******

                    //*****获取字段 手术及操作信息******
                    String 手术及操作信息json = getSpecialStr(电子病历菜单及详情字符串, "手术及操作信息", "院感信息");
                    System.out.println("手术及操作信息json");
                    System.out.println(手术及操作信息json);
                    JSONObject jsonObject_手术及操作信息 = JSON.parseObject(手术及操作信息json);
                    String 手术名称 = "";
                    for (int j = 1; j < 100; j++) {
                        String temp = jsonObject_手术及操作信息.getString("手术名称" + j);
                        if (temp.length() != 0 && !temp.isEmpty()) {
                            手术名称 = 手术名称 + temp + "\r\n";
                        } else {
                            break;
                        }
                    }
                    cellList.get(40).setCellValue(手术名称);
                    String 手术日期 = "";
                    for (int j = 1; j < 100; j++) {
                        String temp = jsonObject_手术及操作信息.getString("手术日期" + j);
                        if (temp.length() != 0 && !temp.isEmpty()) {
                            手术日期 = 手术日期 + temp + "\r\n";
                        } else {
                            break;
                        }
                    }
                    cellList.get(41).setCellValue(手术日期);
                    //*****获取字段 手术及操作信息******

                    //*****获取字段 院感信息******
                    String 院感信息json = getSpecialStr(电子病历菜单及详情字符串, "院感信息", "护理与重症监护信息");
                    System.out.println("院感信息json");
                    System.out.println(院感信息json);
                    JSONObject jsonObject_院感信息 = JSON.parseObject(院感信息json);
                    cellList.get(42).setCellValue(jsonObject_院感信息.getString("术后并发症"));
                    //*****获取字段 院感信息******

                    //*****获取字段 出院记录1******
                    String 出院记录1json = getSpecialStr(电子病历菜单及详情字符串, "出院记录", "EMR120001 出院记录\":[{\"年");
                    System.out.println("出院记录1json");
                    System.out.println(出院记录1json);
                    JSONObject jsonObject_出院记录1 = JSON.parseObject(出院记录1json);
                    cellList.get(43).setCellValue(jsonObject_出院记录1.getString("病历号"));
                    cellList.get(44).setCellValue(jsonObject_出院记录1.getString("出院诊断"));
                    cellList.get(45).setCellValue(jsonObject_出院记录1.getString("医师建议"));
                    //*****获取字段 出院记录1******

                    //*****获取字段 出院记录2******
                    String 出院记录2json = getSpecialStr(电子病历菜单及详情字符串, "日\"}]},{\"EMR120001 出院记录", "查体一般情况");
                    System.out.println("出院记录2json");
                    System.out.println(出院记录2json);
                    JSONObject jsonObject_出院记录2 = JSON.parseObject(出院记录2json);
                    String 检查结果 = "";
                    for (int j = 1; j < 100; j++) {
                        String temp = jsonObject_出院记录2.getString("检查结果" + j);
                        if (temp.length() != 0 && !temp.isEmpty()) {
                            检查结果 = 检查结果 + temp + "\r\n";
                        } else {
                            break;
                        }
                    }
                    cellList.get(46).setCellValue(检查结果);
                    //*****获取字段 出院记录2******

                    //*****获取字段 查体一般情况******
                    String 查体一般情况json01 = getSpecialStr(电子病历菜单及详情字符串, "查体一般情况", "\"一般情况\"");
                    String 查体一般情况json = 查体一般情况json01 + "}";
                    System.out.println("查体一般情况json");
                    System.out.println(查体一般情况json);
                    JSONObject jsonObject_查体一般情况 = JSON.parseObject(查体一般情况json);
                    cellList.get(30).setCellValue(jsonObject_查体一般情况.getString("体重"));
                    cellList.get(31).setCellValue(jsonObject_查体一般情况.getString("血压"));
                    cellList.get(32).setCellValue(jsonObject_查体一般情况.getString("呼吸"));
                    cellList.get(33).setCellValue(jsonObject_查体一般情况.getString("脉搏"));
                    cellList.get(34).setCellValue(jsonObject_查体一般情况.getString("体温"));

                    //*****获取字段 查体一般情况******

                    //*****获取字段 一般情况******
                    String 一般情况json01 = getSpecialStr(电子病历菜单及详情字符串, "\"一般情况\"", "主诉");
                    String 一般情况json = "{" + 一般情况json01;
                    System.out.println("一般情况json");
                    System.out.println(一般情况json);
                    JSONObject jsonObject_一般情况 = JSON.parseObject(一般情况json);
                    cellList.get(3).setCellValue(jsonObject_一般情况.getString("科室"));
                    cellList.get(8).setCellValue(jsonObject_一般情况.getString("身高"));
                    cellList.get(12).setCellValue(jsonObject_一般情况.getString("现住址市"));
                    cellList.get(13).setCellValue(jsonObject_一般情况.getString("现住址县1"));
                    cellList.get(14).setCellValue(jsonObject_一般情况.getString("BMI"));
                    cellList.get(15).setCellValue(jsonObject_一般情况.getString("住址"));
                    //*****获取字段 一般情况******

                    //*****获取字段 主诉******
                    String 主诉json = getSpecialStr(电子病历菜单及详情字符串, "主诉", "现病史");
                    System.out.println("主诉json");
                    System.out.println(主诉json);
                    JSONObject jsonObject_主诉 = JSON.parseObject(主诉json);
                    //*****获取字段 主诉******

                    //*****获取字段 既往史******
                    String 既往史json01 = getSpecialStr(电子病历菜单及详情字符串, "\"既往史\"", "个人史");
                    String 既往史json = "{" + 既往史json01;
                    System.out.println("既往史json");
                    System.out.println(既往史json);
                    JSONObject jsonObject_既往史 = JSON.parseObject(既往史json);
                    cellList.get(16).setCellValue(jsonObject_既往史.getString("传染病史"));
                    cellList.get(17).setCellValue(jsonObject_既往史.getString("预防接种史"));
                    cellList.get(18).setCellValue(jsonObject_既往史.getString("外伤史"));
                    cellList.get(19).setCellValue(jsonObject_既往史.getString("手术史"));
                    cellList.get(20).setCellValue(jsonObject_既往史.getString("平素健康状况"));
                    cellList.get(21).setCellValue(jsonObject_既往史.getString("过敏史"));
                    cellList.get(22).setCellValue(jsonObject_既往史.getString("输血史"));
                    cellList.get(23).setCellValue(jsonObject_既往史.getString("疾病史"));
                    cellList.get(24).setCellValue(jsonObject_既往史.getString("产伤史"));
                    cellList.get(25).setCellValue(jsonObject_既往史.getString("过敏源"));
                    cellList.get(26).setCellValue(jsonObject_既往史.getString("药物过敏"));
                    cellList.get(27).setCellValue(jsonObject_既往史.getString("过敏药物"));
                    cellList.get(28).setCellValue(jsonObject_既往史.getString("其它"));
                    //*****获取字段 既往史******

                    //*****获取字段 个人史******
                    String 个人史json = getSpecialStr(电子病历菜单及详情字符串, "个人史", "婚姻史");
                    System.out.println("个人史json");
                    System.out.println(个人史json);
                    JSONObject jsonObject_个人史 = JSON.parseObject(个人史json);
                    cellList.get(29).setCellValue(jsonObject_个人史.getString("嗜烟嗜酒史"));
                    //*****获取字段 个人史******

                    //*****获取字段 婚姻史******
                    String 婚姻史json = getSpecialStr(电子病历菜单及详情字符串, "婚姻史", "月经史");
                    System.out.println("婚姻史json");
                    System.out.println(婚姻史json);
                    JSONObject jsonObject_婚姻史 = JSON.parseObject(婚姻史json);
                    //*****获取字段 婚姻史******

                    //*****获取字段 系统回顾******
                    String 系统回顾json01 = getSpecialStr(电子病历菜单及详情字符串, "系统回顾", "\"心脏\"");
                    String 系统回顾json = 系统回顾json01 + "}";
                    System.out.println("系统回顾json");
                    System.out.println(系统回顾json);
                    JSONObject jsonObject_系统回顾 = JSON.parseObject(系统回顾json);
                    //*****获取字段 系统回顾******


                } else {
//                    Row row = sheetInfo1.createRow(num);
//                    Cell cell = row.createCell(0);
//                    cell.setCellValue(jsonFile.getName());
//                    num++;
                    System.out.println("第" + i + "个文件不是标准格式");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        //4、输出文件
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "漆瑞丰吐血整理" + ".xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("输出完毕");
    }
}
