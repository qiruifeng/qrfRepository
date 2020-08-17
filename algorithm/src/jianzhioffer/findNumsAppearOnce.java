package jianzhioffer;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 一个整型数组里除了两个数字之外，其他的数字都出现了两次。请写程序找出这两个只出现一次的数字。
 * num1,num2分别为长度为1的数组。传出参数
 * 将num1[0],num2[0]设置为返回结果
 */
public class findNumsAppearOnce {
    public void FindNumsAppearOnce(int [] array,int num1[] , int num2[]) {
        HashSet<Integer> help=new HashSet<>();
        int[] nums=new int[2];
        for (int i=0;i<array.length;i++){
            if (!help.contains(array[i])){
                help.add(array[i]);
            }else {
                help.remove(array[i]);
            }
        }
        ArrayList<Integer> list=new ArrayList<>(help);
        num1[0]=list.get(0);
        num2[0]=list.get(1);
    }
}
