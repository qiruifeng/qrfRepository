package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定一个包含 n 个整数的数组 nums 和一个目标值 target，判断 nums 中是否存在四个元素 a，b，c 和 d ，使得 a + b + c + d 的值与 target 相等？找出所有满足条件且不重复的四元组。
 * 答案中不可以包含重复的四元组。
 */
public class fourSum {
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        if (nums.length <= 3 && nums == null) {
            return null;
        }

        List<List<Integer>> res = new ArrayList();

        //先把数组排好序
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 3; i++) {

            if (i > 0 && nums[i - 1] == nums[i]) continue;//去重
            for (int j = i + 1; j < nums.length - 2; j++) {

                if (j>i+1&&nums[j-1]==nums[j])continue;//去重
                int l = j + 1;
                int r = nums.length - 1;

                while (l < r) {
                    if (nums[i] + nums[j] + nums[l] + nums[r] == target) {
                        res.add(Arrays.asList(nums[i], nums[j], nums[l], nums[r]));
                        while (l < r && nums[l + 1] == nums[l]) {
                            l++;//去重
                        }
                        while (l < r && nums[r - 1] == nums[r]) {
                            r--;//去重
                        }
                        l++;
                        r--;
                    } else if (nums[i] + nums[j] + nums[l] + nums[r] < target) {
                        l++;
                    } else if (nums[i] + nums[j] + nums[l] + nums[r] > target) {
                        r--;
                    }
                }

            }

        }

        return res;
    }

    public static void main(String[] args) {
        int[] test1={-1,0,1,2,-1,-4};
        int[] test2={-3,-2,-1,0,0,1,2,3};
        int[] test3={1,-2,-5,-4,-3,3,3,5};

//        System.out.println(fourSum(test1,-1));
//        System.out.println(fourSum(test2,0));
        System.out.println(fourSum(test3,-11));
    }
}
