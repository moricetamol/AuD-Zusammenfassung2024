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

        int p = left - 1;
        int q = right + 1;
        while (p < q) {
            while (arr[p] < pivot) {
                p++;
            }
            while (arr[q] > pivot) {
                q--;
            }
            // Increase / decrease p and q until they are equal to pivot
            if (p < q) {
                int temp = arr[p];
                arr[p] = arr[q];
                arr[q] = temp;
                // Swap arr[p] and arr[q]
            }
        }
        return q;
    }
}
