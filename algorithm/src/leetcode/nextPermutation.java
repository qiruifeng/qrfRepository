package leetcode;

import java.util.Arrays;

import static utils.tools.printArr;

/**
 * 实现获取下一个排列的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 * 必须原地修改，只允许使用额外常数空间。
 * 以下是一些例子，输入位于左侧列，其相应输出位于右侧列。
 *
 * 1,2,3 → 1,3,2
 * 3,2,1 → 1,2,3
 * 1,1,5 → 1,5,1
 *
 */
public class nextPermutation {

    /**
     * 1.从右至左遍历·nums，发现第一个小于右边的数nums[i]，将该nums[i]之后的数排升序；2.第二层遍历nums[(i+1):]，发现第一个大于nums[i]的数nums[j]，交换两数，退出遍历break；3.交换了也排好序了，退出第一次层遍历return nums
     * 这里有个细节：nums本来就是降序，第一次遍历找不到nums[i],那么直接sort()。
     * @param nums
     */
    public static void nextPermutation(int[] nums){
        //special case
        if (nums.length<=1)return;

        for (int i=nums.length-1;i>0;i--){
            if (nums[i]>nums[i-1]){
                Arrays.sort(nums,i,nums.length);
                for (int j=i;j<nums.length;j++){
                    if (nums[j]>nums[i-1]){
                        int temp=nums[j];
                        nums[j]=nums[i-1];
                        nums[i-1]=temp;
                        return;
                    }
                }
            }
        }

        Arrays.sort(nums);
        return;

    }

    public static void main(String[] args) {
        int[] a={9,8,5,7,6,4};
        nextPermutation(a);
        printArr(a);
    }
}
