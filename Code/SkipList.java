class SkipList {
    class SkipNode {
        int key;
        SkipNode next;
        SkipNode prev;
        SkipNode down;
        SkipNode up;
        SkipNode(int k) {
            key = k;
        }
    }
    SkipNode head; // First node in highest level
    int height; // starts at 0 -> biggest list at height = 0
    final double P = 0.5; // Probability of inserted node being added to express list
    SkipList() {
        head = new SkipNode(Integer.MIN_VALUE);
        height = 0;
    }
    SkipNode search(int k) {
        SkipNode curr = head;
        while (curr != null) {
            if (curr.key == k) // key found
                return curr;
            else if (curr.next != null && curr.next.key <= k) // If next key is less than or equal to k
                curr = curr.next; // move along the list
            else // If next key is greater than k
                curr = curr.down; // move down the list
        }
        return null; // key not found
    }
    int randomLevel() { // Used to determine in how many lists a node will be added
        double r = Math.random();
        int lvl = 0;
        while (r < P) {
            lvl++;
            r = Math.random();
        }
        return lvl;
    }
    void insert(int k) {
        int lvl = randomLevel();
        while (lvl > height) { // If needed increase list height and add required heads
            head.up = new SkipNode(Integer.MIN_VALUE);
            head.up.down = head;
            head = head.up;
            height++;
        }
        SkipNode[] prevs = new SkipNode[height + 1]; // Holds the previous nodes in each list
        SkipNode curr = head;
        for (int i = height; i >= 0; i--) { // For each level starting from the highest
            while (curr.next != null && curr.next.key < k)
                // Loops through current list until next key is greater than or equal to k
                curr = curr.next;
            if (curr.key == k) return; // key already in list
            prevs[i] = curr; // Adds current node as a predecessor to the new node
            curr = curr.down; // Moves down in the list
        }
        int count = 0; // counter for number of lists in which the new node has been added
        SkipNode dwn = null; // Holds the node that is the down node of the node in a level
        while (count <= lvl) { // Add new nodes to lists in lvl
            SkipNode newNode = new SkipNode(k);
            newNode.next = prevs[count].next; // Includes the new node in the list
            newNode.prev = prevs[count];
            if (prevs[count].next != null) // If there is a next node
                prevs[count].next.prev = newNode; // change previous to new node
            prevs[count].next = newNode; // change next to new node
            newNode.down = dwn; // connect newNode to itself on a lower level
            if (dwn != null) // If added to more than 1 level
                dwn.up = newNode; // connect lower level to newNode
            dwn = newNode; // Update dwn
            count++; // Update counter
        }
    }
    void delete(int k) {
        SkipNode node = search(k); // Find first node in list that has k
        while (node != null) { // Moves down from the found node
            // Remove node from list by removing its references
            node.prev.next = node.next;
            if (node.next != null)
                node.next.prev = node.prev;
            if (node.next == null
                    && node.prev.key == Integer.MIN_VALUE
                    && node.prev.down != null) {
                // If deleted node is the last node in the list, reduce list height and update head
                node.prev.down.up = null;
                head = node.prev.down;
                height--;
            }
            node = node.down;
        }
    }
    void output() { // Only for testing, should not be included in tex
        SkipNode curr = head;
        SkipNode currHead = head;
        for (int i = height; i >= 0; i--) {
            while (curr != null) {
                System.out.print(curr.key + " ");
                curr = curr.next;
            }
            System.out.println();
            currHead = currHead.down;
            curr = currHead;
        }
    }
}
