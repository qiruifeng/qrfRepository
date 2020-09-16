package exam.huawei;

import java.util.HashMap;

public class huaweimiashi {

    public  static  boolean isTENET(String string){
        HashMap<Character,Integer> a=new HashMap<>();
        char[] chars=string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (!a.containsKey(chars[i])){
                a.put(chars[i],1);
            }else {
                a.put(chars[i],a.get(chars[i])+1);
            }
        }

        int jishu=0;
        int oushu=0;
        for (Character character:a.keySet()) {
            if (a.get(character)%2==0){
                oushu++;
            }else {
                jishu++;
            }
        }

        if (jishu==1||jishu==0){
            return true;
        }else {
            return false;
        }

    }

    public static void main(String[] args) {
        String a="code";
        String b="aab";
        String c="qwerrewq";
        System.out.println(isTENET(a));
        System.out.println(isTENET(b));
        System.out.println(isTENET(c));
    }
}
