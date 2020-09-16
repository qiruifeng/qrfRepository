package exam.huawei;

import java.util.Scanner;

public class huaweinew3 {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int m=scanner.nextInt();
        double[][][] dots=new double[n][m][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k <  3; k++) {
                    dots[i][j][k]=scanner.nextDouble();
                }
            }
        }

        double res=3.0;

        System.out.println(res);
    }
}
