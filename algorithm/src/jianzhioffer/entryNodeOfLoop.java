package jianzhioffer;

import utils.ListNode;

/**
 * 给一个链表，若其中包含环，请找出该链表的环的入口结点，否则，输出null。
 * 方法：
 * （1）利用hashmap，看第一个重复的节点即为第一个入环节点
 * （2）不利用额外空间，建立两个指针，快指针一次走两步，慢指针一次走一步，如果有环，那么快慢指针一定会在环上相遇。
 * 相遇的时刻，快指针回到开头，然后快指针由一次走两步变为一次走一步。快指针和慢指针一定会在第一个入环节点相遇。
 * （结论，记住即可）
 */
public class entryNodeOfLoop {
    public ListNode EntryNodeOfLoop(ListNode pHead) {
        if (pHead==null||pHead.next==null||pHead.next.next==null){
            return null;
        }

        ListNode fast=pHead.next.next;
        ListNode slow=pHead.next;

        while (fast!=slow){
            fast=fast.next.next;
            slow=slow.next;
        }

        fast=pHead;
        while (fast!=slow){
            fast=fast.next;
            slow=slow.next;
        }

        return fast;
    }
}
