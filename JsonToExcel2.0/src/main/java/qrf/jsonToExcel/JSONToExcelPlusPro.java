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

public class JSONToExcelPlusPro {
    static String PATH = "F:\\test\\res\\";

    public static void main(String[] args) throws Exception {
        List<File> files = getFiles("F:\\temp2");
        System.out.println(files.size());

        //第一个表，记录患者信息
        Workbook workbook1 = new XSSFWorkbook();
        Sheet sheetInfo1 = workbook1.createSheet("住院患者病历详细信息");

        //*******大表头*******
        Row rowBigTableHeadInfo = sheetInfo1.createRow(0);
        Cell cell1_1 = rowBigTableHeadInfo.createCell(0);
        cell1_1.setCellValue("基本信息");

        Cell cell1_2 = rowBigTableHeadInfo.createCell(5);
        cell1_2.setCellValue("个人基本信息");

        Cell cell1_3 = rowBigTableHeadInfo.createCell(16);
        cell1_3.setCellValue("一般情况");

        Cell cell1_4 = rowBigTableHeadInfo.createCell(22);
        cell1_4.setCellValue("查体一般情况");

        Cell cell1_5 = rowBigTableHeadInfo.createCell(28);
        cell1_5.setCellValue("住院基本信息");

        Cell cell1_6 = rowBigTableHeadInfo.createCell(31);
        cell1_6.setCellValue("疾病诊断信息及病理诊断信息");

        Cell cell1_7 = rowBigTableHeadInfo.createCell(33);
        cell1_7.setCellValue("手术及操作信息");

        Cell cell1_8 = rowBigTableHeadInfo.createCell(35);
        cell1_8.setCellValue("院感信息");

        Cell cell1_9 = rowBigTableHeadInfo.createCell(36);
        cell1_9.setCellValue("出院记录");

        Cell cell1_10 = rowBigTableHeadInfo.createCell(40);
        cell1_10.setCellValue("各种历史");
        //*******大表头*******

        //*******小表头*******
        Row rowSmallTableHeadInfo = sheetInfo1.createRow(1);
        Cell cell2_1 = rowSmallTableHeadInfo.createCell(0);
        cell2_1.setCellValue("就诊ID");

        Cell cell2_2 = rowSmallTableHeadInfo.createCell(1);
        cell2_2.setCellValue("诊断");

        Cell cell2_3 = rowSmallTableHeadInfo.createCell(2);
        cell2_3.setCellValue("住院流水号");

        Cell cell2_4 = rowSmallTableHeadInfo.createCell(3);
        cell2_4.setCellValue("科室");

        Cell cell2_5 = rowSmallTableHeadInfo.createCell(4);
        cell2_5.setCellValue("类型");

        //*******个人基本信息*******
        Cell cell2_6 = rowSmallTableHeadInfo.createCell(5);
        cell2_6.setCellValue("姓名");

        Cell cell2_7 = rowSmallTableHeadInfo.createCell(6);
        cell2_7.setCellValue("性别");

        Cell cell2_8 = rowSmallTableHeadInfo.createCell(7);
        cell2_8.setCellValue("年龄");

        Cell cell2_9 = rowSmallTableHeadInfo.createCell(8);
        cell2_9.setCellValue("出生日期");

        Cell cell2_10 = rowSmallTableHeadInfo.createCell(9);
        cell2_10.setCellValue("就诊号");

        Cell cell2_11 = rowSmallTableHeadInfo.createCell(10);
        cell2_11.setCellValue("身份证号");

        Cell cell2_12 = rowSmallTableHeadInfo.createCell(11);
        cell2_12.setCellValue("婚姻");

        Cell cell2_13 = rowSmallTableHeadInfo.createCell(12);
        cell2_13.setCellValue("电话");

        Cell cell2_14 = rowSmallTableHeadInfo.createCell(13);
        cell2_14.setCellValue("联系人姓名");

        Cell cell2_15 = rowSmallTableHeadInfo.createCell(14);
        cell2_15.setCellValue("联系人电话");

        Cell cell2_16 = rowSmallTableHeadInfo.createCell(15);
        cell2_16.setCellValue("联系人地址");
        //*******个人基本信息*******

        //*******一般情况*******
        Cell cell2_17 = rowSmallTableHeadInfo.createCell(16);
        cell2_17.setCellValue("住院号");

        Cell cell2_18 = rowSmallTableHeadInfo.createCell(17);
        cell2_18.setCellValue("现住址市");

        Cell cell2_19 = rowSmallTableHeadInfo.createCell(18);
        cell2_19.setCellValue("现住址县1");

        Cell cell2_20 = rowSmallTableHeadInfo.createCell(19);
        cell2_20.setCellValue("BMI");

        Cell cell2_21 = rowSmallTableHeadInfo.createCell(20);
        cell2_21.setCellValue("住址");

        Cell cell2_22 = rowSmallTableHeadInfo.createCell(21);
        cell2_22.setCellValue("户口");
        //*******一般情况*******

        //*******查体一般情况*******
        Cell cell2_23 = rowSmallTableHeadInfo.createCell(22);
        cell2_23.setCellValue("身高");

        Cell cell2_24 = rowSmallTableHeadInfo.createCell(23);
        cell2_24.setCellValue("体重");

        Cell cell2_25 = rowSmallTableHeadInfo.createCell(24);
        cell2_25.setCellValue("血压");

        Cell cell2_26 = rowSmallTableHeadInfo.createCell(25);
        cell2_26.setCellValue("脉搏");

        Cell cell2_27 = rowSmallTableHeadInfo.createCell(26);
        cell2_27.setCellValue("呼吸");

        Cell cell2_28 = rowSmallTableHeadInfo.createCell(27);
        cell2_28.setCellValue("体温");
        //*******查体一般情况*******

        //*******住院基本信息*******
        Cell cell2_29 = rowSmallTableHeadInfo.createCell(28);
        cell2_29.setCellValue("入院日期");

        Cell cell2_30 = rowSmallTableHeadInfo.createCell(29);
        cell2_30.setCellValue("出院日期");

        Cell cell2_31 = rowSmallTableHeadInfo.createCell(30);
        cell2_31.setCellValue("实际住院天数");
        //*******住院基本信息*******

        //*******疾病诊断信息及病理诊断信息*******
        Cell cell2_32 = rowSmallTableHeadInfo.createCell(31);
        cell2_32.setCellValue("病理诊断1");

        Cell cell2_33 = rowSmallTableHeadInfo.createCell(32);
        cell2_33.setCellValue("病理号1");
        //*******疾病诊断信息及病理诊断信息*******

        //*******手术及操作信息*******
        Cell cell2_34 = rowSmallTableHeadInfo.createCell(33);
        cell2_34.setCellValue("手术名称");

        Cell cell2_35 = rowSmallTableHeadInfo.createCell(34);
        cell2_35.setCellValue("手术日期");
        //*******手术及操作信息*******

        //*******院感信息*******
        Cell cell2_36 = rowSmallTableHeadInfo.createCell(35);
        cell2_36.setCellValue("术后并发症");
        //*******院感信息*******

        //*******出院记录*******
        Cell cell2_37 = rowSmallTableHeadInfo.createCell(36);
        cell2_37.setCellValue("出院诊断");

        Cell cell2_38 = rowSmallTableHeadInfo.createCell(37);
        cell2_38.setCellValue("出院建议");

        Cell cell2_39 = rowSmallTableHeadInfo.createCell(38);
        cell2_39.setCellValue("病理检查结果");
        //*******出院记录*******

        //*******主诉*******
        Cell cell2_40 = rowSmallTableHeadInfo.createCell(39);
        cell2_40.setCellValue("主诉");
        //*******主诉*******

        //*******现病史*******
        Cell cell2_41 = rowSmallTableHeadInfo.createCell(40);
        cell2_41.setCellValue("现病史");
        //*******现病史*******

        //*******既往史*******
        Cell cell2_42 = rowSmallTableHeadInfo.createCell(41);
        cell2_42.setCellValue("传染病史");

        Cell cell2_43 = rowSmallTableHeadInfo.createCell(42);
        cell2_43.setCellValue("预防接种史");

        Cell cell2_44 = rowSmallTableHeadInfo.createCell(43);
        cell2_44.setCellValue("外伤史");

        Cell cell2_45 = rowSmallTableHeadInfo.createCell(44);
        cell2_45.setCellValue("手术史");

        Cell cell2_46 = rowSmallTableHeadInfo.createCell(45);
        cell2_46.setCellValue("平素健康状况");

        Cell cell2_47 = rowSmallTableHeadInfo.createCell(46);
        cell2_47.setCellValue("过敏史");

        Cell cell2_48 = rowSmallTableHeadInfo.createCell(47);
        cell2_48.setCellValue("输血史");

        Cell cell2_49 = rowSmallTableHeadInfo.createCell(48);
        cell2_49.setCellValue("疾病史");

        Cell cell2_50 = rowSmallTableHeadInfo.createCell(49);
        cell2_50.setCellValue("产伤史");

        Cell cell2_51 = rowSmallTableHeadInfo.createCell(50);
        cell2_51.setCellValue("过敏源");

        Cell cell2_52 = rowSmallTableHeadInfo.createCell(51);
        cell2_52.setCellValue("药物过敏");

        Cell cell2_53 = rowSmallTableHeadInfo.createCell(52);
        cell2_53.setCellValue("过敏药物");

        Cell cell2_54 = rowSmallTableHeadInfo.createCell(53);
        cell2_54.setCellValue("其它");
        //*******既往史*******

        //*******个人史*******
        Cell cell2_55 = rowSmallTableHeadInfo.createCell(54);
        cell2_55.setCellValue("嗜烟嗜酒史");

        Cell cell2_56 = rowSmallTableHeadInfo.createCell(55);
        cell2_56.setCellValue("职业及毒物暴露史");

        Cell cell2_57 = rowSmallTableHeadInfo.createCell(56);
        cell2_57.setCellValue("家族史");
        //*******个人史*******

        //*******系统回顾*******
        Cell cell2_58 = rowSmallTableHeadInfo.createCell(57);
        cell2_58.setCellValue("系统回顾");
        //*******系统回顾*******

        //*******第一个表的行号*******
        int sheet1Num = 2;


        //第二个表，混合患者的文件名
        Sheet sheetInfo2 = workbook1.createSheet("门诊住院混合患者文件名");
        int sheet2Num = 0;//*******第二个表的行号*******

        //第三个表，混合患者的文件名
        Sheet sheetInfo3 = workbook1.createSheet("处理异常的文件名");
        int sheet3Num = 0;//*******第二个表的行号*******

        for (int fileIndex = 0; fileIndex < files.size(); fileIndex++) {
            int fileNum = fileIndex + 1;
            System.out.println("开始处理第" + fileNum + "个文件");

            File jsonFile = files.get(fileIndex);
            try {


                String jsonStr = getJSONString(jsonFile);
                JSONArray jsonArray = JSON.parseArray(jsonStr);
                if (jsonArray.size() == 1) {
                    System.out.println("第" + fileNum + "个文件是标准文件");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Set<String> stringSet = jsonObject.keySet();
                    Row rowInfo = sheetInfo1.createRow(sheet1Num);

                    //初始化0-57个单元格
                    List<Cell> cellList = new ArrayList<>();
                    for (int j = 0; j < 58; j++) {
                        Cell temp = rowInfo.createCell(j);
                        cellList.add(temp);
                    }


                    String 电子病历菜单及详情字符串json = "";
                    try {
                        JSONArray 电子病历菜单及详情_jsonArray = jsonObject.getJSONArray("电子病历菜单及详情");
                        String 电子病历菜单及详情字符串 = 电子病历菜单及详情_jsonArray.toJSONString();
                        电子病历菜单及详情字符串json = modifyString(电子病历菜单及详情字符串);
                        System.out.println("电子病历菜单及详情字符串json为：" + 电子病历菜单及详情字符串json);
                    } catch (Exception e) {
                        System.out.println("获取电子病历菜单及详情字符串异常： " + e.getMessage());
                    }
                    JSONObject jsonObject_电子病例详情字符串 = JSON.parseObject(电子病历菜单及详情字符串json);
                    System.out.println("jsonObject_电子病例详情字符串为：" + jsonObject_电子病例详情字符串.toJSONString());
                    JSONObject JSONObject个人基本信息 = (JSONObject) jsonObject_电子病例详情字符串.get("个人基本信息");
                    JSONObject JSONObject住院基本信息 = (JSONObject) jsonObject_电子病例详情字符串.get("住院基本信息");
                    JSONObject JSONObject疾病诊断信息及病理诊断信息 = (JSONObject) jsonObject_电子病例详情字符串.get("疾病诊断信息及病理诊断信息");
                    JSONObject JSONObject手术及操作信息 = (JSONObject) jsonObject_电子病例详情字符串.get("手术及操作信息");
                    JSONObject JSONObject院感信息 = (JSONObject) jsonObject_电子病例详情字符串.get("院感信息");
                    JSONObject JSONObject出院记录 = (JSONObject) jsonObject_电子病例详情字符串.get("EMR120001 出院记录");
                    JSONObject JSONObject查体一般情况 = (JSONObject) jsonObject_电子病例详情字符串.get("查体一般情况");
                    JSONObject JSONObject主诉 = (JSONObject) jsonObject_电子病例详情字符串.get("主诉");
                    JSONObject JSONObject现病史 = (JSONObject) jsonObject_电子病例详情字符串.get("现病史");
                    JSONObject JSONObject一般情况 = (JSONObject) jsonObject_电子病例详情字符串.get("一般情况");
                    JSONObject JSONObject既往史 = (JSONObject) jsonObject_电子病例详情字符串.get("既往史");
                    JSONObject JSONObject个人史 = (JSONObject) jsonObject_电子病例详情字符串.get("个人史");
                    JSONObject JSONObject家族史 = (JSONObject) jsonObject_电子病例详情字符串.get("家族史");
                    JSONObject JSONObject系统回顾 = (JSONObject) jsonObject_电子病例详情字符串.get("系统回顾");

                    cellList.get(0).setCellValue(jsonObject.getLong("就诊ID"));
                    cellList.get(1).setCellValue(jsonObject.getString("诊断"));
                    cellList.get(2).setCellValue(jsonObject.getString("住院流水号"));
                    cellList.get(3).setCellValue(jsonObject.getString("科室"));
                    cellList.get(4).setCellValue(jsonObject.getString("类型"));

                    if (JSONObject个人基本信息!=null){
                        cellList.get(5).setCellValue(JSONObject个人基本信息.getString("姓名"));
                        cellList.get(6).setCellValue(JSONObject个人基本信息.getString("性别"));
                        cellList.get(7).setCellValue(JSONObject个人基本信息.getString("年龄"));
                        cellList.get(8).setCellValue(JSONObject个人基本信息.getString("出生日期"));
                        cellList.get(9).setCellValue(JSONObject个人基本信息.getString("就诊号"));
                        cellList.get(10).setCellValue(JSONObject个人基本信息.getString("身份证号"));
                        cellList.get(11).setCellValue(JSONObject个人基本信息.getString("婚姻"));
                        cellList.get(12).setCellValue(JSONObject个人基本信息.getString("电话"));
                        cellList.get(13).setCellValue(JSONObject个人基本信息.getString("联系人姓名"));
                        cellList.get(14).setCellValue(JSONObject个人基本信息.getString("联系人电话"));
                        cellList.get(15).setCellValue(JSONObject个人基本信息.getString("联系人地址"));
                    }

                    if (JSONObject一般情况!=null){
                        cellList.get(16).setCellValue(JSONObject一般情况.getString("住院号"));
                        cellList.get(17).setCellValue(JSONObject一般情况.getString("现住址市"));
                        cellList.get(18).setCellValue(JSONObject一般情况.getString("现住址县1"));
                        cellList.get(19).setCellValue(JSONObject一般情况.getString("BMI"));
                        cellList.get(20).setCellValue(JSONObject一般情况.getString("住址"));
                        cellList.get(21).setCellValue(JSONObject一般情况.getString("户口"));
                    }

                    if (JSONObject查体一般情况 != null) {
                        cellList.get(22).setCellValue(JSONObject查体一般情况.getString("身高"));
                        cellList.get(23).setCellValue(JSONObject查体一般情况.getString("体重"));
                        cellList.get(24).setCellValue(JSONObject查体一般情况.getString("血压"));
                        cellList.get(25).setCellValue(JSONObject查体一般情况.getString("脉搏"));
                        cellList.get(26).setCellValue(JSONObject查体一般情况.getString("呼吸"));
                        cellList.get(27).setCellValue(JSONObject查体一般情况.getString("体温"));
                    }

                    if (JSONObject住院基本信息!=null){
                        cellList.get(28).setCellValue(JSONObject住院基本信息.getString("入院日期"));
                        cellList.get(29).setCellValue(JSONObject住院基本信息.getString("出院日期"));
                        cellList.get(30).setCellValue(JSONObject住院基本信息.getString("实际住院天数"));
                    }

                    if (JSONObject疾病诊断信息及病理诊断信息!=null){
                        cellList.get(31).setCellValue(JSONObject疾病诊断信息及病理诊断信息.getString("病理诊断1"));
                        cellList.get(32).setCellValue(JSONObject疾病诊断信息及病理诊断信息.getString("病理号1"));
                    }

                    if (JSONObject手术及操作信息!=null){
                        String 手术名称 = "";
                        String 手术日期 = "";
                        for (int 手术次数 = 1; 手术次数 < 100; 手术次数++) {
                            String temp = JSONObject手术及操作信息.getString("手术名称" + 手术次数);
                            if (temp != null && temp.length() != 0) {
                                手术名称 = 手术名称 + temp + "\r\n";
                            } else {
                                break;
                            }
                        }
                        for (int 手术次数 = 1; 手术次数 < 100; 手术次数++) {
                            String temp = JSONObject手术及操作信息.getString("手术日期" + 手术次数);
                            if (temp != null && temp.length() != 0) {
                                手术日期 = 手术日期 + temp + "\r\n";
                            } else {
                                break;
                            }
                        }
                        cellList.get(33).setCellValue(手术名称);
                        cellList.get(34).setCellValue(手术日期);
                    }

                    if (JSONObject院感信息!=null){
                        cellList.get(35).setCellValue(JSONObject院感信息.getString("术后并发症"));
                    }

                    if (JSONObject出院记录!=null){
                        String 出院诊断 = JSONObject出院记录.getString("出院诊断１");
                        String 其它诊断 = "";
                        for (int 次数 = 1; 次数 < 100; 次数++) {
                            String temp = JSONObject出院记录.getString("其它诊断" + 次数);
                            if (temp != null && temp.length() != 0) {
                                其它诊断 = 其它诊断 + temp + "\r\n";
                            } else {
                                break;
                            }
                        }
                        cellList.get(36).setCellValue(出院诊断 + 其它诊断);
                        cellList.get(37).setCellValue(JSONObject出院记录.getString("出院医嘱"));
                        String 检查结果 = "";
                        for (int 次数 = 1; 次数 < 100; 次数++) {
                            String temp = JSONObject出院记录.getString("检查结果" + 次数);
                            if (temp != null && temp.length() != 0) {
                                检查结果 = 检查结果 + temp + "\r\n";
                            } else {
                                break;
                            }
                        }
                        cellList.get(38).setCellValue(检查结果);
                    }

                    if (JSONObject主诉!=null){
                        cellList.get(39).setCellValue(JSONObject主诉.getString("主诉"));
                    }

                    if (JSONObject现病史!=null){
                        cellList.get(40).setCellValue(JSONObject现病史.getString("现病史"));
                    }

                    if (JSONObject既往史!=null){
                        cellList.get(41).setCellValue(JSONObject既往史.getString("传染病史"));
                        cellList.get(42).setCellValue(JSONObject既往史.getString("预防接种史"));
                        cellList.get(43).setCellValue(JSONObject既往史.getString("外伤史"));
                        cellList.get(44).setCellValue(JSONObject既往史.getString("手术史"));
                        cellList.get(45).setCellValue(JSONObject既往史.getString("平素健康状况"));
                        cellList.get(46).setCellValue(JSONObject既往史.getString("过敏史"));
                        cellList.get(47).setCellValue(JSONObject既往史.getString("输血史"));
                        cellList.get(48).setCellValue(JSONObject既往史.getString("疾病史"));
                        cellList.get(49).setCellValue(JSONObject既往史.getString("产伤史"));
                        cellList.get(50).setCellValue(JSONObject既往史.getString("过敏源"));
                        cellList.get(51).setCellValue(JSONObject既往史.getString("药物过敏"));
                        cellList.get(52).setCellValue(JSONObject既往史.getString("过敏药物"));
                        cellList.get(53).setCellValue(JSONObject既往史.getString("其它"));
                    }

                    if (JSONObject个人史!=null){
                        cellList.get(54).setCellValue(JSONObject个人史.getString("嗜烟嗜酒史"));
                        cellList.get(55).setCellValue(JSONObject个人史.getString("职业及毒物暴露史"));
                    }

                    if (JSONObject家族史 != null) {
                        cellList.get(56).setCellValue(JSONObject家族史.getString("家族史"));
                    }

                    if (JSONObject系统回顾 != null) {
                        cellList.get(56).setCellValue(JSONObject系统回顾.getString("系统回顾"));

                    }

                    sheet1Num++;

                    System.out.println(sheet1Num);
                } else {
                    System.out.println("第" + fileNum + "个文件不是标准文件");

                    Row row = sheetInfo2.createRow(sheet2Num);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(jsonFile.getName());
                    sheet2Num++;
                }


            } catch (Exception e) {
                System.out.println("第" + fileNum + "个文件处理异常");

                Row row=sheetInfo3.createRow(sheet3Num);
                Cell cell = row.createCell(0);
                cell.setCellValue(jsonFile.getName());
                sheet3Num++;
                e.printStackTrace();
                System.out.println("异常信息为" + e.toString());
            }
        }

        //输出文件
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + "漆瑞大帝吐血整理" + ".xlsx");
        workbook1.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("输出完毕");

    }
}
