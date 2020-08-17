package jianzhioffer;

import java.util.ArrayList;

import static utils.tools.printArrayList;

/**
 * 输入一个递增排序的数组和一个数字S，
 * 在数组中查找两个数，使得他们的和正好是S，
 * 如果有多对数字的和等于S，输出两个数的乘积最小的。
 */
public class FindNumbersEqualsSum {
    public static ArrayList<Integer> FindNumbersWithSum(int[] array, int sum) {

        ArrayList<Integer> ans = new ArrayList<>();

        int chengji = Integer.MAX_VALUE;

        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] + array[j] == sum) {
                    if (array[i] * array[j] < chengji) {
                        ans.add(0,array[i]);
                        ans.add(1,array[j]);
                        chengji=array[i] * array[j];
                    }
                }
            }
        }

        return ans;
    }


    public static void main(String[] args) {

        int[] a={1,2,3,4,5,6,7,8,9,10};

        printArrayList(FindNumbersWithSum(a,11));
    }
}
