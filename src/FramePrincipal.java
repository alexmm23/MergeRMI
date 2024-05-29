import javax.swing.*;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.concurrent.*;

public class FramePrincipal extends JFrame implements ActionListener {
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JTextField textField;
    private JLabel label;
    private JLabel label2;
    private JLabel label3;
    private JRadioButton radioSecuencial;
    private JRadioButton radioExecutor;
    private JRadioButton radioForkJoin;
    private ButtonGroup group;
    private JTextArea txtAResults;
    private JTextArea txtAFirstArray;
    private JScrollPane scrollPane;
    private JScrollPane scrollPane2;
    private int[] arr;
    private int[] originalArray;

    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public FramePrincipal() {
        txtAFirstArray = new JTextArea(200,200);
        button3 = new JButton("Generar array");
        txtAFirstArray.setLineWrap(true);
        txtAResults = new JTextArea(200,200);
        //txtAResults.setBounds(300, 30, 200, 200);
        txtAResults.setLineWrap(true);
        scrollPane = new JScrollPane(txtAFirstArray);
        scrollPane.setBounds(50, 30, 200, 200);
        scrollPane2 = new JScrollPane(txtAResults);
        scrollPane2.setBounds(300, 30, 200, 200);
        radioSecuencial = new JRadioButton("Secuencial");
        radioSecuencial.setBounds(350, 250, 100, 30);
        radioExecutor = new JRadioButton("Executor");
        radioExecutor.setBounds(350, 310, 100, 30);
        radioForkJoin = new JRadioButton("ForkJoin");
        radioForkJoin.setBounds(350, 370, 100, 30);
        group = new ButtonGroup();
        group.add(radioSecuencial);
        group.add(radioExecutor);
        group.add(radioForkJoin);
        button1 = new JButton("Calcular");
        button1.setBounds(50, 350, 95, 30);
        button2 = new JButton("Borrar");
        button2.setBounds(150, 350, 95, 30);
        button3.setBounds(50, 250, 200, 30);
        label = new JLabel("Resultado:");
        label.setBounds(200, 500, 200, 30);
        label2 = new JLabel("Array Original");
        label2.setBounds(50, 0, 200, 30);
        label3 = new JLabel("Array Ordenado");
        label3.setBounds(300, 0, 200, 30);
        textField = new JTextField("Ingresa numero de elementos");
        textField.setBounds(50, 300, 200, 30);
        add(button1);
        add(button2);
        add(button3);
        add(textField);
        add(label);
        add(label2);
        add(label3);
        //add(txtAResults);
        //add(txtAFirstArray);
        add(scrollPane);
        add(scrollPane2);
        add(radioSecuencial);
        add(radioExecutor);
        add(radioForkJoin);
        setTitle("Merge Sort");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);

    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == button1) {
            String currentText = textField.getText();
            if(currentText.isBlank() || currentText.isEmpty() || !currentText.matches("[0-9]+") ){
                JOptionPane.showMessageDialog(this,"El numero no puede estar vacio o contener letras.","Advertencia",JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(arr == null){
               JOptionPane.showMessageDialog(this,"Primero debes generar un array.","Advertencia",JOptionPane.WARNING_MESSAGE);
               return;
            }
            arr = Arrays.copyOf(originalArray, originalArray.length);
            String firstArrayText = Arrays.toString(arr);
            txtAFirstArray.setText("Primer array: " + firstArrayText);
            final long startTime = System.currentTimeMillis();
            var option = group.getSelection();
            if (option == radioSecuencial.getModel()) {
                //MergeSort.divide(arr, 0, arr.length - 1);
                try{
                    String res = sendToServer(arr, 0, arr.length - 1);
                    txtAResults.setText(res);
                    System.out.println("Resultado: " + res + " en el cliente");
                }catch (RemoteException | NotBoundException ex){
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("Secuencial");
            } else if (option == radioExecutor.getModel()) {
                //MergeSortExecutor.divide(arr, 0, arr.length - 1);
                System.out.println("Executor");
            } else if (option == radioForkJoin.getModel()) {
                ForkJoinPool pool = new ForkJoinPool();
                pool.invoke(new MergeSortTask(arr, 0, arr.length - 1));
                System.out.println("ForkJoin");
            } else {
                //MergeSort.divide(arr, 0, arr.length - 1);
                System.out.println("Secuencial");
            }
            String result = Arrays.toString(arr);
            final long endTime = System.currentTimeMillis();
            //txtAResults.setText("Resultado: " + result);
            System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
            label.setText("Resultado: " + (endTime - startTime) + " milliseconds");
        }
        if (e.getSource() == button2) {
            txtAResults.setText("");
            txtAFirstArray.setText("");
            textField.setText("");
            arr = null;
            originalArray = null;
        }
        if(e.getSource() == button3){
            if(textField.getText().isEmpty() || textField.getText().isBlank()){
                JOptionPane.showMessageDialog(this,"El campo no puede estar vacio.","Advertencia",JOptionPane.WARNING_MESSAGE);
                return;
            }
            int n = Integer.parseInt(textField.getText());
            originalArray = getRandomArray(n);
            arr = Arrays.copyOf(originalArray, originalArray.length);
            String firstArrayText = Arrays.toString(originalArray);
            txtAFirstArray.setText("Primer array: " + firstArrayText);
        }
    }
    public int[] getRandomArray(int tam){
        int[] arr = new int[tam];
        for (int i = 0; i < tam; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        return arr;
    }
    public String sendToServer(int[] arr, int startingIndex, int endingIndex) throws RemoteException, NotBoundException, ExecutionException, InterruptedException {
        String nom = "Alex";
        Registry rmii = LocateRegistry.getRegistry("localhost", 3000);
        chatServidor servidor = (chatServidor) rmii.lookup("Chat");
        implementacionClienteChat task = new implementacionClienteChat(arr, startingIndex, endingIndex, servidor);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = (Future<String>) executorService.submit(task);
        System.out.println("Resultado: " + future.get() + " en el cliente");
        return future.get();
    }
    public static void main(String[] args) {
        FramePrincipal frame = new FramePrincipal();
        frame.setVisible(true);
    }
}
