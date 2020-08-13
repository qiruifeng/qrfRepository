package leetcode;

import java.util.HashMap;
import java.util.Stack;

/**
 * 判断一个字符串是不是标准括号
 */
public class isValid {
    public static boolean isValid(String s){

        if (s.isEmpty()){
            return true;
        }

        HashMap<Character,Character> map=new HashMap<>();
        map.put(')','(');
        map.put('}','{');
        map.put(']','[');

        Stack<Character> stack= new Stack<>();

        for (int i=0;i<s.length();i++){
            char tmp=s.charAt(i);
            if (map.containsKey(tmp)){
                char topChar=stack.isEmpty()?'*':stack.pop();
                if (topChar!=map.get(tmp)){
                    return false;
                }
            }else {
                stack.push(tmp);
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        String test="({[]}){}[]";
        System.out.println(isValid(test));
    }
}
