package vues;

import controleurs.ControleurAffichage;
import controleurs.TypeSociete;
import entites.Societe;
import exceptions.CustomException;
import exceptions.DaoException;
import vues.model.ModelTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import static utilitaires.Utilitaires.LOGGER;

public class Affichage extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonRetourAcceuil;
    private JTable table;
    private JLabel titre;

    public void init(){
        this.setSize(1000,300);
        this.setVisible(true);
    }
    public Affichage(TypeSociete choix) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ControleurAffichage controleurAffichage = new ControleurAffichage();

        if (choix.equals(TypeSociete.CLIENT)) {
            titre.setText("Tableau des clients");
        } else if (choix.equals(TypeSociete.PROSPECT)) {
            titre.setText("Tableau des prospects");
        }

        ArrayList societes = null;
        try {
            societes = controleurAffichage.getListSociete(choix);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DaoException de) {
            JOptionPane.showMessageDialog(null,de.getMessage()+"\n Fermeture " +
                    "de l'application.");
            System.exit(1);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }

        ArrayList sortedSocietes = (ArrayList) societes.stream()
                .sorted(Comparator.comparing(Societe::getRaisonSociale))
                .collect(Collectors.toCollection(ArrayList::new));

        table.setModel(new ModelTable(sortedSocietes));

        buttonRetourAcceuil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                controleurAffichage.retourAcceuil();
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

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
