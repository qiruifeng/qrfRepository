package lessonFromZuo.basic_class_05;


import static utils.tools.generateRandomMatrix;

public class minPath {

    public static int matrixMinPath(int[][] matrix) {
        return process(matrix, 0, 0);
    }

    public static int process(int[][] matrix, int i, int j) {
        if (i == matrix.length - 1 && j == matrix[0].length - 1) {
            return matrix[i][j];
        }
        if (i == matrix.length - 1) {
            return matrix[i][j] + process(matrix, i, j + 1);
        }
        if (j == matrix[0].length - 1) {
            return matrix[i][j] + process(matrix, i + 1, j);
        }
        return matrix[i][j] + Math.min(process(matrix, i + 1, j), process(matrix, i, j + 1));
    }

    public static int matrixMinPathDp(int[][] m) {
        int row = m.length;
        int col = m[0].length;
        int dp[][] = new int[row][col];

        dp[0][0] = m[0][0];

        if (m == null) {
            return 0;
        }
        for (int i = 0; i < m.length - 1; i++) {
            dp[i + 1][0] = dp[i][0] + m[i + 1][0];
        }
        for (int j = 0; j < m[0].length - 1; j++) {
            dp[0][j + 1] = dp[0][j] + m[0][j + 1];
        }
        for (int i = 1; i < m.length; i++) {
            for (int j = 1; j < m[0].length; j++) {
                dp[i][j] = m[i][j] + Math.min(dp[i][j - 1], dp[i - 1][j]);
            }
        }

        return dp[row - 1][col - 1];

    }


    public static void main(String[] args) {
        int[][] m = generateRandomMatrix(1000, 1000);
        Long startTs1 = System.currentTimeMillis();
        System.out.println(matrixMinPathDp(m));
        Long endTs1 = System.currentTimeMillis();
        System.out.println(startTs1);
        System.out.println(endTs1);
        System.out.println("计算时间为： "+(endTs1-startTs1)+"ms");

//        Long startTs2 = System.currentTimeMillis();
//        System.out.println(matrixMinPathDp(m));
//        Long endTs2 = System.currentTimeMillis();
////        System.out.println(startTs2);
////        System.out.println(endTs2);
//        System.out.println("计算时间为： "+(endTs2-startTs2)+"ms");


    }
}
