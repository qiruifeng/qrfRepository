package leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 给出 n 代表生成括号的对数，请你写出一个函数，使其能够生成所有可能的并且有效的括号组合。
 * 例如，给出 n = 3，生成结果为：
 * [
 * "((()))",
 * "(()())",
 * "(())()",
 * "()(())",
 * "()()()"
 * ]
 */
public class generateParenthesis {

    //暴力递归
    public static List<String> generateParenthesis(int n){
        if (n<1){
            return Collections.emptyList();
        }
        List<String> ans=new ArrayList<>();
        generateAll("(",2*n,ans);
        return ans;
    }

    //递归
    public static void generateAll(String str, int max, List<String> result) {

        //stop condition
        if (str.length() == max) {
            if (isValid.isValid(str)){
                result.add(str);
            }
            return;
        }

        String l=str+"(";
        String r=str+")";

        generateAll(l,max,result);
        generateAll(r,max,result);

    }

    //回溯，在找节点中途中有不符合条件的直接去除
    public static List<String> generateParenthesis1(int n){
        if (n<1){
            return Collections.emptyList();
        }

        List<String> ans=new ArrayList<>();

        backtrackGenerate(ans,"(",1,0,n);
        return ans;
    }

    /**
     *
     * @param result 最终结果链表
     * @param strNow 当前的字符串
     * @param left   当前字符串包含"("的个数
     * @param right  当前字符串包含"("的个数
     * @param max    括号对的个数
     */
    public static void backtrackGenerate(List<String > result,String strNow,int left,int right,int max){
        //终止条件你
        if (strNow.length()==2*max){
            result.add(strNow);
            return;
        }

        //回溯子问题的条件，当"("不到最大时就加"("
        //当"("没有足够的")"匹配时，就加")"匹配
        if (left<max){
            backtrackGenerate(result,strNow+"(",left+1,right,max);
        }
        if (left>right){
            backtrackGenerate(result,strNow+")",left,right+1,max);
        }
    }

    public static void main(String[] args) {
//        generateAll(3);
        System.out.println(generateParenthesis1(3));
    }
}
