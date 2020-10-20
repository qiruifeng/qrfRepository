package qrf.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class util {

    public static List<File> getFiles(String path) {
        File root = new File(path);
        List<File> files = new ArrayList<File>();
        if (!root.isDirectory()) {
            files.add(root);
        } else {
            File[] subFiles = root.listFiles();
            for (File f : subFiles) {
                files.addAll(getFiles(f.getAbsolutePath()));
            }
        }
        return files;
    }

    public static String getJSONString(File jsonFile) {
        String jsonStr = "";
        try {
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
//            System.out.println(jsonStr);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonStr;
    }


    /**
     * 判断大字符串包含小字符串的位置
     *
     * @param string 大字符串
     * @param target 小字符串
     * @return
     */
    public static int getElectronicMedicalRecordMenuAndDetailsString(String string, String target) {
        int L = 0;
        int R = 0;
        int l = target.length();
        for (int i = 0; i < string.length() - l + 1; i++) {
            String temp = string.substring(i, i + l);
            if (temp.equals(target)) {
                return i;
            }
        }

        return -1;
    }

}
