package leetcode;

public class isPalindrome {
    public static boolean isPalindrome(int x) {
        Integer res = x;
        String a = res.toString();
        for (int i = 0; i < a.length() / 2; i++) {
            if (a.charAt(i) != a.charAt(a.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println(isPalindrome(10));
    }
}
