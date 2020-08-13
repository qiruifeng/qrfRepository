package leetcode;

/**
 * 实现 strStr() 函数。
 * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
 * <p>
 * 输入: haystack = "hello", needle = "ll"
 * 输出: 2
 * <p>
 * 输入: haystack = "aaaaa", needle = "bba"
 * 输出: -1
 */
public class strStr {
    public static int strStr(String haystack, String needle) {
        return haystack.indexOf(needle);
    }

    public static int strStr1(String haystack, String needle) {

        for (int i = 0; i <= haystack.length() - needle.length(); i++) {
            int j;
            for (j = 0; j < needle.length(); j++) {
                if (haystack.charAt(i+j) != needle.charAt(j)) {
                    break;
                }
            }
            if (j == needle.length()) {
                return i;
            }

        }

        return -1;


    }


    public static void main(String[] args) {
        System.out.println(strStr1("hello", "hello"));
    }
}
