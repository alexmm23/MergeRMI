import java.rmi.Remote;

public interface chatCliente extends Remote {
    void mensajeCliente(String mensaje) throws java.rmi.RemoteException;
    String entradaCliente(int[]arr, int inicio, int fin, int metodo) throws java.rmi.RemoteException;
}
