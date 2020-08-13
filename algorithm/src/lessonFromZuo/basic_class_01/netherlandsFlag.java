package lessonFromZuo.basic_class_01;

import static basic_class_01.compareUtil.*;

/**
 * 荷兰国旗问题
 * 给定一个数组arr，和一个数num，请把小于num的数放在数组的左边，等于num的数放在数组的中间，大于num的数放在数组的右边。
 */
public class netherlandsFlag {
    public static int[] partition(int[] arr, int l, int r, int num) {
        int less = l - 1;
        int more = r + 1;
//        int cur = l;
        while (l < more) {
            if (arr[l] < num) {
                less++;
                exchange(arr,  less ,l);
                l++;

            } else if (arr[l] > num) {
                more = more - 1;
                exchange(arr, more,l);
            } else {
                l++;
            }
        }
        printArray(arr);
        return new int[]{less + 1, more - 1};
    }

    public static int[] partition1(int[] arr, int l, int r, int p) {
        int less = l - 1;
        int more = r + 1;
        while (l < more) {
            if (arr[l] < p) {
                exchange(arr, ++less, l++);
            } else if (arr[l] > p) {
                exchange(arr, --more, l);
            } else {
                l++;
            }
        }
        printArray(arr);
        return new int[]{less + 1, more - 1};
    }

    public static void main(String[] args) {
        //int[] test = generateRandomArray(10,10);
        int[] test = {1, 5, 6, 4,  6, 4, 3, 3,  2, 2, 2, 2, 2};
        printArray(test);
        System.out.println();
        int[] arr1=copyArray(test);
        int[] arr2=copyArray(test);

        int[] res1 = partition(arr1, 0, test.length - 1, 4);
        System.out.println();
        int[] res2 = partition1(arr2, 0, test.length - 1, 4);
        System.out.println();
        System.out.print(res1[0] + " ");
        System.out.print(res1[1]);
        System.out.println();
        System.out.print(res2[0] + " ");
        System.out.print(res2[1]);

    }
}
