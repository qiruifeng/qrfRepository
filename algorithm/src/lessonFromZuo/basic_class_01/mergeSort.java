package lessonFromZuo.basic_class_01;


import static utils.compareUtil.*;

//归并排序，（外排序自己理解的版本）
public class mergeSort {
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    public static void mergeSort(int[] arr, int L, int R) {
        if (R==L) {
            return;
        }
        int mid = (L + R) / 2;
        mergeSort(arr, L, mid);
        mergeSort(arr, mid + 1, R);
        merge(arr, L, mid, R);
    }

    private static void merge(int[] arr, int L, int mid, int R) {
        int[] tmp = new int[R - L + 1];
        int i = 0;
        int p = L;
        int q = mid + 1;
        while (p <= mid && q <= R) {
            if (arr[p] < arr[q]) {
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
        while (q <= R) {
            tmp[i] = arr[q];
            i++;
            q++;
        }
        for (int j = 0; j < tmp.length; j++) {
            arr[L + j] = tmp[j];
        }
    }

    public static void main(String[] args) {
        int[] test = {1, 3, 4, 5,6,6,6,6,6,6,6,24,28,32,68, 2, 4, 6,7,8,9,10,11,48,97};
        merge(test, 0, 14, test.length-1);
        for (int i = 0; i < test.length; i++) {
            System.out.print(test[i] + " ");
        }

        int testTime = 500000;
        int maxSize = 10;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            mergeSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                succeed = false;
                break;
            }
        }
        System.out.println(succeed ? "算法正确" : "算法失败");
    }
}
