class RBNode {
    Integer key;
    RBNode left;
    RBNode right;
    RBNode parent;
    Color color;

    RBNode(Integer k) {
        key = k;
        left = null;
        right = null;
        parent = null;
        color = null;
    }
}
