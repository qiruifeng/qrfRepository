package lessonFromZuo.basic_class_01;

/**
 * 两个单链表相交的一系列问题
 */
public class findFirstIntersectNode {
    public static class Node {
        public int value;
        public Node next;

        public Node(int data) {
            this.value = data;
        }
    }

    public static Node getIntersectNode(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        Node loop1 = getLoopNode(head1);
        Node loop2 = getLoopNode(head2);
        if (loop1 == null && loop2 == null) {
            return noLoopFirstIntersectNode(head1, head2);
        }

        if (loop1 != null && loop2 != null) {
            return bothLoopNode(head1, loop1, head2, loop2);
        }

        return null;
    }

    private static Node bothLoopNode(Node head1, Node loop1, Node head2, Node loop2) {

        Node cur1 = null;
        Node cur2 = null;

        if (loop1 == loop2) {//这种情况是先相交再成环
            int n1 = 0;
            int n2 = 0;
            cur1 = head1;
            cur2 = head2;
            while (cur1 != loop1) {
                cur1 = cur1.next;
                n1++;
            }
            while (cur2 != loop2) {
                cur2 = cur2.next;
                n2++;
            }

            int n = Math.abs(n1 - n2);

            if (n1 > n2) {
                while (n != 0) {
                    head1 = head1.next;
                    n--;
                }
            } else {
                while (n != 0) {
                    head2 = head2.next;
                    n--;
                }

            }

            while (head1 != head2) {
                head1 = head1.next;
                head2 = head2.next;
            }

            return head1;
        } else {
            cur1 = loop1.next;
            while (cur1 != loop1) {
                cur1 = cur1.next;
                if (cur1 == loop2) {
                    return loop1;
                }
            }
            return null;
        }
    }

    private static Node noLoopFirstIntersectNode(Node head1, Node head2) {
        int n1 = 0;
        int n2 = 0;
        Node cur1 = head1;
        Node cur2 = head2;
        while (cur1 != null) {
            cur1 = cur1.next;
            n1++;
        }
        while (cur2 != null) {
            cur2 = cur2.next;
            n2++;
        }

        int n = Math.abs(n1 - n2);

        if (n1 > n2) {
            while (n != 0) {
                head1 = head1.next;
                n--;
            }
        } else {
            while (n != 0) {
                head2 = head2.next;
                n--;
            }

        }

        while (head1 != head2) {
            head1 = head1.next;
            head2 = head2.next;
        }

        return head1;
    }

    private static Node getLoopNode(Node head) {
        //边界条件链表为空或者只有1个和2个节点的时候，一定无环，直接返回空
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node slow = head.next;
        Node fast = head.next.next;
        while (slow != fast) {
            if (fast.next==null||fast.next.next==null){//这一步判断是判断有没有环，没有环直接返回空
                return null;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return fast;
    }

    public static void main(String[] args) {
        // 1->2->3->4->5->6->7->null
        Node head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);

        // 0->9->8->6->7->null
        Node head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(getIntersectNode(head1, head2).value);

        // 1->2->3->4->5->6->7->4...
        head1 = new Node(1);
        head1.next = new Node(2);
        head1.next.next = new Node(3);
        head1.next.next.next = new Node(4);
        head1.next.next.next.next = new Node(5);
        head1.next.next.next.next.next = new Node(6);
        head1.next.next.next.next.next.next = new Node(7);
        head1.next.next.next.next.next.next = head1.next.next.next; // 7->4

        // 0->9->8->2...
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next; // 8->2
        System.out.println(getIntersectNode(head1, head2).value);

        // 0->9->8->6->4->5->6..
        head2 = new Node(0);
        head2.next = new Node(9);
        head2.next.next = new Node(8);
        head2.next.next.next = head1.next.next.next.next.next; // 8->6
        System.out.println(getIntersectNode(head1, head2).value);

    }
}
