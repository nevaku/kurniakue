/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.ui;

import com.kurniakue.common.Common;
import static com.kurniakue.common.Common.PrintMode.*;
import static com.kurniakue.common.Common.formatDate;
import static com.kurniakue.common.Common.parseDate;
import com.kurniakue.trxreader.TrxDbReader;
import com.kurniakue.trxreader.data.KurniaKueDb;
import io.loli.datepicker.DatePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author harun1
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        DetailOptBox = new javax.swing.JCheckBox();
        EmailOptBox = new javax.swing.JCheckBox();
        ListTextOptBox = new javax.swing.JCheckBox();
        ListCsvOptBox = new javax.swing.JCheckBox();
        DbUpdateOptBox = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        transactionDateBox = new javax.swing.JTextField();
        baseDirBox = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jButton1.setText("Data Entry");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Generate Report");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        DetailOptBox.setText("Detail");

        EmailOptBox.setText("Email");

        ListTextOptBox.setText("List Text");

        ListCsvOptBox.setText("List csv");

        DbUpdateOptBox.setText("DB Update");

        jButton3.setText("Exit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        transactionDateBox.setText("2015-01-01");
        transactionDateBox.setPreferredSize(new java.awt.Dimension(100, 20));
        transactionDateBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                transactionDateBoxFocusLost(evt);
            }
        });
        transactionDateBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionDateBoxActionPerformed(evt);
            }
        });

        baseDirBox.setPreferredSize(new java.awt.Dimension(100, 20));
        baseDirBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                baseDirBoxFocusLost(evt);
            }
        });
        baseDirBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                baseDirBoxActionPerformed(evt);
            }
        });

        browseButton.setText("...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DbUpdateOptBox)
                    .addComponent(ListCsvOptBox)
                    .addComponent(EmailOptBox)
                    .addComponent(DetailOptBox)
                    .addComponent(ListTextOptBox)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(transactionDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(baseDirBox, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(transactionDateBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(DetailOptBox)
                    .addComponent(baseDirBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EmailOptBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ListTextOptBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ListCsvOptBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DbUpdateOptBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showDataEntry();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        exit();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        destroy();
    }//GEN-LAST:event_formWindowClosed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        generateReport();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void transactionDateBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_transactionDateBoxFocusLost
        
    }//GEN-LAST:event_transactionDateBoxFocusLost

    private void transactionDateBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionDateBoxActionPerformed
        
    }//GEN-LAST:event_transactionDateBoxActionPerformed

    private void baseDirBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_baseDirBoxFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_baseDirBoxFocusLost

    private void baseDirBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_baseDirBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_baseDirBoxActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        browseBaseDir();
    }//GEN-LAST:event_browseButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            instance.init();
            instance.setVisible(true);
            //TrxWriter.main(args);
            //TrxDbReader.main(args);
            //instance.dispose();
        });
    }

    private static final MainFrame instance = new MainFrame();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox DbUpdateOptBox;
    private javax.swing.JCheckBox DetailOptBox;
    private javax.swing.JCheckBox EmailOptBox;
    private javax.swing.JCheckBox ListCsvOptBox;
    private javax.swing.JCheckBox ListTextOptBox;
    private javax.swing.JTextField baseDirBox;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JTextField transactionDateBox;
    // End of variables declaration//GEN-END:variables

    public static MainFrame getInstance() {
        return instance;
    }

    private void showDataEntry() {
        TransactionDialog.showDialog();
    }

    private void exit() {
        dispose();
    }

    private void destroy() {
        KurniaKueDb.stop();
    }

    private void init() {
        KurniaKueDb.init();
        DatePicker.datePicker(transactionDateBox);
        transactionDateBox.setText(formatDate(new Date(), "yyyy-MM-dd"));
        baseDirBox.setText(Common.driveDir);
    }

    private void generateReport() {
        //Common.printModes = EnumSet.of(Detail, ListCsv);
        Common.printModes = EnumSet.of(DbUpdate);
        if (DetailOptBox.isSelected()) {
            Common.printModes.add(Detail);
        }
        if (EmailOptBox.isSelected()) {
            Common.printModes.add(Email);
        }
        if (ListTextOptBox.isSelected()) {
            Common.printModes.add(List);
        }
        if (ListCsvOptBox.isSelected()) {
            Common.printModes.add(ListCsv);
        }
        if (!DbUpdateOptBox.isSelected()) {
            Common.printModes.remove(DbUpdate);
        }
        String strDate = transactionDateBox.getText();
        Date date = parseDate(strDate, "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String baseDir = baseDirBox.getText();
        TrxDbReader.processBillFromDb(calendar, baseDir);
        JOptionPane.showMessageDialog(this, "Complete");
    }

    private void browseBaseDir() {
        JFileChooser explorer = new JFileChooser(baseDirBox.getText());
        explorer.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (explorer.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            baseDirBox.setText(explorer.getSelectedFile().getPath());
        }
    }
}