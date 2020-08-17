package jianzhioffer;

import java.util.ArrayList;

/**
 * 和为S的连续正数序列
 */
public class findContinuousSequence {
    public ArrayList<ArrayList<Integer>> FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();

        for (int i = 1; i <= sum / 2; i++) {
            double n=0.5*(-1+Math.sqrt(1-4*(i-i*i-2*sum)));
            if ((int)n==n){
                ArrayList<Integer> list=new ArrayList<>();
                for (int j=i;j<=n;j++){
                    list.add(j);
                }
                ans.add(list);
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        double a = 5.1;
        double b = 5.00000000;

        double c = 4654;

        System.out.println("a是整数吗？ 答案：" + ((int) a == a));
        System.out.println("b是整数吗？ 答案：" + ((int) b == b));
        System.out.println("c是整数吗？ 答案：" + ((int) c == c));
    }
}
