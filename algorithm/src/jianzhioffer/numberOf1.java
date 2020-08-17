package jianzhioffer;


/**
 * 输入一个整数，输出该数32位二进制表示中1的个数。其中负数用补码表示。
 */


public class numberOf1 {
    public static int NumberOf1(int n) {
        String string=Integer.toBinaryString(n);
        char[] chars=string.toCharArray();
        int ans=0;
        for (int i=0;i<chars.length;i++){
            if(chars[i]-'1'==0){
                ans++;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(NumberOf1(10));
    }
}
