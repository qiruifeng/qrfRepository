package lessonFromZuo.basic_class_02;


import utils.Node;

import static utils.tools.printTree;

/**
 * 该结构比普通二叉树节点结构多了一个指向父节点的parent指针。
 * 假设有一棵Node类型的节点组成的二叉树，树中每个节点的parent指针都正确地指向 自己的父节点，头节点的parent指向null。
 * 只给一个在二叉树中的某个节点node，请实现返回node的后继节点的函数。 （不通过遍历整棵树的方式）
 * ps：在二叉树的中序遍历的序列中， node的下一个节点叫作node的后继节点。
 * 规律（先记住）：
 * 一个节点如果有右孩子，那么这个节点的后继节点就是整个右子树最左的节点；
 * 如果没有右孩子，那么就从自己开始(包括自己)一路往上找父节点，直到找到一个节点是其父节点的左孩子，那么这个父节点就是此节点的后继节点
 */
public class getSuccessorNode {
    public static Node getSuccessorNode(Node node){
        if (node==null){
            return null;
        }
        if (node.right!=null){
            return getMostLeftNode(node.right);
        }else {
            Node cur=node;
            while (cur.parent!=null&&cur!=cur.parent.left){
                cur=cur.parent;
            }
            return cur.parent;
        }
    }

    public static Node getMostLeftNode(Node node){
        if (node==null){
            return null;
        }
        while (node.left!=null) {
            node=node.left;
        }
        return node;
    }

    public static void main(String[] args) {
        Node head = new Node(6);
        head.parent = null;
        head.left = new Node(3);
        head.left.parent = head;
        head.left.left = new Node(1);
        head.left.left.parent = head.left;
        head.left.left.right = new Node(2);
        head.left.left.right.parent = head.left.left;
        head.left.right = new Node(4);
        head.left.right.parent = head.left;
        head.left.right.right = new Node(5);
        head.left.right.right.parent = head.left.right;
        head.right = new Node(9);
        head.right.parent = head;
        head.right.left = new Node(8);
        head.right.left.parent = head.right;
        head.right.left.left = new Node(7);
        head.right.left.left.parent = head.right.left;
        head.right.right = new Node(10);
        head.right.right.parent = head.right;

        printTree(head);

        Node test = head.left.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left.left.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.left.right.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right.left.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right.left;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right;
        System.out.println(test.value + " next: " + getSuccessorNode(test).value);
        test = head.right.right; // 10's next is null
        System.out.println(test.value + " next: " + getSuccessorNode(test));
    }
}
