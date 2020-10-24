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
    public static int getSmallInBigIndex(String string, String target) {
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

    /**
     *
     * @param string
     * @param target
     * @return
     */
    public static List<Integer> getSmallInBigIndexList(String string, String target){
        List<Integer> ans=new ArrayList<>();
        int l=target.length();
        for (int i = 0; i < string.length()- l + 1; i++) {
            String temp = string.substring(i, i + l);
            if (temp.equals(target)){
                ans.add(i);
            }
        }
        return ans;
    }

    /**
     * 把origin修改成json的形式
     *
     * @param origin
     * @return
     */
    public static String modifyString(String origin) {
        String tmp1 = origin.replaceAll("\\{", "");
        String tmp2 = tmp1.replaceAll("}", "");
        String tmp3 = tmp2.replaceAll("\\[", "{");
        String tmp4 = tmp3.replaceAll("]", "}");
        return tmp4;
    }

    /**
     * 把origin的转化为jsonString
     * @param origin
     * @return
     */
    public static String modifyStringToJSONString(String origin){
        return null;
    }

    /**
     * 在origin内截取start和end之间的字符串
     *
     * @param origin
     * @param start
     * @param end
     * @return
     */
    public static String getSpecialStr(String origin, String start, String end) {
        int int_start = getSmallInBigIndex(origin, start);
        int int_end = getSmallInBigIndex(origin, end);
        String res = origin.substring(int_start + start.length() + 2, int_end - 4);
        return modifyString(res);
    }

    /**
     * 在origin内截取target后面的字段内容
     * @param origin
     * @param target
     * @return
     */
    public static List<String> getSpecialStr(String origin,String target){
        List<String> ans=new ArrayList<>();
        List<Integer> indexList=getSmallInBigIndexList(origin,target);
        for (int i = 0; i < indexList.size(); i++) {
            String strTemp=getStrFromIndex(origin,indexList.get(i),target);
            String strTempJson=modifyString(strTemp);
            ans.add(strTempJson);
        }
        return ans;
    }

    /**
     * 从begin开始截取origin后面[]的内容
     *
     * @param origin
     * @param start
     * @return
     */
    public static String getStrFromIndex(String origin,int start,String target){
        int end=0;
        for (int i = start; i < origin.length(); i++) {
            if (origin.charAt(i)==']'){
                end=i;
                break;
            }
        }
        return origin.substring(start+target.length()+2,end+1);
    }

}
