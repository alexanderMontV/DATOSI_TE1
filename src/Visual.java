import javax.swing.*;
import java.awt.*;


public class Visual {
    
    /** 
     * @param args
     */
    public static void main(String[] args) {

        JFrame frame = new JFrame(); //Frame principal y sus ajustes
        frame.setVisible(true);
        frame.setTitle("Hello frame");
        frame.setSize(600,540);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);
        frame.getContentPane().setBackground(new Color(0x3625F6));

        JPanel topBar = new JPanel(); //Barra superior
        topBar.setBounds(0,0,600,72);
        topBar.setBackground(new Color(0xD9D9D9));
        topBar.setLayout(null);
        frame.add(topBar);

        JLabel ip = new JLabel("ip:");
        ip.setBounds(125,30,18,20);
        topBar.add(ip);
        JPanel ipChartBack = new JPanel();
        ipChartBack.setBounds(168,20,263,43);
        ipChartBack.setBackground(new Color(0XFFFFFF));
        topBar.add(ipChartBack);

        //Area de chat
        JPanel chatArea = new JPanel();
        chatArea.setBounds(0,72,600,336);
        chatArea.setBackground(new Color(0XE9E9E9));
        chatArea.setVisible(true);
        frame.add(chatArea);

        //Area de envRar mensaje
        JPanel msgBox = new JPanel();
        msgBox.setBounds(0,408,600,92);
        msgBox.setBackground(new Color(0xD9D9D9));
        msgBox.setLayout(null);
        frame.add(msgBox);

        JPanel msgBack= new JPanel();
        msgBack.setBounds(25,15,435,60);
        msgBack.setBackground(new Color(0XFFFFFF));
        msgBox.add(msgBack);

        JButton btnEnviar = new JButton();
        btnEnviar.setBackground(new Color(0X090075));
        btnEnviar.setBounds(486,15,75,60);
        msgBox.add(btnEnviar);


        JTextField ipChart = new JTextField();
        ipChart.setBounds(168,20,250,43);
        ipChart.setBorder(null);
        ipChart.setHorizontalAlignment(JTextField.CENTER);
        topBar.add(ipChart);

        JTextField msgChart = new JTextField();
        msgChart.setBounds(40,15,410,60);
        msgChart.setBorder(null);
        msgBox.add(msgChart);

        JTextArea mensajeEntrante = new JTextArea();
        mensajeEntrante.setSize(265,70);
        mensajeEntrante.setBackground(new Color(0X605E5E));
        chatArea.add(mensajeEntrante);


    }
}
