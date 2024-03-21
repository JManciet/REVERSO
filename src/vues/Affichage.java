package vues;

import controleurs.ControleurAffichage;
import utilitaires.Gravite;
import utilitaires.TypeSociete;
import entites.Societe;
import exceptions.CustomException;
import exceptions.DaoException;
import utilitaires.Utilitaires;
import vues.model.ModelTable;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import static utilitaires.Utilitaires.LOGGER;

/**
 * Classe representant la fenêtre d'affichage d'une liste de Sociétés (Client ou Prospect).
 */
public class Affichage extends JDialog {
    private JPanel contentPane;
    private JButton buttonRetourAcceuil;
    private JTable table;
    private JLabel titre;

    /**
     * Initialise la fenêtre d'affichage.
     */
    public void init(){
        this.setSize(1000,300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Constructeur de la fenêtre d'affichage.
     *
     * @param choix Le type de société à afficher (CLIENT ou PROSPECT)
     */
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

        ArrayList<Societe> societes = null;
        try {
            societes = controleurAffichage.getListSociete(choix);
        } catch (DaoException de) {
            JOptionPane.showMessageDialog(null,de.getMessage());
            if (de.getGravite() == Gravite.SEVERE) {
                System.exit(1);
            }
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

        ArrayList<Societe> sortedSocietes =
                Utilitaires.sortSocieteByRaisonSociale(societes);

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
