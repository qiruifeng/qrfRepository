package leetcode;

public class findMedianSortedArrays {
    public static double findMedianSortedArrays(int[] arr1, int[] arr2) {
        int a = arr1.length;
        int b = arr2.length;
        int left = (a + b + 1) / 2;
        int right = (a + b + 2) / 2;
        return (KthSmall(arr1, 0, a - 1, arr2, 0, b - 1, left) + KthSmall(arr1, 0, a - 1, arr2, 0, b - 1, right)) * 0.5;
    }

    public static double KthSmall(int[] arr1, int start1, int end1, int[] arr2, int start2, int end2, int k) {
        int len1 = end1 - start1 + 1;
        int len2 = end2 - start2 + 1;
        //确保第一个数组是短的那一个，这样不用分情况了
        if (len1>len2){
            return KthSmall(arr2,start2,end2,arr1,start1,end1,k);
        }

        //先写边界条件
        if (len1==0){//第一个数组减为长度减为0，直接就是第二个数组的第k个数了
            return arr2[start2+k-1];
        }
        if (k==1){//如果k已经到1了，两个数组长度还没有减为0，那么就是两个数组第一个数小的那个
            return Math.min(arr1[start1],arr2[start2]);
        }

        //如果上面两个都没中，就是继续往下迭代,i和j分别是arr1和arr2舍去的
        int i=start1+Math.min(k/2,len1)-1;//每次去掉k/2个数，这里防止数组长度比k/2小，用min函数取个最小的
        int j=start2+Math.min(k/2,len2)-1;

        if (arr1[i]>arr2[j]){
            return KthSmall(arr1,start1,end1,arr2,j+1,end2,k-(j-start2+1));
        }else {
            return KthSmall(arr1,i+1,end1,arr2,start2,end2,k-(i-start1+1));
        }

    }

    public static void main(String[] args) {
        int[] a={1,3};
        int[] b={2};


        System.out.println(findMedianSortedArrays(a,b));
    }
}
