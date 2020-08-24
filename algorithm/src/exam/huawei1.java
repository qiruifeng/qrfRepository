package exam;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import static utils.tools.printMatrix;

public class huawei1 {


    static int count = 0;

    public static int[][] baoshu(int ah, int al, int bh, int bl) {
        int[][] matrix = new int[bh-ah+1][bl-al+1];

        if (ah == bh) {
            for (int i = al; i <= bl; i++) {
                count++;
                matrix[ah][i]=count;
            }
        } else if (al == bl) {
            for (int i = ah; i <= bh; i++) {
                count++;
                matrix[i][al]=count;
            }
        } else {
            for (int i = al; i < bl; i++) {
                count++;
                matrix[ah][i]=count;
            }
            for (int i = ah; i < bh; i++) {
                count++;
                matrix[i][bl]=count;
                //System.out.print(matrix[i][bl]+" ");
            }
            for (int i = bl; i > al; i--) {
                count++;
                matrix[bh][i]=count;
            }
            for (int i = bh; i > ah; i--) {
                count++;
                matrix[i][al]=count;
            }
        }
        return matrix;
    }


    public static ArrayList<Integer[]> res(int[][] matrix){

        ArrayList<Integer[]> ans=new ArrayList<>();

        for (int i=0;i<matrix.length;i++){
            for (int j=0;j<matrix[0].length;j++){
                if ((matrix[i][j]%10==7)&&((matrix[i][j]&1)==1)&&(matrix[i][j]/10>0)){
                    Integer[] a=new Integer[2];
                    a[0]=i;
                    a[1]=j;
                    ans.add(a);
                }
            }
        }

        return ans;
    }

    public static List<Integer[]> print(int m,int n){
        int le=0;
        int ri=n-1;
        int up=0;
        int dow=m-1;

        int num=1;

        ArrayList<Integer[]> ans=new ArrayList<>();
        while (le<=ri&&up<=dow){
            if (le==ri){
                for (int i=up;i<=dow;i++){
                    if (judge(num)){
                        ans.add(new Integer[]{i,le});
                    }
                    num++;
                }
                break;
            }else if (up==dow){
                for (int i=le;i<ri;i++){
                    if (judge(num)) ans.add(new Integer[]{up,i});
                    num++;
                }

                for (int i=up;i<dow;i++){
                    if (judge(num)) ans.add(new Integer[]{i,ri});
                    num++;
                }
                for (int i=ri;i>le;i--){
                    if (judge(num)) ans.add(new Integer[]{dow,i});
                    num++;
                }

                for (int i=dow;i>up;i--){
                    if (judge(num)) ans.add(new Integer[]{i,le});
                    num++;
                }
                le++;
                ri--;
                up++;
                dow--;
            }
        }
        return ans;
    }

    public static boolean judge(int num){
        int ge = num % 10;
        int temp = num / 10;
        Integer shi = temp % 10;
        if (ge == 7 && shi % 2 == 1) return true;
        else return false;
    }

    public static void main(String[] args) {
        int[][] matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        int[][] matrix1 = {{1, 2, 3, 4, 5, 6, 7, 8, 9}, {10, 11, 12, 13, 14, 15, 16, 17, 18},{1,1,1,1,1,1,}};
        //spiralOrderPrint(matrix1);

        int[][] test=baoshu(0,0,1,8);

        ArrayList<Integer[]> answer=new ArrayList<>();
        answer=res(test);

        System.out.println(answer);

//        Scanner scanner=new Scanner(System.in);
//        int m=scanner.nextInt();
//        int n=scanner.nextInt();
//
//        //printMatrix(test);

    }

}


