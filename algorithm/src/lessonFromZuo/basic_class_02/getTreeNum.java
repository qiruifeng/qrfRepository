package lessonFromZuo.basic_class_02;


import utils.Node;

public class getTreeNum {

    public static int getNum(Node head) {
        if (head == null) {
            return 0;
        } else {
            return subProcess(head, 1, getMostLeftLevel(head,1));
        }
    }

    /**
     * 由当前节点，当前节点所在层数和整棵树的层数得到以当前节点为头的树的总共的节点数
     *
     * @param cur           当前节点
     * @param curLevel      当前节点所在层数
     * @param mostLeftLevel 总层数
     * @return
     */
    public static int subProcess(Node cur, int curLevel, int mostLeftLevel) {

        if (mostLeftLevel == curLevel) {//当当前节点在最后一层时，以当前节点为头的树的总共的节点数就是1
            return 1;
        }
        if (getMostLeftLevel(cur.right, curLevel + 1) == mostLeftLevel) {
            return (int) ((Math.pow(2,(mostLeftLevel - curLevel)))+subProcess(cur.right, curLevel + 1, mostLeftLevel));
            //return (2 ^ (mostLeftLevel - curLevel) + subProcess(cur.right, curLevel + 1, mostLeftLevel));
        } else {
            return (int)(Math.pow(2,(mostLeftLevel - curLevel - 1)))+ subProcess(cur.left, curLevel + 1, mostLeftLevel);
            // return (2 ^ (mostLeftLevel - curLevel - 1) + subProcess(cur.left, curLevel + 1, mostLeftLevel));
        }

    }

    /**
     * 由当前节点和当前节点所在层数，得到整棵树的层数
     *
     * @param cur       当前节点
     * @param curLevel2 当前节点所在层数
     * @return
     */
    public static int getMostLeftLevel(Node cur, int curLevel2) {
        while (cur != null) {
            curLevel2++;
            cur = cur.left;
        }
        return curLevel2 - 1;
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
//        head.right = new Node(3);
//        head.left.left = new Node(4);
//        head.left.right = new Node(5);
//        head.right.left = new Node(6);
        System.out.println(getNum(head));

    }
}
