/**
 * 赫夫曼单词查找树的节点
 * Created by hongjiyao_2014150120 on 16-11-18.
 */
class TrieNode implements Comparable<TrieNode> {
    final char ch;                      // 节点的字母
    final int freq;                     // 频率
    TrieNode left, right;               // 左右孩子节点
    TrieNode parent;                    // 父节点

    /**
     * 构造函数
     */
    TrieNode(final char ch, final int freq, final TrieNode left, final TrieNode right, final TrieNode parent) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
        this.parent = parent;
    }

    /**
     * 判断是否是叶子节点
     *
     * @return 是否是叶子节点
     * 赫夫曼编码是棵完全二叉树
     * @throws Exception 左右节点不同时为空或者不空
     */
    boolean isLeaf() throws Exception {
        if (((left == null) && (right == null)) || ((left != null) && (right != null))) {
            return (left == null);
        } else {
            throw new Exception("左右节点情况不同");
        }
    }

    /**
     * 判断是不是父节点的左孩子
     *
     * @return 是不是
     */
    boolean isLeftChild() {
        return parent != null && this == parent.left;
    }

    /**
     * 根据频率比较大小的比较器
     *
     * @param that 另一节点
     * @return 比较结果
     */
    public int compareTo(final TrieNode that) {
        return this.freq - that.freq;
    }

    /**
     * 判断是否是根节点
     *
     * @return 是否是根节点
     */
    boolean isRoot() {
        return parent == null;
    }
}
