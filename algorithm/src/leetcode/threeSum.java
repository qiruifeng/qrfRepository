package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://leetcode-cn.com/problems/3sum/
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复的三元组。
 * <p>
 * 注意：答案中不可以包含重复的三元组
 */
public class threeSum {
    public static List<List<Integer>> threeSum(int[] nums) {

        if (nums.length <= 2 && nums == null) {
            return null;
        }

        List<List<Integer>> res = new ArrayList();


        //先把数组排好序
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0) break;
            if (i > 0 && nums[i - 1] == nums[i]) continue;//去重

            int L = i + 1;
            int R = nums.length - 1;

            while (L < R) {
                if (nums[i] + nums[L] + nums[R] == 0) {
                    res.add(Arrays.asList(nums[i], nums[L], nums[R]));
                    while (L < R && nums[L + 1] == nums[L]) {
                        L++;//去重
                    }
                    while (L < R && nums[R - 1] == nums[R]) {
                        R--;//去重
                    }
                    L++;
                    R--;
                } else if (nums[i] + nums[L] + nums[R] < 0) {
                    L++;
                } else if (nums[i] + nums[L] + nums[R] > 0) {
                    R--;
                }
            }


        }

        return res;
    }

    public static void main(String[] args) {
        int[] test = {1, 8, 9, 7, -8, -49, 0, -8, 0, 0, 0, 0, 0};
        int[] test1 = {0, 0, 0};
        System.out.println("开始");
        System.out.println(threeSum(test1));
        System.out.println("结束");
    }
}
