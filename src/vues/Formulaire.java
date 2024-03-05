package vues;

import controleurs.ControleurFormulaire;
import controleurs.TypeAction;
import controleurs.TypeSociete;
import entites.*;
import exceptions.DaoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

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
    private JComboBox comboBoxAnnee;
    private JComboBox comboBoxMois;
    private JComboBox comboBoxJour;
    private JButton buttonCreate;
    private JButton buttonDelete;
    private JButton buttonUpdate;
    private Integer idAdresse;
    private Integer idSociete;

    public Formulaire(TypeSociete choix, String nom, TypeAction action) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ControleurFormulaire controleurFormulaire = new ControleurFormulaire();

        Societe societe = null;
        try {
            societe = controleurFormulaire.getSociete(choix, nom);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DaoException de) {
            JOptionPane.showMessageDialog(null,de.getMessage());
            dispose();
        }

        if (societe != null) {
            idAdresse = societe.getAdresse().getIdentifiant();
            idSociete = societe.getIdentifiant();
            textFieldRaisonSocial.setText(societe.getRaisonSociale());
            textFieldTelephone.setText(societe.getTelephone());
            textFieldEmail.setText(societe.geteMail());
            textFieldNumeroRue.setText(societe.getAdresse().getNumeroRue());
            textFieldNomRue.setText(societe.getAdresse().getNomRue());
            textFieldCodePostal.setText(societe.getAdresse().getCodePostal());
            textFieldVille.setText(societe.getAdresse().getVille());
            textAreaCommentaires.setText(societe.getCommentaires());
            if (societe instanceof Client) {
                zoneClient.setVisible(true);
                textFieldChiffreAffaire.setText(String.valueOf(((Client) societe).getChiffreAffaires()));
                textFieldNombreEmployes.setText(String.valueOf(((Client) societe).getNbrEmployes()));
            } else if (societe instanceof Prospect) {
                zoneProspect.setVisible(true);
                populateCalendar(((Prospect) societe).getDateProspection());
                if (((Prospect) societe).getInteresse() != null && ((Prospect) societe).getInteresse().equals(Interessement.OUI)) {
                    ouiRadioButtonInteresse.setSelected(true);
                } else if (((Prospect) societe).getInteresse() != null && ((Prospect) societe).getInteresse().equals(Interessement.NON)) {
                    nonRadioButtonInteresse.setSelected(true);
                }
            }
        } else {
            if (choix.equals(TypeSociete.CLIENT)) {
                zoneClient.setVisible(true);
            } else if (choix.equals(TypeSociete.PROSPECT)) {
                populateCalendar(LocalDate.now());
                zoneProspect.setVisible(true);
            }
        }


        String typeSociete = (choix.equals(TypeSociete.CLIENT) ? "client" :
                "prospect");
        if (action.equals(TypeAction.CREATION)) {
            buttonCreate.setVisible(true);
            buttonCreate.setText("Valider la création du " + typeSociete);
        } else if (action.equals(TypeAction.MODIFICATION)) {
            buttonUpdate.setVisible(true);
            buttonUpdate.setText("Valider la modification du " + typeSociete);
        } else if (action.equals(TypeAction.SUPPRESSION)) {
            buttonDelete.setVisible(true);
            buttonDelete.setText("Supprimer le " + typeSociete);
            desactivateComponent(getContentPane().getComponents());
        }

        buttonCancel.addActionListener(e -> {
            dispose();
            controleurFormulaire.retourAcceuil();
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        comboBoxAnnee.addActionListener(e -> {
            populateComboBoxJour();

        });

        comboBoxMois.addActionListener(e -> {
            populateComboBoxJour();

        });
        buttonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                dispose();
                Societe societe = instantiateSociete(choix);
                try {
                    controleurFormulaire.createSociete(societe);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (DaoException de) {
                    JOptionPane.showMessageDialog(null,de.getMessage());
                }
            }
        });
        buttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Societe societe = instantiateSociete(choix);
                try {
                    controleurFormulaire.updateSociete(societe);
                    dispose();
                    controleurFormulaire.retourAcceuil();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (DaoException de) {
                    JOptionPane.showMessageDialog(null,de.getMessage());
                }
            }
        });

        Societe finalSociete = societe;
        buttonDelete.addActionListener(e -> {

            int dialogChoix = JOptionPane.showConfirmDialog(
                    null,
                    "Voulez-vous vraiment supprimer le " + (choix.equals(TypeSociete.CLIENT) ? "client " : "prospect ") + finalSociete.getRaisonSociale() + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (dialogChoix == JOptionPane.YES_OPTION) {
                try {
                    dispose();
                    controleurFormulaire.deleteSociete(finalSociete);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (DaoException de) {
                    JOptionPane.showMessageDialog(null,de.getMessage());
                }
            }

        });
    }

    private Societe instantiateSociete(TypeSociete choix) {

        Adresse adresse = new Adresse(
                idAdresse,
                textFieldNumeroRue.getText(),
                textFieldNomRue.getText(),
                textFieldCodePostal.getText(),
                textFieldVille.getText()
        );
        Societe societe = null;
        if (choix.equals(TypeSociete.CLIENT)) {
            societe = new Client(
                    idSociete,
                    textFieldRaisonSocial.getText(),
                    adresse,
                    textFieldTelephone.getText(),
                    textFieldEmail.getText(),
                    textAreaCommentaires.getText(),
                    Double.valueOf(textFieldChiffreAffaire.getText()),
                    Integer.valueOf(textFieldNombreEmployes.getText())
            );
        } else if (choix.equals(TypeSociete.PROSPECT)) {
            Interessement interesse = null;
            if (ouiRadioButtonInteresse.isSelected()) {
                interesse = Interessement.OUI;
            } else if (nonRadioButtonInteresse.isSelected()) {
                interesse = Interessement.NON;
            }
            societe = new Prospect(
                    idSociete,
                    textFieldRaisonSocial.getText(),
                    adresse,
                    textFieldTelephone.getText(),
                    textFieldEmail.getText(),
                    textAreaCommentaires.getText(),
                    LocalDate.of(
                            (Integer) comboBoxAnnee.getSelectedItem(),
                            comboBoxMois.getSelectedIndex()+1,
                            (Integer) comboBoxJour.getSelectedItem()
                    ),
                    interesse
            );
        }

        return societe;

    }

    private void desactivateComponent(Component[] components) {

        for (Component comp : components) {
            if (comp instanceof JPanel panel) {
                desactivateComponent(((JPanel) comp).getComponents());
            } else {
                if (comp instanceof JTextField || comp instanceof JTextArea || comp instanceof JComboBox<?> || comp instanceof JRadioButton)
                    comp.setEnabled(false);
            }
        }


//        for (Component comp : getContentPane().getComponents()) {
//            if (comp instanceof JPanel panel) {
//                for (Component child : panel.getComponents()) {
//                    child.setEnabled(false);
//                }
//            }else{
//                comp.setEnabled(false);
//            }
//        }

    }

    private void populateCalendar(LocalDate dateProspection) {

        List<String> mois = Arrays.asList("Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre");


        List<Integer> annees = new ArrayList<>();
        int anneeActuel = LocalDate.now().getYear();
        for (int i = anneeActuel; i >= 2000; i--) {
            annees.add(i);
        }


        comboBoxAnnee.setModel(new DefaultComboBoxModel<>(annees.toArray()));

        comboBoxMois.setModel(new DefaultComboBoxModel<>(mois.toArray()));


        populateComboBoxJour();

        comboBoxAnnee.setSelectedItem(anneeActuel);
        comboBoxMois.setSelectedIndex(dateProspection.getMonthValue() - 1);
        comboBoxJour.setSelectedIndex(dateProspection.getDayOfMonth() - 1);
    }

    private void populateComboBoxJour() {

        int anneeSelectionnee = comboBoxAnnee.getSelectedIndex();
        int moisSelectionne = comboBoxMois.getSelectedIndex();
        int indexJourActuellementSaisi = comboBoxJour.getSelectedIndex();

        Calendar calendar = Calendar.getInstance();
        calendar.set(anneeSelectionnee, moisSelectionne, 1);

        // Récupération du nombre de jours maximum
        int nbJours = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Mise à jour du modèle de la liste déroulante "Jour"
        List<Integer> jours = new ArrayList<>();
        for (int i = 1; i <= nbJours; i++) {

            jours.add(i);
        }

        comboBoxJour.setModel(new DefaultComboBoxModel<>(jours.toArray()));

        //Si le jour actuellement saisi n'existe pas réinitialiser
        // comboBoxJour

        if (indexJourActuellementSaisi > nbJours - 1) {
            comboBoxJour.setSelectedIndex(-1);
        } else {
            comboBoxJour.setSelectedIndex(indexJourActuellementSaisi);
        }
    }
}
