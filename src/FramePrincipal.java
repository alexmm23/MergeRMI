import javax.swing.*;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.random.RandomGenerator;

public class FramePrincipal extends JFrame implements ActionListener {
    private JButton button1;
    private JButton button2;
    private JButton button3;
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
    private String nom;
    private Registry rmii;
    private chatServidor servidor;
    private JLabel resultadoSecuencial;
    private JLabel resultadoExecutor;
    private JLabel resultadoForkJoin;
    private implementacionClienteChat task;

    public FramePrincipal() throws RemoteException, NotBoundException {
        rmii = LocateRegistry.getRegistry("localhost", 3000);
        servidor = (chatServidor) rmii.lookup("Chat");
        resultadoExecutor = new JLabel("Resultado Executor");
        resultadoExecutor.setBounds(350, 290, 200, 30);
        resultadoSecuencial = new JLabel("Resultado Secuencial");
        resultadoSecuencial.setBounds(350, 230, 200, 30);
        resultadoForkJoin = new JLabel("Resultado ForkJoin");
        resultadoForkJoin.setBounds(350, 350, 200, 30);
        txtAFirstArray = new JTextArea(200,200);
        button3 = new JButton("Generar array");
        txtAFirstArray.setLineWrap(true);
        txtAResults = new JTextArea(200,200);
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
        add(label2);
        add(label3);
        add(resultadoExecutor);
        add(resultadoSecuencial);
        add(resultadoForkJoin);
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
        nom = "Cliente " + RandomGenerator.getDefault().nextInt(0, 100);
        //task = implementacionClienteChat.getInstance(new int[]{1, 2}, 0, 1, servidor, nom);
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
            int optionNumberSelected = 0;
            if (option == radioSecuencial.getModel()) {
                optionNumberSelected = 1;
                System.out.println("Secuencial");
            } else if (option == radioExecutor.getModel()) {
                optionNumberSelected = 2;
                System.out.println("Executor");
            } else if (option == radioForkJoin.getModel()) {
                optionNumberSelected = 3;
                System.out.println("ForkJoin");
            } else {
                System.out.println("Secuencial");
            }
            try{
                String res = sendToServer(arr, 0, arr.length - 1, optionNumberSelected);
                //txtAResults.setText(res);
                //System.out.println("Resultado: " + res + " en el cliente");
            }catch (RemoteException | NotBoundException ex){
                ex.printStackTrace();
            } catch (ExecutionException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            switch (optionNumberSelected){
                case 1:
                    resultadoSecuencial.setText("Resultado Secuencial: " + (System.currentTimeMillis() - startTime) + " ms");
                    break;
                case 2:
                    resultadoExecutor.setText("Resultado Executor: " + (System.currentTimeMillis() - startTime) + " ms");
                    break;
                case 3:
                    resultadoForkJoin.setText("Resultado ForkJoin: " + (System.currentTimeMillis() - startTime) + " ms");
                    break;
            }
            final long endTime = System.currentTimeMillis();

            System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
            //label.setText("Resultado: " + (endTime - startTime) + " milliseconds");
        }
        if (e.getSource() == button2) {
            txtAResults.setText("");
            txtAFirstArray.setText("");
            textField.setText("");
            resultadoExecutor.setText("Resultado Executor");
            resultadoSecuencial.setText("Resultado Secuencial");
            resultadoForkJoin.setText("Resultado ForkJoin");
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
            txtAFirstArray.setText("Array generado: " + firstArrayText + "\n");
        }
    }
    public int[] getRandomArray(int tam){
        int[] arr = new int[tam];
        for (int i = 0; i < tam; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        return arr;
    }
    public String sendToServer(int[] arr, int startingIndex, int endingIndex, int metodo) throws RemoteException, NotBoundException, ExecutionException, InterruptedException {
        task = implementacionClienteChat.getInstance(arr, startingIndex, endingIndex, servidor, nom);
        //task.setNombre(nom);
        task.setMetodo(metodo);
        task.setTextAreaResults(txtAResults);
        task.setTextAreaOrigin(txtAFirstArray);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> future = executorService.submit(task);
        //System.out.println("Resultado: " + future.get() + " en el cliente");
        return future.get();
    }
    public static void main(String[] args) {
        try{
            FramePrincipal frame = new FramePrincipal();
            frame.setVisible(true);
        }catch (RemoteException | NotBoundException e){
            e.printStackTrace();
        }
    }
}
