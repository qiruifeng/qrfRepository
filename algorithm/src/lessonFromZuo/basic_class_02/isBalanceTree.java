package lessonFromZuo.basic_class_02;


import utils.Node;

import static utils.tools.printTree;

public class isBalanceTree {

    public static class returnData {
        public boolean isB;
        public int h;

        public returnData(boolean isB, int h) {
            this.isB = isB;
            this.h = h;
        }
    }

    public static boolean isBalance(Node head) {
        return subProcess(head).isB;
    }

    public static returnData subProcess(Node head) {
        if (head == null) {
            return new returnData(true, 0);
        }

        returnData leftData = subProcess(head.left);

        if (!leftData.isB) {
            return new returnData(false, 0);
        }

        returnData rightDta = subProcess(head.right);

        if (!rightDta.isB) {
            return new returnData(false, 0);
        }

        if (Math.abs(leftData.h - rightDta.h) > 1) {
            return new returnData(false, 0);
        }

        return new returnData(true, Math.max(leftData.h, rightDta.h) + 1);

    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);
        head.right.right.right = new Node(8);
        head.right.right.right.right = new Node(9);

        printTree(head);

        System.out.println(isBalance(head));

    }

}
