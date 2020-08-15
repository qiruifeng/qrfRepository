package jianzhioffer;

import utils.TreeNode;

import java.util.ArrayList;
import java.util.Stack;

import static utils.tools.printArrayList;

/**
 * 输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。要求不能创建任何新的结点，只能调整树中结点指针的指向。
 * 搜索二叉树的定义就是，当前节点的左孩子都比它小，右孩子都比他大，所以中序遍历的结果就是升序的
 */
public class treeConvertToDoubleArrayList {


    /**
     * 这种方法会超时
     * @param pRootOfTree
     * @return
     */
    public static TreeNode Convert1(TreeNode pRootOfTree) {

        ArrayList<TreeNode> help = inOrderUnRecur(pRootOfTree);

        for (int i=0;i<help.size();i++){
            if (i==0){
                help.get(i).left=help.get(help.size()-1);
                help.get(i).right=help.get(i+1);
                continue;
            }

            if (i==help.size()-1){
                help.get(i).left=help.get(i-1);
                help.get(i).right=help.get(0);
                continue;
            }

            help.get(i).left=help.get(i-1);
            help.get(i).right=help.get(i+1);



        }

        return pRootOfTree;
    }

    public static ArrayList<TreeNode> inOrderUnRecur(TreeNode head) {
        System.out.println("inOrderUnRecur");

        ArrayList<TreeNode> res = new ArrayList<>();

        if (head != null) {
            TreeNode cur = head;
            Stack<TreeNode> stack = new Stack<TreeNode>();
            while (cur != null || !stack.isEmpty()) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    res.add(cur);
                    cur = cur.right;
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        TreeNode head = new TreeNode(5);
        head.left = new TreeNode(3);
        head.right = new TreeNode(8);
        head.left.left = new TreeNode(2);
        head.left.right = new TreeNode(4);
        head.left.left.left = new TreeNode(1);
        head.right.left = new TreeNode(7);
        head.right.left.left = new TreeNode(6);
        head.right.right = new TreeNode(10);
        head.right.right.left = new TreeNode(9);
        head.right.right.right = new TreeNode(11);

        //printArrayList(inOrderUnRecur(head));

        TreeNode res=Convert1(head);
        for (int i=0;i<10;i++){
            System.out.print(res.val+" ");
            res=res.right;
        }

    }


}
