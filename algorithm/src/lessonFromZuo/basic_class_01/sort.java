package lessonFromZuo.basic_class_01;

import java.util.Arrays;

import static lessonFromZuo.basic_class_01.compareUtil.*;


public class sort {

    //冒泡
    public static void bubbleSort(int[] arry) {
        if (arry == null || arry.length < 2) {
            return;
        }
        for (int end = arry.length - 1; end > 0; end--) {
            for (int i = 0; i < end; i++) {
                if (arry[i] > arry[i + 1]) {
                    exchange(arry, i, i + 1);
                }
            }
        }
    }

    //选择排序
    public static void selectionSort(int[] arry) {
        if (arry == null || arry.length < 2) {
            return;
        }
        for (int i = 0; i < arry.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < arry.length; j++) {
                if (arry[min] > arry[j]) {
                    min = j;
                }
//                min = arry[j] < arry[min] ? j : min;
            }
            exchange(arry, i, min);

        }
    }

    //插入排序
    private static void insetSort(int[] arry) {
        if (arry == null || arry.length < 2) {
            return;
        }
        for (int i = 1; i < arry.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arry[j - 1] > arry[j]) {
                    exchange(arry, j, j - 1);
                }
            }
        }
    }

    //递归找最大值
    public static int getMax(int[] arry, int L, int R) {
        if (L == R) {
            return arry[L];
        }
        int mid = (L + R) / 2;
        int leftMax = getMax(arry,L,mid);
        int rightMax=getMax(arry,mid+1,R);
        return Math.max(leftMax,rightMax);
    }




    public static void main(String[] args) {
        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            bubbleSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "算法正确" : "算法失败");

        int[] arry = generateRandomArray(maxSize, maxValue);
        System.out.print("原数组为  :" + " ");
        printArray(arry);
        System.out.println();
        System.out.print("冒泡排序为:" + " ");
        bubbleSort(arry);
        printArray(arry);
        System.out.println();
        System.out.print("插入排序为:" + " ");
        insetSort(arry);
        printArray(arry);
        System.out.println();
        System.out.print("选择排序为:" + " ");
        selectionSort(arry);
        printArray(arry);
        System.out.println();
        System.out.print("递归找最大值为:" + getMax(arry,0,arry.length-1));
    }

}
