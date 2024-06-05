import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MergeSortExecutor {

    private static final ExecutorService ex = Executors.newCachedThreadPool();

    public static void divide(int[] arr, int startingIndex, int endingIndex) {
        if (startingIndex < endingIndex) {
            int middleIndex = (startingIndex + endingIndex) / 2;

            // Ejecutar la primera mitad en un hilo separado
            ex.execute(() -> divide(arr, startingIndex, middleIndex));

            // Llamar recursivamente para la segunda mitad en el hilo actual
            divide(arr, middleIndex + 1, endingIndex);

            try {
                // Esperar a que termine la tarea de la primera mitad
                ex.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

                // Realizar la fusión después de que ambas mitades estén ordenadas
                MergeSort.merge(arr, startingIndex, middleIndex, endingIndex);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sort(int[] arr) {
        divide(arr, 0, arr.length - 1);
        ex.shutdown();
    }
}
