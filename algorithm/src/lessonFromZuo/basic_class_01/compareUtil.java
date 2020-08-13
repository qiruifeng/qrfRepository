package lessonFromZuo.basic_class_01;

import java.util.Arrays;

/**
 * 对数器
 */
public class compareUtil {

    //随机数组生成器
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    //绝对正确的排序
    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    //判断两个数组是否相等
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    //打印数组
    public static void printArray(int[] arry) {
        if (arry == null) {
            return;
        }
        for (int i = 0; i < arry.length; i++) {
            System.out.print(arry[i] + " ");
        }

    }

    //复制数组
    public static int[] copyArray(int[] arry) {
        if (arry == null) {
            return null;
        }
        int[] tmp = new int[arry.length];
        for (int i = 0; i < arry.length; i++) {
            tmp[i] = arry[i];
        }
        return tmp;
    }

    //交换数组中的两个数
    public static void exchange(int[] arry, int i, int j) {
        int temp;
        temp = arry[i];
        arry[i] = arry[j];
        arry[j] = temp;

    }
}
