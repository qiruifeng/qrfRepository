package lessonFromZuo.basic_class_01;

import static utils.compareUtil.*;


/**
 * 小和问题
 * 在一个数组中，每一个数左边比当前数小的数累加起来，叫做这个数组的小和。求一个数组的小和
 */
public class smallSum {

    public static int smallSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return smallSum(arr, 0, arr.length - 1);
    }

    private static int smallSum(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        int mid = l + ((r - l) >> 1);// >>为位运算，效果就是将括号里面的值除以2
        return smallSum(arr, l, mid) + smallSum(arr, mid + 1, r) + wholeSmallSum(arr, l, mid, r);

    }

    private static int wholeSmallSum(int[] arr, int l, int mid, int r) {
        int[] tmp = new int[r - l + 1];
        int i = 0;
        int p = l;
        int q = mid + 1;
        int res = 0;
        while (p <= mid && q <= r) {
            if (arr[p] < arr[q]) {
                res = res + arr[p] * (r - q + 1);
                tmp[i] = arr[p];
                i++;
                p++;
            } else {
                tmp[i] = arr[q];
                i++;
                q++;
            }
        }//在执行了这个while循环之后，必然会有一个指针会越界，要么p，要么q
        while (p <= mid) {
            tmp[i] = arr[p];
            i++;
            p++;
        }
        while (q <= r) {
            tmp[i] = arr[q];
            i++;
            q++;
        }
        for (int j = 0; j < tmp.length; j++) {
            arr[l + j] = tmp[j];
        }
        return res;
    }

    //正确的方法
    public static int comparator(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int res = 0;
        for (int i = 1; i < arr.length; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    res = res + arr[j];
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < 1; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            // int[] arr1 = {1,2,3,4,5,6};
            int[] arr2 = copyArray(arr1);
            int test1 = smallSum(arr1);
            int test2 = comparator(arr2);
            if (test1 != test2) {
                succeed = false;
                break;
            }
        }


        System.out.println(succeed ? "算法正确" : "算法失败");
    }
}