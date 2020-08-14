package jianzhioffer;

import utils.TreeNode;

/**
 * 输入两棵二叉树A，B，判断B是不是A的子结构。（ps：我们约定空树不是任意一个树的子结构）
 */
public class hasSubtree {
    //遍历大树，判断相等的节点进行相等结构比较
    public static boolean HasSubtree(TreeNode root1, TreeNode root2) {
        if(root2==null||root1==null){
            return false;
        }

        if (root1.val==root2.val){
            if(yesOrNo(root1,root2)){
                return true;
            }
        }

        return HasSubtree(root1.left,root2)||HasSubtree(root1.right,root2);

    }

    //头节点相同，是否有子结构
    public static boolean yesOrNo(TreeNode root1, TreeNode root2){
        //子结构已经循环完毕，代表全部匹配
        if (root2==null){
            return true;
        }

        //大树已经循环完毕，并未成功匹配
        if (root1==null){
            return false;
        }

        if (root1.val==root2.val){
            return yesOrNo(root1.left,root2.left)&&yesOrNo(root1.right,root2.right);
        }

        return false;
    }

}
