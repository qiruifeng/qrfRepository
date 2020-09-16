package exam.huawei;

import java.util.ArrayList;
import java.util.List;


public class huaweimianshi2 {


    public static List<String[]> generatorList(String string){

        List<String[]> res=new ArrayList<>();
        int L=string.length();
        for (int i = 1; i < L-3; i++) {
            for (int j = 1; j < L-2; j++) {
                if (i+j>L-2)continue;
                for (int k = 0; k < L-3; k++) {
                    if (i+j+k>L-1){
                        continue;
                    }else {
                        String[] s=new String[4];
                        String s1=string.substring(0,i);
                        String s2=string.substring(i,i+j);
                        String s3=string.substring(i+j,i+j+k);
                        String s4=string.substring(i+j+k);
                        s[0]=s1;
                        s[1]=s2;
                        s[2]=s3;
                        s[3]=s4;
                        res.add(s);
                    }
                }
            }
        }
        return res;
    }

    public static boolean isSimpleTrueNum(String str){

        if (str=="0")return true;
        if (str.length()!=1&&str.charAt(0)=='0')return false;

        if (Integer.valueOf(str)>255||Integer.valueOf(str)<0)return false;
        return true;
    }

    public static boolean listIsIP(String[] strings){

        for (int i = 0; i < strings.length; i++) {
            if (!isSimpleTrueNum(strings[i]))return false;
        }
        return true;
    }

    public static int IPNUM(String string){

        int ans=0;

        List<String[]> strings=generatorList(string);

        for (int i = 0; i < strings.size(); i++) {
            if (listIsIP(strings.get(i))){
                ans++;
            }
        }

        return ans;
    }

    public static void main(String[] args) {

    }
}
