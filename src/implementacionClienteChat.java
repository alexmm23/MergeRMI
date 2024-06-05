import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.*;

public final class implementacionClienteChat extends UnicastRemoteObject implements chatCliente, Callable<String> {
    public chatServidor servidor;
    public String nombre  = null;
    public int[] arr ;
    public int inicio;
    public int fin;
    public int metodo;
    public JTextArea textAreaResults;
    public JTextArea textAreaOrigin;
    private static implementacionClienteChat instance = null;

    private implementacionClienteChat(String nombre, chatServidor servidor) throws RemoteException {
        this.nombre = nombre;
        this.servidor = servidor;
        servidor.registro(this);
    }
    private implementacionClienteChat(int[] arr,int inicio, int fin,  chatServidor servidor, String nombre) throws RemoteException {
        this.arr = arr;
        this.servidor = servidor;
        this.inicio = inicio;
        this.fin = fin;
        this.nombre = nombre;
        servidor.registro(this);
    }
    public static implementacionClienteChat getInstance(int[] arr, int inicio, int fin, chatServidor servidor, String nombre) throws RemoteException {
        if(instance == null){
            instance = new implementacionClienteChat(arr, inicio, fin, servidor, nombre);
        }else{
            instance.arr = arr;
            instance.inicio = inicio;
            instance.fin = fin;
            instance.nombre = nombre;
        }
        return instance;
    }
    @Override
    public void mensajeCliente(String mensaje) throws RemoteException{
        System.out.println(mensaje);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMetodo(int metodo) {
        this.metodo = metodo;
    }
    public void setTextAreaResults(JTextArea textArea) {
        this.textAreaResults = textArea;
    }
    public void setTextAreaOrigin(JTextArea textArea) {
        this.textAreaOrigin = textArea;
    }

    @Override
    public String entradaCliente(int[] arr, int inicio, int fin, int metodo) throws RemoteException {
        textAreaOrigin.append("Array antes de ordenar para cliente: "+nombre + "\n" + Arrays.toString(arr));
        textAreaOrigin.append("\n\n");
        String respuesta = "";
        if(metodo == 1){
            MergeSort.divide(arr, inicio, fin);
        } else if (metodo == 2) {
            MergeSortExecutor.sort(arr);
        } else if (metodo == 3) {
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new MergeSortTask(arr, inicio, fin));
        } else {
            MergeSort.divide(arr, inicio, fin);
        }
        respuesta += "Array ordenado para cliente: "+nombre + "\n";
        for(int i = inicio; i <= fin; i++){
            System.out.print(arr[i] + " ");
            respuesta += arr[i] + " ";
        }
        System.out.println();
        textAreaResults.append(respuesta);
        textAreaResults.append("\n\n");
        return respuesta;

    }

    @Override
    public String getNombre() throws RemoteException {
        return this.nombre;
    }

    @Override
    public String call() {
        Scanner s = new Scanner(System.in);
        String mensaje;
        while (true) {
            //mensaje = s.nextLine();
            try {
                //servidor.mensaje(nombre + " dice: " + mensaje);
                //servidor.entradaCliente(arr, inicio, fin, (char)1);
                return servidor.entradaCliente(arr, inicio, fin, metodo);
            } catch (RemoteException e) {
                System.out.println("Excepcion en implementacionClienteChat: " + e);
                e.printStackTrace();
            }
        }
    }
}
