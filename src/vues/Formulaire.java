package vues;

import controleurs.ControleurAcceuil;
import controleurs.ControleurFormulaire;
import controleurs.TypeAction;
import controleurs.TypeSociete;
import entites.Client;
import entites.Societe;
import exceptions.DaoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class Formulaire extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldRaisonSocial;
    private JTextField textFieldTelephone;
    private JTextField textFieldEmail;
    private JTextField textFieldNumeroRue;
    private JTextField textFieldNomRue;
    private JTextField textFieldCodePostal;
    private JTextField textFieldVille;
    private JTextField textFieldChiffreAffaire;
    private JTextField textFieldNombreEmployes;
    private JTextArea textAreaCommentaires;
    private JTextField textFieldDateProspection;
    private JRadioButton ouiRadioButtonInteresse;
    private JRadioButton nonRadioButtonInteresse;
    private JPanel zoneClient;
    private JPanel zoneProspect;

    public Formulaire(TypeSociete choix, String nom, TypeAction action) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ControleurFormulaire controleurFormulaire = new ControleurFormulaire();

        if (choix.equals(TypeSociete.CLIENT)) zoneClient.setVisible(true);
        else zoneProspect.setVisible(true);

        Societe societe = null;
        try {
            societe = controleurFormulaire.societe(choix, nom);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        if(societe != null) {
            textFieldRaisonSocial.setText(societe.getRaisonSociale());
        }

        if(action != null && action.equals(TypeAction.SUPPRESSION)) {

            for (Component comp : getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;
                    for (Component child : panel.getComponents()) {
                        child.setEnabled(false);
                    }
                }else{
                    comp.setEnabled(false);
                }
            }

        }


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
