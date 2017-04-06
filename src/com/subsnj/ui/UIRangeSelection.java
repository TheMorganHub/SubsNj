package com.subsnj.ui;

import com.subsnj.util.Utils;
import javax.swing.SpinnerNumberModel;

public class UIRangeSelection extends javax.swing.JDialog {

    private int[] range;

    public UIRangeSelection() {
        super(UISyncer.getINSTANCE(), true);
        initComponents();
        setLocationRelativeTo(getParent());
        getRootPane().setDefaultButton(btnConfirm);
        range = new int[2];
    }

    /**
     * Displays the select range dialog. The min range isn't needed because it
     * will always be 0.
     *
     * @param maxRange The maximum range displayed by this dialog. Usually the
     * amount of subtitles in the active parser.
     * @return an array with the min and max ranges.
     */
    public int[] showDialog(int maxRange) {
        spinnerTo.setValue(maxRange);
        ((SpinnerNumberModel) spinnerTo.getModel()).setMaximum(maxRange);
        setVisible(true);
        range[0] = (int) spinnerFrom.getValue();
        range[1] = (int) spinnerTo.getValue();
        return range;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnConfirm = new javax.swing.JButton();
        spinnerFrom = new javax.swing.JSpinner();
        spinnerTo = new javax.swing.JSpinner();
        iLblFrom = new javax.swing.JLabel();
        iLblTo = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select a range");

        btnConfirm.setText("Confirm");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        spinnerFrom.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        spinnerTo.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        iLblFrom.setText("From");

        iLblTo.setText("To");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(iLblFrom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(iLblTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerTo, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirm)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iLblFrom)
                    .addComponent(iLblTo))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirm)
                    .addComponent(btnCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        actionConfirm();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        //return both -1
        spinnerFrom.setValue(-1);
        spinnerTo.setValue(-1);
        dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    public void actionConfirm() {
        if ((int) spinnerFrom.getValue() > (int) spinnerTo.getValue()) {
            Utils.showMessage("Invalid range", "The minimum index cannot be larger than the maximum.", this);
            return;
        }
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JLabel iLblFrom;
    private javax.swing.JLabel iLblTo;
    private javax.swing.JSpinner spinnerFrom;
    private javax.swing.JSpinner spinnerTo;
    // End of variables declaration//GEN-END:variables
}
