package jianzhioffer;

import java.util.ArrayList;
import java.util.Arrays;

public class getLeastNumbers {
    public static ArrayList<Integer> GetLeastNumbers_Solution(int[] input, int k) {



        ArrayList<Integer> ans = new ArrayList<>();

        if (k>input.length){
            return ans;
        }
        Arrays.sort(input);

        for (int i = 0; i < k; i++) {
            ans.add(input[i]);
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] test={4,5,1,6,2,7,3,8};
        GetLeastNumbers_Solution(test,4);
    }
}
