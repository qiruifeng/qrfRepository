package jianzhioffer;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 */
public class getMiddleNum {

    Queue<Integer> queue = new PriorityQueue<>();

    public void Insert(Integer num) {
        if (num != null) {
            queue.add(num);
        }

    }

    public Double GetMedian() {

        Double ans = 0.0;

        if (queue.size() % 2 == 1) {
            for (int i = 0; i < queue.size() / 2 + 2; i++) {
                int a = queue.poll();
                if (i == queue.size() / 2 + 1) {
                    ans = new Double((double) (a + a) / 2);
                }
            }
        } else {
            int less = 0;
            int more = 0;
            for (int i = 0; i < queue.size() / 2 + 2; i++) {
                int a = queue.poll();

                if (i == queue.size() / 2) {
                    less = a;
                }

                if (i == queue.size() / 2 + 1) {
                    more = a;
                }
            }
            ans = new Double((double) (less + more) / 2);
        }

        return ans;
    }

}
