class Stack {
    private int[] arr;
    private int top;

    Stack(int size) {
        arr = new int[size];
        top = -1;
        // Creates a new array with size
    }

    boolean isEmpty() {
        return top < 0;
        // Returns true if empty
    }

    int pop() {
        return arr[top--];
        // Removes and returns the top element
    }

    void push(int k) {
        arr[++top] = k;
        // Adds an element
    }
}
