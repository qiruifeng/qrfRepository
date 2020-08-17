package jianzhioffer;

import java.util.Arrays;

public class printMinNumber {
    public static String PrintMinNumber(int[] numbers) {
        StringBuilder bigString = new StringBuilder();
        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < numbers.length; i++) {
            bigString.append(numbers[i]);
        }

        char[] help = bigString.toString().toCharArray();

        Arrays.sort(help);


        ans.append(help);

        if(ans.charAt(0)=='0') {
            int i=0;
            char exchangA=ans.charAt(0);
            while (ans.charAt(i)=='0'){
                i++;
            }
            ans.setCharAt(0,ans.charAt(i));
            ans.setCharAt(i,exchangA);
        }


        return ans.toString();

    }

    public static void main(String[] args) {
        int[] a = {4, 4, 20, 5, 2, 1};
        System.out.println(PrintMinNumber(a));
    }
}
