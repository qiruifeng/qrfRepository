package exam.kedaxunfei;

import java.util.Scanner;

public class Main1 {

    public static int dpMaxGift(int[][] m){
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
                dp[i][j] = m[i][j] + Math.max(dp[i][j - 1], dp[i - 1][j]);
            }
        }

        return dp[row - 1][col - 1];

    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        int m = scanner.nextInt();
//        int n = scanner.nextInt();
        String a=scanner.nextLine();
        String[] b=a.split(",");
        int m=Integer.valueOf(b[0]);
        int n=Integer.valueOf(b[1]);


        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = scanner.nextInt();
            }
        }

        int res=dpMaxGift(matrix);
        System.out.println(res);
    }
}
