package exam.kedaxunfei;

import java.util.Arrays;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String m = scanner.nextLine();
        int n=Integer.valueOf(m);
        String string=scanner.nextLine();
        String[] strings=string.split(",");
        int[] arr=new int[n];


        for (int i = 0; i < n; i++) {
            arr[i] = Integer.valueOf(strings[i]);
        }

        Arrays.sort(arr);

        for (int i = 0; i < n - 1; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println(arr[n - 1]);

    }
}
