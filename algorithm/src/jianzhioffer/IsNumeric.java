package jianzhioffer;

public class IsNumeric {
    public static boolean isNumeric(char[] str) {

        try{
            double re = Double.parseDouble(new String(str));
        }catch (Exception e){
            return false;
        }



        return true;
    }

    public static void main(String[] args) {

        char[] a={'1','e','4','5','6','8','4'};
        System.out.println(isNumeric(a));

    }
}
