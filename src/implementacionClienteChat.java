import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class implementacionClienteChat extends UnicastRemoteObject implements chatCliente, Callable<String> {
    public chatServidor servidor;
    public String nombre  = null;
    public int[] arr ;
    public int inicio;
    public int fin;
    public char metodo;
    implementacionClienteChat(String nombre, chatServidor servidor) throws RemoteException {
        this.nombre = nombre;
        this.servidor = servidor;
        servidor.registro(this);
    }
    implementacionClienteChat(int[] arr,int inicio, int fin,  chatServidor servidor) throws RemoteException {
        this.arr = arr;
        this.servidor = servidor;
        this.inicio = inicio;
        this.fin = fin;
        servidor.registro(this);
    }
    @Override
    public void mensajeCliente(String mensaje) throws RemoteException{
        System.out.println(mensaje);
    }

    @Override
    public String entradaCliente(int[] arr, int inicio, int fin, char metodo) throws RemoteException {
        String respuesta = "";
        if(metodo == 1){
            MergeSort.divide(arr, inicio, fin);
        }
        System.out.println("Arreglo ordenado: ");
        for(int i = inicio; i <= fin; i++){
            System.out.print(arr[i] + " ");
            respuesta += arr[i] + " ";
        }
        return respuesta;
    }

    @Override
    public String call() {
        Scanner s = new Scanner(System.in);
        String mensaje;
        while (true) {
            mensaje = s.nextLine();
            try {
                //servidor.mensaje(nombre + " dice: " + mensaje);
                servidor.entradaCliente(arr, inicio, fin, (char)1);
                return servidor.entradaCliente(arr, inicio, fin, (char)1);
            } catch (RemoteException e) {
                System.out.println("Excepcion en implementacionClienteChat: " + e);
                e.printStackTrace();
            }
        }
    }
}
