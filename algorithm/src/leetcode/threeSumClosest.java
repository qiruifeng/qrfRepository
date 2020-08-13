package leetcode;

import java.util.Arrays;

public class threeSumClosest {
    public static int threeSumClosest(int[] nums, int target) {

        if (nums.length <= 2 && nums == null) {
            return 0;
        }
        //先把数组排好序
        Arrays.sort(nums);

        int distance=Integer.MAX_VALUE;
        int ans = 0;
        for (int i = 0; i < nums.length - 2; i++) {
            int L = i + 1;
            int R = nums.length - 1;
            while (L < R){
                if (nums[i] + nums[L] + nums[R]==target){
                    return target;
                }else if (nums[i] + nums[L] + nums[R]<target){
                    if (Math.abs(nums[i] + nums[L] + nums[R]-target)<distance){
                        distance=Math.abs(nums[i] + nums[L] + nums[R]-target);
                        ans=nums[i] + nums[L] + nums[R];
                    }
                    L++;
                }else if (nums[i] + nums[L] + nums[R]>target){
                    if (Math.abs(nums[i] + nums[L] + nums[R]-target)<distance){
                        distance=Math.abs(nums[i] + nums[L] + nums[R]-target);
                        ans=nums[i] + nums[L] + nums[R];
                    }
                    R--;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] test = {1,1,1,0};
        System.out.println(threeSumClosest(test,-100));
    }
}
