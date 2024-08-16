class Quicksort {
    void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            // Region contains more than one element
            int part = partition(arr, left, right);
            quickSort(arr, left, part);
            quickSort(arr, part + 1, right);
        }
    }

    private int partition(int[] arr, int left, int right) {
        int pivot = arr[left];
        // Pivot is the first element in the region

        int p = left - 1;
        int q = right + 1;
        while (p < q) {
            do p++; while (arr[p] < pivot);
            do q--; while (arr[q] > pivot);
            // Increase / decrease p and q until the elements are bigger/smaller-equal pivot
            if (p < q) {
                /* p < q here means that theres a number bigger equal pivot on the left side
                and a number smaller equal than the pivot on the right side
                Therefore, we swap them to sort them into their halves*/
                int temp = arr[p];
                arr[p] = arr[q];
                arr[q] = temp;
                // Swap arr[p] and arr[q]
            }
        } /* This loop runs until p and q cross each other
        which means that */
        return q;
        // q is the index at which:
        // all indices greater than q contain elements bigger equal pivot
        // all indices smaller equal q contain elements smaller equal pivot
    }
}
