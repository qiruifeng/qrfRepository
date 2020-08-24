package exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class zijiefeishu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();

        int start = 1;
        int end = n;
        List<Integer> nums = new ArrayList<>();
        int count = 0;
        while (end - start >= 2) {
            start = (start + end) / 2;
            if (count < k) {
                start = (int) Math.ceil((start + end) / 2.0);
            }
            nums.add(start);
            count++;
        }

        int sum = 0;
        for (int i = k; i < nums.size(); i++) {
            sum += nums.get(i);
        }
        System.out.println(sum);
    }

}
