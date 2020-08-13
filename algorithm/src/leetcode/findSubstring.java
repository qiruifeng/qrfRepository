package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 给定一个字符串 s 和一些长度相同的单词 words。找出 s 中恰好可以由 words 中所有单词串联形成的子串的起始位置。
 * 注意子串要与 words 中的单词完全匹配，中间不能有其他字符，但不需要考虑 words 中单词串联的顺序。
 *
 * 输入：
 *   s = "barfoothefoobarman",
 *   words = ["foo","bar"]
 * 输出：[0,9]
 * 解释：
 * 从索引 0 和 9 开始的子串分别是 "barfoo" 和 "foobar" 。
 * 输出的顺序不重要, [9,0] 也是有效答案。
 *
 * 输入：
 *   s = "wordgoodgoodgoodbestword",
 *   words = ["word","good","best","word"]
 * 输出：[]
 */
public class findSubstring {
    public static List<Integer> findSubstring1(String s, String[] words) {
        List<Integer> result=new ArrayList<>();
        if (s==null||words==null||words.length==0)return result;
        int wordsNum = words.length,wordLen=words[0].length();
        //将words中的单词及其数量存入hashmap
        HashMap<String,Integer> allWords=new HashMap<>();
        for (String word : words) {
            Integer value = allWords.getOrDefault(word, 0);
            allWords.put(word,value+1);
        }
        //分成wordLen中情况，分别从0开始每次移动一个单词长度~从wordLen-1开始每次移动一个单词长度
        for (int j=0;j<wordLen;j++){
            //haswords存放当前子串中匹配的单词及其个数，count当前子串匹配的单词数量
            HashMap<String,Integer> haswords=new HashMap<>();
            int count=0;
            //遍历从j开始的每个子串，每次动一个单词长度
            for (int i=j;i<s.length()-wordLen*wordsNum+1;i+=wordLen){
                //防止情况三出现之后，情况一继续移除
                boolean hasRemoved=false;
                while (count<wordsNum){
                    String curWord = s.substring(i + count * wordLen, i + (count + 1) * wordLen);
                    //当前单词匹配，加入haswords
                    if (allWords.containsKey(curWord)) {
                        Integer value = haswords.getOrDefault(curWord, 0);
                        haswords.put(curWord,value+1);
                        count++;
                        //情况三，当前单词匹配，但是数量超了
                        if (haswords.get(curWord) > allWords.get(curWord)) {
                            hasRemoved=true;
                            //从i开始逐个单词，从haswords中移除，removeNum记录移除的单词个数
                            int removeNum=0;
                            while (haswords.get(curWord) > allWords.get(curWord)) {
                                String fristWord = s.substring(i + removeNum * wordLen, i + (removeNum + 1) * wordLen);
                                Integer v = haswords.get(fristWord);
                                haswords.put(fristWord,v-1);
                                removeNum++;
                            }
                            //移除完毕之后，更新count
                            count-=removeNum;
                            //移动i的位置(注意removeNum要-1，因为跳出当前循环之后，i还要+wordLen)
                            i+=(removeNum-1)*wordLen;
                            break;
                        }
                    }else{//情况二，当前单词不匹配
                        //清空haswords
                        haswords.clear();
                        //i移动到当前单词位置(因为跳出当前循环之后，i还要+wordLen)
                        i+=count*wordLen;
                        count=0;
                        break;
                    }
                }
                //情况一，匹配成功
                if (count==wordsNum)result.add(i);
                //如果情况三没有出现
                if (count>0&&!hasRemoved){
                    //移除成功匹配子串的第一个元素
                    String fristWord = s.substring(i, i + wordLen);
                    Integer v = haswords.get(fristWord);
                    haswords.put(fristWord,v-1);
                    count--;
                }
            }
        }
        return result;
    }


    public static List<Integer> findSubstring(String s, String[] words){
        //1. 把所有的子串和出现次数存在一个哈希表中
        List<Integer> ans=new ArrayList<>();

        //特判
        if (words==null||words.length==0||s==null||s.length()==0){
            return ans;
        }

        //字符串个数和长度
        int wordNum=words.length;
        int wordLen=words[0].length();


        HashMap<String ,Integer> allWordsAndCount=new HashMap<>();
        for (String word:words) {
            Integer value = allWordsAndCount.getOrDefault(word, 0);//有就拿到他的值，没有就返回默认。第一次肯定是没有；之后再加入如果有就增加计数，没有就新增键值对
            allWordsAndCount.put(word, ++value);
        }



        //2. 开始循环，每次遍历子串个数，记录是否出现子串和出现的次数
        for(int i=0;i<s.length()-wordLen*wordNum+1;i++){

            //循环前准备工作，定义计数，判断有几个在words里，定义一个哈希表，用来存放出现过的词和次数
            int count=0;//记录包含的单词数
            HashMap<String ,Integer> hasWord = new HashMap<>();

            while (count<wordNum){
                String tempWord=s.substring(i+count*wordLen,i+count*wordLen+wordLen);
                //判断当前词是否存在，如果存在，看他出现了几次，如果出现的次数超过了在words中出现的次数，跳出循环
                if (!allWordsAndCount.containsKey(tempWord)){
                    break;
                }else {
                    Integer value=hasWord.getOrDefault(tempWord,0);
                    hasWord.put(tempWord,++value);
                    if (hasWord.get(tempWord)>allWordsAndCount.get(tempWord))break;
                }
                count++;
            }
            if (count==wordNum){
                ans.add(i);
            }
        }
        return ans;
    }

    public static void main(String[] args) {

        String[] strs={"foo","bar"};

        System.out.println(findSubstring("barfoothefoobarman",strs));
    }
}
