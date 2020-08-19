package jianzhioffer;

import java.util.HashMap;

/**
 * 请实现一个函数用来找出字符流中第一个只出现一次的字符。例如，当从字符流中只读出前两个字符"go"时，
 * 第一个只出现一次的字符是"g"。当从该字符流中读出前六个字符“google"时，第一个只出现一次的字符是"l"。
 */
public class firstOneChar {

    StringBuilder input = new StringBuilder("");
    HashMap<Character, Integer> map = new HashMap<>();


    //Insert one char from stringstream
    public void Insert(char ch) {
        if (!map.containsKey(ch)) {
            map.put(ch, 1);
        } else {
            map.put(ch, map.get(ch) + 1);
        }

        input.append(ch);
    }

    //return the first appearence once char in current stringstream
    public char FirstAppearingOnce() {
        for (int i = 0; i < input.length(); i++) {
            if (map.get(input.charAt(i)) == 1) {
                return input.charAt(i);
            }
        }

        return '#';
    }
}
