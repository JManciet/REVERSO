package vues;

import controleurs.ChoixClientProspect;

import javax.swing.*;
import java.awt.event.*;

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
    private ChoixClientProspect choix;

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
        buttonClient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choix = ChoixClientProspect.CLIENT;
                panelChoixGestion.setVisible(false);
                panelChoixAction.setVisible(true);
                labelChoixGestion.setText("Gestion des clients");

            }
        });
        buttonProspect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choix = ChoixClientProspect.PROSPECT;
                panelChoixGestion.setVisible(false);
                panelChoixAction.setVisible(true);
                labelChoixGestion.setText("Gestion des prospects");
            }
        });
        buttonAcceuil.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelChoixGestion.setVisible(true);
                panelChoixAction.setVisible(false);
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
