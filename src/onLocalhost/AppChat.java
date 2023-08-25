package onLocalhost;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class AppChat {

    
    /** 
     * @param args
     */
    public static void main(String[] args) {

        MarcoApp marco=new MarcoApp();


        marco.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Integer serverPort = Chat.getMyPort();
                InetAddress serverInet = Chat.getMyInet();
                try {
                    Socket mysocket = new Socket("localhost", 9999); //envia

                    paqueteDato online = new paqueteDato();

                    online.setPuerto(serverPort);
                    online.setMensaje("OFFLINE");


                    ObjectOutputStream flujo = new ObjectOutputStream(mysocket.getOutputStream());

                    flujo.writeObject(online);

                    mysocket.close();
                    System.exit(0);

                } catch (UnknownHostException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }catch (Exception e2) {
                    System.out.println("Error desconocido 2");
                }

            }
        });
    }


}


class MarcoApp extends JFrame{

    public MarcoApp() {

        setBounds(600, 300, 280, 350);

        Chat ChatApp = new Chat();

        add(ChatApp);

        setVisible(true);

        addWindowListener(new online());
    }

}

//Clase para enviar la ip del usuario online
class online extends WindowAdapter {
    Integer serverPort = Chat.getMyPort();
    InetAddress serverInet = Chat.getMyInet();
    @Override
    public void windowOpened(WindowEvent e) {
        try {
            Socket mysocket = new Socket("localhost", 9999); //envia

            paqueteDato online = new paqueteDato();

            online.setPuerto(serverPort);
            online.setMensaje("ONLINE");


            ObjectOutputStream flujo = new ObjectOutputStream(mysocket.getOutputStream());

            flujo.writeObject(online);

            mysocket.close();

        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }catch (Exception e2) {
            System.out.println("Error desconocido 2");
        }
    }

}
class Chat extends JPanel implements Runnable {

    String nombreUsuario = new String();
    private static Integer myPort;
    private static InetAddress myInet;

    public Chat() {

        nombreUsuario = JOptionPane.showInputDialog("Nombre de usuario: ");

        JLabel usuario = new JLabel("Usuario: ");

        add(usuario);

        JLabel nombre = new JLabel();

        nombre.setText(nombreUsuario);

        add(nombre);

        JLabel texto = new JLabel("Online: ");

        add(texto);

        ip = new JComboBox();

        add(ip);

        chat = new JTextArea(12, 20);

        add(chat);

        chatApp = new JTextField(20);

        add(chatApp);

        System.out.println(chatApp.getText());

        boton = new JButton("Enviar");

        enviarTexto event = new enviarTexto();

        boton.addActionListener(event);

        add(boton);

        Thread hilo = new Thread(this);

        hilo.start();

    }

    private JTextField chatApp;

    private JComboBox ip;

    private JTextArea chat;

    private JButton boton;

    public static Integer getMyPort() {
        return myPort;
    }

    public static InetAddress getMyInet() {
        return myInet;
    }

    @Override
    public void run() {
        try {

            ServerSocket server = new ServerSocket(0);//puerto del cliente a la espera
            myPort= server.getLocalPort();
            myInet= server.getInetAddress();

            Socket cliente;

            paqueteDato paqueteR;

            while (true) {

                cliente = server.accept();

                ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());

                paqueteR = (paqueteDato) flujoEntrada.readObject();

                if (!paqueteR.getMensaje().equals("ONLINE") && !paqueteR.getMensaje().equals("OFFLINE")){

                    chat.append("\n" + paqueteR.getUsuario() + ": " + paqueteR.getMensaje());

                }else {

                    ArrayList <Integer> ipDesplegable = new ArrayList<Integer>();

                    ipDesplegable = paqueteR.getIPs();

                    ip.removeAllItems();

                    for (Integer z:ipDesplegable){

                        ip.addItem(z);
                    }
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private class enviarTexto implements ActionListener { //ActionListener se usa para detectar y manejar eventos de acción
        @Override
        public void actionPerformed(ActionEvent e) {
            chat.append("\n" + chatApp.getText());
            try {
                Socket mysocket = new Socket("localhost",9999); //Abre el socket

                paqueteDato datos = new paqueteDato(); //Crear un paquete con la información que se va a enviar (Objeto)

                datos.setUsuario(nombreUsuario);

                datos.setIp(Objects.requireNonNull(ip.getSelectedItem()).toString());

                datos.setPuerto(Objects.requireNonNull((Integer) ip.getSelectedItem()));

                datos.setMensaje(chatApp.getText());

                ObjectOutputStream paqueteDatos = new ObjectOutputStream(mysocket.getOutputStream()); //Flujo de salidad para poder enviar el objeto por la red

                paqueteDatos.writeObject(datos);

                mysocket.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

class paqueteDato implements Serializable {  //"implements Serializable" es para que todos los obj sea puedan hacer en bit

    private String ip, mensaje, usuario;
    private Integer puerto;

    private ArrayList<Integer> IPs;

    public ArrayList<Integer> getIPs() {
        return IPs;
    }

    public void setIPs(ArrayList<Integer> IPs) {
        this.IPs = IPs;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIp2(ArrayList ip) {
        this.ip = String.valueOf(ip);
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }
}