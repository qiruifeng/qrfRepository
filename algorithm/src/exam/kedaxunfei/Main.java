package exam.kedaxunfei;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String s=scanner.nextLine();
        while (s.contains("__")){
            s=s.replaceAll("__","_");
        }

        int i=s.indexOf('_');

        if (i==0){
            s=s.substring(1);
        }

        int last=s.lastIndexOf('_');
        if (last==s.length()-1){
            s=s.substring(0,last);
        }
        System.out.println(s);
    }
}
