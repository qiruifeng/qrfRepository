package leetcode;

import utils.ListNode;

import static utils.tools.printList;

/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头

 */
public class addTwoNumbers {
    /**
     * 这种方法会有数溢出的问题，当数字过大的时候（比如超过Long.MAX_VALUE时），计算结果就会出错
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode addTwoNumbers01(ListNode l1,ListNode l2){
        long a=0;
        long b=0;
        int i=0;
        ListNode cur1 = l1;
        ListNode cur2=l2;

        while (cur1!=null){

            a= a+cur1.val*((long) Math.pow(10,i++));
            cur1=cur1.next;

        }
        i=0;
        while (cur2!=null){

            b= b+cur2.val*((long) Math.pow(10,i++));
            cur2=cur2.next;
        }

        Long res=a+b;

        String str=res.toString();
        char[] chars=str.toCharArray();

        ListNode result=new ListNode(chars[chars.length-1]-'0');
        if (chars.length<2){
            return result;
        }
        ListNode cur=result;
        for (int j=chars.length-2;j>=0;j--){

            cur.next=new ListNode(chars[j]-'0');
            cur=cur.next;
        }
        return result;
    }

    public static ListNode addTwoNumbers(ListNode l1,ListNode l2){
        ListNode result=new ListNode(0);
        ListNode cur=result;
        int carry=0;

        while (l1!=null||l2!=null){
            int x=(l1==null)?0:l1.val;
            int y=(l2==null)?0:l2.val;
            int sum=x+y+carry;
            carry = sum / 10;
            sum=sum%10;

            cur.next=new ListNode(sum);
            cur=cur.next;
            if (l1!=null){
                l1=l1.next;
            }
            if (l2 != null) {
                l2=l2.next;
            }

        }

        //遍历结束之后
        if (carry==1){
            cur.next=new ListNode(1);
        }

        return result.next;



    }

    public static void main(String[] args) {
        ListNode head=new ListNode(9);
//        head.next=new ListNode(2);
//        head.next.next=new ListNode(3);

        printList(head);

        ListNode head2=new ListNode(1);
        head2.next=new ListNode(9);
        head2.next.next=new ListNode(9);
        head2.next.next.next=new ListNode(9);
        head2.next.next.next.next=new ListNode(9);
        head2.next.next.next.next.next=new ListNode(9);
        head2.next.next.next.next.next.next=new ListNode(9);
        head2.next.next.next.next.next.next.next=new ListNode(9);
        head2.next.next.next.next.next.next.next.next=new ListNode(9);
        head2.next.next.next.next.next.next.next.next.next=new ListNode(9);

        printList(head2);

        printList(addTwoNumbers(head,head2));

    }
}
