package vues;

import controleurs.TypeSociete;
import controleurs.ControleurAcceuil;
import exceptions.DaoException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.SQLException;

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
    private JList list1;
    private JPanel panelList;
    private JLabel selection;
    private TypeSociete choix;

    public Acceuil() {

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

                selection.setText("Choisissez le "+(choix.equals(TypeSociete.CLIENT)? "client":"prospect")+" à " +
                        "modifier :");
                panelList.setVisible(true);
                try {
                    list1.setListData(controleurAcceuil.liste(choix).toArray());
                } catch (DaoException | SQLException d) {
                    LOGGER.severe("Problème avec la connection : "+d.getMessage());
                    System.out.println("problème, voir log");
                }
            }
        });
        SUPPRESSIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selection.setText("Choisissez le "+(choix.equals(TypeSociete.CLIENT)? "client":"prospect")+" à " +
                        "supprimer :");
                panelList.setVisible(true);
                try {
                    list1.setListData(controleurAcceuil.liste(choix).toArray());
                } catch (DaoException | SQLException d) {
                    LOGGER.severe("Problème avec la connection : "+d.getMessage());
                    System.out.println("problème, voir log");
                }
            }
        });
        CREATIONButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                panelList.setVisible(false);
                Formulaire formulaire = new Formulaire(choix);
                formulaire.setSize(700,400);
                formulaire.setVisible(true);
            }
        });
        AFFICHAGEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelList.setVisible(false);
            }
        });

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Récupérer l'objet sélectionné
                    Object elementSelectionne = list1.getSelectedValue();

                    if (elementSelectionne != null) {
                        // Exécution de la suppression
                        int dialogChoix = JOptionPane.showConfirmDialog(
                                null,
                                "Voulez-vous vraiment supprimer le "+(choix.equals(TypeSociete.CLIENT)? "client ":"prospect ")+elementSelectionne+" ?",
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION
                        );
                        System.out.println(elementSelectionne);
                        if (dialogChoix == JOptionPane.YES_OPTION) {
                        try {
                            controleurAcceuil.suppression(choix,
                                    elementSelectionne.toString());
                            list1.setListData(controleurAcceuil.liste(choix).toArray());
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        } catch (DaoException ex) {
                            throw new RuntimeException(ex);
                        }
                        }
                        //list1.clearSelection();
                    }


                }
            }
        });
    }



    private void onCancel() {
        // add your code here if necessary
        dispose();

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
