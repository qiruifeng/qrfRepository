package leetcode;

import utils.ListNode;

import static utils.tools.printList;

/**
 * 给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
 * k 是一个正整数，它的值小于或等于链表的长度。
 * 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 */
public class reverseKGroup {

    public static ListNode reverseKGroup(ListNode head, int k){
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode pre = dummy;
        ListNode end = dummy;

        while (end.next != null) {
            for (int i = 0; i < k && end != null; i++) end = end.next;
            if (end == null) break;
            ListNode start = pre.next;
            ListNode next = end.next;
            end.next = null;
            pre.next = reverse(start);
            start.next = next;
            pre = start;

            end = pre;
        }
        return dummy.next;
    }

    /**
     * 流程：先把当前节点的后继节点用一个节点next记录下来，然后让当前节点指向自己的前驱pre；
     * 然后把前驱赋值为当前节点，最后把当前节点跳到next节点
     * 整个过程就是先记录当前节点的下一个节点，然后把当前节点指向反向的过程
     * @param head
     * @return
     */
    public static ListNode reverse(ListNode head) {
        ListNode pre = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = pre;
            pre = curr;
            curr = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        ListNode l1=new ListNode(1);
        l1.next=new ListNode(2);
        l1.next.next=new ListNode(3);
        l1.next.next.next=new ListNode(4);
        l1.next.next.next.next=new ListNode(5);
        l1.next.next.next.next.next=new ListNode(6);
        l1.next.next.next.next.next.next=new ListNode(7);
        l1.next.next.next.next.next.next.next=new ListNode(8);
        l1.next.next.next.next.next.next.next.next=new ListNode(9);
        l1.next.next.next.next.next.next.next.next.next=new ListNode(10);
        printList(reverseKGroup(l1,4));
    }

}
