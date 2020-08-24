package exam;

import java.util.Scanner;

public class yuanfudao {

    public static int getMatrixSum(int[][] matrix, int startHang, int startLie, int endHang, int endLie) {
        int sum=0;

        for (int i=startHang;i<=endHang;i++){
            for (int j=startLie;j<=endLie;j++){
                sum=sum+matrix[i][j];
            }
        }

        return sum;
    }




    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();//N行
        int M = scanner.nextInt();//M列

        int max=Integer.MIN_VALUE;

        int[][] nums = new int[N][2*M-1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                nums[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < N; i++){
            for (int j=M;j<2*M-1;j++){
                nums[i][j]=nums[i][j-M];
            }
        }


        if (N == 1 && M == 1) {
            System.out.println(nums[0][0]);
        }

        for (int i=1;i<=N;i++){//行格子
            for (int j=1;j<=M;j++){//列格子
                for (int i1=0;i1<N-i+1;i1++){
                    for (int j1=0;j1<2*M-j;j1++){
                        int temp=getMatrixSum(nums,i1,j1,i1+i-1,j1+j-1);
                        if (temp>max){
                            max=temp;
                        }
                    }
                }
            }
        }


        System.out.println(max);

    }
}
