package jianzhioffer;

import utils.TreeLinkNode;

/**
 * 给定一个二叉树和其中的一个结点，
 * 请找出中序遍历顺序的下一个结点并且返回。
 * 注意，树中的结点不仅包含左右子结点，同时包含指向父结点的指针。
 *
 * 该结构比普通二叉树节点结构多了一个指向父节点的parent指针。
 * 假设有一棵Node类型的节点组成的二叉树，树中每个节点的parent指针都正确地指向 自己的父节点，头节点的parent指向null。
 * 只给一个在二叉树中的某个节点node，请实现返回node的后继节点的函数。 （不通过遍历整棵树的方式）
 * ps：在二叉树的中序遍历的序列中， node的下一个节点叫作node的后继节点。
 * 规律（先记住）：
 * 一个节点如果有右孩子，那么这个节点的后继节点就是整个右子树最左的节点；
 * 如果没有右孩子，那么就从自己开始(包括自己)一路往上找父节点，直到找到一个节点是其父节点的左孩子，那么这个父节点就是此节点的后继节点
 */
public class GetTreeNextTreeLinkNode {
    public TreeLinkNode GetNext(TreeLinkNode pNode) {

        if (pNode==null){
            return null;
        }

        if (pNode.right!=null){
            return getMostLeftNode(pNode.right);
        }else {
            while (pNode.next!=null&&pNode!=pNode.next.left){
                pNode=pNode.next;
            }
            return pNode.next;
        }

    }

    public TreeLinkNode getMostLeftNode(TreeLinkNode cur){
        if (cur==null){
            return null;
        }

        if (cur.left!=null){
            return cur.left;
        }

        return cur;
    }
}
