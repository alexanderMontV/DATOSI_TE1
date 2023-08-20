import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.net.ServerSocket;

public class Servidor  {

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
			ServerSocket server = new ServerSocket(9999);

			String ip, mensaje;

			paqueteDato paqueteR;

			while (true) {

				Socket mysocket = server.accept();

				ObjectInputStream paqueteEntrada = new ObjectInputStream(mysocket.getInputStream()); //Flujo de datos usando mysocket

				paqueteR = (paqueteDato) paqueteEntrada.readObject(); //Leer el flujo de datos como Object

				ip = paqueteR.getIp(); //Obtiene la ip y el mensaje con métodos getter y setter

				mensaje = paqueteR.getMensaje();

				areaTexto.append("\n" + ip + "\n" + mensaje);

				Socket enviaDestinatario = new Socket(ip, 9090);

				ObjectOutputStream paqueteE = new ObjectOutputStream(enviaDestinatario.getOutputStream());

				paqueteE.writeObject(paqueteR);

				paqueteE.close();

				enviaDestinatario.close();

				mysocket.close();

			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
