package leetcode;

import utils.ListNode;

import static utils.tools.printList;

public class removeNthFromEnd {


    public static ListNode removeNthFromEnd(ListNode head, int n) {
          ListNode pre =new ListNode(0);
          pre.next=head;
          ListNode fast=pre;
          ListNode slow=pre;
          int i=0;

          while (fast!=null){
              fast=fast.next;
              i++;
              if (i>n+1){
                  slow=slow.next;
              }
//              if (i==n+1){
//                  return head.next;
//              }
          }
          slow.next=slow.next.next;
          return pre.next;

    }

    public static void main(String[] args) {
        ListNode head=new ListNode(1);
 //       head.next=new ListNode(2);
//        head.next.next=new ListNode(3);
//        head.next.next.next=new ListNode(4);
//        head.next.next.next.next=new ListNode(5);


        printList(removeNthFromEnd(head,1));
    }
}
