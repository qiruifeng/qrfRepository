package qrf.jsonToExcel;


import java.io.*;
import java.util.List;

import static qrf.util.util.*;
import static qrf.util.util.getSpecialStr;

public class readFile {
//    public static List<File> getFiles(String path){
//        File root = new File(path);
//        List<File> files = new ArrayList<File>();
//        if(!root.isDirectory()){
//            files.add(root);
//        }else{
//            File[] subFiles = root.listFiles();
//            for(File f : subFiles){
//                files.addAll(getFiles(f.getAbsolutePath()));
//            }
//        }
//        return files;
//    }

    public static void main(String[] args) {
        //String jsonStr = "";
        List<File> files = getFiles("F:\\temp2");
//        for(File f : files){
//            System.out.print(f.getName());
//        }

        File jsonFile = files.get(0);
//        System.out.println(jsonFile.getName().substring(0,7));
//        try  {
//            FileReader fileReader = new FileReader(jsonFile);
//            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
//            int ch = 0;
//            StringBuffer sb = new StringBuffer();
//            while ((ch = reader.read()) != -1) {
//                sb.append((char) ch);
//            }
//            fileReader.close();
//            reader.close();
//            jsonStr = sb.toString();
//            System.out.println(jsonStr);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String jsonStr = getJSONString(jsonFile);
//        System.out.println(jsonStr.length());
        //*****获取字段 电子病历菜单及详情******
        String 电子病历菜单及详情 = "电子病历菜单及详情";
        String 授权委托书 = "授权委托书";
        int l = getSmallInBigIndex(jsonStr, 电子病历菜单及详情);
        int r = getSmallInBigIndex(jsonStr, 授权委托书);
        String 电子病历菜单及详情字符串 = jsonStr.substring(l + 12, r - 3);
        System.out.println(电子病历菜单及详情字符串);
        //*****获取字段 电子病历菜单及详情******

        //*****获取字段 个人基本信息******
//        String 个人基本信息 = "个人基本信息";
//        String 住院基本信息 = "住院基本信息";
//        int int_个人基本信息 = getSmallInBigIndex(电子病历菜单及详情字符串, 个人基本信息);
//        int int_住院基本信息 = getSmallInBigIndex(电子病历菜单及详情字符串, 住院基本信息);
//        String 个人基本信息字符串 = 电子病历菜单及详情字符串.substring(int_个人基本信息 + 8, int_住院基本信息 - 4);
//        String 个人基本信息json = modifyString(个人基本信息字符串);
//        System.out.println(个人基本信息json);
        String 个人基本信息json=getSpecialStr(电子病历菜单及详情字符串,"个人基本信息","住院基本信息");
        System.out.println("个人基本信息json");
        System.out.println(个人基本信息json);
        //*****获取字段 个人基本信息******

        //*****获取字段 住院基本信息******
//        String 疾病诊断信息及病理诊断信息 = "疾病诊断信息及病理诊断信息";
//        int int_疾病诊断信息及病理诊断信息 = getSmallInBigIndex(电子病历菜单及详情字符串, 疾病诊断信息及病理诊断信息);
//        String 住院基本信息字符串 = 电子病历菜单及详情字符串.substring(int_住院基本信息 + 8, int_疾病诊断信息及病理诊断信息 - 4);
//        String 住院基本信息json = modifyString(住院基本信息字符串);
//        System.out.println(住院基本信息json);
        String 住院基本信息json=getSpecialStr(电子病历菜单及详情字符串,"住院基本信息","疾病诊断信息及病理诊断信息");
        System.out.println("住院基本信息json");
        System.out.println(住院基本信息json);
        //*****获取字段 住院基本信息******

        //*****获取字段 疾病诊断信息及病理诊断信息******
        String 疾病诊断信息及病理诊断信息json=getSpecialStr(电子病历菜单及详情字符串,"疾病诊断信息及病理诊断信息","手术及操作信息");
        System.out.println("疾病诊断信息及病理诊断信息json");
        System.out.println(疾病诊断信息及病理诊断信息json);
        //*****获取字段 疾病诊断信息及病理诊断信息******

        //*****获取字段 手术及操作信息******
        String 手术及操作信息json=getSpecialStr(电子病历菜单及详情字符串,"手术及操作信息","院感信息");
        System.out.println("手术及操作信息json");
        System.out.println(手术及操作信息json);
        //*****获取字段 手术及操作信息******

        //*****获取字段 院感信息******
        String 院感信息json=getSpecialStr(电子病历菜单及详情字符串,"院感信息","护理与重症监护信息");
        System.out.println("院感信息json");
        System.out.println(院感信息json);
        //*****获取字段 院感信息******

        //*****获取字段 出院记录1******
        String 出院记录1json=getSpecialStr(电子病历菜单及详情字符串,"出院记录","EMR120001 出院记录\":[{\"年");
        System.out.println("出院记录1json");
        System.out.println(出院记录1json);
        //*****获取字段 出院记录1******

        //*****获取字段 出院记录2******
        String 出院记录2json=getSpecialStr(电子病历菜单及详情字符串,"日\"}]},{\"EMR120001 出院记录","查体一般情况");
        System.out.println("出院记录2json");
        System.out.println(出院记录2json);
        //*****获取字段 出院记录2******

        //*****获取字段 查体一般情况******
        String 查体一般情况json01=getSpecialStr(电子病历菜单及详情字符串,"查体一般情况","\"一般情况\"");
        String 查体一般情况json=查体一般情况json01+"}";
        System.out.println("查体一般情况json");
        System.out.println(查体一般情况json);
        //*****获取字段 查体一般情况******

        //*****获取字段 一般情况******
        String 一般情况json01=getSpecialStr(电子病历菜单及详情字符串,"\"一般情况\"","主诉");
        String 一般情况json="{"+一般情况json01;
        System.out.println("一般情况json");
        System.out.println(一般情况json);
        //*****获取字段 一般情况******

        //*****获取字段 主诉******
        String 主诉json=getSpecialStr(电子病历菜单及详情字符串,"主诉","现病史");
        System.out.println("主诉json");
        System.out.println(主诉json);
        //*****获取字段 主诉******

        //*****获取字段 既往史******
        String 既往史json01=getSpecialStr(电子病历菜单及详情字符串,"\"既往史\"","个人史");
        String 既往史json="{"+既往史json01;
        System.out.println("既往史json");
        System.out.println(既往史json);
        //*****获取字段 既往史******

        //*****获取字段 婚姻史******
        String 婚姻史json=getSpecialStr(电子病历菜单及详情字符串,"婚姻史","月经史");
        System.out.println("婚姻史json");
        System.out.println(婚姻史json);
        //*****获取字段 婚姻史******

        //*****获取字段 系统回顾******
        String 系统回顾json01=getSpecialStr(电子病历菜单及详情字符串,"系统回顾","\"心脏\"");
        String 系统回顾json=系统回顾json01+"}";
        System.out.println("系统回顾json");
        System.out.println(系统回顾json);
        //*****获取字段 系统回顾******




//        JSONArray jsonArray=JSON.parseArray(jsonStr);
//        System.out.println(jsonArray.toString());
//        JSONObject jsonObject= (JSONObject) jsonArray.get(0);
//        System.out.println(jsonObject.toString());
//        PatientCase patientCase = JSON.parseObject(jsonObject.toString(), PatientCase.class);
//        List<ElectronicMedicalRecordMenuAndDetails> jsonArray1=patientCase.get电子病历菜单及详情();
//        System.out.println(jsonArray1.get(0).toString());
//        //System.out.println(patientCase);


    }
}
