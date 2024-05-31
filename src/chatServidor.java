import java.rmi.Remote;

public interface chatServidor extends Remote {
    void registro(chatCliente cliente) throws java.rmi.RemoteException;
    void mensaje(String mensaje) throws java.rmi.RemoteException;
    String entradaCliente(int[]arr, int inicio, int fin, int metodo) throws java.rmi.RemoteException;
}
