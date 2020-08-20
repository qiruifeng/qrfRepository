package exam;

import java.util.ArrayList;
import java.util.Scanner;

public class shunfeng {

    //第一层循环循环链表中的每一个数组，表示第一次插入的时间序列
    //第二从循环开始，依次循环后面的数组，如果和前面的链表不相交，表明不冲突，添加进链表
    //第二层循环结束，得到可以的时间序列，把金额算出来
    //第一层循环结束，依次对比可以序列的金钱，拿到最大的一个
    public static int getMaxMoney(ArrayList<Integer[]> list) {
        if (list.size() == 0) {
            return 0;
        }

        if (list.size() == 1) {
            return list.get(0)[2];
        }

        int MaxMoney = Integer.MIN_VALUE;



        for (int i = 0; i < list.size(); i++) {
            ArrayList<Integer[]> temp = new ArrayList<>();
            temp.add(list.get(i));

            for (int j = i + 1; j < list.size(); j++) {
                if (!lianbiaoxiangjiao(temp, list.get(j))) {
                    temp.add(list.get(j));
                }
            }
            if (getListMoney(temp) > MaxMoney) {
                MaxMoney = getListMoney(temp);
            }
        }

        return MaxMoney;
    }

    /**
     * 得到成功的时间序列的金钱
     * @param list
     * @return
     */
    public static int getListMoney(ArrayList<Integer[]> list) {
        int ans = 0;
        for (int i = 0; i < list.size(); i++) {
            ans = ans + list.get(i)[2];
        }

        return ans;
    }

    /**
     * 判断两个数组是否相交
     * 相交表示时间冲突，不相交表示时间不冲突
     * @param a
     * @param b
     * @return
     */
    public static boolean xianduanxiangjiao(Integer[] a, Integer[] b) {
        if (a[0] <= b[0] && a[1] >= b[1]) return true;

        if (a[0] >= b[0] && a[1] <= b[1]) return true;

        if (a[0] < b[1] && a[1] >= b[1]) return true;

        if (a[1] > b[0] && b[0] >= a[0]) return true;

        return false;
    }

    /**
     * 判断一个链表中的数组和新的数组是否相交
     * 相交表示时间冲突，不相交表示时间不冲突
     * @param list
     * @param array
     * @return
     */
    public static boolean lianbiaoxiangjiao(ArrayList<Integer[]> list, Integer[] array) {

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)[0] < min) {
                min = list.get(i)[0];
            }
            if (list.get(i)[1] > max) {
                max = list.get(i)[1];
            }

            if (xianduanxiangjiao(list.get(i), array)) {
                return true;
            }
        }

        if (array[0] <= min && array[1] >= max) return true;

        return false;


    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        ArrayList<Integer[]> nums = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Integer[] a = new Integer[3];
            for (int j = 0; j < 3; j++) {
                a[j] = scanner.nextInt();
            }
            nums.add(a);
        }

        int ans = 0;
        ans = getMaxMoney(nums);
        System.out.println(ans);

    }

}
