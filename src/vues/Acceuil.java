package vues;

import utilitaires.TypeAction;
import utilitaires.TypeSociete;
import controleurs.ControleurAcceuil;
import entites.Societe;
import exceptions.CustomException;
import exceptions.DaoException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

import static utilitaires.Utilitaires.LOGGER;

public class Acceuil extends JDialog {
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonClient;
    private JButton buttonProspect;
    private JButton buttonAcceuil;
    private JButton CREATIONButton;
    private JButton SUPPRESSIONButton;
    private JButton MODIFICATIONButton;
    private JButton AFFICHAGEButton;
    private JLabel questionAcceuil;
    private JLabel labelChoixGestion;
    private JPanel panelChoixGestion;
    private JPanel panelChoixAction;
    private JList listSociete;
    private JPanel panelList;
    private JLabel selection;
    private TypeSociete choix;
    private ControleurAcceuil controleurAcceuil = new ControleurAcceuil();
    private TypeAction action;

    public void init(){
        this.setSize(600,300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    };
    public Acceuil() {

        setTitle("REVERSO");
        setContentPane(contentPane);
        setModal(true);

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





        buttonClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choix = TypeSociete.CLIENT;
                buttonAcceuil.setVisible(true);
                panelChoixGestion.setVisible(false);
                panelChoixAction.setVisible(true);
                labelChoixGestion.setText("Gestion des clients");

            }
        });
        buttonProspect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choix = TypeSociete.PROSPECT;
                buttonAcceuil.setVisible(true);
                panelChoixGestion.setVisible(false);
                panelChoixAction.setVisible(true);
                labelChoixGestion.setText("Gestion des prospects");
            }
        });
        buttonAcceuil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelList.setVisible(false);
                buttonAcceuil.setVisible(false);
                panelChoixGestion.setVisible(true);
                panelChoixAction.setVisible(false);
            }
        });
        MODIFICATIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = TypeAction.MODIFICATION;
                populateListSociete();

            }
        });
        SUPPRESSIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action = TypeAction.SUPPRESSION;
                populateListSociete();

            }
        });
        CREATIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                controleurAcceuil.pageFormulaire(choix,null,TypeAction.CREATION);
            }
        });
        AFFICHAGEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    dispose();
                    controleurAcceuil.pageAffichage(choix);

            }
        });

        listSociete.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Récupérer l'objet sélectionné
                    Object elementSelectionne = listSociete.getSelectedValue();

                    if (elementSelectionne != null) {

                        try {
                            Societe societe =
                                    controleurAcceuil.getSociete(choix,
                                    elementSelectionne.toString());
                            dispose();
                            controleurAcceuil.pageFormulaire(choix,
                                    societe,action);
                        } catch (CustomException | DaoException ex) {
                            JOptionPane.showMessageDialog(null,
                                    ex.getMessage());
                            System.exit(1);
                        } catch (Exception ex) {
                            LOGGER.severe("Une erreur inconnue s'est produite" +
                                    " : "+ex);
                            JOptionPane.showMessageDialog(null,
                                    "Une erreur inconnue s'est produite. " +
                                            "Veuillez contacter un " +
                                            "administrateur si l'erreur " +
                                            "perssiste.\nFermeture de l'application.");
                            System.exit(1);
                        }


                    }


                }
            }
        });
    }

    private void populateListSociete() {
        try {
            ArrayList<String> listeNoms = controleurAcceuil.listeNoms(choix);
            if(listeNoms.isEmpty()){
                JOptionPane.showMessageDialog(null,
                        "Aucun " + (choix.equals(TypeSociete.CLIENT) ?
                                "client" : "prospect") + " à " + (action.equals(TypeAction.SUPPRESSION) ? "supprimer" : "modifier") +". Veuillez dabord en créer un.");
            }else {
                Collections.sort(listeNoms);
                listSociete.setListData(listeNoms.toArray());
                selection.setText("Choisissez le " + (choix.equals(TypeSociete.CLIENT) ? "client" : "prospect") + " à " + (action.equals(TypeAction.SUPPRESSION) ? "supprimer" : "modifier") + " :");
                panelList.setVisible(true);
            }
        } catch (CustomException | DaoException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage());
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
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
