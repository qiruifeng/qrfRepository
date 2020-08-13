package leetcode;

import static utils.tools.printArr;

/**
 * 给定一个排序数组，你需要在 原地 删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
 * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
 */
public class removeDuplicates {
    public static int removeDuplicates(int[] nums){

        //special case
        if (nums.length<1){
            return 0;
        }
        if (nums.length==1){
            return 1;
        }

        int end=0;
        for (int i=1;i<nums.length;i++){
            if (nums[i]!=nums[i-1]){
                end++;
                nums[end]=nums[i];
            }
        }

        return end+1;
    }

    public static void main(String[] args) {
        int[] test={0,1,2,3,4,5,6};

        System.out.println(removeDuplicates(test));
        printArr(test);

    }
}
