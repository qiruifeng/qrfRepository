package exam;

import java.util.Scanner;

public class qiAnXin1 {

    public static int jump(int n){
        if (n<1||n>36){
            return 0;
        }

        if (n==1){
            return 1;
        }

        if (n==2){
            return 2;
        }

        return jump(n-2)+jump(n-1);
    }


    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();

        System.out.println(jump(n));

    }
}
