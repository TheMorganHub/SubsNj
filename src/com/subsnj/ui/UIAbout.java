package com.subsnj.ui;

public class UIAbout extends javax.swing.JDialog {

    public UIAbout() {
        super(UISyncer.getINSTANCE(), true);
        initComponents();
        getRootPane().setDefaultButton(btnThanks);
        setLocationRelativeTo(getParent());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLogo = new javax.swing.JLabel();
        lblSubLogo = new javax.swing.JLabel();
        lblDesignedBy = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();
        btnThanks = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About SubsNj");
        setMinimumSize(new java.awt.Dimension(408, 264));

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgresources/SubsNj_logo.png"))); // NOI18N

        lblSubLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSubLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgresources/SubsNj_logo_bottom.png"))); // NOI18N

        lblDesignedBy.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDesignedBy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDesignedBy.setText("Designed by Morgan");

        lblVersion.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVersion.setText("v1.0.1");
        lblVersion.setEnabled(false);

        btnThanks.setText("Thanks!");
        btnThanks.setFocusable(false);
        btnThanks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanksActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblSubLogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
            .addComponent(lblLogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblVersion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThanks)
                .addContainerGap())
            .addComponent(lblDesignedBy, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubLogo)
                .addGap(18, 18, 18)
                .addComponent(lblDesignedBy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThanks, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblVersion, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThanksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanksActionPerformed
        dispose();
    }//GEN-LAST:event_btnThanksActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThanks;
    private javax.swing.JLabel lblDesignedBy;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblSubLogo;
    private javax.swing.JLabel lblVersion;
    // End of variables declaration//GEN-END:variables
}
