class AVLTree {
    class AVLNode {
        Integer key;
        int height;
        AVLNode left;
        AVLNode right;
        AVLNode(Integer k) {
            key = k;
            height = 1;
        }
    }
    AVLNode root;
    // Search and traversal like in BST
    int height(AVLNode n) {
        return (n == null) ? -1 : n.height;
    }
    void updateHeight(AVLNode n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }
    int getBalance(AVLNode n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }
    // Rotations work a bit different as in other Trees as we don't have parents.
    // Does not update the tree itself -> just returns the new root of the rotated subtree
    // Does not need to check for null reference as right.left/left.right respectively
    // are always well defined when called in insert and delete
    AVLNode rotateLeft(AVLNode x) { // O(1)
        AVLNode y = x.right;
        AVLNode z = y.left;
        y.left = x;
        x.right = z;
        updateHeight(x);
        updateHeight(y);
        return y;
    }
    AVLNode rotateRight(AVLNode x) { // O(1)
        AVLNode y = x.left;
        AVLNode z = y.right;
        y.right = x;
        x.left = z;
        updateHeight(x);
        updateHeight(y);
        return y;
    }
    AVLNode insert(AVLNode partRoot, int key) { // O(h) = O(log(n))
        if (partRoot == null) // Found insertion point
            return new AVLNode(key);
        else if (key < partRoot.key) // If node smaller than partRoot -> go left
            partRoot.left = insert(partRoot.left, key);
        else if (key > partRoot.key) // If node bigger than partRoot -> go right
            partRoot.right = insert(partRoot.right, key);
        else // If node == partRoot -> throw exception
            throw new UException("Duplicate node");
        return fixBalance(partRoot);
        // Rebalance every node above the inserted node.
    }
    AVLNode delete(AVLNode node, int key) { // O(h) = O(log(n))
        if (node == null) {// Node not found
            return node;
        } else if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else { // Node found -> Commence deletion
            if (node.left == null || node.right == null) // If half leaf or leaf
                node = (node.left == null) ? node.right : node.left;
            else { // If complete node
                AVLNode next = node.right;
                while (next.left != null) {
                    next = next.left;
                }
                node.key = next.key;
                node.right = delete(node.right, next.key);
            }
        }
        return fixBalance(node); // Rebalance every node above the deleted node
    }
    AVLNode fixBalance(AVLNode z) { // O(1)
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) { // If right heavy
            if (height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
            }
        } else if (balance < -1) { // If left heavy
            if (height(z.left.left) > height(z.left.right)) {
                z = rotateRight(z);
            } else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
            }
        }
        return z;
    }
}
