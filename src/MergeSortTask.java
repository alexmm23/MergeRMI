import java.util.concurrent.RecursiveAction;

class MergeSortTask extends RecursiveAction {
    private static final int THRESHOLD = 10;
    private int[] array;
    private int left;
    private int right;
    private int[] temp;

    public MergeSortTask(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
        this.temp = new int[right - left + 1];
    }

    @Override
    protected void compute() {
        if (left < right) {
            if (right - left <= THRESHOLD) {
                insertionSort(array, left, right);
            } else {
                int mid = (left + right) / 2;

                MergeSortTask leftTask = new MergeSortTask(array, left, mid);
                MergeSortTask rightTask = new MergeSortTask(array, mid + 1, right);

                invokeAll(leftTask, rightTask);

                merge(left, mid, right);
            }
        }
    }

    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= left && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    private void merge(int left, int mid, int right) {
        System.arraycopy(array, left, temp, 0, right - left + 1);

        int i = 0, j = mid - left + 1, k = left;

        while (i <= mid - left && j <= right - left) {
            if (temp[i] <= temp[j]) {
                array[k++] = temp[i++];
            } else {
                array[k++] = temp[j++];
            }
        }

        while (i <= mid - left) {
            array[k++] = temp[i++];
        }

        while (j <= right - left) {
            array[k++] = temp[j++];
        }
    }
}

