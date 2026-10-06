package view;

/**
 * BoardingForm: passive view (designer form).
 * Logic lives in BoardingController.
 */
public class BoardingForm extends javax.swing.JFrame {

    public BoardingForm() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    // Message line
    public void showMessage(String text, boolean ok) {
        lblMessage.setForeground(ok ? new java.awt.Color(60, 140, 90) : new java.awt.Color(200, 60, 60));
        lblMessage.setText(text);
    }

    // Table data
    public void setTableModel(javax.swing.table.TableModel model) {
        tblBoarding.setModel(model);
    }

    // Stat cards
    public void setStats(int total, long active, long available, long occupied) {
        lblTotalCount.setText(String.valueOf(total));
        lblActiveCount.setText(String.valueOf(active));
        lblAvailableCount.setText(String.valueOf(available));
        lblOccupiedCount.setText(String.valueOf(occupied));
    }

    public javax.swing.JButton getBtnBookRoom() {
        return btnBookRoom;
    }

    public javax.swing.JButton getBtnCheckOut() {
        return btnCheckOut;
    }

    public javax.swing.JButton getBtnClearBoarding() {
        return btnClearBoarding;
    }

    public javax.swing.JButton getBtnDeleteBooking() {
        return btnDeleteBooking;
    }

    public javax.swing.JComboBox<String> getCmbCustomer() {
        return cmbCustomer;
    }

    public javax.swing.JComboBox<String> getCmbPet() {
        return cmbPet;
    }

    public javax.swing.JComboBox<String> getCmbRoom() {
        return cmbRoom;
    }

    public javax.swing.JComboBox<String> getCmbStatus() {
        return cmbStatus;
    }

    public javax.swing.JLabel getLblActiveCount() {
        return lblActiveCount;
    }

    public javax.swing.JLabel getLblAvailableCount() {
        return lblAvailableCount;
    }

    public javax.swing.JLabel getLblMessage() {
        return lblMessage;
    }

    public javax.swing.JLabel getLblOccupiedCount() {
        return lblOccupiedCount;
    }

    public javax.swing.JLabel getLblTotalCount() {
        return lblTotalCount;
    }

    public javax.swing.JTable getTblBoarding() {
        return tblBoarding;
    }

    public javax.swing.JTextField getTxtCheckIn() {
        return txtCheckIn;
    }

    public javax.swing.JTextField getTxtCheckOut() {
        return txtCheckOut;
    }

