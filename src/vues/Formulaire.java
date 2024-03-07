package vues;

import controleurs.ControleurFormulaire;
import controleurs.TypeAction;
import controleurs.TypeSociete;
import entites.*;
import exceptions.CustomException;
import exceptions.DaoException;
import utilitaires.Utilitaires;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static utilitaires.Utilitaires.LOGGER;

public class Formulaire extends JDialog {
    private JPanel contentPane;
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
    private final TypeSociete choix;
    private final TypeAction action;
    private final ControleurFormulaire controleurFormulaire;
    public void init() {
        this.setSize(700,500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public Formulaire(TypeSociete choix, Societe societe, TypeAction action) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonDelete);

        this.choix = choix;
        this.action = action;

        controleurFormulaire = new ControleurFormulaire();

        if (societe != null) {
            idAdresse = societe.getAdresse().getIdentifiant();
            idSociete = societe.getIdentifiant();
            textFieldRaisonSocial.setText(societe.getRaisonSociale());
            textFieldTelephone.setText(societe.getTelephone());
            textFieldEmail.setText(societe.getEMail());
            textFieldNumeroRue.setText(societe.getAdresse().getNumeroRue());
            textFieldNomRue.setText(societe.getAdresse().getNomRue());
            textFieldCodePostal.setText(societe.getAdresse().getCodePostal());
            textFieldVille.setText(societe.getAdresse().getVille());
            textAreaCommentaires.setText(societe.getCommentaires());
            if (societe instanceof Client) {
                zoneClient.setVisible(true);
                textFieldChiffreAffaire.setText(Utilitaires.aroundTwoDecimalAndFormat(((Client) societe).getChiffreAffaires()));
                textFieldNombreEmployes.setText(String.valueOf(((Client) societe).getNbrEmployes()));
            } else if (societe instanceof Prospect) {
                zoneProspect.setVisible(true);
                populateComboBoxForDate(((Prospect) societe).getDateProspection());
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
                populateComboBoxForDate(LocalDate.now());
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

                validationFormulaire();

            }
        });
        buttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                validationFormulaire();

            }
        });

        buttonDelete.addActionListener(e -> {

            int dialogChoix = JOptionPane.showConfirmDialog(
                    null,
                    "Voulez-vous vraiment supprimer le " + (choix.equals(TypeSociete.CLIENT) ? "client " : "prospect ") + societe.getRaisonSociale() + " ?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (dialogChoix == JOptionPane.YES_OPTION) {
                try {
                    controleurFormulaire.deleteSociete(societe);
                    dispose();
                    controleurFormulaire.retourAcceuil();
                } catch (DaoException de) {
                    JOptionPane.showMessageDialog(null, de.getMessage());
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

        });
    }

    private void validationFormulaire() {
        if (isFormValid(getContentPane().getComponents())) {
            try {
                Societe societe = instantiateSociete(choix);
                if(action.equals(TypeAction.MODIFICATION)) {
                    controleurFormulaire.updateSociete(societe);
                } else if (action.equals(TypeAction.CREATION)){
                    controleurFormulaire.createSociete(societe);
                }
                dispose();
                controleurFormulaire.retourAcceuil();
            } catch (NumberFormatException nfe) {
                String valeurEnCause =
                        Utilitaires.valueAsGetException(nfe.getMessage());
                JOptionPane.showMessageDialog(null,
                        "La valeur \""+valeurEnCause+"\" renseignée " +
                                "n'est pas un nombre valide.");
            } catch (CustomException ce) {
                JOptionPane.showMessageDialog(null, ce.getMessage());
            } catch (DaoException de) {
                JOptionPane.showMessageDialog(null, de.getMessage());
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.severe("Une erreur inconnue s'est produite" +
                        " : "+e);
                JOptionPane.showMessageDialog(null,
                        "Une erreur inconnue s'est produite. " +
                                "Veuillez contacter un " +
                                "administrateur si l'erreur " +
                                "perssiste.\nFermeture de l'application.");
                System.exit(1);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Merci de compléter " +
                    "les champs manquant");
        }
    }

    private Societe instantiateSociete(TypeSociete choix) throws Exception {

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
                    Utilitaires.aroundTwoDecimalAndFormat(textFieldChiffreAffaire.getText()),
                    Integer.parseInt(textFieldNombreEmployes.getText())
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
                            comboBoxMois.getSelectedIndex() + 1,
                            (Integer) comboBoxJour.getSelectedItem()
                    ),
                    interesse
            );
        }

        return societe;

    }

    private boolean isFormValid(Component[] components) {

        boolean result = true;
        for (Component comp : components) {
            if (comp instanceof JPanel panel) {
                if (panel.isVisible()) {
                    result &= isFormValid(((JPanel) comp).getComponents());
                }
            } else {
                if (comp instanceof JTextField) {
                    if (((JTextField) comp).getText().isEmpty()) {
                        result = false;
                        comp.setBackground(Color.YELLOW);
                    } else {
                        comp.setBackground(Color.WHITE);
                    }
                }
            }
        }

        return result;
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

    }

    private void populateComboBoxForDate(LocalDate dateProspection) {

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
