package jianzhioffer;

import static utils.tools.printArr;

public class findGreatestSumOfSubArray {
    public static int[] FindGreatestSumOfSubArray(int[] array) {
//        if(array.length == 1){
//            return array[0];
//        }
//        if(array == null){
//            return 0;
//        }

        if(array.length == 1){
            return array;
        }
        if(array == null){
            return null;
        }

        int max = array[0];
        int[] dp = new int[array.length];
        dp[0] = array[0];
        for(int i = 1; i < array.length; i++){
            int temp = dp[i-1]+array[i];
            if(temp > array[i]){
                dp[i] = temp;
            }else{
                dp[i] = array[i];
            }
            if(dp[i] > max){
                max = dp[i];
            }
        }
        return dp;
    }

    public static void main(String[] args) {
        int[] a={1,-2,3,10,-4,7,2,-5};
        printArr(FindGreatestSumOfSubArray(a));
    }
}
