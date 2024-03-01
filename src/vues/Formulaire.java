package vues;

import controleurs.ControleurFormulaire;
import controleurs.TypeAction;
import controleurs.TypeSociete;
import entites.Client;
import entites.Interessement;
import entites.Prospect;
import entites.Societe;
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

    public Formulaire(TypeSociete choix, String nom, TypeAction action) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ControleurFormulaire controleurFormulaire = new ControleurFormulaire();

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
            textFieldTelephone.setText(societe.getTelephone());
            textFieldEmail.setText(societe.geteMail());
            textFieldNumeroRue.setText(societe.getAdresse().getNumeroRue());
            textFieldNomRue.setText(societe.getAdresse().getNomRue());
            textFieldCodePostal.setText(societe.getAdresse().getCodePostal());
            textFieldVille.setText(societe.getAdresse().getVille());
            textAreaCommentaires.setText(societe.getCommentaires());
            if(societe instanceof Client) {
                zoneClient.setVisible(true);
                textFieldChiffreAffaire.setText(String.valueOf(((Client) societe).getChiffreAffaires()));
                textFieldNombreEmployes.setText(String.valueOf(((Client) societe).getNbrEmployes()));
            }
            if(societe instanceof Prospect) {
                zoneProspect.setVisible(true);
                populateCalendar(((Prospect) societe).getDateProspection());
                if(((Prospect) societe).getInteresse().equals(Interessement.OUI.toString())){
                    ouiRadioButtonInteresse.setSelected(true);
                }else{
                    nonRadioButtonInteresse.setSelected(true);
                }
            }
        }else{
            populateCalendar(LocalDate.now());
        }

        if(action != null && action.equals(TypeAction.SUPPRESSION)) {


            desactiverComponent(getContentPane().getComponents());

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




        comboBoxAnnee.addActionListener(e -> {
            populateComboBoxJour();

        });

        comboBoxMois.addActionListener(e -> {
            populateComboBoxJour();

        });
    }

    private void desactiverComponent(Component[] components) {

        for (Component comp : components) {
            if (comp instanceof JPanel panel) {
                desactiverComponent(((JPanel) comp).getComponents());
            }else{
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
        comboBoxMois.setSelectedIndex(dateProspection.getMonthValue()-1);
        comboBoxJour.setSelectedIndex(dateProspection.getDayOfMonth()-1);
    }

    private void populateComboBoxJour() {

        int anneeSelectionnee = comboBoxAnnee.getSelectedIndex();
        int moisSelectionne = comboBoxMois.getSelectedIndex();

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
        int indexJourActuellementSaisi = comboBoxJour.getSelectedIndex();
        if(indexJourActuellementSaisi>nbJours-1) {
            comboBoxJour.setSelectedIndex(-1);
        }else {
            comboBoxJour.setSelectedIndex(indexJourActuellementSaisi);
        }
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
