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

    /**
     * 直观打印二叉树
     * @param head
     */
    public static void printTree(Node head) {
        System.out.println("Binary Tree:");
        printInOrder(head, 0, "H", 17);
        System.out.println();
    }

    public static void printInOrder(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printInOrder(head.right, height + 1, "v", len);
        String val = to + head.value + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printInOrder(head.left, height + 1, "^", len);
    }

    public static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    /**
     * 随机生成二维数组
     * @param rowSize
     * @param colSize
     * @return
     */
    public static int[][] generateRandomMatrix(int rowSize, int colSize) {
        if (rowSize < 0 || colSize < 0) {
            return null;
        }
        int[][] result = new int[rowSize][colSize];
        for (int i = 0; i != result.length; i++) {
            for (int j = 0; j != result[0].length; j++) {
                result[i][j] = (int) (Math.random() * 10);
            }
        }
        return result;
    }
}
