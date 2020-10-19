package qrf.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class util {

    public static List<File> getFiles(String path){
        File root = new File(path);
        List<File> files = new ArrayList<File>();
        if(!root.isDirectory()){
            files.add(root);
        }else{
            File[] subFiles = root.listFiles();
            for(File f : subFiles){
                files.addAll(getFiles(f.getAbsolutePath()));
            }
        }
        return files;
    }

    public static String getJSONString(File jsonFile){
        String jsonStr = "";
        try  {
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            System.out.println(jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }

    public static void makeSheet(String sheetName){

    }

}
