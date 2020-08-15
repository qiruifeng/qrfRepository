package jianzhioffer;

import utils.TreeNode;

public class treeConvertToDoubleArrayListAns {
    TreeNode tempHead = null; //
    TreeNode realHead = null; //保存双向链表的头结点

    public TreeNode Convert(TreeNode pRootOfTree) {
        //如果头结点为空，返回null
        if (pRootOfTree == null) {
            return null;
        }

        //转换
        ConvertMethod(pRootOfTree);

        //返回双向链表的头结点
        return realHead;
    }

    //采用递归的方法进行中序遍历，遍历过程中，建立头结点和下一个节点的双向指针
    public void ConvertMethod(TreeNode pRootOfTree) {
        //递归遍历左节点
        if (pRootOfTree.left != null) {
            ConvertMethod(pRootOfTree.left);
        }

        //建立根节点与下一个节点的双向指针
        if(tempHead==null){    //第一次运行的时候，头结点为空，初始化头结点
            tempHead=pRootOfTree;//用于记录当前头结点
            realHead=pRootOfTree;//用于记录双向链表的头结点
        }else {  //第一次运行以后，建立tempHead节点与其下一个节点的双向指针
            //建立当前头结点与下一个节点的双向指针
            tempHead.right = pRootOfTree;
            pRootOfTree.left = tempHead;

            tempHead = pRootOfTree; //当前头节点后移一位
        }

        //递归遍历右节点
        if (pRootOfTree.right != null) {
            ConvertMethod(pRootOfTree.right);
        }

    }
}
