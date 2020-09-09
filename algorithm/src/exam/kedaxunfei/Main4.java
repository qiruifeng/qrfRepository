package exam.kedaxunfei;

import java.util.Scanner;

public class Main4 {


    public static void printNum(int num, int count) {
        while (count < num && num % count != 0) {
            count++;
        }

        if (count < num) {
            System.out.print(count + "*");
            printNum(num/count ,2);
        }else {
            System.out.println(count);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        printNum(m,2);

    }
}
