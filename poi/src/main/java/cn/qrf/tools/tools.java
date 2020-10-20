package cn.qrf.tools;



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
     * 打印一维数组
     * @param arr
     */
    public static <T> void printArr(T[] arr){
        for (int i=0;i<arr.length;i++){
            System.out.print(arr[i]+" ");
        }
        System.out.println();
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

    /**
     * 打印二维数组
     * @param matrix
     */
    public static <T> void printMatrix(T[][] matrix){
        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix[0].length;j++){
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println();
        }

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

}
