import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MergeSortExecutor{
    public static void divide(int [] arr, int startingIndex, int endingIndex){
        if(startingIndex < endingIndex){
            ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            int middleIndex = (startingIndex + endingIndex) / 2;
            ex.execute(()->MergeSort.divide(arr, startingIndex, middleIndex));
            ex.execute(()->MergeSort.divide(arr, middleIndex + 1, endingIndex));
            ex.shutdown();
            try{
                ex.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                MergeSort.merge(arr, startingIndex, middleIndex, endingIndex);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
