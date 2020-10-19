package qrf.jsonToExcel;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import qrf.entity.PatientCase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static qrf.util.util.getFiles;
import static qrf.util.util.getJSONString;

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

        File jsonFile=files.get(0);
        System.out.println(jsonFile.getName().substring(0,7));
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
        String jsonStr=getJSONString(jsonFile);


        JSONArray jsonArray=JSON.parseArray(jsonStr);
        System.out.println(jsonArray.toString());
        JSONObject jsonObject= (JSONObject) jsonArray.get(0);
        System.out.println(jsonObject.toString());
        PatientCase patientCase = JSON.parseObject(jsonObject.toString(), PatientCase.class);

        System.out.println(patientCase);

    }
}
