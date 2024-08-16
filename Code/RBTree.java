class RBTree {
    class RBNode {
        Integer key;
        RBNode left;
        RBNode right;
        RBNode parent;
        Color color;
        RBNode(Integer k) {
            key = k;
        }
    }
    RBNode sent;
    RBNode root = null;
    RBTree() {
        sent = new RBNode(null);
        sent.color = Color.BLACK;
        sent.left = sent;
        sent.right = sent;
        // Sentinel always points to itself ->
        // node.parent.parent and its children will never result in null references
    }
    // Traversal and search are the same as BSTree
    void insert(RBNode z) { // Omega(1), O(log n), Theta(log n)
        // Very similar to BSTree, with addition of color and parent of sentinel instead of null
        RBNode x = root; // Traversal starting from the root
        RBNode px = sent; // Parent of x, initially sentinel unlike BST
        while (x != null) {
            px = x;
            if (z.key < x.key)
                x = x.left;
            else
                x = x.right;
        }// Traversing the tree until finding the insertion point
        z.parent = px; // Sets the parent of the node to be inserted
        if (px == sent) // px only sentinel if the tree is empty -> loop never runs -> z is root
            root = z;
        else if (z.key < px.key) // Key smaller -> left child
            px.left = z;
        else // Key bigger -> right child
            px.right = z;
        z.color = Color.RED; // Sets color of new Node to red, will not necessarily stay red
        fixColorsAfterInsertion(z); // Fixes colors in tree after insertion to maintain RB properties
        // May add the same node twice as it doesn't check for duplicates
    }
    void fixColorsAfterInsertion(RBNode z) { // Omega(1), O(log n), Theta(log n)
        while (z.parent.color == Color.RED) { // While z's parent is red
            if (z.parent == z.parent.parent.left) { // If z's parent is a left child
                RBNode y = z.parent.parent.right; // Gets sibling of z's parent
                if (y != null && y.color == Color.RED) { // CASE 1: z's parent is a left child and sibling is red
                    z.parent.color = Color.BLACK; // Set z's parent to black
                    y.color = Color.BLACK; // Set z's uncle to black
                    z.parent.parent.color = Color.RED; // Set z's grandparent to red
                    z = z.parent.parent; // Set z to z's grandparent
                } else { // CASE 2: z's parent is a left child and sibling is black
                    if (z == z.parent.right) { // If z is a right child
                        z = z.parent; // Set z to z's parent
                        rotateLeft(z); // Rotate new z to left
                    }
                    z.parent.color = Color.BLACK; // Set z's parent to black
                    z.parent.parent.color = Color.RED; // Set z's grandparent to red
                    rotateRight(z.parent.parent); // Rotate z's grandparent to right
                }
            } else { // If z's parent is a right child
                // Same as above but with right and left exchanged
                RBNode y = z.parent.parent.left;
                if (y != null && y.color == Color.RED) { // CASE 3: z's parent is a right child and sibling is red
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else { // CASE 4: z's parent is a right child and sibling is black
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = Color.BLACK; // Set root to black, as it always should be
    }
    void rotateLeft(RBNode x) { // O(1)
        RBNode y = x.right;
        x.right = y.left; // Set x's right child to y's left child
        if (y.left != null) // If y has a left child
            y.left.parent = x; // Set y's left child's parent to x
        y.parent = x.parent; // Set y's parent to x's parent
        if (x.parent == sent) // If x is the root, set y to be the root
            root = y;
        else if (x == x.parent.left) // If x is a left child, set x's parent's left child to y
            x.parent.left = y;
        else // If x is a right child, set x's parent's right child to y
            x.parent.right = y;
        y.left = x; // Set y's left child to x
        x.parent = y; // Set x's parent to y
    }
    void rotateRight(RBNode x) { // O(1)
        // Same as rotateLeft but with right and left exchanged
        RBNode y = x.left;
        x.left = y.right;
        if (y.right != null)
            y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == sent)
            root = y;
        else if (x == x.parent.right)
            x.parent.right = y;
        else
            x.parent.left = y;
        y.right = x;
        x.parent = y;
    }
    void delete(RBNode z) { // Omega(1), O(log n), Theta(log n)
        RBNode a = z.parent; // a represent node with black depth imbalance
        int dbh = 0; // delta black height, -1 for right, 1 for left leaning
        if (z.left == null && z.right == null) { // CASE 1: z is a leaf
            if (z.color == Color.BLACK && z != root) { // If z is black
                if (z == z.parent.left) // If z is a left child
                    dbh = -1; // Set delta black height to -1
                else // If z is a right child
                    dbh = 1; // Set delta black height to 1
            }
            transplant(z, null); // Transplant null to zs parent
        } else if (z.left == null) { // CASE 2: z only has a right child
            RBNode y = z.right;
            transplant(z, z.right);
            y.color = z.color;
        } else if (z.right == null) { // CASE 3: z only has a left child
            RBNode y = z.left;
            transplant(z, z.left);
            y.color = z.color;
        } else { // CASE 4: z has two children
            RBNode y = z.right;
            a = y;
            boolean wentLeft = false;
            while (y.left != null) { // find next biggest Node
                a = y;
                y = y.left;
                wentLeft = true;
            }
            if (y.parent != z) { // If next biggest element is not child of z
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            if (y.color == Color.BLACK) {
                if (wentLeft) // Tree imbalanced depending on y location
                    dbh = -1;
                else
                    dbh = 1;
            }
            y.color = z.color;
        }
        if (dbh != 0) // If black height imbalance
            fixColorsAfterDeletion(a, dbh);
    }
    void fixColorsAfterDeletion(RBNode a, int dbh) { // Omega(1), O(log n), Theta(log n)
        if (dbh == -1) { // Extra black node on the right
            RBNode x = a.left;
            RBNode b = a.right;
            RBNode c = b.left; // Left child of right child of a
            RBNode d = b.right; // Right child of right child of a
            if (x != null && x.color == Color.RED) {
                // Easy case: x is red
                x.color = Color.BLACK;
            } else if (a.color == Color.BLACK
                    && b.color == Color.RED) {
                // Case 1: a black, b red
                rotateLeft(a);
                a.color = Color.RED;
                b.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (a.color == Color.RED
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) {
                // Case 2a: a red, b black, c and d black
                a.color = Color.BLACK;
                b.color = Color.RED;
            } else if (a.color == Color.BLACK
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) {
                // Case 2b: a black, b black, c and d black
                b.color = Color.RED;
                if (a == a.parent.left)
                    dbh = 1;
                else if (a == a.parent.right)
                    dbh = -1;
                else
                    dbh = 0;
                fixColorsAfterDeletion(a.parent, dbh);
            } else if (b.color == Color.BLACK
                    && c != null && c.color == Color.RED
                    && (d == null || d.color == Color.BLACK)) {
                // Case 3: a either, b black, c red, d black
                rotateRight(b);
                c.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (b.color == Color.BLACK
                    && d!= null && d.color == Color.RED) {
                // Case 4: a either, b black, c either, d red
                rotateLeft(a);
                b.color = a.color;
                a.color = Color.BLACK;
                d.color = Color.BLACK;
                }
        } else { // Extra black node on the left
            // Same as above but with right and left exchanged
            RBNode x = a.right;
            RBNode b = a.left;
            RBNode c = b.right; // Right child of left child of a
            RBNode d = b.left; // Left child of left child of a
            if (x != null && x.color == Color.RED) {
                // Easy case: x is red
                x.color = Color.BLACK;
            } else if (a.color == Color.BLACK
                    && b.color == Color.RED) {
                // Case 1: a black, b red
                rotateRight(a);
                a.color = Color.RED;
                b.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (a.color == Color.RED
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) {
                // Case 2a: a red, b black, c and d black
                a.color = Color.BLACK;
                b.color = Color.RED;
            } else if (a.color == Color.BLACK
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) {
                // Case 2b: a black, b black, c and d black
                b.color = Color.RED;
                if (a == a.parent.right)
                    dbh = 1;
                else if (a == a.parent.left)
                    dbh = -1;
                else
                    dbh = 0;
                fixColorsAfterDeletion(a.parent, dbh);
            } else if (b.color == Color.BLACK
                    && c != null && c.color == Color.RED
                    && (d == null || d.color == Color.BLACK)) {
                // Case 3: a either, b black, c red, d black
                rotateLeft(b);
                c.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (b.color == Color.BLACK
                    && d!= null && d.color == Color.RED) {
                // Case 4: a either, b black, c either, d red
                rotateRight(a);
                b.color = a.color;
                a.color = Color.BLACK;
                d.color = Color.BLACK;
            }
        }
        // All cases except 2b mean end of the method in this or the next instance. 2b can go on tho.
    }
    void transplant(RBNode u, RBNode v) { // O(1)
        // Transplants v to the position of u
        if (u.parent == sent) // If u is the root, v becomes the new root
            root = v;
        else if (u == u.parent.left) // If u is a left child, v becomes a left child
            u.parent.left = v;
        else // If u is a right child, v becomes a right child
            u.parent.right = v;
        if (v != null) // If v is not null, v becomes a child of u's parent
            v.parent = u.parent;
    }
}