import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class AppChat {

	public static void main(String[] args) {
		
		MarcoApp marco=new MarcoApp();
		
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoApp extends JFrame{
	
	public MarcoApp() {

		setBounds(600, 300,280,350);

		Chat ChatApp = new Chat();
		
		add(ChatApp);
		
		setVisible(true);
		}	
	
}

class Chat extends JPanel implements Runnable {
	
	public Chat(){

		JLabel texto=new JLabel("IP");

		add(texto);

		ip = new JTextField(8);

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


	private JTextField chatApp, ip;

	private JTextArea chat;

	private JButton boton;

	@Override
	public void run() {

		try{

			ServerSocket server = new ServerSocket(9090);

			Socket cliente;

			paqueteDato paqueteR;

			while (true){

				cliente = server.accept();

				ObjectInputStream flujoEntrada = new ObjectInputStream(cliente.getInputStream());

				paqueteR = (paqueteDato) flujoEntrada.readObject();

				chat.append("\n" + paqueteR.getMensaje() + ":" + paqueteR.getIp());

			}

		}catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	private class enviarTexto implements ActionListener{ //ActionListener se usa para detectar y manejar eventos de acción
		@Override
		public void actionPerformed(ActionEvent e) {
			chat.append("\n" + chatApp.getText());
			try {

				Socket mysocket = new Socket("192.168.18.130",9999); //Abre el socket

				paqueteDato datos = new paqueteDato(); //Crear un paquete con la información que se va a enviar (Objeto)

				datos.setIp(ip.getText());

				datos.setMensaje(chatApp.getText());

				ObjectOutputStream paqueteDatos = new ObjectOutputStream(mysocket.getOutputStream()); //Flujo de salidad para poder enviar el objeto por la red

				paqueteDatos.writeObject(datos);

				mysocket.close();

			}
			catch (IOException ex) {
				System.out.println(ex.getMessage());
			}
		}

	}


}

class paqueteDato implements Serializable{  //"implements Serializable" es para que todos los obj sea puedan hacer en bit

	private String ip, mensaje;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}