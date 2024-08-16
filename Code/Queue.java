class Queue {
    private int[] arr;
    private int front;
    private int back;

    Queue(int size) {
        arr = new int[size];
        front = -1;
        back = -1;
    }

    boolean isEmpty() {
        return back == -1;
    }

    boolean isFull() {
        return (front + 1) % arr.length == back;
        // If front + 1 is equal to back, the queue is full
        // Modulo makes this usable for cyclic arrays
    }

    void enqueue(int k) { // O(1)
        if (isFull()) {
            throw new UException("Queue is full");
        } else{
            if (isEmpty())
                front = 0;
            back = (back + 1) % arr.length;
            // Modulo so that cyclic arrays work
            arr[back] = k;
        }
    }

    int dequeue() { // O(1)
        if (isEmpty()) {
            throw new UException("Queue is empty");
        } else {
            int temp = arr[front];
            front = (front + 1) % arr.length;
            // Modulo so that cyclic arrays work
            if (front == back) {
                front = -1;
                back = -1;
            }
            // If front and back are equal, the queue is empty -> reset
            return temp;
        }
    }
}
