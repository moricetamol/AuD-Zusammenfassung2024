class BSTree {

    BSTNode root = null;

    void insert(BSTNode z) {
        BSTNode x = root; // Traversal starting from the root
        BSTNode px = null; // Parent of x, initially null

        while(x != null) {
            px = x;
            if (z.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }// Traversing the tree until finding the insertion point

        z.parent = px; // Sets the parent of the node to be inserted
        if (px == null) { // px only null if the tree is empty-> loop never runs -> z is root
            root = z;
        } else if (z.key < px.key) { // Key smaller -> left child
            px.left = z;
        } else { // Key bigger -> right child
            px.right = z;
        }
        // May add the same node twice as it doesn't check for duplicates
    }

    void delete(BSTNode z) {
        if (z.left == null) { // If z has no left child, transplants the right child to z's position
            transplant(z, z.right);
        } else if (z.right == null) { // If z has no right child, transplants the left child to z's position
            transplant(z, z.left);
        } else { // If z has both left and right children
            BSTNode y = z.right;
            while (y.left != null) {
                y = y.left;
            } // Finds the next biggest element of z = smallest in right subtree of z
            if (y.parent != z) { // If the next biggest element y is not child of z
                transplant(y, y.right); // Transplants the right child of y to y's position
                y.right = z.right; // The right child of y becomes the right child of z
                y.right.parent = y; // The parent of the right child of y becomes y
            }
            transplant(z, y); // Transplants y to z's position
            y.left = z.left; // The left child of y becomes the left child of z
            y.left.parent = y; // The parent of the left child of y becomes y
        }
    }

    void transplant(BSTNode u, BSTNode v) {
        // Transplants v to the parent of u
        if (u.parent == null) { // If u is the root, v becomes the new root
            root = v;
        } else if (u == u.parent.left) { // If u is a left child, v becomes a left child
            u.parent.left = v;
        } else { // If u is a right child, v becomes a right child
            u.parent.right = v;
        }
        if (v != null) { // If v is not null, v becomes a child of u's parent
            v.parent = u.parent;
        }
    }
    BSTNode iterativeSearch(int k) {
        BSTNode curr = root;
        while (curr != null && curr.key != k) {
            if (k < curr.key) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }
        return curr;
        // Returns null if element not found
    }

    BSTNode recursiveSearch(int k, BSTNode curr) {
        if (curr == null) {
            return null;
        }
        if (k < curr.key) {
            return recursiveSearch(k, curr.left);
        } else if (k > curr.key) {
            return recursiveSearch(k, curr.right);
        }

        return curr;
        // Returns null if element not found
    }

    void traversal(BSTNode curr) {
        if (curr != null) {
            return;
        }
        // Any actions that should be done in a specific order can be done
        // Here for preorder traversal
        traversal(curr.left);
        // Here for inorder traversal
        traversal(curr.right);
        // Here for postorder traversal
        // Left and right can also be exchanged to traverse in reverse order
    }
}
