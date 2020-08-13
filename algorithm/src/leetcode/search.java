package leetcode;

/**
 * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
 * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
 * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。
 * 你可以假设数组中不存在重复的元素。
 * 你的算法时间复杂度必须是 O(log n) 级别。
 * <p>
 * 输入: nums = [4,5,6,7,0,1,2], target = 0
 * 输出: 4
 * <p>
 * 输入: nums = [4,5,6,7,0,1,2], target = 3
 * 输出: -1
 */
public class search {
    public static int search(int[] nums, int target) {

        if (nums == null || nums.length == 0) {
            return -1;
        }
        if (nums.length == 1) {
            return nums[0] == target ? 0 : -1;
        }

        int center = findCenter(nums);

        if (center==0){
            return hasNum(center,nums.length-1,target,nums);
        }
        if (nums[nums.length-1]>=target){
            return hasNum(center,nums.length-1,target,nums);
        }else {
            return hasNum(0,center-1,target,nums);
        }

    }

    public static int findCenter(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        int center = 0;


        while (right - left > 1) {
            int mid = (left + right) / 2;
            if (nums[mid] > nums[right]) {
                left = mid;
            }
            if (nums[mid] < nums[right]) {
                right = mid;
            }
        }

        center = nums[left] > nums[right] ? right : left;


        return center;
    }

    public static int hasNum(int left, int right, int target,int[] nums) {
        if (nums[left]==target)return left;
        if (nums[right]==target)return right;
        while (right - left > 1) {

            int mid = (right+left)/2;
            if (nums[mid]<target){
                left=mid;
            }else if (nums[mid]>target){
                right=mid;
            }else {
                return mid;
            }

            if (nums[left]==target)return left;
            if (nums[right]==target)return right;
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] test = {1, 2, 3, 4, 5};
        int[] test1 = {5,6,7,8,9,1,3,4};
        int[] test2={1,3};

        System.out.println(search(test2,1));
    }
}
