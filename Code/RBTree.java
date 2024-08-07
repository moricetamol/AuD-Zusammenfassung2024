class RBTree {
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

    void insert(RBNode z) {
        // Very similar to BSTree, with addition of color and parent of sentinel instead of null
        RBNode x = root; // Traversal starting from the root
        RBNode px = sent; // Parent of x, initially sentinel unlike BST

        while (x != null) {
            px = x;
            if (z.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }// Traversing the tree until finding the insertion point

        z.parent = px; // Sets the parent of the node to be inserted
        if (px == sent) { // px only sentinel if the tree is empty -> loop never runs -> z is root
            root = z;
        } else if (z.key < px.key) { // Key smaller -> left child
            px.left = z;
        } else { // Key bigger -> right child
            px.right = z;
        }

        z.color = Color.RED; // Sets color of new Node to red, will not necessarily stay red
        fixColorsAfterInsertion(z); // Fixes colors in tree after insertion to maintain RB properties
        // May add the same node twice as it doesn't check for duplicates
    }
    void fixColorsAfterInsertion(RBNode z) {
        while (z.parent.color == Color.RED) { // While z's parent is red
            if (z.parent == z.parent.parent.left) { // If z's parent is a left child
                RBNode y = z.parent.parent.right; // Gets sibling of z's parent
                if (y != null && y.color == Color.RED) { // If sibling exists and is red
                    z.parent.color = Color.BLACK; // Set z's parent to black
                    y.color = Color.BLACK; // Set z's sibling to black
                    z.parent.parent.color = Color.RED; // Set z's grandparent to red
                    z = z.parent.parent; // Set z to z's grandparent
                } else { // If z doesn't have a sibling or sibling is black
                    if (z == z.parent.right) { // If z is a right child
                        z = z.parent; // Set z to z's parent
                        rotateLeft(z); // Rotate new z to left
                    }
                    z.parent.color = Color.BLACK; // Set z's parent to black
                    z.parent.parent.color = Color.RED; // Set z's grandparent to red
                    rotateRight(z.parent.parent); // Rotate z's grandparent to right
                }
            } else {
                // Same as above but with right and left exchanged
                RBNode y = z.parent.parent.left;
                if (y != null && y.color == Color.RED) {
                    z.parent.color = Color.BLACK;
                    y.color = Color.BLACK;
                    z.parent.parent.color = Color.RED;
                    z = z.parent.parent;
                } else {
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
        // Never needs to check nodes below z as their properties will not change after insertion
    }
    void rotateLeft(RBNode x) {
        RBNode y = x.right;

        x.right = y.left; // Set x's right child to y's left child
        if (y.left != null) { // If y has a left child
            y.left.parent = x; // Set y's left child's parent to x
        }
        y.parent = x.parent; // Set y's parent to x's parent
        if (x.parent == sent) { // If x is the root, set y to be the root
            root = y;
        } else if (x == x.parent.left) { // If x is a left child, set x's parent's left child to y
            x.parent.left = y;
        } else { // If x is a right child, set x's parent's right child to y
            x.parent.right = y;
        }
        y.left = x; // Set y's left child to x
        x.parent = y; // Set x's parent to y
    }
    void rotateRight(RBNode x) {
        // Same as rotateLeft but with right and left exchanged
        RBNode y = x.left;

        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == sent) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }
    
    void delete(RBNode z) {
        RBNode a = z.parent; // a is the parent of z
        int dbh = 0; // delta black height, -1 for right, 1 for left leaning

        if (z.left == null && z.right == null) { // If z is a leaf
            if (z.color == Color.BLACK && z != root) { // If z is black
                if (z == z.parent.left) { // If z is a left child
                    dbh = -1; // Set delta black height to -1
                } else { // If z is a right child
                    dbh = 1; // Set delta black height to 1
                }
            }
            transplant(z, null); // Transplant z to null
        } else if (z.left == null) { // If z only has a right child
            RBNode y = z.right;
            transplant(z, z.right);
            y.color = z.color;
        } else if (z.right == null) { // If z only has a left child
            RBNode y = z.left;
            transplant(z, z.left);
            y.color = z.color;
        } else { // If z has two children
            RBNode y = z.right;
            a = y;
            boolean wentLeft = false;
            while (y.left != null) {
                a = y;
                y = y.left;
                wentLeft = true;
            }
            if (y.parent != z) {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            if (y.color == Color.BLACK) {
                if (wentLeft) {
                    dbh = -1;
                } else {
                    dbh = 1;
                }
            }
            y.color = z.color;
        }
        if (dbh != 0) {
            fixColorsAfterDeletion(a, dbh);
        }
    }

    void fixColorsAfterDeletion(RBNode a, int dbh) {
        if (dbh == -1) { // Extra black node on the right
            RBNode x = a.left;
            RBNode b = a.right;
            RBNode c = b.left;
            RBNode d = b.right;
            if (x != null && x.color == Color.RED) { // Easy case: x is red
                x.color = Color.BLACK;
            } else if (a.color == Color.BLACK
                    && b.color == Color.RED) { // Case 1: a black, b red
                rotateLeft(a);
                a.color = Color.RED;
                b.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (a.color == Color.RED
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) { // Case 2a: a red, b black, c and d black
                a.color = Color.BLACK;
                b.color = Color.RED;
            } else if (a.color == Color.BLACK
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) { // Case 2b: a black, b black, c and d black
                b.color = Color.RED;
                if (a == a.parent.left) {
                    dbh = 1;
                } else if (a == a.parent.right) {
                    dbh = -1;
                } else {
                    dbh = 0;
                }
                fixColorsAfterDeletion(a.parent, dbh);
            } else if (b.color == Color.BLACK
                    && c != null && c.color == Color.RED
                    && (d == null || d.color == Color.BLACK)) { // Case 3: a either, b black, c red, d black
                rotateRight(b);
                c.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (b.color == Color.BLACK
                    && d!= null && d.color == Color.RED) { // Case 4: a either, b black, c either, d red
                rotateLeft(a);
                b.color = a.color;
                a.color = Color.BLACK;
                d.color = Color.BLACK;
                }
        } else { // Extra black node on the left
            // Same as above but with right and left exchanged
            RBNode x = a.right;
            RBNode b = a.left;
            RBNode c = b.right;
            RBNode d = b.left;
            if (x != null && x.color == Color.RED) { // Easy case: x is red
                x.color = Color.BLACK;
            } else if (a.color == Color.BLACK
                    && b.color == Color.RED) { // Case 1: a black, b red
                rotateRight(a);
                a.color = Color.RED;
                b.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (a.color == Color.RED
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) { // Case 2a: a red, b black, c and d black
                a.color = Color.BLACK;
                b.color = Color.RED;
            } else if (a.color == Color.BLACK
                    && b.color == Color.BLACK
                    && (c == null || c.color == Color.BLACK)
                    && (d == null || d.color == Color.BLACK)) { // Case 2b: a black, b black, c and d black
                b.color = Color.RED;
                if (a == a.parent.right) {
                    dbh = 1;
                } else if (a == a.parent.left) {
                    dbh = -1;
                } else {
                    dbh = 0;
                }
                fixColorsAfterDeletion(a.parent, dbh);
            } else if (b.color == Color.BLACK
                    && c != null && c.color == Color.RED
                    && (d == null || d.color == Color.BLACK)) { // Case 3: a either, b black, c red, d black
                rotateLeft(b);
                c.color = Color.BLACK;
                fixColorsAfterDeletion(a, dbh);
            } else if (b.color == Color.BLACK
                    && d!= null && d.color == Color.RED) { // Case 4: a either, b black, c either, d red
                rotateRight(a);
                b.color = a.color;
                a.color = Color.BLACK;
                d.color = Color.BLACK;
            }
        }
    }

    void transplant(RBNode u, RBNode v) {
        // Transplants v to the parent of u
        if (u.parent == sent) { // If u is the root, v becomes the new root
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
}
