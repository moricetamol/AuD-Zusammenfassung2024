class LinkedList {
    LinkedElement head = null;

    void insert(int k) {
        LinkedElement elem = new LinkedElement(k);
        if (head == null) {
            head = elem;
        }
        else {
            head.next = elem;
            head = elem;
        }
    }

    void delete(int k) {
        LinkedElement prev = null;
        LinkedElement curr = head;
        while (curr != null && curr.key != k) {
            prev = curr;
            curr = curr.next;
        }
        if (curr == null) {
            throw new UException("Element not found");
        }
        if (prev != null) {
            prev.next = curr.next;
        }
        else {
            head = curr.next;
        }
    }

    LinkedElement search(int k) {
        LinkedElement curr = head;
        while (curr != null && curr.key != k) {
            curr = curr.next;
        }
        if (curr == null) {
            throw new UException("Element not found");
        }
        return curr;
    }
}
