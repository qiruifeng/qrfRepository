package utils;

public class tools {
    /**
     * 随机生成一维数组
     * @param n
     * @return
     */
    public static int[] generateRandomArrays(int n){
        if (n<=0){
            return null;
        }
        int[] arr=new int[n];
        for (int i=0;i<n;i++){
            arr[i]=(int)(Math.random()*10);
        }
        return arr;
    }

    /**
     * 打印数组
     * @param arr
     */
    public static void printArr(int[] arr){
        for (int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }

    /**
     * 打印字符串
     * @param arr
     */
    public static void printStr(String[] arr){
        for (int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
    }

    /**
     * 打印链表
     * @param head
     */
    public static void printList(ListNode head){
        ListNode cur=head;
        while (cur!=null){
            System.out.print(cur.val+" ");
            cur=cur.next;
        }
        System.out.println();
    }
}
