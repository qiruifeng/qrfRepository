package leetcode;

import utils.ListNode;

import static utils.tools.printList;


/**
 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 */
public class swapPairs {
    public static ListNode swapPairs(ListNode head){

        ListNode dump=new ListNode(0);
        dump.next=head;

        ListNode temp=dump;

        while (temp.next!=null&&temp.next.next!=null){
            ListNode start=temp.next;
            ListNode end=temp.next.next;
            temp.next=end;
            start.next=end.next;
            end.next=start;


            temp=start;
        }

        return dump.next;

    }

    public static ListNode swapPairs1(ListNode head){

        if(head == null || head.next == null){
            return head;
        }

        ListNode pre=new ListNode(0);
        pre.next=head;
        ListNode temp=head.next;

        pre.next=temp;
        head.next=swapPairs1(temp.next);

        temp.next=head;
        return pre.next;

    }

    public static void main(String[] args) {
        ListNode l1=new ListNode(1);
        l1.next=new ListNode(2);
        l1.next.next=new ListNode(3);
        //printList(swapPairs(l1));
        printList(swapPairs1(l1));
    }
}
