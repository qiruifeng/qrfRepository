package leetcode;

public class longestPalindrome {
    public static String longestPalindrome(String s) {

        if (s.length()==0){
            return "";
        }

        if(s.length()==1){
            return s;
        }

        int start = 0, end = 0;
        int maxRadius = 0;

        char[] help = new char[2 * s.length() - 1];

        for (int i = 0; i < s.length(); i++) {
            help[2 * i] = s.charAt(i);
            if (i < s.length() - 1) {
                help[2 * i + 1] = '1';
            }
        }

        for (int n = 1; n < help.length - 1; n++) {
            int radius = 1;

            while (n - radius >= 0 && n + radius < help.length && help[n - radius] == help[n + radius]) {
                if (radius > maxRadius && (n - radius) % 2 == 0) {
                    maxRadius = radius;
                    start = (n - radius) / 2;
                    end = (n + radius) / 2 + 1;
                }
                radius++;
            }

        }

        if (maxRadius == 0) {
            return s.substring(0, 1);
        }

        return s.substring(start, end);
    }

    public static boolean isPalindromic(String s) {
        int len = s.length();
        for (int i = 0; i < len / 2; i++) {
            if (s.charAt(i) != s.charAt(len - i - 1)) {
                return false;
            }
        }
        return true;
    }

    // 暴力解法
    public static String longestPalindrome1(String s) {
        String ans = "";
        int max = 0;
        int len = s.length();
        for (int i = 0; i < len; i++)
            for (int j = i + 1; j <= len; j++) {
                String test = s.substring(i, j);
                if (isPalindromic(test) && test.length() > max) {
                    ans = s.substring(i, j);
                    max = Math.max(max, ans.length());
                }
            }
        return ans;
    }



    public static void main(String[] args) {
        String test = "asd";
        System.out.println(longestPalindrome(test));
    }
}
