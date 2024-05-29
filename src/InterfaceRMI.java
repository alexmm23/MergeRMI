import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote{
    String nombre(String nombre) throws RemoteException;
}
