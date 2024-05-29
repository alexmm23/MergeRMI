import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class implementacionChat extends UnicastRemoteObject implements chatServidor {
    public ArrayList<chatCliente> clientes;
    public implementacionChat() throws RemoteException {
        //super();
        clientes = new ArrayList<chatCliente>();
    }

    @Override
    public void registro(chatCliente cliente) throws RemoteException {
        clientes.add(cliente);
    }

    @Override
    public void mensaje(String mensaje) throws RemoteException {
        int a = 0;
        while (a < clientes.size()) {
            clientes.get(a).mensajeCliente(mensaje);
            a++;
        }
    }
    public String entradaCliente(int[]arr, int inicio, int fin, char metodo) throws RemoteException {
        int a = 0;
        StringBuilder respuesta = new StringBuilder();
        while (a < clientes.size()) {
            respuesta.append(clientes.get(a).entradaCliente(arr, inicio, fin, metodo));
            a++;
        }
        return respuesta.toString();
    }
}
