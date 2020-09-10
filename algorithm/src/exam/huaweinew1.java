package exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class huaweinew1 {

    public static boolean isSubString(String s,String target){
        if (s.indexOf(target)!=-1){
            return true;
        }else {
            return false;
        }
    }

    public static String getSpecialString(String s,char num){
        StringBuilder ans=new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i)<num){
                ans.append(s.charAt(i));
            }
        }

        return ans.toString();
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        List<String> stringList=new ArrayList<>();
        while (scanner.hasNextLine()){
            stringList.add(scanner.nextLine());
        }

        char target=stringList.get(stringList.size()-2).charAt(0);

        List<String> ansList=new ArrayList<>();
        List<String> specialList=new ArrayList<>();
        for (int i = 0; i < stringList.size()-2; i++) {

            if (stringList.get(i).length()>100){
                System.out.println(stringList.get(i));
            }else {
                if (isSubString(getSpecialString(stringList.get(i),target),getSpecialString(stringList.get(stringList.size()-1),target))){
                    System.out.println(stringList.get(i));
                }
            }

        }






//        System.out.println(isSubString("123456","13"));
    }
}
