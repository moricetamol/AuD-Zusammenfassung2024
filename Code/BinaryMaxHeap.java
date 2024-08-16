class BinaryMaxHeap {
    Integer[] heap;
    int size; // Size used to insert new elements at the first empty index
    BinaryMaxHeap(int n) { // Creates new heap with size n
        heap = new Integer[n];
        size = 0;
    }
    BinaryMaxHeap(Integer[] arr) { // Creates heap from array
        arrayToHeap(arr);
    }
    void arrayToHeap(Integer[] arr) { // Used to transfer array to heap
        heap = arr.clone();
        size = 0;
        for (Integer i: arr) {
            if (i == null) break;
            size++;
        }
    }
    int parent(int i) {
        return (int) Math.ceil((double) i / 2) - 1;
    }
    int left(int i) {
        return 2 * (i + 1) - 1;
    }
    int right(int i) {
        return 2 * (i + 1);
    }
    boolean isEmpty() {
        return size == 0;
    }
    void insert(int k) { // O(h) = O(log n)
        size++;
        heap[size - 1] = k; // Add new element at first empty index
        int i = size - 1;
        while (i > 0 && heap[i] > heap[parent(i)]) { // Moves upward through tree
            int temp = heap[i];
            heap[i] = heap[parent(i)];
            heap[parent(i)] = temp; // Swap elements if child is bigger than parent
            i = parent(i); // Move up
        }
    }
    void heapify(int i) { // O(h) = O(log n)
        // Used if heap is new unsorted array.
        // If heap was build using insert, this should not be necessary
        int max = i;
        int l = left(i);
        int r = right(i);
        if(l < size && heap[i] < heap[l]) // If left child is bigger than i
            max = l; // Set max to left
        if(r < size && heap[max] < heap[r]) // if right child is bigger than max (i or left)
            max = r; // Set max to right
        if(max != i) { // If max is not i -> max is left or right
            int temp = heap[i];
            heap[i] = heap[max];
            heap[max] = temp;
            // Swap i and max
            heapify(max); // max is now i
            // Move down the tree
        }
    }
    void buildHeap() { // O(nh) = O(n log n)
        for(int i = parent(size - 1); i >= 0; i--)
            heapify(i); // Calls heapify on each node starting from the second to last row
        // Heap should now be sorted accordingly -> biggest node at root
    }
    int[] heapSort() { // O(nh) = O(n log n)
        // Assumes new array for heap
        buildHeap(); // Sorts heap accordingly
        int[] res = new int[size]; // Create new array for sorted result
        int i = 0;
        while(!isEmpty())
            res[i++] = extractMax(); // Extract max and add to array
        return res; // Array is now sorted in reverse natural order
    }
    int extractMax() { // O(h) = O(log n)
        if(isEmpty())
            throw new UException("Underflow");
        int max = heap[0]; // Biggest value at root
        heap[0] = heap[size - 1]; // Sets root to last element
        size--; // Decrease size
        heapify(0); // Heapify new root
        return max;
    }
}