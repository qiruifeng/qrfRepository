package exam.huawei;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class huaweinew2 {

    public static  void fenge(String str, List<String> list){
        int begin=0;
        for (int i = 0; i < str.length(); i++) {
            if (isFuhao(str.charAt(i))){
                list.add(str.substring(begin,i));
                list.add(str.substring(i,i+1));
                begin=i+1;
            }
        }
        if (begin<str.length()){
            list.add(str.substring(begin,str.length()));
        }
    }

    public static boolean isFuhao(char c){
        if (c==','||c=='.'||c=='!'||c=='?'){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String all=scanner.nextLine();
        all.trim();
        String[] allString=all.split(";");

        String daiceStr=allString[0];
        String daanStr=allString[1];

        int res=0;
        if (daanStr==null||daanStr.length()==0) System.out.println("(0,0)");
        String[] daan=daanStr.split(" ");
        String[] daice=daiceStr.split(" ");

        List<String> daiceList=new ArrayList<>();
        List<String> daanList=new ArrayList<>();

        for (int i = 0; i < daan.length; i++) {
            daan[i]=daan[i].trim();
        }

        for (int i = 0; i < daice.length; i++) {
            daice[i]=daice[i].trim();
        }

        for (int i = 0; i < daice.length; i++) {
            fenge(daice[i],daiceList);
        }

        for (int i = 0; i < daan.length; i++) {
            fenge(daan[i],daanList);
        }

        System.out.println("("+res+","+daanList.size()+")");
    }























}
