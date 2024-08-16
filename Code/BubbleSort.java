class BubbleSort {

    void bubbleSort(int[] arr) {
        for(int i = arr.length - 1; i > 0; i--) {
            // Runs from arr.length - 1 to 0 (non exclusive)
            // (i = 0 would immediately terminate)
            boolean sorted = true;
            for(int j = 0; j < i; j++) {
                // Runs from 0 to i - 1
                if(arr[j] > arr[j + 1]) {
                    // If the current element is greater than the next
                    // Swap them
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    sorted = false;
                }
            }
            if(sorted)
                break;
        }
    }
}
