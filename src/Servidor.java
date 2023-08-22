import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.net.ServerSocket;
import java.util.ArrayList;

public class 	Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor marco = new MarcoServidor();
		
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable {
	
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
	public void run() {

		try {
			ServerSocket server = new ServerSocket(9999); //Socket de entrada

			String ip, mensaje, usuario;

			ArrayList <String> listaIp = new ArrayList<String>();

			paqueteDato paqueteR;

			while (true) {

				Socket mysocket = server.accept();

				ObjectInputStream paqueteEntrada = new ObjectInputStream(mysocket.getInputStream()); //Flujo de datos usando mysocket

				paqueteR = (paqueteDato) paqueteEntrada.readObject(); //Leer el flujo de datos como Object

				ip = paqueteR.getIp(); //Obtiene la ip, el mensaje y el usuario con métodos getter y setter

				mensaje = paqueteR.getMensaje();

				usuario = paqueteR.getUsuario();

				if (mensaje.equals("ONLINE")){

					InetAddress IP = mysocket.getInetAddress(); //Obtiene ip de los clientes online

					String ipCliente = IP.getHostAddress();

					listaIp.add(ipCliente);

					paqueteR.setIPs(listaIp);

					for (String z: listaIp){

						System.out.println("Array: " + z);

						Socket socketDestino = new Socket(z, 8080); //Socket salida

						ObjectOutputStream paqueteE = new ObjectOutputStream(socketDestino.getOutputStream());

						paqueteE.writeObject(paqueteR);

						paqueteE.close();

						socketDestino.close();

						mysocket.close();
					}

				}else{

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
