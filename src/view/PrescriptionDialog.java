package view;


/**
 * PrescriptionDialog: passive view (designer form).
 * Logic lives in the controller.
 */
public class PrescriptionDialog extends javax.swing.JDialog {


    public PrescriptionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        java.awt.Color sage = new java.awt.Color(157, 201, 163);
        for (javax.swing.JTable t : new javax.swing.JTable[]{tblLines, tblMedicines}) {
            t.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 12));
            t.setSelectionBackground(sage);
            t.setSelectionForeground(new java.awt.Color(74, 91, 106));
        }
        showStockTab(false);
    }

    public javax.swing.JPanel getPnlRoot() {
        return pnlRoot;
    }

    public javax.swing.JLabel getLblHeader() {
        return lblHeader;
    }

    public javax.swing.JButton getBtnTabPrescription() {
        return btnTabPrescription;
    }

    public javax.swing.JButton getBtnTabStock() {
        return btnTabStock;
    }

    public javax.swing.JPanel getPnlPrescription() {
        return pnlPrescription;
    }

    public javax.swing.JLabel getLblMedicine() {
        return lblMedicine;
    }

    public javax.swing.JComboBox<String> getCmbMedicine() {
        return cmbMedicine;
    }

    public javax.swing.JLabel getLblQuantity() {
        return lblQuantity;
    }

    public javax.swing.JTextField getTxtQuantity() {
        return txtQuantity;
    }

    public javax.swing.JLabel getLblDosage() {
        return lblDosage;
    }

    public javax.swing.JTextField getTxtDosage() {
        return txtDosage;
    }

    public javax.swing.JLabel getLblInstructions() {
        return lblInstructions;
    }

    public javax.swing.JTextField getTxtInstructions() {
        return txtInstructions;
    }

    public javax.swing.JButton getBtnAddMedicine() {
        return btnAddMedicine;
    }

    public javax.swing.JButton getBtnRemoveLine() {
        return btnRemoveLine;
    }

    public javax.swing.JButton getBtnClose() {
        return btnClose;
    }

    public javax.swing.JTable getTblLines() {
        return tblLines;
    }

    public javax.swing.JLabel getLblTotal() {
        return lblTotal;
    }

    public javax.swing.JPanel getPnlStock() {
        return pnlStock;
    }

    public javax.swing.JLabel getLblName() {
        return lblName;
    }

    public javax.swing.JTextField getTxtName() {
        return txtName;
    }

    public javax.swing.JLabel getLblCategory() {
        return lblCategory;
    }

    public javax.swing.JTextField getTxtCategory() {
        return txtCategory;
    }

    public javax.swing.JLabel getLblPrice() {
        return lblPrice;
    }

    public javax.swing.JTextField getTxtPrice() {
        return txtPrice;
    }

    public javax.swing.JLabel getLblStock() {
        return lblStock;
    }

    public javax.swing.JTextField getTxtStock() {
        return txtStock;
    }

    public javax.swing.JLabel getLblStatus() {
        return lblStatus;
    }

    public javax.swing.JComboBox<String> getCmbStatus() {
        return cmbStatus;
    }

    public javax.swing.JButton getBtnAddStock() {
        return btnAddStock;
    }

    public javax.swing.JButton getBtnUpdateStock() {
        return btnUpdateStock;
    }

    public javax.swing.JButton getBtnDeleteStock() {
        return btnDeleteStock;
    }

    public javax.swing.JButton getBtnClearStock() {
        return btnClearStock;
    }

    public javax.swing.JTable getTblMedicines() {
        return tblMedicines;
    }

    // Header text
    public void setHeader(String text) {
        lblHeader.setText(text);
    }

    // Switch tab
    public void showStockTab(boolean stock) {
        pnlStock.setVisible(stock);
        pnlPrescription.setVisible(!stock);
        btnTabStock.setBackground(stock ? new java.awt.Color(157, 201, 163) : new java.awt.Color(235, 235, 230));
        btnTabPrescription.setBackground(stock ? new java.awt.Color(235, 235, 230) : new java.awt.Color(157, 201, 163));
    }

    // Medicine combo
    public void setMedicineItems(java.util.List<String> items) {
        cmbMedicine.removeAllItems();
        items.forEach(cmbMedicine::addItem);
    }

    // Prescription rows
    public void setLines(java.util.List<Object[]> rows) {
        tblLines.setModel(readOnly(new String[]{"Medicine", "Qty", "Dosage", "Instructions", "Unit price", "Line total"}, rows));
    }

    // Stock rows
    public void setMedicineRows(java.util.List<Object[]> rows) {
        tblMedicines.setModel(readOnly(new String[]{"ID", "Name", "Category", "Unit Price", "Stock", "Status"}, rows));
    }

    public void setTotal(String text) {
        lblTotal.setText("Total medicine cost: " + text);
    }

    // Clear prescription inputs
    public void clearLineInputs() {
        txtQuantity.setText("");
        txtDosage.setText("");
        txtInstructions.setText("");
    }

    // Fill stock form
    public void fillStockForm(String name, String category, String price, String stock, String status) {
        txtName.setText(name);
        txtCategory.setText(category);
        txtPrice.setText(price);
        txtStock.setText(stock);
        cmbStatus.setSelectedItem(status);
    }

    // Reset stock form
    public void clearStockForm() {
        fillStockForm("", "", "", "", "Active");
        tblMedicines.clearSelection();
    }

    // Field readers
    public String getQuantityText() { return txtQuantity.getText(); }
    public String getDosageText() { return txtDosage.getText(); }
    public String getInstructionsText() { return txtInstructions.getText(); }
    public int getMedicineIndex() { return cmbMedicine.getSelectedIndex(); }
    public String getNameText() { return txtName.getText(); }
    public String getCategoryText() { return txtCategory.getText(); }
    public String getPriceText() { return txtPrice.getText(); }
    public String getStockText() { return txtStock.getText(); }
    public String getStatusText() { return (String) cmbStatus.getSelectedItem(); }
    public int getSelectedLineRow() { return tblLines.getSelectedRow(); }
    public int getSelectedMedicineRow() { return tblMedicines.getSelectedRow(); }

    private static javax.swing.table.DefaultTableModel readOnly(String[] cols, java.util.List<Object[]> rows) {
        javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        rows.forEach(tm::addRow);
        return tm;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlRoot = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        btnTabPrescription = new javax.swing.JButton();
        btnTabStock = new javax.swing.JButton();
        pnlPrescription = new javax.swing.JPanel();
        lblMedicine = new javax.swing.JLabel();
        cmbMedicine = new javax.swing.JComboBox<>();
        lblQuantity = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        lblDosage = new javax.swing.JLabel();
        txtDosage = new javax.swing.JTextField();
        lblInstructions = new javax.swing.JLabel();
        txtInstructions = new javax.swing.JTextField();
        btnAddMedicine = new javax.swing.JButton();
        btnRemoveLine = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLines = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        pnlStock = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        lblCategory = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        lblPrice = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        lblStock = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnAddStock = new javax.swing.JButton();
        btnUpdateStock = new javax.swing.JButton();
        btnDeleteStock = new javax.swing.JButton();
        btnClearStock = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMedicines = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Medicines");
        setPreferredSize(new java.awt.Dimension(980, 680));
        setSize(new java.awt.Dimension(980, 680));

        pnlRoot.setBackground(new java.awt.Color(250, 246, 240));
        pnlRoot.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblHeader.setForeground(new java.awt.Color(74, 91, 106));
        lblHeader.setText("Medicines for Treatment");
        pnlRoot.add(lblHeader, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, 900, 34));

        btnTabPrescription.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTabPrescription.setForeground(new java.awt.Color(74, 91, 106));
        btnTabPrescription.setBackground(new java.awt.Color(157, 201, 163));
        btnTabPrescription.setText("Prescription");
        pnlRoot.add(btnTabPrescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 56, 160, 32));

        btnTabStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTabStock.setForeground(new java.awt.Color(74, 91, 106));
        btnTabStock.setBackground(new java.awt.Color(235, 235, 230));
        btnTabStock.setText("Medicine Stock");
        pnlRoot.add(btnTabStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 56, 160, 32));

        pnlPrescription.setBackground(new java.awt.Color(255, 255, 255));
        pnlPrescription.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlPrescription.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMedicine.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMedicine.setForeground(new java.awt.Color(113, 128, 140));
        lblMedicine.setText("MEDICINE");
        pnlPrescription.add(lblMedicine, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 12, 300, 18));

        cmbMedicine.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbMedicine.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Medicine" }));
        pnlPrescription.add(cmbMedicine, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 32, 300, 30));

        lblQuantity.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblQuantity.setForeground(new java.awt.Color(113, 128, 140));
        lblQuantity.setText("QUANTITY");
        pnlPrescription.add(lblQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 12, 120, 18));

        txtQuantity.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlPrescription.add(txtQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 32, 120, 30));

        lblDosage.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDosage.setForeground(new java.awt.Color(113, 128, 140));
        lblDosage.setText("DOSAGE");
        pnlPrescription.add(lblDosage, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 12, 200, 18));

        txtDosage.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlPrescription.add(txtDosage, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 32, 200, 30));

        lblInstructions.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblInstructions.setForeground(new java.awt.Color(113, 128, 140));
        lblInstructions.setText("INSTRUCTIONS");
        pnlPrescription.add(lblInstructions, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 72, 300, 18));

        txtInstructions.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlPrescription.add(txtInstructions, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 92, 652, 30));

        btnAddMedicine.setBackground(new java.awt.Color(157, 201, 163));
        btnAddMedicine.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddMedicine.setForeground(new java.awt.Color(74, 91, 106));
        btnAddMedicine.setText("Add Medicine");
        pnlPrescription.add(btnAddMedicine, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 136, 150, 36));

        btnRemoveLine.setBackground(new java.awt.Color(220, 170, 170));
        btnRemoveLine.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRemoveLine.setForeground(new java.awt.Color(120, 40, 40));
        btnRemoveLine.setText("Remove Selected");
        pnlPrescription.add(btnRemoveLine, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 136, 180, 36));

        btnClose.setBackground(new java.awt.Color(235, 235, 230));
        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClose.setForeground(new java.awt.Color(74, 91, 106));
        btnClose.setText("Close");
        pnlPrescription.add(btnClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 136, 150, 36));

        tblLines.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblLines.setForeground(new java.awt.Color(74, 91, 106));
        tblLines.setRowHeight(24);
        tblLines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Medicine", "Qty", "Dosage", "Instructions", "Unit price", "Line total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblLines);

        pnlPrescription.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 186, 910, 300));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(74, 91, 106));
        lblTotal.setText("Total medicine cost: 0.00");
        pnlPrescription.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 496, 500, 28));

        pnlRoot.add(pnlPrescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 96, 940, 540));

        pnlStock.setBackground(new java.awt.Color(255, 255, 255));
        pnlStock.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlStock.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblName.setForeground(new java.awt.Color(113, 128, 140));
        lblName.setText("NAME");
        pnlStock.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 12, 200, 18));

        txtName.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlStock.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 32, 200, 30));

        lblCategory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCategory.setForeground(new java.awt.Color(113, 128, 140));
        lblCategory.setText("CATEGORY");
        pnlStock.add(lblCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 12, 160, 18));

        txtCategory.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlStock.add(txtCategory, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 32, 160, 30));

        lblPrice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPrice.setForeground(new java.awt.Color(113, 128, 140));
        lblPrice.setText("UNIT PRICE");
        pnlStock.add(lblPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(398, 12, 120, 18));

        txtPrice.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlStock.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(398, 32, 120, 30));

        lblStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStock.setForeground(new java.awt.Color(113, 128, 140));
        lblStock.setText("STOCK");
        pnlStock.add(lblStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 12, 100, 18));

        txtStock.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlStock.add(txtStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 32, 100, 30));

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(113, 128, 140));
        lblStatus.setText("STATUS");
        pnlStock.add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(642, 12, 140, 18));

        cmbStatus.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive" }));
        pnlStock.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(642, 32, 140, 30));

        btnAddStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddStock.setForeground(new java.awt.Color(74, 91, 106));
        btnAddStock.setBackground(new java.awt.Color(157, 201, 163));
        btnAddStock.setText("Add");
        pnlStock.add(btnAddStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 76, 150, 36));

        btnUpdateStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdateStock.setForeground(new java.awt.Color(74, 91, 106));
        btnUpdateStock.setBackground(new java.awt.Color(190, 210, 230));
        btnUpdateStock.setText("Update");
        pnlStock.add(btnUpdateStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 76, 150, 36));

        btnDeleteStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDeleteStock.setForeground(new java.awt.Color(120, 40, 40));
        btnDeleteStock.setBackground(new java.awt.Color(220, 170, 170));
        btnDeleteStock.setText("Delete");
        pnlStock.add(btnDeleteStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 76, 150, 36));

        btnClearStock.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClearStock.setForeground(new java.awt.Color(74, 91, 106));
        btnClearStock.setBackground(new java.awt.Color(235, 235, 230));
        btnClearStock.setText("Clear");
        pnlStock.add(btnClearStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 76, 150, 36));

        tblMedicines.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        tblMedicines.setForeground(new java.awt.Color(74, 91, 106));
        tblMedicines.setRowHeight(24);
        tblMedicines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Category", "Unit Price", "Stock", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblMedicines);

        pnlStock.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 126, 910, 398));

        pnlRoot.add(pnlStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 96, 940, 540));

        getContentPane().add(pnlRoot, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMedicine;
    private javax.swing.JButton btnAddStock;
    private javax.swing.JButton btnClearStock;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDeleteStock;
    private javax.swing.JButton btnRemoveLine;
    private javax.swing.JButton btnTabPrescription;
    private javax.swing.JButton btnTabStock;
    private javax.swing.JButton btnUpdateStock;
    private javax.swing.JComboBox<String> cmbMedicine;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblDosage;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblInstructions;
    private javax.swing.JLabel lblMedicine;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel pnlPrescription;
    private javax.swing.JPanel pnlRoot;
    private javax.swing.JPanel pnlStock;
    private javax.swing.JTable tblLines;
    private javax.swing.JTable tblMedicines;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtDosage;
    private javax.swing.JTextField txtInstructions;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
