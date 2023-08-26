package onLocalhost;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor  {

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoServidor marco = new MarcoServidor();
        marco.setTitle("Servidor LocalHost");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class MarcoServidor extends JFrame implements Runnable {

    public MarcoServidor(){

        setBounds(600,300,280,350);

        areaTexto = new JTextArea ();
        areaTexto.setEditable(false);

        JScrollPane scroll = new JScrollPane (areaTexto);

        add(scroll);

        setVisible(true);

        Thread escucho = new Thread(this);

        escucho.start();

    }

    private	JTextArea areaTexto;

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(9999); //Socket de entrada

            String ip, mensaje, usuario;
            Integer portS;

            ArrayList <String> listaIp = new ArrayList<String>();
            ArrayList <Integer> listaIpnPort = new ArrayList<Integer>();

            paqueteDato paqueteR;

            while (true) {

                Socket mysocket = server.accept();

                ObjectInputStream paqueteEntrada = new ObjectInputStream(mysocket.getInputStream()); //Flujo de datos usando mysocket

                paqueteR = (paqueteDato) paqueteEntrada.readObject(); //Leer el flujo de datos como Object



                portS=paqueteR.getPuerto();

                mensaje = paqueteR.getMensaje();

                usuario = paqueteR.getUsuario();

                if (mensaje.equals("ONLINE") || mensaje.equals(("OFFLINE"))){


                    if (mensaje.equals("ONLINE")){
                    listaIpnPort.add(portS);}
                    else{
                        System.out.println("puerto removido "+portS);
                        listaIpnPort.remove(portS);}

                    paqueteR.setIPs(listaIpnPort);

                    for (Integer z: listaIpnPort){

                        System.out.println("Array: " + z);

                        Socket socketDestino = new Socket("localhost", z); //Socket salida

                        ObjectOutputStream paqueteE = new ObjectOutputStream(socketDestino.getOutputStream());

                        paqueteE.writeObject(paqueteR);

                        paqueteE.close();

                        socketDestino.close();

                        mysocket.close();
                    }

                }else{

                    areaTexto.append("\nIP: localhost:" +portS + "\nUsuario: " + usuario + "\nMensaje: " + mensaje);

                    Socket socketDestino = new Socket("localhost", portS); //Socket salida

                    ObjectOutputStream paqueteE = new ObjectOutputStream(socketDestino.getOutputStream());

                    paqueteE.writeObject(paqueteR);

                    paqueteE.close();

                    socketDestino.close();

                    mysocket.close();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
