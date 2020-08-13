package leetcode;

public class longestCommonPrefix {
    //先把数组第一个定位前缀，只要跟后面的字符串没有在第一个位置出现相同串就把前缀减去最后一个字符

    /**
     * longestCommonPrefix
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
        String pre = strs[0];
        if (strs.length==0){
            return "";
        }
        for (int i=1;i<strs.length;i++){
            while (strs[i].indexOf(pre)!=0){
                pre=pre.substring(0,pre.length()-1);
                if (pre.length()==0){
                    return "";
                }
            }
        }
        return pre;
    }

    public static void main(String[] args) {
        String[] test = {"qwert", "qwenyt", "qwejhrt"};
        String res = longestCommonPrefix(test);
        System.out.println(res);
    }
}
