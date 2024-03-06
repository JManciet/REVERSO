package vues;

import controleurs.ControleurAffichage;
import controleurs.TypeSociete;
import entites.Client;
import entites.Prospect;
import exceptions.DaoException;
import vues.model.ModelTable;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Affichage extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable table1;
    private JLabel titre;

    public void init(){
        this.setSize(1000,300);
        this.setVisible(true);
    }
    public Affichage(TypeSociete choix) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        if (choix.equals(TypeSociete.CLIENT)) {
            titre.setText("Tableau des clients");
        } else if (choix.equals(TypeSociete.PROSPECT)) {
            titre.setText("Tableau des prospects");
        }

        ArrayList societes = null;
        try {
            societes = new ControleurAffichage().getListSociete(choix);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }


        table1.setModel(new ModelTable(societes));
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
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
