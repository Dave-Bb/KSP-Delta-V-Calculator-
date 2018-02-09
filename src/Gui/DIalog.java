package Gui;

import OrbitCalculations.Body;

import javax.swing.*;
import java.awt.event.*;

public class DIalog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JCheckBox landingCheckBox;
    private JCheckBox returnTripCheckBox;
    private JLabel lowOrbit;
    private JLabel escapeVelocityTransfer;
    private JLabel totalLabel;
    private JLabel landingTxField;
    private JLabel takeOffDest;
    private JLabel returnTripLabel;
    private JLabel landingStart;
    private String startName;
    private String destinationName;
    private Body startBody;
    private Body destinationBody;

    public DIalog() {

        startName = "Moho";
        destinationName = "Moho";
        startBody = new Body();
        destinationBody = new Body();



        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startName = comboBox1.getSelectedItem().toString();
            }
        });
        landingCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(landingCheckBox.isSelected()){
                    System.out.println("With Landing Checked");
                }
            }
        });
        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destinationName = comboBox2.getSelectedItem().toString();
            }
        });
        returnTripCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void onOK() {
        // add your code here
        calculate();
    }

    private void calculate(){
        String destination = "";
        for(Body body: Body.BODIES){
            if(body.checkName(startName)){
                startBody = body;
            }

        }
        for(Body body: Body.BODIES){
            if(body.checkName(destinationName)){
                destinationBody = body;
            }
        }

        System.out.println("START: "+startBody.getName());
        System.out.println("DESTINATION: "+destinationBody.getName());

        double landing = 0;
        if(landingCheckBox.isSelected()){
            landing = destinationBody.calculateLanding();
        }

        double lowOrbitV = startBody.calculateLowOrbitVelocityTakeOff();
        double transfer = startBody.calculateTransferOrbitTo(destinationBody);//startBody.lowOrbitToEscapeVelocity()+destinationBody.escapeVelocityToLowOrbit();

        double takeOffDestination = 0;

        if(returnTripCheckBox.isSelected()){
            takeOffDestination = destinationBody.calculateLowOrbitVelocity();
        }


        double total = lowOrbitV+transfer+landing+takeOffDestination;

        lowOrbit.setText(lowOrbitV+"");
        escapeVelocityTransfer.setText(transfer+"");
        landingTxField.setText(landing+"");
        takeOffDest.setText(takeOffDestination+"");
        totalLabel.setText(total+"");

    }
    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        DIalog dialog = new DIalog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

}
