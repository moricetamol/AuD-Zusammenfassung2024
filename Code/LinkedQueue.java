class LinkedQueue {
    LinkedElement front = null;
    LinkedElement back = null;

    boolean isEmpty() {
        return front == null;
    }

    void enqueue(int k) {
        if (isEmpty()) {
            front = new LinkedElement(k);
            back = front;
        } else {
            back.next = new LinkedElement(k);
            back = back.next;
        }
    }

    int dequeue() {
        if (isEmpty()) {
            throw new UException("Queue is empty");
        } else {
            int temp = front.key;
            front = front.next;
            return temp;
        }
    }
}
