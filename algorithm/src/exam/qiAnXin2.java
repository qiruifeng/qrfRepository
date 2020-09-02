package exam;

import static utils.tools.printArr;

public class qiAnXin2 {

    public static void up(int[] nums, int index) {
        for (int i = 0; i < index; i++) {
            nums[i] = nums[i] + 1;
        }
    }

    public static int[] houseArr(int[] nums) {
        int[] ans = new int[nums.length];
        ans[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                //ans[i] = 1;
                continue;
            }

            if (nums[i] > nums[i - 1]) ans[i] = ans[i - 1] + 1;

            if (ans[i - 1] != 0 && nums[i] < nums[i - 1]) {
                ans[i] = ans[i - 1] - 1;
                if (ans[i] == 0) {
                    up(ans, i + 1);
                }
            }

            if (ans[i - 1] == 0&&nums[i] < nums[i - 1]){

            }
        }

        return ans;
    }

    public static int house(int[] person) {
        int ans = 0;

        int[] houseArr = houseArr(person);
        for (int i = 0; i < houseArr.length; i++) {
            ans = ans + houseArr[i];
        }

        return ans;
    }


    public static void main(String[] args) {
        int[] a = {1, 2};
        printArr(houseArr(a));
        System.out.println(house(a));
    }
}
