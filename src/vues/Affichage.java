package vues;

import controleurs.ControleurAffichage;
import controleurs.TypeAction;
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
    private JButton buttonRetourAcceuil;
    private JTable table;
    private JLabel titre;

    public void init(){
        this.setSize(1000,300);
        this.setVisible(true);
    }
    public Affichage(TypeSociete choix) {

        init();

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonRetourAcceuil);

        ControleurAffichage controleurAffichage = new ControleurAffichage();

        if (choix.equals(TypeSociete.CLIENT)) {
            titre.setText("Tableau des clients");
        } else if (choix.equals(TypeSociete.PROSPECT)) {
            titre.setText("Tableau des prospects");
        }

        ArrayList societes = null;
        try {
            societes = controleurAffichage.getListSociete(choix);
        } catch (DaoException | CustomException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            LOGGER.severe("Une erreur inconnue s'est produite" +
                    " : "+e);
            JOptionPane.showMessageDialog(null,
                    "Une erreur inconnue s'est produite. " +
                            "Veuillez contacter un " +
                            "administrateur si l'erreur " +
                            "perssiste.\nFermeture de l'application.");
            System.exit(1);
        }

        if(societes.isEmpty()) {
            dispose();
            JOptionPane.showMessageDialog(null,
                    "Aucun " + (choix.equals(TypeSociete.CLIENT) ?
                            "client" : "prospect") + " à afficher. Veuillez " +
                            "dabord en créer un.");
            controleurAffichage.retourAcceuil();
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
