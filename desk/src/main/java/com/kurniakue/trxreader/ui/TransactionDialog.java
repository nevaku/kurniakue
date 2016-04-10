/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.ui;

import static com.kurniakue.common.Common.CASH;
import static com.kurniakue.common.Common._id;
import com.kurniakue.common.EnumField;
import com.kurniakue.common.Tool;
import com.kurniakue.trxreader.data.Customer;
import com.kurniakue.trxreader.data.CustomerD;
import com.kurniakue.trxreader.data.Item;
import com.kurniakue.trxreader.data.Item.F;
import com.kurniakue.trxreader.data.ItemD;
import com.kurniakue.trxreader.data.Persistor;
import com.kurniakue.trxreader.data.Prop;
import com.kurniakue.trxreader.data.Prop.N;
import com.kurniakue.trxreader.data.Record;
import com.kurniakue.trxreader.data.StringObj;
import com.kurniakue.trxreader.data.Transaction;
import com.kurniakue.trxreader.data.TransactionD;
import io.loli.datepicker.DatePicker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import static java.awt.event.MouseEvent.BUTTON3;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author harun1
 */
public class TransactionDialog extends javax.swing.JDialog {

    private static TransactionDialog dialog;

    private boolean ok = false;
    private Transaction record;

    /**
     * Creates new form customerDialog
     *
     * @param parent
     * @param modal
     */
    public TransactionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        DatePicker.datePicker(transactionDateBox);
        //transactionDateBox.setText(Common.formatDate(new Date(), "yyyy-MM-dd"));

