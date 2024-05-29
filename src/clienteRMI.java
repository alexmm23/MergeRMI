import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class clienteRMI {
    public static void main(String[] args) {
        try {
            String nombre = JOptionPane.showInputDialog("Introduce tu nombre: ");
            String nom = nombre;
            Registry rmii = LocateRegistry.getRegistry("25.49.132.248", 1099);
            chatServidor servidor = (chatServidor) rmii.lookup("Chat");
            //new Thread(new implementacionClienteChat(nom,servidor)).start();
        } catch (Exception e) {
            System.out.println("Excepcion en clienteRMI: " + e);
            e.printStackTrace();
        }

      }
}
