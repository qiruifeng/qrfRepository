package lessonFromZuo.basic_class_01;

import static utils.compareUtil.*;

/**
 * 快速排序
 * 荷兰国旗问题是给你一个数进行划分，这个快速排序是直接拿数组的最后一个数值进行划分
 */
public class quickSort {
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    public static void quickSort(int[] arr, int L, int R) {
        if (L < R) {
            exchange(arr, L + (int) (Math.random() * (R - L + 1)), R);
            int[] p = partition(arr, L, R);
            quickSort(arr, 0, p[0] - 1);
            quickSort(arr, p[1] + 1, R);
        }
    }

    public static int[] partition(int[] arr, int L, int R) {
        int less = L - 1;
        int more = R + 1;
        int cur = L;
        int num = arr[R];
        while (cur < more) {
            if (arr[cur] < num) {
                exchange(arr, cur++, ++less);
            } else if (arr[cur] > num) {
                exchange(arr, --more, cur);
            } else {
                cur++;
            }
        }
        return new int[]{less + 1, more - 1};
    }

    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            quickSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "算法正确" : "算法失败");
        int[] arr = {4, 6, 9, 4, 3, 4, 2, 5, 7};
        // int[] arr2 = copyArray(arr);
        System.out.println();
        quickSort(arr);
        printArray(arr);
//        System.out.println();
//        comparator(arr2);
//        printArray(arr2);


    }
}