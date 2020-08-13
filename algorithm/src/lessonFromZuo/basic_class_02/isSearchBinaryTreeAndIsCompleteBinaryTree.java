package lessonFromZuo.basic_class_02;

import tools.Node;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


import static tools.tools.printTree;

public class isSearchBinaryTreeAndIsCompleteBinaryTree {

    /**
     * 用中序遍历改的
     * @param head
     * @return
     */
    public static boolean isSBT(Node head) {

        int pre = Integer.MIN_VALUE;

        if (head != null) {
            Node cur = head;
            Stack<Node> stack = new Stack<Node>();
            while (cur != null || !stack.isEmpty()) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    //System.out.println(cur.value + " ");
                    if (cur.value < pre) {
                        return false;
                    }
                    pre = cur.value;
                    cur = cur.right;
                }
            }
            return true;
        }
        return false;
    }


    public static boolean isCBT(Node head){

        boolean iscbt=true;//标志整个树是否为完全二叉树
        boolean leafPeriod=false;//标志着当有一个节点不是左右两个孩子都全时，它后面遍历的节点都要判断是不是叶节点

        if (head == null) {
            return true;
        }
        Queue<Node> queue = new LinkedList<Node>();
        Node cur=null;
        Node l = null;
        Node r = null;
        queue.offer(head);
        while (!queue.isEmpty()){
            cur=queue.poll();
            l = cur.left;
            r = cur.right;

            if ((l==null&&r!=null)||(leafPeriod&&!isLeaf(cur))){
                iscbt=false;
            }

            if (l != null) {
                queue.offer(l);
            }
            if (r != null) {
                queue.offer(r);
            }

            if (isLeaf(cur)){
                leafPeriod=true;
            }
        }
        return iscbt;
    }

    /**
     * 判断一个节点是否是叶节点
     * @param node
     * @return
     */
    public static boolean isLeaf(Node node){
        boolean isLeaf=false;
        if (node.left==null&&node.right==null){
            isLeaf=true;
        }
        return isLeaf;
    }

    public static void main(String[] args) {
        Node head=new Node(5);
        head.left=new Node(3);
        head.left.left=new Node(2);
        head.left.right=new Node(4);
        head.left.left.left=new Node(1);
        head.right=new Node(8);
        head.right.left=new Node(6);
        head.right.left.right=new Node(7);
        head.right.right=new Node(10);
        head.right.right.left=new Node(11);

        Node head2=new Node(1);

        head2.left=new Node(2);
        head2.left.left=new Node(4);
        head2.left.right=new Node(5);

        head2.right=new Node(3);
        head2.right.left=new Node(6);
        head2.right.right=new Node(7);

        printTree(head);
        System.out.println(isCBT(head));



    }
}
