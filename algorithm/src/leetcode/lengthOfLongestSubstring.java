package leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 */
public class lengthOfLongestSubstring {


    public static int lengthOfLongestSubstring(String s) {
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, i = 0, j = 0;
        while (i < n && j < n) {
            // try to extend the range [i, j]
            if (!set.contains(s.charAt(j))){
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            }
            else {
                set.remove(s.charAt(i++));
            }
        }
        return ans;
    }

    /**
     * 哈希表天然合适
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring1(String s) {
        char[] chars = s.toCharArray();
        int max = 0;
        int start;
        int end = 1;
        boolean b = false;

        if (chars.length == 1) {
            return 1;
        }

        for (start = 0; start < chars.length - 1; start++) {
            for (int i = start + 1; i < chars.length; i++) {
                if (chars[start] - chars[i] == 0) {
                    end = i;
                    b = true;
                    break;
                }

            }
            max = Math.max(max, end - start);
        }
        if (b == false) {
            max = s.length();
        }
        return max;

    }

    public static int lengthOfLongestSubstring2(String s){
        HashSet set=new HashSet<>();
        int result=0,i=0,j=0;
        while (i<s.length()&&j<s.length()){
            if (!set.contains(s.charAt(j))) {
                set.add(s.charAt(j++));
                result=Math.max(result,j-i);
            }else {
                set.remove(s.charAt(i++));
            }
        }
        return result;

    }

    public static void main(String[] args) {
        String test = "qweqrtw";
        System.out.println(lengthOfLongestSubstring2(test));
    }
}
