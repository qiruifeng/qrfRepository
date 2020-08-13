package lessonFromZuo.basic_class_01;

public class PrintMatrixSpiralOrder {

    public static void printMatrixBordering(int[][] matrix, int ah, int al, int bh, int bl) {//Ah左边顶点行，Bh左边顶点列
        if (ah == bh) {
            for (int i = al; i <= bl; i++) {
                System.out.print(matrix[ah][i]+" ");
            }
        } else if (al == bl) {
            for (int i = ah; i <= bh; i++) {
                System.out.print(matrix[i][al]+" ");
            }
        } else {
            for (int i = al; i < bl; i++) {
                System.out.print(matrix[ah][i]+" ");
            }
            for (int i = ah; i < bh; i++) {
                System.out.print(matrix[i][bl]+" ");
            }
            for (int i = bl; i > al; i--) {
                System.out.print(matrix[bh][i]+" ");
            }
            for (int i = bh; i > ah; i--) {
                System.out.print(matrix[i][al]+" ");
            }
        }
    }

    public static void spiralOrderPrint(int[][] matrix) {
        int ah = 0;
        int al = 0;
        int bh = matrix.length - 1;
        int bl = matrix[0].length - 1;
        while (ah <= bh && al <= bl) {
            printMatrixBordering(matrix, ah++, al++, bh--, bl--);
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3, 4}, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }};
        spiralOrderPrint(matrix);

    }

}
