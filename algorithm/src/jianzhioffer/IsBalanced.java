package jianzhioffer;

import utils.TreeNode;

public class IsBalanced {

    public boolean IsBalanced_Solution(TreeNode root) {
        if (root == null) {
            return true;
        }

        if (Math.abs(getDepth(root.left) - getDepth(root.right))>1){
            return false;
        }

        return IsBalanced_Solution(root.left)&&IsBalanced_Solution(root.right);
    }

    /**
     * 得到一棵树的深度
     *
     * @param root
     * @return
     */
    public int getDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftDepth = getDepth(root.left) + 1;
        int rightDepth = getDepth(root.right) + 1;

        return Math.max(leftDepth, rightDepth);

    }
}
