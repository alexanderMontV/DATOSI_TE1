package onNetwork;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * classe Servido, clase principal del archivo, ejecuta el main que da arranque al server
 */
public class 	Servidor  {

	
	/** 
	 * funcion main, crea un objeto de tipo MarcoServido(clase local) y define la accion de cierre
	 * @param args default args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor marco = new MarcoServidor(); //Crear Objeto de tipo MarcoServido
		
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Operación de cierre
			
	}	
}

/**
	Clase MarcoServidor, extiende de JFrame
	Usa runable para ejecutar el metodo run en un thread
	@see https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html

*/
class MarcoServidor extends JFrame implements Runnable {
	
	/**
	 * constructor de la clase MarcoServido
	 * @param null no se reciben argumentos
	 */
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);
			
		JPanel plantilla = new JPanel();
		
		plantilla.setLayout(new BorderLayout());

		areaTexto = new JTextArea();
		
		plantilla.add(areaTexto,BorderLayout.CENTER);
		
		add(areaTexto);
		
		setVisible(true);

		Thread escucho = new Thread(this);

		escucho.start();
		
		}
	
	private	JTextArea areaTexto;

	@Override

	/**
	 * Metodo run que se ejecutan por Runnable.class
	 * @param null no recibe parametros
	 */
	public void run() {

		try {
			ServerSocket server = new ServerSocket(9999); //Socket de entrada, es el que escuha mensjaes entrantes

			String ip, mensaje, usuario;

			ArrayList <String> listaIp = new ArrayList<String>();

			paqueteDato paqueteR;
			
			//se mantiene en ejecución esperando nuevos eventos
			while (true) {

				Socket mysocket = server.accept();

				ObjectInputStream paqueteEntrada = new ObjectInputStream(mysocket.getInputStream()); //Flujo de datos usando mysocket

				paqueteR = (paqueteDato) paqueteEntrada.readObject(); //Leer el flujo de datos como Object

				ip = paqueteR.getIp(); //Obtiene la ip, el mensaje y el usuario con métodos getter y setter

				mensaje = paqueteR.getMensaje();

				usuario = paqueteR.getUsuario();

				//metodo que recibe una nuvea conexión  y la añade a la lista de disponibles ()

				if (mensaje.equals("ONLINE")){

					InetAddress IP = mysocket.getInetAddress(); //Obtiene ip de los clientes online

					String ipCliente = IP.getHostAddress();

					listaIp.add(ipCliente);

					paqueteR.setIPs(listaIp);

					//acutaliza la lista de cada uno de los clientes que se han abierto
					for (String z: listaIp){ 

						Socket socketDestino = new Socket(z, 8080); //Socket salida

						ObjectOutputStream paqueteE = new ObjectOutputStream(socketDestino.getOutputStream());

						paqueteE.writeObject(paqueteR);

						paqueteE.close();

						socketDestino.close();

						mysocket.close();
					}

				}
				//entonces tiene que ser un mensaje normal
				else{

					//mostrar en el area de texto la indo del usuario y del msg 
					areaTexto.append("\nIP: " + ip + "\nUsuario: " + usuario + "\nMensaje: " + mensaje);

					Socket socketDestino = new Socket(ip, 8080); //Socket salida

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
