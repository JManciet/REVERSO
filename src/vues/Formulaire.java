package vues;

import controleurs.TypeSociete;
import entites.Interessement;

import javax.swing.*;
import java.awt.event.*;

public class Formulaire extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JTextArea textArea1;
    private JTextField textField10;
    private JRadioButton ouiRadioButton;
    private JRadioButton nonRadioButton;
    private JPanel zoneClient;
    private JPanel zoneProspect;

    public Formulaire(TypeSociete choix) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        if (choix.equals(TypeSociete.CLIENT)) zoneClient.setVisible(true);
        else zoneProspect.setVisible(true);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
        Acceuil acceuil = new Acceuil();
        acceuil.setSize(500,300);
        acceuil.setVisible(true);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        Acceuil acceuil = new Acceuil();
        acceuil.setSize(500,300);
        acceuil.setVisible(true);
    }
}
