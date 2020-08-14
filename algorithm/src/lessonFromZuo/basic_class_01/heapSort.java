package lessonFromZuo.basic_class_01;


import static utils.compareUtil.exchange;
import static utils.compareUtil.printArray;

public class heapSort {
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }
//        int heapSize=arr.length;//heapSize是越界的位置，所以是数组最后一个下标的下一位
//        exchange(arr,0,--heapSize);
//        heapify(arr,0,heapSize--);

        for (int heapSize=arr.length;heapSize>0;heapSize--){
            exchange(arr,0,heapSize-1);
            heapify(arr,0,heapSize-1);
        }

    }

    //之前的数组已经是一个大根堆，新加了一个数，让新的数组重新变成大根堆
    public static void heapInsert(int[] arr, int index) {

        while (arr[index] > arr[(index - 1) / 2]) {
            exchange(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    //之前的数组是大根堆，突然有一个数变小了，重新调整成大根堆的过程
    //0~heapSize-1范围内已经形成了大根堆，这时index位置上的数变小了导致这个位置上的数要往下面沉
    public static void heapify(int[] arr, int index, int heapSize) {//其中heapSize是数组中的一段，一定不会比数组的长度大
        int left = index * 2 + 1;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && arr[left] < arr[left + 1]//这一步是找到左右两个孩子中的最大值
                    ? left + 1
                    : left;
            largest=arr[index]>arr[largest]//这一步是看自己和左右孩子中的最大值谁大
                    ?index
                    :largest;
            if (largest==index){
                break;
            }
            exchange(arr,index,largest);
            index=largest;
            left=index*2+1;

//            int left1=index*2+1;
//            int largest1;
//            while (left1<heapSize){
//                if (left1+1>heapSize&&arr[left1]>arr[index]){
//                    largest1=left1;
//                }else if (arr[left1]>arr[index]&&arr[left1]>=arr[left1+1]){
//                    largest1=left1;
//                }else if (arr[left1+1]>arr[index]&&arr[left1+1]>arr[left1]){
//                    largest1=left1+1;
//                }else if (arr[index]>=arr[left1]&&arr[index]>=arr[left1+1]){
//                    largest
//                }
//
//                if (left1+1<heapSize&&(arr[left1]>arr[index]&&arr[left1]>arr[left1+1])){
//                    largest1=left1;
//                }
//                if (left1+1<heapSize&&(arr[left1+1]>arr[index]&&arr[left1+1]>arr[left1])){
//                    largest1=left1+1;
//                }
//                if ()
//            }

        }
    }

    public static void main(String[] args) {
        int[] a = {4,8,9,4,6,7,9,45,9,49,46,49,9,46,4,9,4};
        heapSort(a);
        printArray(a);
    }
}
