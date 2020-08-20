package jianzhioffer;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 */
public class getMiddleNum {

    Queue<Integer> big = new PriorityQueue<>();//小顶堆，第一个弹出来的数最小。存的都是大的那一半数
    Queue<Integer> small=new PriorityQueue<>(new Comparator<Integer>() {//大顶堆，第一个弹出来的数最大，存的都是小的那一半数
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2-o1;
        }
    });
    int count=0;


    public void Insert(Integer num) {
        count++;
        if ((count&1)==1){
            if (!small.isEmpty()&&num<small.peek()){
                small.offer(num);
                num=small.poll();
            }
            big.offer(num);
        }else {
            if (!big.isEmpty()&&num>big.peek()){
                big.offer(num);
                num=big.poll();
            }
            small.offer(num);
        }
    }

    public Double GetMedian() {
        Double ans = 0.0;
        if ((count&1)==1){
            ans=big.size()>small.size()?(double)big.peek():(double)small.peek();
        }else {
            ans=(double)0.5*(big.peek()+small.peek());
        }

        return ans;
    }

    public static void main(String[] args) {

    }

}
