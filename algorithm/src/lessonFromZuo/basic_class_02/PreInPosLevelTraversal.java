package lessonFromZuo.basic_class_02;


import utils.Node;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static utils.tools.printTree;

public class PreInPosLevelTraversal {
    public static void preOrderRecur(Node head) {

        if (head == null) {
            return;
        }

        System.out.print(head.value + " ");
        preOrderRecur(head.left);
        preOrderRecur(head.right);
    }

    public static void inOrderRecur(Node head) {

        if (head == null) {
            return;
        }

        inOrderRecur(head.left);
        System.out.print(head.value + " ");
        inOrderRecur(head.right);
    }

    public static void posOrderRecur(Node head) {

        if (head == null) {
            return;
        }

        posOrderRecur(head.left);
        posOrderRecur(head.right);
        System.out.print(head.value + " ");
    }

    /**
     * 先序遍历非递归版本
     * 如果栈中右孩子不为空，压右孩子；如果栈中左孩子不为空，压左孩子
     * 理解：当前节点在弹出的时候，先压右再压左，所以栈里面弹出的时候是逆序的
     *
     * @param head
     */
    public static void preOrderUnRecur(Node head) {
        System.out.println("preOrderUnRecur");

        if (head != null) {
            Node cur = head;
            Stack<Node> stack = new Stack<Node>();
            stack.push(cur);
            while (!stack.isEmpty()) {
                cur = stack.pop();
                System.out.println(cur.value);//弹出就打印
                if (cur.right != null) {
                    stack.push(cur.right);
                }
                if (cur.left != null) {
                    stack.push(cur.left);
                }
            }
        }
        System.out.println();
    }

    /**
     * 中序遍历非递归
     * 当前节点一定会把自己的左边界都压到栈里去
     * 当前节点为空，从栈拿一个，打印，然后当前节点往右走；当前节点不为空，入栈，当前节点往左
     * @param head
     */
    public static void inOrderUnRecur(Node head) {
        System.out.println("inOrderUnRecur");

        if (head != null) {
            Node cur = head;
            Stack<Node> stack = new Stack<Node>();
            while (cur != null || !stack.isEmpty()) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    System.out.println(cur.value + " ");
                    cur = cur.right;
                }
            }
            System.out.println();
        }

    }

    /**
     * 在先序遍历基础上更改完成后序遍历，先序遍历的入栈顺序是【中右左】，所以出栈顺序是【中左右】；而后序遍历，在先序基础上，将入栈顺序改成【中左右】，这样出栈的顺序就是【中右左】，也就是说当前节点，先压左孩子，再压右孩子，那么出栈顺序就是【中右左】。
     * 那么按照这个打印顺序，在本来应该打印当前节点的时候，不打印，而是把它放到一个辅助栈里面去，那么辅助栈中弹出的顺序将是【左右中】
     * @param head
     */
    public static void posOrderUnRecur(Node head) {
        System.out.println("posOrderUnRecur:");
        if (head != null) {
            Node cur = head;
            Stack<Node> s1 = new Stack<Node>();
            Stack<Node> s2 = new Stack<Node>();
            s1.push(cur);
            while (!s1.isEmpty()) {
                cur=s1.pop();
                s2.push(cur);
                if (cur.left!=null){
                    s1.push(cur.left);
                }
                if (cur.right!=null){
                    s1.push(cur.right);
                }
            }
            while (!s2.isEmpty()){
                System.out.print(s2.pop().value+" ");
            }
        }
    }

    /**
     * 按层遍历
     * @param head
     * @return
     */
    public static void traversalByLevel(Node head) {
        System.out.println("========按层遍历为=========");
        if (head == null) {
            return;
        }
        //String res = head.value + "!";
        Queue<Node> queue = new LinkedList<Node>();
        queue.offer(head);
        Node cur;
        while (!queue.isEmpty()) {
            cur = queue.poll();
            System.out.print(cur.value+" ");
            if (cur.left != null) {

                //res += head.left.value + "!";
                queue.offer(cur.left);
                //System.out.println(cur.left+" ");
            }
            if (cur.right != null) {
                //res += cur.right.value + "!";
                queue.offer(cur.right);
            }
        }
    }

    public static void main(String[] args) {
        Node head = new Node(5);
        head.left = new Node(3);
        head.right = new Node(8);
        head.left.left = new Node(2);
        head.left.right = new Node(4);
        head.left.left.left = new Node(1);
        head.right.left = new Node(7);
        head.right.left.left = new Node(6);
        head.right.right = new Node(10);
        head.right.right.left = new Node(9);
        head.right.right.right = new Node(11);

        // recursive
        System.out.println("==============recursive==============");

//        System.out.println("preOrderRecur");
//        System.out.println("inOrderRecur");
        System.out.println("posOrderRecur");
//        inOrderRecur(head);
//        preOrderRecur(head);
        posOrderRecur(head);
        System.out.println();
//        System.out.print("in-order: ");
//        inOrderRecur(head);
//        System.out.println();
//        System.out.print("pos-order: ");
//        posOrderRecur(head);
//        System.out.println();

//        // unrecursive
        System.out.println("============unrecursive=============");
//        preOrderUnRecur(head);
//        inOrderUnRecur(head);
        posOrderUnRecur(head);

        printTree(head);
    }
}
