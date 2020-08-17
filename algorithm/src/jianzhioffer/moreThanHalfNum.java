package jianzhioffer;

import java.util.HashMap;

import java.util.Map;

/**
 * 数组中有一个数字出现的次数超过数组长度的一半，
 * 请找出这个数字。例如输入一个长度为9的数组{1,2,3,2,2,2,5,4,2}。
 * 由于数字2在数组中出现了5次，超过数组长度的一半，因此输出2。如果不存在则输出0。
 */
public class moreThanHalfNum {
    public static int MoreThanHalfNum_Solution(int [] array) {

        int res=0;

        if (array.length==0){
            return 0;
        }

        HashMap<Integer,Integer> help=new HashMap();

        for (int i=0;i<array.length;i++){
            if (!help.containsKey(array[i])){
                help.put(array[i],1);
            }else {
                help.put(array[i],help.get(array[i])+1);
            }
        }

        for (Map.Entry<Integer,Integer> entry: help.entrySet()){
            if (entry.getValue()>array.length/2){
                res=entry.getKey();
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] a={1,1,1,4};
        System.out.println(MoreThanHalfNum_Solution(a));
    }
}