        loadDefaults();
        loadItems();
        loadCustomers();
        loadTransaction();
        initSpinner(countBox);
        initTransactionTable();
        getRootPane().setDefaultButton(saveTransactionButton);
        addEscapeListener();
    }

    private OptionListModel<StringObj> itemNoOptionsModel;

    private void loadItems() {
        itemNoOptionsModel = new OptionListModel();
        ItemD item = Persistor.get(ItemD.class);
        itemNoOptionsModel.addAll(item.getAllItemsAsStringObj());
        if (itemNoOptionsModel.isEmpty()) {
            itemNoOptionsModel.add(new StringObj(new DummyRecord("RRAY"), D.Value));
            itemNoOptionsModel.add(new StringObj(new DummyRecord("RDAM"), D.Value));
            itemNoOptionsModel.add(new StringObj(new DummyRecord("PSTL"), D.Value));
            itemNoOptionsModel.add(new StringObj(new DummyRecord("KLUM"), D.Value));
        }

        AutoCompleteDecorator.decorate(itemNoBox, itemNoOptionsModel, false);
    }

    private OptionListModel<StringObj> customerIdOptionsModel;

    private void loadCustomers() {
        customerIdOptionsModel = new OptionListModel();
        CustomerD customer = Persistor.get(CustomerD.class);
        customerIdOptionsModel.addAll(customer.getAllItemsAsStringObj());
        if (customerIdOptionsModel.isEmpty()) {
            customerIdOptionsModel.add(new StringObj(new DummyRecord("QU001"), D.Value));
            customerIdOptionsModel.add(new StringObj(new DummyRecord("PS002"), D.Value));
            customerIdOptionsModel.add(new StringObj(new DummyRecord("DV003"), D.Value));
            customerIdOptionsModel.add(new StringObj(new DummyRecord("BT004"), D.Value));
        }

        //recreateCustomerBox();
        AutoCompleteDecorator.decorate(customerNameBox, customerIdOptionsModel, false);

        customerIdCounterBox.setText(CustomerD.get().getCustomerIdCounter());
    }

    private void recreateCustomerBox() {
        customerNamePanel.remove(customerNameBox);
        customerNameBox = new JTextField();

        customerNameBox.setPreferredSize(new java.awt.Dimension(230, 20));
        customerNameBox.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNameBoxFocusLost(evt);
            }
        });
        customerNameBox.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerNameBoxMouseClicked(evt);
            }
        });
        customerNameBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerNameBoxActionPerformed(evt);
            }
        });
        customerNamePanel.add(customerNameBox, 1);
    }

    private void addEscapeListener() {
        ActionListener escListener = (ActionEvent e) -> {
            close();
        };

        getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        options = new javax.swing.JPopupMenu();
        tableMenu = new javax.swing.JPopupMenu();
        EditMenu = new javax.swing.JMenuItem();
        DeleteMenu = new javax.swing.JMenuItem();
        Top = new javax.swing.JPanel();
        transactionDatePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        transactionDateBox = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        itemNoBox = new javax.swing.JTextField();
        itemNameBox = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        priceBox = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        countBox = new javax.swing.JSpinner();
        amountBox = new javax.swing.JTextField();
        customerNamePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        customerNameBox = new javax.swing.JTextField();
        saveTransactionButton = new javax.swing.JButton();
        rekapButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        customerIdBox = new javax.swing.JTextField();
        emailBox = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        aliasBox = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        transactionDatePanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        transactionIdBox = new javax.swing.JTextField();
        Center = new javax.swing.JPanel();
        transactionScroll = new javax.swing.JScrollPane();
        transactionTable = new javax.swing.JTable();
        Bottom = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        right = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        customerIdCounterBox = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        amountOfTheDateBox = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        amountOfTheMonthBox = new javax.swing.JTextField();

        EditMenu.setText("Edit");
        EditMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditMenuActionPerformed(evt);
            }
        });
        tableMenu.add(EditMenu);

        DeleteMenu.setText("Delete");
        DeleteMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteMenuActionPerformed(evt);
            }
        });
        tableMenu.add(DeleteMenu);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Top.setPreferredSize(new java.awt.Dimension(500, 270));
        Top.setLayout(new java.awt.GridLayout(0, 1));

        transactionDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("Transaction Date");
        jLabel1.setPreferredSize(new java.awt.Dimension(200, 30));
        transactionDatePanel.add(jLabel1);

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
        transactionDatePanel.add(transactionDateBox);

        jButton1.setText("<");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        transactionDatePanel.add(jButton1);

        jButton2.setText(">");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        transactionDatePanel.add(jButton2);

        jButton6.setText("All");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        transactionDatePanel.add(jButton6);

        Top.add(transactionDatePanel);

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));
        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel7.setText("Item");
        jLabel7.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel9.add(jLabel7);

        itemNoBox.setPreferredSize(new java.awt.Dimension(65, 20));
        itemNoBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemNoBoxFocusLost(evt);
            }
        });
        itemNoBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemNoBoxMouseClicked(evt);
            }
        });
        itemNoBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNoBoxActionPerformed(evt);
            }
        });
        jPanel9.add(itemNoBox);

        itemNameBox.setPreferredSize(new java.awt.Dimension(230, 20));
        jPanel9.add(itemNameBox);

        Top.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(204, 255, 204));
        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel8.setText("Item @Price");
        jLabel8.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel10.add(jLabel8);

        priceBox.setPreferredSize(new java.awt.Dimension(100, 20));
        priceBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceBoxActionPerformed(evt);
            }
        });
        jPanel10.add(priceBox);

        jButton3.setText("Save Item");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton3);

        Top.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(204, 255, 204));
        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel9.setText("Count & Amount");
        jLabel9.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel11.add(jLabel9);

        countBox.setPreferredSize(new java.awt.Dimension(65, 20));
        countBox.setValue(1);
        countBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                countBoxStateChanged(evt);
            }
        });
        jPanel11.add(countBox);

        amountBox.setEditable(false);
        amountBox.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        amountBox.setText("Rp 4.500");
        amountBox.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel11.add(amountBox);

        Top.add(jPanel11);

        customerNamePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("Customer");
        jLabel2.setPreferredSize(new java.awt.Dimension(200, 30));
        customerNamePanel.add(jLabel2);

        customerNameBox.setPreferredSize(new java.awt.Dimension(230, 20));
        customerNameBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                customerNameBoxFocusLost(evt);
            }
        });
        customerNameBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerNameBoxMouseClicked(evt);
            }
        });
        customerNameBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerNameBoxActionPerformed(evt);
            }
        });
        customerNamePanel.add(customerNameBox);

        saveTransactionButton.setText("Save Transaction");
        saveTransactionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveTransactionButtonActionPerformed(evt);
            }
        });
        customerNamePanel.add(saveTransactionButton);

        rekapButton.setText("Rekap");
        rekapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rekapButtonActionPerformed(evt);
            }
        });
        customerNamePanel.add(rekapButton);

        Top.add(customerNamePanel);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel6.setText("Customer Email");
        jLabel6.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel5.add(jLabel6);

        customerIdBox.setPreferredSize(new java.awt.Dimension(65, 20));
        jPanel5.add(customerIdBox);

        emailBox.setPreferredSize(new java.awt.Dimension(300, 20));
        jPanel5.add(emailBox);

        Top.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setText("Customer Alias");
        jLabel5.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel6.add(jLabel5);

        aliasBox.setPreferredSize(new java.awt.Dimension(300, 20));
        jPanel6.add(aliasBox);

        jButton4.setText("Save Customer");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton4);

        jButton8.setText("Delete Customer");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton8);

        Top.add(jPanel6);

        transactionDatePanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel3.setText("Transaction ID");
        jLabel3.setPreferredSize(new java.awt.Dimension(200, 30));
        transactionDatePanel1.add(jLabel3);

        transactionIdBox.setPreferredSize(new java.awt.Dimension(100, 20));
        transactionIdBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                transactionIdBoxFocusLost(evt);
            }
        });
        transactionIdBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transactionIdBoxActionPerformed(evt);
            }
        });
        transactionDatePanel1.add(transactionIdBox);

        Top.add(transactionDatePanel1);

        getContentPane().add(Top, java.awt.BorderLayout.NORTH);

        Center.setPreferredSize(new java.awt.Dimension(1050, 324));
        Center.setLayout(new java.awt.BorderLayout());

        transactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No", "Transaction Date", "Item", "Customer"
            }
        ));
        transactionTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        transactionTable.setComponentPopupMenu(tableMenu);
        transactionScroll.setViewportView(transactionTable);

        Center.add(transactionScroll, java.awt.BorderLayout.CENTER);

        getContentPane().add(Center, java.awt.BorderLayout.CENTER);

        Bottom.setBackground(new java.awt.Color(255, 255, 204));
        Bottom.setPreferredSize(new java.awt.Dimension(737, 51));

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        okButton.setText("Close");
        okButton.setPreferredSize(new java.awt.Dimension(80, 20));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel2.add(okButton);

        javax.swing.GroupLayout BottomLayout = new javax.swing.GroupLayout(Bottom);
        Bottom.setLayout(BottomLayout);
        BottomLayout.setHorizontalGroup(
            BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1124, Short.MAX_VALUE)
            .addGroup(BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BottomLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1104, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        BottomLayout.setVerticalGroup(
            BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
            .addGroup(BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BottomLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        getContentPane().add(Bottom, java.awt.BorderLayout.SOUTH);

        right.setPreferredSize(new java.awt.Dimension(215, 226));
        right.setLayout(new java.awt.GridLayout(0, 1));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jButton7.setText("Fix Amount");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton7);

        jButton9.setText("Add Credit Flag");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton9);

        jLabel12.setText("Last Customer Counter");
        jLabel12.setPreferredSize(new java.awt.Dimension(180, 30));
        jPanel7.add(jLabel12);

        customerIdCounterBox.setPreferredSize(new java.awt.Dimension(65, 20));
        jPanel7.add(customerIdCounterBox);

        jLabel10.setText("Total amount of the date");
        jLabel10.setPreferredSize(new java.awt.Dimension(180, 30));
        jPanel7.add(jLabel10);

        amountOfTheDateBox.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel7.add(amountOfTheDateBox);

        right.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel11.setText("Total Amount of the month");
        jLabel11.setPreferredSize(new java.awt.Dimension(180, 30));
        jPanel8.add(jLabel11);

        amountOfTheMonthBox.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel8.add(amountOfTheMonthBox);

        right.add(jPanel8);

        getContentPane().add(right, java.awt.BorderLayout.LINE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        close();
    }//GEN-LAST:event_okButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        goToYesterday();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        goToTomorrow();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        saveItem();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        saveCustomer();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void itemNoBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNoBoxActionPerformed
        loadSelectedItem();
    }//GEN-LAST:event_itemNoBoxActionPerformed

    private void itemNoBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemNoBoxFocusLost
        loadSelectedItem();
    }//GEN-LAST:event_itemNoBoxFocusLost

    private void itemNoBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemNoBoxMouseClicked
        if (evt.getButton() != BUTTON3) {
            return;
        }
        showItemOptions();
    }//GEN-LAST:event_itemNoBoxMouseClicked

    private void customerNameBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerNameBoxActionPerformed
        loadSelectedCustomer();
    }//GEN-LAST:event_customerNameBoxActionPerformed

    private void customerNameBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_customerNameBoxFocusLost
        loadSelectedCustomer();
    }//GEN-LAST:event_customerNameBoxFocusLost

    private void customerNameBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerNameBoxMouseClicked
        if (evt.getButton() != BUTTON3) {
            return;
        }
        showCustomerOptions();
    }//GEN-LAST:event_customerNameBoxMouseClicked

    private void saveTransactionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveTransactionButtonActionPerformed
        //saveItem();
        //saveCustomer();
        saveTransaction();
        //int row = transactionTable.getRowCount() - 1;
        //transactionTable.setRowSelectionInterval(row, row);
        //transactionScroll.
        customerNameBox.requestFocusInWindow();
    }//GEN-LAST:event_saveTransactionButtonActionPerformed

    private void priceBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceBoxActionPerformed
        updateAmount();
    }//GEN-LAST:event_priceBoxActionPerformed

    private void countBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_countBoxStateChanged
        updateAmount();
    }//GEN-LAST:event_countBoxStateChanged

    private void transactionDateBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionDateBoxActionPerformed
        storeLastDate();
        loadTransaction();
    }//GEN-LAST:event_transactionDateBoxActionPerformed

    private void transactionDateBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_transactionDateBoxFocusLost
        storeLastDate();
        loadTransaction();
    }//GEN-LAST:event_transactionDateBoxFocusLost

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        loadTransaction("");
    }//GEN-LAST:event_jButton6ActionPerformed

    private void DeleteMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteMenuActionPerformed
        deleteSelectedTransaction();
    }//GEN-LAST:event_DeleteMenuActionPerformed

    private void transactionIdBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_transactionIdBoxFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_transactionIdBoxFocusLost

    private void transactionIdBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transactionIdBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_transactionIdBoxActionPerformed

    private void EditMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditMenuActionPerformed
        editSelectedTransaction();
    }//GEN-LAST:event_EditMenuActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        TransactionD.get().fixAmount(transactionDateBox.getText());
        loadTransaction();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        deleteSelectedCustomer();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        TransactionD.get().addCreditFlag("");
        loadTransaction();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void rekapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rekapButtonActionPerformed
        recapitulateCustomerAtMonth();
    }//GEN-LAST:event_rekapButtonActionPerformed

    public void storeLastDate() {
        String lastDate = transactionDateBox.getText();
        Prop.store(N.LastDate, lastDate);
    }

    public void showItemOptions() {
        String start;
        int pos = itemNoBox.getSelectionStart();
        String selectedText = itemNoBox.getSelectedText();
        if (itemNoBox.getText() == null) {
            start = "";
        } else if (selectedText == null || "".equals(selectedText)) {
            start = itemNoBox.getText();
        } else {
            start = itemNoBox.getText().substring(0, pos);
        }

        options.removeAll();
        for (StringObj itemStringObj : itemNoOptionsModel) {
            if ("".equals(start) || itemStringObj.toString().startsWith(start)) {
                JMenuItem menuItem = new JMenuItem(
                        itemStringObj.toString() + " - "
                        + itemStringObj.getRecord().getString(F.ItemName));
                menuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        itemNoBox.setText(itemStringObj.toString());
                    }
                });
                options.add(menuItem);
            }
        }
        options.show(itemNoBox, 0, itemNoBox.getPreferredSize().height);
    }

    public void showCustomerOptions() {
        String start;
        int pos = customerNameBox.getSelectionStart();
        String selectedText = customerNameBox.getSelectedText();
        if (customerNameBox.getText() == null) {
            start = "";
        } else if (selectedText == null || "".equals(selectedText)) {
            start = customerNameBox.getText();
        } else {
            start = customerNameBox.getText().substring(0, pos);
        }

        options.removeAll();
        for (StringObj customerStringObj : customerIdOptionsModel) {
            if ("".equals(start) || customerStringObj.toString().startsWith(start)) {
                JMenuItem menuCustomer = new JMenuItem(
                        customerStringObj.toString() + " - "
                        + customerStringObj.getRecord().getString(Customer.F.CustomerName));
                menuCustomer.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        customerNameBox.setText(customerStringObj.toString());
                    }
                });
                options.add(menuCustomer);
            }
        }
        options.show(customerNameBox, 0, customerNameBox.getPreferredSize().height);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransactionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            TransactionDialog dialog1 = new TransactionDialog(new javax.swing.JFrame(), true);
            dialog1.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {

                }
            });
            dialog1.setVisible(true);
        });
    }

    private void close() {
        ok = true;
        setVisible(false);
    }

    public static boolean showDialog() {
        dialog = new TransactionDialog(
                MainFrame.getInstance(), true);
        dialog.ok = false;
        dialog.setVisible(true);
        return dialog.ok;
    }

    public static TransactionDialog get() {
        return dialog;
    }

    public static void disposeDialog() {
        dialog.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bottom;
    private javax.swing.JPanel Center;
    private javax.swing.JMenuItem DeleteMenu;
    private javax.swing.JMenuItem EditMenu;
    private javax.swing.JPanel Top;
    private javax.swing.JTextField aliasBox;
    private javax.swing.JTextField amountBox;
    private javax.swing.JTextField amountOfTheDateBox;
    private javax.swing.JTextField amountOfTheMonthBox;
    private javax.swing.JSpinner countBox;
    private javax.swing.JTextField customerIdBox;
    private javax.swing.JTextField customerIdCounterBox;
    private javax.swing.JTextField customerNameBox;
    private javax.swing.JPanel customerNamePanel;
    private javax.swing.JTextField emailBox;
    private javax.swing.JTextField itemNameBox;
    private javax.swing.JTextField itemNoBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton okButton;
    private javax.swing.JPopupMenu options;
    private javax.swing.JTextField priceBox;
    private javax.swing.JButton rekapButton;
    private javax.swing.JPanel right;
    private javax.swing.JButton saveTransactionButton;
    private javax.swing.JPopupMenu tableMenu;
    private javax.swing.JTextField transactionDateBox;
    private javax.swing.JPanel transactionDatePanel;
    private javax.swing.JPanel transactionDatePanel1;
    private javax.swing.JTextField transactionIdBox;
    private javax.swing.JScrollPane transactionScroll;
    private javax.swing.JTable transactionTable;
    // End of variables declaration//GEN-END:variables

    public String getEmail() {
        return emailBox.getText();
    }

    public String getAlias() {
        return aliasBox.getText();
    }

    public void setCustomerName(String name) {
        customerNameBox.setText(name);
    }

    public void setEmail(String email) {
        emailBox.setText(email);
    }

    public void setAlias(String alias) {
        aliasBox.setText(alias);
    }

    public void clear() {
        customerNameBox.setText("");
        emailBox.setText("");
        aliasBox.setText("");
    }

    public Transaction getRecord() {
        Transaction transaction = new Transaction();
        return transaction;
    }

    private void goToYesterday() {
        String now = transactionDateBox.getText();
        String yesterday = Tool.addDay(now, -1);
        transactionDateBox.setText(yesterday);
        storeLastDate();
        loadTransaction();
    }

    private void goToTomorrow() {
        String now = transactionDateBox.getText();
        String yesterday = Tool.addDay(now, +1);
        transactionDateBox.setText(yesterday);
        storeLastDate();
        loadTransaction();
    }

    private void saveItem() {
        String itemNo = String.valueOf(this.itemNoBox.getText());
        Item item = ItemD.get().find(itemNo, new Item());
        item.put(F.ItemNo, itemNo);
        item.put(F.ItemName, itemNameBox.getText());
        item.put(F.Price, Tool.tint(priceBox.getText()));
        ItemD.get().persist(item);

        loadItems();
        this.itemNoBox.setText(itemNo);
        itemNoBox.requestFocusInWindow();
    }

    private void loadSelectedItem() {
        String itemNo = String.valueOf(this.itemNoBox.getText());
        Item item = ItemD.get().find(itemNo, new Item());
        if (item.tstr(_id) == null) {
            itemNameBox.setText("");
            priceBox.setText("");
            return;
        }

        itemNameBox.setText(item.tstr(F.ItemName));
        priceBox.setText(item.tstr(F.Price));
        updateAmount();
    }

    private void loadSelectedCustomer() {
        String customerName = String.valueOf(customerNameBox.getText());
        Customer customer = CustomerD.get().find(customerName, new Customer());
        if (customer.tstr(_id) == null) {
            customerIdBox.setText("");
            emailBox.setText("");
            aliasBox.setText("");
            return;
        }

        customerIdBox.setText(customer.tstr(Customer.F.CustomerID));
        emailBox.setText(customer.tstr(Customer.F.Email));
        aliasBox.setText(customer.tstr(Customer.F.Alias));
    }

    private void saveCustomer() {
        String customerName = String.valueOf(this.customerNameBox.getText());
        Customer customer = CustomerD.get().find(customerName, new Customer());
        customer.put(Customer.F.CustomerName, customerName);
        customer.put(Customer.F.CustomerID, customerIdBox.getText());
        customer.put(Customer.F.Email, emailBox.getText());
        customer.put(Customer.F.Alias, aliasBox.getText());
        CustomerD.get().persist(customer);

        loadCustomers();
        this.customerNameBox.setText(customerName);
        customerNameBox.requestFocusInWindow();
    }

    private void saveTransaction() {
        int trxId;
        Transaction transaction;
        if ("".equals(Tool.tstr(transactionIdBox.getText()))) {
            trxId = Prop.getInt(N.LastTransactionID);
            if (trxId < 0) {
                trxId = 0;
            }
            trxId += 1;
            Prop.store(N.LastTransactionID, trxId);
            transaction = new Transaction();
        } else {
            trxId = Tool.tint(transactionIdBox.getText());
            transaction = TransactionD.get().find(trxId, new Transaction());
        }

        String customerName = String.valueOf(this.customerNameBox.getText());
        Customer customer = CustomerD.get().find(customerName, new Customer());
        customer.put(Customer.F.CustomerName, customerName);

        String itemNo = String.valueOf(itemNoBox.getText());
        Item item = ItemD.get().find(itemNo, new Item());

        transaction.put(Transaction.F.TransactionID, trxId);
        transaction.put(Transaction.F.Date, transactionDateBox.getText());
        transaction.put(Transaction.F.CustomerID, customer.getString(Customer.F.CustomerID));
        transaction.put(Transaction.F.CustomerName, customerName);
        transaction.put(Transaction.F.ItemNo, itemNo);
        transaction.put(Transaction.F.ItemName, item.getString(Item.F.ItemName));
        transaction.put(Transaction.F.Price, Tool.tint(priceBox.getText()));
        transaction.put(Transaction.F.Count, Tool.tint(countBox.getValue()));
        int amount = Tool.tint(priceBox.getText()) * Tool.tint(countBox.getValue());
        transaction.put(Transaction.F.Amount, amount);

        int dcflag = -1;
        if (CASH.equals(itemNo)) {
            dcflag = 1;
        }
        transaction.put(Transaction.F.DCFlag, dcflag);

        TransactionD.get().persist(transaction);
        transactionIdBox.setText("");

        loadTransaction();
    }

    private void updateAmount() {
        int amount = Tool.tint(priceBox.getText()) * Tool.tint(countBox.getValue());
        amountBox.setText(Tool.formatMoney(amount));
    }

    private void initSpinner(JSpinner spinner) {
        JComponent comp = spinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
    }

    private void initTransactionTable() {
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(3);

        transactionTable.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                transactionTable.scrollRectToVisible(transactionTable.getCellRect(transactionTable.getRowCount() - 1, 0, true));
            }
        });
    }

    private void loadTransaction() {
        String transactionDate = transactionDateBox.getText();
        loadTransaction(transactionDate);
    }

    private void loadTransaction(String transactionDate) {
        TransactionD transactiond = TransactionD.get();
        List<Transaction> transactionList = transactiond.getTransactionsByDate(
                transactionDate
        );

        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(transactionList.size());
        int row = 0;
        int no = 1;
        String lastItemNo = null;
        for (Transaction transaction : transactionList) {
            String itemNo = transaction.tstr(Transaction.F.ItemNo);
            if (!itemNo.equals(lastItemNo)) {
                no = 1;
                lastItemNo = itemNo;
            }

            model.setValueAt(no, row, 0);
            model.setValueAt(
                    transaction.getString(Transaction.F.Date)
                    + "," + transaction.getString(Transaction.F.TransactionID),
                    row, 1);
            model.setValueAt(
                    transaction.getString(Transaction.F.ItemNo)
                    + "," + transaction.getString(Transaction.F.ItemName)
                    + "," + transaction.getString(Transaction.F.Price)
                    + "," + transaction.getString(Transaction.F.Count)
                    + "," + Tool.formatNumber(transaction.getInt(Transaction.F.Amount)
                            * transaction.getInt(Transaction.F.DCFlag)),
                    row, 2);
            model.setValueAt(
                    transaction.getString(Transaction.F.CustomerID)
                    + "," + transaction.getString(Transaction.F.CustomerName),
                    row, 3);
            row += 1;
            no += 1;
        }

        int amountOfTheDate = TransactionD.get().getAmountOfTheDate(transactionDate);
        amountOfTheDateBox.setText(Tool.formatMoney(amountOfTheDate));

        int amountOfTheMonth = TransactionD.get().getAmountOfTheMonth(transactionDate);
        amountOfTheMonthBox.setText(Tool.formatMoney(amountOfTheMonth));
    }

    private void loadDefaults() {
        String lastDate = Prop.tstr(N.LastDate);
        if ("".equals(lastDate)) {
            lastDate = Tool.formatDate(new Date(), "yyyy-MM-dd");
        }
        transactionDateBox.setText(lastDate);
    }

    private void editSelectedTransaction() {
        int row = transactionTable.getSelectedRow();
        String dateid = Tool.tstr(transactionTable.getValueAt(row, 1));
        String[] dateids = dateid.split(",");
        int transactionId = Tool.tint(dateids[1]);
        Transaction transaction = TransactionD.get().find(transactionId, new Transaction());

        transactionDateBox.setText(transaction.getString(Transaction.F.Date));
        transactionIdBox.setText(transaction.getString(Transaction.F.TransactionID));

        itemNoBox.setText(transaction.getString(Transaction.F.ItemNo));
        loadSelectedItem();

        // price of item may change over time
        priceBox.setText(transaction.getString(Transaction.F.Price));
        countBox.setValue(transaction.getInt(Transaction.F.Count));

        customerNameBox.setText(transaction.getString(Transaction.F.CustomerName));
        loadSelectedCustomer();
    }

    private void deleteSelectedTransaction() {
        int row = transactionTable.getSelectedRow();
        String dateid = Tool.tstr(transactionTable.getValueAt(row, 1));
        int ret = JOptionPane.showConfirmDialog(this, "Are you sure to delete Transaction " + dateid);
        if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION) {
            return;
        }

        String[] dateids = dateid.split(",");
        int transactionId = Tool.tint(dateids[1]);
        TransactionD.get().delete(transactionId);

        loadTransaction();
    }

    private void deleteSelectedCustomer() {
        int ret = JOptionPane.showConfirmDialog(this, "Are you sure to delete Customer " + customerNameBox.getText());
        if (ret == JOptionPane.NO_OPTION || ret == JOptionPane.CANCEL_OPTION) {
            return;
        }

        CustomerD.get().delete(customerNameBox.getText());
        loadCustomers();
    }

    private void recapitulateCustomerAtMonth() {
        String customerName = customerNameBox.getText();
        String transactionMonth = Tool.convertDateFormat(transactionDateBox.getText(), "yyyy-MM-dd", "yyyy-MM");
        TransactionD.get().printCustomerAtMonth(customerName, transactionMonth);
        TransactionD.get().recapitulateCustomerAtMonth(customerName, transactionMonth);
    }

    public static class OptionListModel<E> extends ArrayList<E> implements MutableComboBoxModel<E> {

        private E selectedItem;

        @Override
        public void setSelectedItem(Object anItem) {
            selectedItem = (E) anItem;
        }

        @Override
        public E getSelectedItem() {
            return selectedItem;
        }

        @Override
        public int getSize() {
            return super.size();
        }

        @Override
        public E getElementAt(int index) {
            return super.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener listener) {

        }

        @Override
        public void removeListDataListener(ListDataListener l) {

        }

        @Override
        public void addElement(E item) {
            add(item);
        }

        @Override
        public void removeElement(Object obj) {
            remove((E) obj);
        }

        @Override
        public void insertElementAt(E item, int index) {
            add(index, item);
        }

        @Override
        public void removeElementAt(int index) {
            remove(index);
        }
    }

    public enum D implements EnumField {

        Value
    }

    public static class DummyRecord extends Record {

        public DummyRecord(String value) {
            put(D.Value, value);
        }
    }
}