    public javax.swing.JTextField getTxtNotes() {
        return txtNotes;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBoardingContent = new javax.swing.JPanel();
        pnlCardTotal = new javax.swing.JPanel();
        lblTotalTitle = new javax.swing.JLabel();
        lblTotalCount = new javax.swing.JLabel();
        pnlCardActive = new javax.swing.JPanel();
        lblScheduledTitle = new javax.swing.JLabel();
        lblActiveCount = new javax.swing.JLabel();
        pnlCardAvailable = new javax.swing.JPanel();
        lblAvailableCount = new javax.swing.JLabel();
        lblAvailableTitle = new javax.swing.JLabel();
        pnlCardOccupied = new javax.swing.JPanel();
        lblOccupiedCount = new javax.swing.JLabel();
        lblOccupiedTitle = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlBoardingDetails = new javax.swing.JPanel();
        lblFormSubtitle = new javax.swing.JLabel();
        lblformTitle = new javax.swing.JLabel();
        lblCheckIn = new javax.swing.JLabel();
        lblPet = new javax.swing.JLabel();
        cmbPet = new javax.swing.JComboBox<>();
        lblRoom = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblCustomer = new javax.swing.JLabel();
        txtCheckIn = new javax.swing.JTextField();
        lblCheckOut = new javax.swing.JLabel();
        txtCheckOut = new javax.swing.JTextField();
        lblMessage = new javax.swing.JLabel();
        btnBookRoom = new javax.swing.JButton();
        btnCheckOut = new javax.swing.JButton();
        btnDeleteBooking = new javax.swing.JButton();
        btnClearBoarding = new javax.swing.JButton();
        cmbCustomer = new javax.swing.JComboBox<>();
        cmbRoom = new javax.swing.JComboBox<>();
        cmbStatus = new javax.swing.JComboBox<>();
        lblNotes = new javax.swing.JLabel();
        txtNotes = new javax.swing.JTextField();
        lblBoardingListTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBoarding = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1500, 900));
        setPreferredSize(new java.awt.Dimension(1500, 900));
        setSize(new java.awt.Dimension(1500, 900));

        pnlBoardingContent.setBackground(new java.awt.Color(250, 246, 240));
        pnlBoardingContent.setMinimumSize(new java.awt.Dimension(1500, 900));
        pnlBoardingContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlCardTotal.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTotalTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTotalTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblTotalTitle.setText("TOTAL");
        pnlCardTotal.add(lblTotalTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        lblTotalCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTotalCount.setForeground(new java.awt.Color(74, 91, 106));
        lblTotalCount.setText("0");
        pnlCardTotal.add(lblTotalCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        pnlBoardingContent.add(pnlCardTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 280, 90));

        pnlCardActive.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardActive.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardActive.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblScheduledTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblScheduledTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblScheduledTitle.setText("ACTIVE");
        pnlCardActive.add(lblScheduledTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        lblActiveCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblActiveCount.setForeground(new java.awt.Color(74, 91, 106));
        lblActiveCount.setText("0");
        pnlCardActive.add(lblActiveCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        pnlBoardingContent.add(pnlCardActive, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 280, 90));

        pnlCardAvailable.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardAvailable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardAvailable.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAvailableCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblAvailableCount.setForeground(new java.awt.Color(74, 91, 106));
        lblAvailableCount.setText("0");
        pnlCardAvailable.add(lblAvailableCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        lblAvailableTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblAvailableTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblAvailableTitle.setText("AVAILABLE ROOMS");
        pnlCardAvailable.add(lblAvailableTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        pnlBoardingContent.add(pnlCardAvailable, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 90, 280, 90));

        pnlCardOccupied.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardOccupied.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardOccupied.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblOccupiedCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblOccupiedCount.setForeground(new java.awt.Color(74, 91, 106));
        lblOccupiedCount.setText("0");
        pnlCardOccupied.add(lblOccupiedCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        lblOccupiedTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblOccupiedTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblOccupiedTitle.setText("OCCUPIED ROOMS");
        pnlCardOccupied.add(lblOccupiedTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        pnlBoardingContent.add(pnlCardOccupied, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 90, 280, 90));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblTitle.setText("Boarding");
        pnlBoardingContent.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 500, 35));

        lblSubtitle.setForeground(new java.awt.Color(110, 125, 135));
        lblSubtitle.setText("Manage pet boarding and rooms");
        pnlBoardingContent.add(lblSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 500, 20));

        pnlBoardingDetails.setBackground(new java.awt.Color(255, 255, 255));
        pnlBoardingDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlBoardingDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFormSubtitle.setForeground(new java.awt.Color(110, 125, 135));
        lblFormSubtitle.setText("Register and manage boarding bookings");
        pnlBoardingDetails.add(lblFormSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 500, 20));

        lblformTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblformTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblformTitle.setText("BOARDING BOOKING");
        pnlBoardingDetails.add(lblformTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 500, 25));

        lblCheckIn.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCheckIn.setForeground(new java.awt.Color(113, 128, 140));
        lblCheckIn.setText("CHECK-IN (YYYY-MM-DD)");
        pnlBoardingDetails.add(lblCheckIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 270, 18));

        lblPet.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblPet.setForeground(new java.awt.Color(113, 128, 140));
        lblPet.setText("PET");
        pnlBoardingDetails.add(lblPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 270, 18));

        cmbPet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlBoardingDetails.add(cmbPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, 270, 32));

        lblRoom.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblRoom.setForeground(new java.awt.Color(113, 128, 140));
        lblRoom.setText("ROOM");
        pnlBoardingDetails.add(lblRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 90, 270, 18));

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(113, 128, 140));
        lblStatus.setText("STATUS");
        pnlBoardingDetails.add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 90, 270, 18));

        lblCustomer.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCustomer.setForeground(new java.awt.Color(113, 128, 140));
        lblCustomer.setText("CUSTOMER");
        pnlBoardingDetails.add(lblCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 270, 18));
        pnlBoardingDetails.add(txtCheckIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 270, 32));

        lblCheckOut.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCheckOut.setForeground(new java.awt.Color(113, 128, 140));
        lblCheckOut.setText("CHECK-OUT (YYYY-MM-DD)");
        pnlBoardingDetails.add(lblCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 270, 18));
        pnlBoardingDetails.add(txtCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 180, 270, 32));

        lblMessage.setForeground(java.awt.Color.red);
        pnlBoardingDetails.add(lblMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 600, 20));

        btnBookRoom.setBackground(new java.awt.Color(157, 201, 163));
        btnBookRoom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBookRoom.setForeground(new java.awt.Color(74, 91, 106));
        btnBookRoom.setText("Book Room");
        pnlBoardingDetails.add(btnBookRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 270, 45));

        btnCheckOut.setBackground(new java.awt.Color(190, 210, 230));
        btnCheckOut.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCheckOut.setForeground(new java.awt.Color(74, 91, 106));
        btnCheckOut.setText("Check Out");
        pnlBoardingDetails.add(btnCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, 270, 45));

        btnDeleteBooking.setBackground(new java.awt.Color(220, 170, 170));
        btnDeleteBooking.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteBooking.setForeground(new java.awt.Color(120, 40, 40));
        btnDeleteBooking.setText("Delete");
        pnlBoardingDetails.add(btnDeleteBooking, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 270, 270, 45));

        btnClearBoarding.setBackground(new java.awt.Color(235, 235, 230));
        btnClearBoarding.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClearBoarding.setForeground(new java.awt.Color(74, 91, 106));
        btnClearBoarding.setText("Clear");
        pnlBoardingDetails.add(btnClearBoarding, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 270, 270, 45));

        cmbCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlBoardingDetails.add(cmbCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 270, 32));

        cmbRoom.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlBoardingDetails.add(cmbRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 270, 32));

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlBoardingDetails.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 270, 32));

        lblNotes.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblNotes.setForeground(new java.awt.Color(113, 128, 140));
        lblNotes.setText("NOTES");
        pnlBoardingDetails.add(lblNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, 560, 18));
        pnlBoardingDetails.add(txtNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 180, 560, 32));

        pnlBoardingContent.add(pnlBoardingDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 1180, 340));

        lblBoardingListTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblBoardingListTitle.setForeground(new java.awt.Color(113, 128, 140));
        lblBoardingListTitle.setText("ALL BOOKINGS");
        pnlBoardingContent.add(lblBoardingListTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 400, 25));

        tblBoarding.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblBoarding.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Customer", "Pet", "Room", "Check-in", "Check-out", "Days", "Total", "Status"
            }
        ));
        tblBoarding.setRowHeight(28);
        jScrollPane1.setViewportView(tblBoarding);

        pnlBoardingContent.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 1180, 250));

        getContentPane().add(pnlBoardingContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBookRoom;
    private javax.swing.JButton btnCheckOut;
    private javax.swing.JButton btnClearBoarding;
    private javax.swing.JButton btnDeleteBooking;
    private javax.swing.JComboBox<String> cmbCustomer;
    private javax.swing.JComboBox<String> cmbPet;
    private javax.swing.JComboBox<String> cmbRoom;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblActiveCount;
    private javax.swing.JLabel lblAvailableCount;
    private javax.swing.JLabel lblAvailableTitle;
    private javax.swing.JLabel lblBoardingListTitle;
    private javax.swing.JLabel lblCheckIn;
    private javax.swing.JLabel lblCheckOut;
    private javax.swing.JLabel lblCustomer;
    private javax.swing.JLabel lblFormSubtitle;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblNotes;
    private javax.swing.JLabel lblOccupiedCount;
    private javax.swing.JLabel lblOccupiedTitle;
    private javax.swing.JLabel lblPet;
    private javax.swing.JLabel lblRoom;
    private javax.swing.JLabel lblScheduledTitle;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalCount;
    private javax.swing.JLabel lblTotalTitle;
    private javax.swing.JLabel lblformTitle;
    private javax.swing.JPanel pnlBoardingContent;
    private javax.swing.JPanel pnlBoardingDetails;
    private javax.swing.JPanel pnlCardActive;
    private javax.swing.JPanel pnlCardAvailable;
    private javax.swing.JPanel pnlCardOccupied;
    private javax.swing.JPanel pnlCardTotal;
    private javax.swing.JTable tblBoarding;
    private javax.swing.JTextField txtCheckIn;
    private javax.swing.JTextField txtCheckOut;
    private javax.swing.JTextField txtNotes;
    // End of variables declaration//GEN-END:variables
}
