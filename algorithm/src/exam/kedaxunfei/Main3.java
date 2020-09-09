//package exam.kedaxunfei;
//
//import java.util.Scanner;
//
//public class Main3 {
//
//
//
//    public static String duoyu(String s){
//        StringBuilder stringBuilder=new StringBuilder(s);
//
//        int index=0;
//        while (index<s.length()&&stringBuilder.charAt(index)=='_'){
//            stringBuilder.deleteCharAt(index);
//            if (stringBuilder.charAt(index)!='_')break;
//        }
//
//        stringBuilder.reverse();
//
//        while (index<s.length()&&stringBuilder.charAt(index)=='_'){
//            stringBuilder.deleteCharAt(index);
//            if (stringBuilder.charAt(index)!='_')break;
//        }
//
//    }
//
//    public static void main(String[] args) {
//        Scanner scanner=new Scanner(System.in);
//        String s=scanner.nextLine();
//    }
//}
