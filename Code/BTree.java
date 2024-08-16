class BTree {
    class BNode {
        int[] keys; // array of all keys in the node
        int t; // degree, defines the maximum number of keys in the node
        BNode[] children; // array of children to the node
        int n; // current number of keys in the node, used to find the first free index
        boolean isLeaf; // true if the node is a leaf node -> no children
        BNode(int t, boolean isLeaf) {
            this.keys = new int[2 * t - 1];
            this.t = t;
            this.children = new BNode[2 * t]; // 1 more than keys
            this.n = 0;
            this.isLeaf = isLeaf;
        }
        int findKey(int k) { // Finds index of key in node, i = n if not in node
            int i = 0;
            while (i < n && keys[i] < k) i++;
            return i;
        }
        boolean inNode(int i) {
            return (i < n && keys[i] == 0);
        }
        int getPredecessor(int i) {
            BNode child = children[i];
            while (!child.isLeaf)
                child = child.children[child.n];
            return child.keys[child.n - 1];
        }
        int getSuccessor(int i) {
            BNode child = children[i + 1];
            while (!child.isLeaf)
                child = child.children[0];
            return child.keys[0];
        }
        void fill(int i) {
            if (i != 0 && children[i - 1].n >= t) // If the previous child is filled more than half
                borrowFromPrev(i);
            else if (i != n && children[i + 1].n >= t) // If the next child is filled more than half
                borrowFromNext(i);
            else // If both children are not filled more than half
                if (i != n) // Merge with next child
                    merge(i);
                else // Merge with previous child
                    merge(i - 1);
        }
        void borrowFromPrev(int i) {
            BNode child = children[i];
            BNode sibling = children[i - 1]; // Get previous child, to be borrowed from
            for (int j = child.n - 1; j >= 0; j--)// Move keys to the right
                child.keys[j + 1] = child.keys[j];

            if (!child.isLeaf)
                for (int j = child.n; j >= 0; j--) // Move children to the right
                    child.children[j + 1] = child.children[j];
            child.keys[0] = keys[i - 1]; // Move key from parent to child
            if (!child.isLeaf) // Move children if not child not a leaf
                child.children[0] = sibling.children[sibling.n];
            keys[i - 1] = sibling.keys[sibling.n - 1]; // Move key from sibling to parent
            child.n++; // Increase number of keys in child
            sibling.n--; // Decrease number of keys in sibling
        }
        void borrowFromNext(int i) {
            BNode child = children[i];
            BNode sibling = children[i + 1]; // Get next child, to be borrowed from
            child.keys[child.n] = keys[i]; // Move key from parent to child
            if (!child.isLeaf) // if child isnt a leaf move last first child of sibling to child
                child.children[child.n + 1] = sibling.children[0];
            keys[i] = sibling.keys[0]; // Move key from sibling to parent
            for (int j = 1; j < sibling.n; j++)// Move keys to the left
                sibling.keys[j - 1] = sibling.keys[j];
            if (!sibling.isLeaf)
                for (int j = 1; j <= sibling.n; j++)// Move children to the left
                    sibling.children[j - 1] = sibling.children[j];
            child.n++; // Increase number of keys in child
            sibling.n--; // Decrease number of keys in sibling
        }
        void merge(int i) {
            BNode child = children[i];
            BNode sibling = children[i + 1];
            child.keys[t - 1] = keys[i]; // Move key from parent to child
            for (int j = 0; j < sibling.n; j++) // Move keys from sibling to second half of child
                child.keys[j + t] = sibling.keys[j];
            if (!child.isLeaf)
                for (int j = 0; j <= sibling.n; j++) // Move children from sibling to second half of child
                    child.children[j + t] = sibling.children[j];
            for (int j = i + 1; j < n; j++) // Move keys to fill the gap created by moving to child
                keys[j - 1] = keys[j];
            for (int j = i + 2; j <= n; j--) // Move children to fill the gap created by moving to child
                children[j - 1] = children[j];
            child.n += sibling.n + 1;
            n--;
        }
        void insertNonFull(int k) { // Omega(1), O(t), Theta(t)
            int i = n -1; // first free index
            if (isLeaf) { // If node has no children
                while (i >= 0 && keys[i] > k) {
                    keys[i + 1] = keys[i];
                    i--;
                } // Moves through the array and moves elements bigger than k to the right
                // Creates insertion point
                keys[i + 1] = k; // Inserts k
                n++; // Increases number of keys
            } else { // If node has children
                while (i >= 0 && keys[i] > k) i--; // Moves through array and finds insertion point
                if (children[i + 1].n == 2 * t - 1) { // If insertion child is full
                    splitChild(i + 1, children[i + 1]); // Splits insertion child
                    if (keys[i + 1] < k) // If key at insertion point is smaller than k
                        i++; // Move insertion point one to the right
                }
                children[i + 1].insertNonFull(k); // Insert k into insertion child
            }
        }
        void splitChild(int i, BNode y) {
            BNode z = new BNode(y.t, y.isLeaf); // Creates new node akin to y
            z.n = t - 1; // Has half the number of keys as the node to be split
            for (int j = 0; j < t - 1; j++) // Copies the second half of the keys to the new node
                z.keys[j] = y.keys[j + t];
            if (!y.isLeaf) { // If y has children
                for (int j = 0; j < t; j++) // Copies the second half of the children to the new node
                    z.children[j] = y.children[j + t];
            }
            y.n = t - 1; // Node now has half the keys it had before
            for (int j = n; j > i; j--) // Searches for insertion point of new child
                children[j + 1] = children[j];
            children[i + 1] = z; // Inserts new child
            for (int j = n - 1; j >= i; j--) // Creates insertion point for new key
                keys[j + 1] = keys[j];
            keys[i] = y.keys[t - 1]; // Inserts new key
            n++; // Increases number of keys
        }
        void traverse() {
            for (int i = 0; i < n; i++) {
                if (!isLeaf)
                    children[i].traverse(); // Traverses children first in order
                System.out.print(keys[i] + " ");
            }
            if (!isLeaf)
                children[n].traverse();
        }
        BNode search(int k) {
            int i = findKey(k); // find key
            if (inNode(i)) // If k in node
                return this;
            if (isLeaf) // If k not in node and node is leaf, k doesnt exist in tree
                return null;
            return children[i].search(k); // search for k in corresponding child
        }
        void delete(int k) {
            int i = findKey(k);
            if (inNode(i)) {
                if (isLeaf)
                    deleteFromLeaf(i);
                else
                    deleteFromNonLeaf(i);
            } else {
                if (isLeaf) {
                    System.out.println("Key not found");
                    return;
                }
                boolean flag = (i == n); // if key is present in subtree of last child
                if (children[i].n < t) // If child that contains key has less than t keys
                    fill(i); // Fill that child
                if (flag && i > n) // If key is present in subtree of last child
                    children[i - 1].delete(k); // Delete from that subtree
                else // If key is not present in subtree of last child
                    children[i].delete(k); // Delete from that subtree
            }
        }
        void deleteFromLeaf (int i) {
            for (int j = i + 1; j < n; j++) // Move all keys after i to the left
                keys[j - 1] = keys[j];
            n--; // reduce number of keys
        }
        void deleteFromNonLeaf (int i) {
            int k = keys[i];
            if (children[i].n >= t) { // If child that contains key has more than t keys
                int pred = getPredecessor(i); // Get predecessor
                keys[i] = pred; // Replace key with predecessor
                children[i].delete(pred); // Delete predecessor from child
            } else if (children[i + 1].n >= t) {// If child that contains key has more than t keys
                int succ = getSuccessor(i); // Get successor
                keys[i] = succ; // Replace key with successor
                children[i + 1].delete(succ); // Delete successor from child
            } else { // If both children have less than t keys
                merge(i); // Merge children
                children[i].delete(k); // Delete key from child
            }
        }
    }
    BNode root;
    int t;
    BTree(int t) {
        root = null;
        this.t = t;
    }
    void insert(int k) {
        if (root == null) {// If tree is empty
            root = new BNode(t, true); // Create root
            root.keys[0] = k; // Insert k
            root.n = 1; // Increase number of keys
        } else { // If tree is not empty
            if (root.n == 2 * t - 1) { // If root is full
                BNode s = new BNode(t, false); // Create new node
                s.children[0] = root; // set root as first child of new node
                s.splitChild(0, root); // Split root
                int i = 0;
                if (s.keys[0] < k) i++; // If only key in new node is smaller than k, move insertion point
                s.children[i].insertNonFull(k); // Insert k in corresponding child
                root = s; // sets new node as root
            } else // If root is not full
                root.insertNonFull(k); // Simply insert
        }
    }
    void delete(int k) {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        }
        root.delete(k); // Delete k
        if (root.n == 0) { // If root is empty
            if (root.isLeaf) // If root is leaf -> tree is empty
                root = null; // Delete root
            else
                root = root.children[0]; // Replace root with its child
        }
    }
    void search (int k) {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        }
        root.search(k); // Search for k
    }
    void traverse() {
        if (root != null)
            root.traverse();
    }
}
