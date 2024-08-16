class SplayTree extends BSTree {
    //Same Nodes as BSTree
    void splay(BSTNode z) { // O(h)
        while(z != root) {
            if (z.parent.parent == null) // If father is root
                zig(z);
            else {
                if (z == z.parent.parent.left.left || z == z.parent.parent.right.right)
                    zigZig(z);
                else
                    zigZag(z);
            }
        }
    }
    void zig(BSTNode z) { // O(1)
        if (z == z.parent.left)
            rotateRight(z.parent);
        else
            rotateLeft(z.parent);
    }
    void zigZig(BSTNode z) { // O(1)
        if (z == z.parent.left) {
            rotateRight(z.parent.parent);
            rotateRight(z.parent);
        } else {
            rotateLeft(z.parent.parent);
            rotateLeft(z.parent);
        }
    }
    void zigZag(BSTNode z) { // O(1)
        if (z == z.parent.left) {
            rotateRight(z.parent);
            rotateLeft(z.parent.parent);
        } else {
            rotateLeft(z.parent);
            rotateRight(z.parent.parent);
        }
    }
    //Rotate works the same as in RBTree
    void rotateLeft(BSTNode x) { // O(1)
        BSTNode y = x.right;
        x.right = y.left; // Set x's right child to y's left child
        if (y.left != null) // If y has a left child
            y.left.parent = x; // Set y's left child's parent to x
        y.parent = x.parent; // Set y's parent to x's parent
        if (x.parent == null) // If x is the root, set y to be the root
            root = y;
        else if (x == x.parent.left) // If x is a left child, set x's parent's left child to y
            x.parent.left = y;
        else // If x is a right child, set x's parent's right child to y
            x.parent.right = y;
        y.left = x; // Set y's left child to x
        x.parent = y; // Set x's parent to y
    }
    void rotateRight(BSTNode x) { // O(1)
        BSTNode y = x.left;
        x.left = y.right;
        if (y.right != null)
            y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null)
            root = y;
        else if (x == x.parent.right)
            x.parent.right = y;
        else
            x.parent.left = y;
        y.right = x;
        x.parent = y;
    }
    BSTNode search(int key) { // O(h), like BSTree, with additional splay
        BSTNode x = root;
        while(x != null && x.key != key) {
            if (key < x.key)
                x = x.left;
            else
                x = x.right;
        }
        if (x == null)
            return null;
        splay(x);
        return root; // After splay the root is the searched node
    }
    void insert (BSTNode z) { // O(h)
        super.insert(z); // Inserts node using BSTrees insert
        splay(z); // Splays node to the root
    }
    void delete (BSTNode z) { // O(h)
        splay(z);
        BSTNode r = root.right; // Save right child
        BSTNode biggestL = root.left; // Save left child
        root.right.parent = null; // Remove parent reference of right child of root
        root.left.parent = null; // Remove parent reference of left child of root
        root = biggestL; // Set root to left child
        while (biggestL.right != null) // Get biggest node in left subtree
            biggestL = biggestL.right;
        splay(biggestL); // Splay biggest node -> becomes root
        root.right = r; // Put right subtree back in place
        r.parent = root; // Change parent of right subtree root
    }
}
