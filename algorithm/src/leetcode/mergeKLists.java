package leetcode;

import utils.ListNode;

import java.util.Comparator;
import java.util.PriorityQueue;

import static utils.tools.printList;

/**
 * 将K个有序链表合并成一个
 */
public class mergeKLists {
    public static ListNode mergeKLists(ListNode[] lists) {
        if (lists.length==0){
            return null;
        }
        for (int i = 0; i < lists.length - 1; i++) {
            lists[0] = mergeTwoLists.mergeTwoLists(lists[0], lists[i + 1]);
        }
        return lists[0];
    }

    //优先级队列的方法
    public static ListNode mergeKLists1(ListNode[] lists){
        //特判条件
        if (lists.length==0||lists==null){
            return null;
        }

        //定义一个优先级队列并重写比较方法
        PriorityQueue<ListNode> queue=new PriorityQueue<>(lists.length, new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return (int)(o1.val-o2.val);
            }
        });

        //定义哑节点，方便返回
        ListNode pre=new ListNode(0);
        ListNode cur=pre;

        //遍历lists数组，先将所有的头节点加入优先级队列，用增强for循环
        for (ListNode node:lists){
            if (node!=null){
                queue.add(node);
            }
        }


        //让cur.next指向从优先队列中拿出一个元素，cur移动到此元素，再让cur.next入队列（入队列即比较了）。相当于每次比较k个链表表头的和最小表头的下一个元素
        while (!queue.isEmpty()){
            cur.next=queue.poll();
            cur=cur.next;
            if (cur.next!=null){
                queue.add(cur.next);
            }
        }

        return pre.next;


    }

    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(5);

        ListNode l2 = new ListNode(1);
        l2.next = new ListNode(3);
        l2.next.next = new ListNode(4);

        ListNode l3 = new ListNode(2);
        l3.next = new ListNode(6);

        ListNode[] test = {l1, l2, l3};
        printList(mergeKLists1(test));

    }


}
