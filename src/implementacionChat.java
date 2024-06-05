import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class implementacionChat extends UnicastRemoteObject implements chatServidor {
    public ArrayList<chatCliente> clientes;
    public implementacionChat() throws RemoteException {
        //super();
        clientes = new ArrayList<>();
    }

    @Override
    public void registro(chatCliente cliente) throws RemoteException {
        clientes.add(cliente);
        System.out.println("Cliente registrado: " +  cliente.getNombre());
        System.out.println("Clientes conectados: " + clientes.size());
    }

    @Override
    public void mensaje(String mensaje) throws RemoteException {
        int a = 0;
        while (a < clientes.size()) {
            clientes.get(a).mensajeCliente(mensaje);
            a++;
        }
    }
    public String entradaCliente(int[]arr, int inicio, int fin, int metodo) throws RemoteException {
        int a = 0;
        StringBuilder respuesta = new StringBuilder();
        while (a < clientes.size()) {
            clientes.get(a).setNombre(clientes.get(a).getNombre());
            respuesta.append(clientes.get(a).entradaCliente(arr, inicio, fin, metodo));
            //System.out.println("Clite: "+clientes.get(a).getNombre);
            a++;
        }
        return respuesta.toString();
    }
}
