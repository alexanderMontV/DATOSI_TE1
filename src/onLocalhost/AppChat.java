package onLocalhost;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Cliente de chat para ejecutar en localhost exlusivamente
 * @author Alexander Montero Vargas, Bryan Sibaja Garro
 *
 * */
public class AppChat {

    
    /**
     *Metodo main clase AppChat, se encarga de crear el MarcoApp y definir el evento de cierre de app
     * @param args default args
     */
    public static void main(String[] args) {

        MarcoApp marco=new MarcoApp(); //crear un objeto de tipo MarcoApp correspondiente a la ventana del cliente

        marco.setTitle("Cliente LocalHost");
        marco.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            /**
             * Sistema de detección de cierre de ventana para eliminar el cliente de la lista de clientes disponibles y cerrar la ventana
             * @see https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event
             * */
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Integer serverPort = Chat.getMyPort();
                try {
                    Socket mysocket = new Socket("localhost", 9999); //envía el paquete offline al server para indicar que el cliente está en se desconectó

                    paqueteDato online = new paqueteDato();

                    online.setPuerto(serverPort);
                    online.setMensaje("OFFLINE"); //mensaje OFFLINE


                    ObjectOutputStream flujo = new ObjectOutputStream(mysocket.getOutputStream());

                    flujo.writeObject(online);

                    mysocket.close();
                    System.exit(0);

                    //NOTA: Se añade System.exit a todas las exepciones para evitar que el app no ser cierre si encuentra un error
                } catch (UnknownHostException ex) {
                    System.exit(0);
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    System.exit(0);
                    throw new RuntimeException(ex);
                }catch (Exception e2) {
                    System.exit(0);
                    System.out.println("Error desconocido 2");
                }

            }
        });
    }


}

/**
 * Clase MarcoApp que muestra el Frame de la pantalla de chat
 * */
class MarcoApp extends JFrame{

    /**
     * Constructor de la clase MarcoApp
     * */
    public MarcoApp() {

        setBounds(600, 300, 280, 350);

        Chat ChatApp = new Chat();

        add(ChatApp);

        setVisible(true);

        addWindowListener(new online());
    }

}

/**
 * Clase para enviar ONLINE al server indicando el estado del cliente y para así registrar el puerto
 * que se va a usar para recibir mensajes
 *
 */
class online extends WindowAdapter {
    Integer serverPort = Chat.getMyPort();
    @Override
    public void windowOpened(WindowEvent e) {
        try {
            Socket mysocket = new Socket("localhost", 9999); //envia el paquete online con el dato "ONLINE" al servidor

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

/**
 * Area de texto, muestra los mensajes y queda a la espera de recibir nuevos mensajes
 * en el puerto que abre automaticamente cuando encuentra disponible
 * @see "https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html"
 * */
class Chat extends JPanel implements Runnable {

    String nombreUsuario = new String();
    private static Integer myPort;
    private static InetAddress myInet;

    /**
     * Constructor del componente visual del app de Chat
     * */
    public Chat() {

        nombreUsuario = JOptionPane.showInputDialog("Nombre de usuario: "); //Input que se abre al iniciar el app y que solicita el nombre se usuario

        JLabel usuario = new JLabel("Usuario: ");

        add(usuario);

        JLabel nombre = new JLabel();

        nombre.setText(nombreUsuario);

        add(nombre);

        JLabel texto = new JLabel("Online: ");

        add(texto);

        ip = new JComboBox();

        add(ip);

        chat = new JTextArea (12,20);
        chat.setEditable(false);

        JScrollPane scroll = new JScrollPane (chat);

        add(scroll);


        setVisible(true);

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
    //Atributos visuales
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

    /**Metodo run*/
    @Override
    public void run() {
        try {
            /**
             * @see https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html#ServerSocket-int-
             * */
            ServerSocket server = new ServerSocket(0);//puerto del cliente a la espera de recibir mensajes, el puerto abre en el que encuentre disponible
            myPort= server.getLocalPort();//obtiene el valor del puerto que abre automáticamente para poder emplearlo en el servidor

            Socket cliente;

            paqueteDato paqueteR;

            while (true) { //siempre ejecutandose para esperar un nuevo mensaje

                cliente = server.accept();

                ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());

                paqueteR = (paqueteDato) flujoEntrada.readObject();

                //verifica si no es un mensaje de estado, entonces añade el mensaje recibido al area de chat
                if (!paqueteR.getMensaje().equals("ONLINE") && !paqueteR.getMensaje().equals("OFFLINE")){

                    chat.append("\n" + paqueteR.getUsuario() + ": " + paqueteR.getMensaje());

                //si es un mensaje de estado se actualiza la lista de dirección desplegable
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


    /**
     * Clase para enviar mensajes de texto al servidor en con un destino en otro cliente en el localhost (varía puerto)
     * */
    private class enviarTexto implements ActionListener { //ActionListener se usa para detectar y manejar eventos de acción
        @Override
        /**
         * Listener de los eventos del botón enviar, obtiene el valor que se ingres en el area de texto
         * y arma un paquede de datos para enviarlo al servidor con los datos del nombre de usuario, mensaje y
         * puerto de destino del cliente al que se le desdea enviar
         * */
        public void actionPerformed(ActionEvent e) {
            chat.append("\n" + chatApp.getText());
            try {
                Socket mysocket = new Socket("localhost",9999); //Abre el socket para enviar los datos al servidor

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

/**
 * PaqueteDato es la clase de todos los paquetes seralizables que se envían entre el servidor y los clientes
 * @see "https://docs.oracle.com/javase%2F7%2Fdocs%2Fapi%2F%2F/java/io/Serializable.html"
 * */
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