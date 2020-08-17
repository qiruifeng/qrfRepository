package jianzhioffer;

import java.util.HashSet;

public class IsContinuous {
    public boolean isContinuous(int [] numbers) {
        HashSet<Integer> set=new HashSet<>();
        int max=0;
        int min=16;
        int zeroCount=0;
        for (int i=0;i<numbers.length;i++){
            if (numbers[i]==0){
                zeroCount++;
            }else {
                set.add(numbers[i]);
                if (numbers[i]<min){
                    min=numbers[i];
                }

                if (numbers[i]>max){
                    max=numbers[i];
                }
            }
        }

        if ((max-min)<5&&(5-set.size())==zeroCount){
            return true;
        }else {
            return false;
        }
    }
}
