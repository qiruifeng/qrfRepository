package jianzhioffer;

public class NumberOf1Between1AndN {
    public static int NumberOf1Between1AndN_Solution(int n) {
        StringBuffer bigString=new StringBuffer();
        for (int i=1;i<=n;i++){
            bigString.append(i);
        }
        int res=0;

        for (int i=0;i<bigString.length();i++){
            if (bigString.charAt(i)=='1'){
                res++;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(NumberOf1Between1AndN_Solution(20));
    }
}
