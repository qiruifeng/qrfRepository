package leetcode;

import static utils.tools.printArr;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
 */
public class isSun {
    public static int[] twoSum(int[] nums, int target) {
        int[] p=new int[2];
        int p1=0;
        int p2=1;
        for(int i=p1;i<nums.length-1;i++){
            for(int j=i+1;j<nums.length;j++){
                if(nums[i]+nums[j]==target){
                    p[0]=i;
                    p[1]=j;
                    return p;
                }
            }
        }

        return null;

    }

    public static void main(String[] args) {
        int[] test={2,5,5,11};
        printArr(test);
        System.out.println();
        printArr(twoSum(test,10));
    }
}
