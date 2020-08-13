package lessonFromZuo.basic_class_01;

//外排序的代码重构
public class merge {
    public static void merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = m + 1;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];//help[i++] = (arr[p1] < arr[p2] ? arr[p1++] : arr[p2++]) 若问号为真则返回第一个，若为假则返回第二个
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
    }

    public static void main(String[] args) {
        int[] test = {1, 3, 4,5, 2, 4, 6};
        merge(test, 0, 3, 6);
        for (int i = 0; i < test.length; i++) {
            System.out.print(test[i] + " ");
        }
    }
}
