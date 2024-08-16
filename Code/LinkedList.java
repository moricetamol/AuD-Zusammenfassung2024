class LinkedList {
    class LinkedElement {
        Integer key = null;
        LinkedElement next = null;

        LinkedElement(Integer key) {
            this.key = key;
        }
    }
    LinkedElement head = null; // First element in list
    LinkedElement tail = null; // Last element in list

    void insert(int k) { // O(1)
        LinkedElement elem = new LinkedElement(k);
        if (head == null) {
            head = elem;
            tail = elem;
        }
        else {
            tail.next = elem;
            tail = elem;
        }
    }

    void delete(int k) { // O(n)
        LinkedElement prev = null;
        LinkedElement curr = head;
        while (curr != null && curr.key != k) {
            prev = curr;
            curr = curr.next;
        }
        if (curr == null)
            throw new UException("Element not found");

        if (prev != null) {
            prev.next = curr.next;
            if (curr == tail)
                tail = prev;
        } else {
            head = curr.next;
        }
    }

    LinkedElement search(int k) { // O(n)
        LinkedElement curr = head;
        while (curr != null && curr.key != k)
            curr = curr.next;
        if (curr == null)
            throw new UException("Element not found");
        return curr;
    }
}
