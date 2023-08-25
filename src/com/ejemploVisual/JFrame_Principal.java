package com.ejemploVisual;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JFrame_Principal extends JFrame{
    private JTextField textField1;
    private JButton button1;

    public JFrame_Principal() {
        initComponents();
        button1.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String textFieldValue = textField1.getText();
                JOptionPane.showMessageDialog(null, "Hola, "+textFieldValue);
            }
        });
    }

    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("JFrame_Principal");
        frame.setContentPane(new JFrame_Principal().Jpanel_Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700,400);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private void initComponents() {

    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel Jpanel_Main;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
