package exam.xiaomi;

import java.util.HashSet;
import java.util.Scanner;

public class xiaomi1 {




    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        String str=scanner.nextLine();
        HashSet<Character> set=new HashSet<>();
        StringBuilder ans=new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if (!set.contains(str.charAt(i))){
                set.add(str.charAt(i));
                ans.append(str.charAt(i));
            }
        }

        System.out.println(ans);

    }
}
