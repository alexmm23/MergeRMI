public class MergeSort {
    public static void divide(int[] arr, int startingIndex,int endingIndex){
        if(startingIndex<endingIndex) {
            int middleIndex = (startingIndex + endingIndex) / 2;
            //Se divide con los indices del array, se marcan los limites de la division
            //Impresion de debug
            /*System.out.println("Array divided into two parts: ");
            System.out.println("First part: ");
            for(int i=startingIndex;i<=middleIndex;i++){
                System.out.print(arr[i]+" ");
            }
            System.out.println();
            System.out.println("Second part: ");
            for(int i=middleIndex+1;i<=endingIndex;i++){
                System.out.print(arr[i]+" ");
            }
            System.out.println();
            */
            divide(arr, startingIndex, middleIndex);
            divide(arr, middleIndex + 1, endingIndex);
            //Cuando se llega a la ultima division, se llama a la funcion merge para unir de abajo hacia arriba
            merge(arr,startingIndex,middleIndex,endingIndex);
        }
    }
    public static void merge(int[] arr, int startingIndex,int middleIndex, int endingIndex){
        int arrayTam1 = middleIndex - startingIndex + 1;
        int arrayTam2 = endingIndex - middleIndex;

        //Hay que crear arreglos auxiliares para ordenarlos individualemnte
        int[] leftArray = new int[arrayTam1];
        int[] rightArray = new int[arrayTam2];
        //Se copian los valores del array original a los auxiliares
        for(int i = 0; i < arrayTam1; i++){
            leftArray[i] = arr[startingIndex + i];
        }
        for(int j = 0; j < endingIndex - middleIndex; j++){
            rightArray[j] = arr[middleIndex + 1 + j];
        }
        //Se crean indices para recorrer los arreglos auxiliares
        int i=0,j=0;
        //Se crea un indice para recorrer el arreglo original
        int k=startingIndex;

        while(i<arrayTam1 && j<arrayTam2){
            if(leftArray[i]<=rightArray[j]){
                arr[k] = leftArray[i];
                i++;
            }else{
                arr[k] = rightArray[j];
                j++;
            }
            k++;
        }
        while(i<arrayTam1){
            arr[k] = leftArray[i];
            i++;
            k++;
        }
        while (j < arrayTam2){
            arr[k] = rightArray[j];
            j++;
            k++;
        }

    }

}
