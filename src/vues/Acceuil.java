package vues;

import controleurs.TypeAction;
import controleurs.TypeSociete;
import controleurs.ControleurAcceuil;
import entites.Societe;
import exceptions.DaoException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static utilitaires.Utilitaires.LOGGER;

public class Acceuil extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
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

    public void init(){
        this.setSize(600,300);
        this.setVisible(true);
    };

    public Acceuil() {

        final TypeAction[] action = new TypeAction[1];
        setTitle("REVERSO");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
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


        ControleurAcceuil controleurAcceuil = new ControleurAcceuil();


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

                try {
                    populateOrderedListSociete(controleurAcceuil.liste(choix));
                    action[0] = TypeAction.MODIFICATION;
                    selection.setText("Choisissez le "+(choix.equals(TypeSociete.CLIENT)? "client":"prospect")+" à " +
                            "modifier :");
                    panelList.setVisible(true);
                } catch (DaoException de) {
                    JOptionPane.showMessageDialog(null,de.getMessage());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        SUPPRESSIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    populateOrderedListSociete(controleurAcceuil.liste(choix));
                    action[0] = TypeAction.SUPPRESSION;
                    selection.setText("Choisissez le "+(choix.equals(TypeSociete.CLIENT)? "client":"prospect")+" à " +
                            "supprimer :");
                    panelList.setVisible(true);
                } catch (DaoException de) {
                    JOptionPane.showMessageDialog(null,de.getMessage());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        CREATIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                controleurAcceuil.formulaire(choix,null,TypeAction.CREATION);
            }
        });
        AFFICHAGEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelList.setVisible(false);
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
                            controleurAcceuil.formulaire(choix,
                                    societe,action[0]);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (DaoException de) {
                            JOptionPane.showMessageDialog(null,de.getMessage());
                            listSociete.clearSelection();
                        }


                    }


                }
            }
        });
    }

    private void populateOrderedListSociete(List liste) {
        Collections.sort(liste);
        listSociete.setListData(liste.toArray());
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
