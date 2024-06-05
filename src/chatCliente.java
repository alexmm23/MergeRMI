import java.rmi.Remote;

public interface chatCliente extends Remote {
    void mensajeCliente(String mensaje) throws java.rmi.RemoteException;
    String entradaCliente(int[]arr, int inicio, int fin, int metodo) throws java.rmi.RemoteException;
    String getNombre() throws java.rmi.RemoteException;
    void setNombre(String nombre) throws java.rmi.RemoteException;
}
