package view;


/**
 * ReportsForm: passive view (designer form).
 * Logic lives in the controller.
 */
public class ReportsForm extends javax.swing.JFrame {


    public ReportsForm() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); // Full screen
        setLocationRelativeTo(null);
    }

    public javax.swing.JLabel getLblTitle() {
        return lblTitle;
    }

    public javax.swing.JLabel getLblSubtitle() {
        return lblSubtitle;
    }

    public javax.swing.JSpinner getSpnFrom() {
        return spnFrom;
    }

    public javax.swing.JSpinner getSpnTo() {
        return spnTo;
    }

    public javax.swing.JButton getBtnRevenuePdf() {
        return btnRevenuePdf;
    }

    public javax.swing.JButton getBtnRevenuePreview() {
        return btnRevenuePreview;
    }

    public javax.swing.JButton getBtnServicesPdf() {
        return btnServicesPdf;
    }

    public javax.swing.JButton getBtnServicesPreview() {
        return btnServicesPreview;
    }

    public javax.swing.JComboBox<String> getCmbInvoice() {
        return cmbInvoice;
    }

    public javax.swing.JButton getBtnInvoicePdf() {
        return btnInvoicePdf;
    }

    public javax.swing.JButton getBtnInvoicePreview() {
        return btnInvoicePreview;
    }

    public javax.swing.JLabel getLblMessage() {
        return lblMessage;
    }

    // Set range
    public void setDateRange(java.util.Date from, java.util.Date to)
    {
        initDate(spnFrom, from);
        initDate(spnTo, to);
    }

    private void initDate(javax.swing.JSpinner s, java.util.Date d)
    {
        s.setModel(new javax.swing.SpinnerDateModel(d, null, null, java.util.Calendar.DAY_OF_MONTH));
        s.setEditor(new javax.swing.JSpinner.DateEditor(s, "yyyy-MM-dd"));
    }

    public java.util.Date getFromDate()
    {
        return (java.util.Date) spnFrom.getValue();
    }

    public java.util.Date getToDate()
    {
        return (java.util.Date) spnTo.getValue();
    }

    public void setMessage(String text)
    {
        lblMessage.setText(text);
    }

    // Fill invoices
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setInvoices(java.util.List<util.ComboItem> items)
    {
        javax.swing.JComboBox raw = cmbInvoice;
        raw.removeAllItems();
        for (util.ComboItem ci : items)
        {
            raw.addItem(ci);
        }
    }

    // Picked invoice
    public util.ComboItem getSelectedInvoice()
    {
        Object o = cmbInvoice.getSelectedItem();
        return o instanceof util.ComboItem ? (util.ComboItem) o : null;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContent = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlRange = new javax.swing.JPanel();
        capFrom = new javax.swing.JLabel();
        spnFrom = new javax.swing.JSpinner();
        capTo = new javax.swing.JLabel();
        spnTo = new javax.swing.JSpinner();
        pnlRevenue = new javax.swing.JPanel();
        capRevenueTitle = new javax.swing.JLabel();
        capRevenueDesc = new javax.swing.JLabel();
        btnRevenuePdf = new javax.swing.JButton();
        btnRevenuePreview = new javax.swing.JButton();
        pnlServices = new javax.swing.JPanel();
        capServicesTitle = new javax.swing.JLabel();
        capServicesDesc = new javax.swing.JLabel();
        btnServicesPdf = new javax.swing.JButton();
        btnServicesPreview = new javax.swing.JButton();
        pnlInvoice = new javax.swing.JPanel();
        capInvoiceTitle = new javax.swing.JLabel();
        capInvoiceDesc = new javax.swing.JLabel();
        cmbInvoice = new javax.swing.JComboBox<>();
        btnInvoicePdf = new javax.swing.JButton();
        btnInvoicePreview = new javax.swing.JButton();
        lblMessage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare 360 - Reports");
        setMinimumSize(new java.awt.Dimension(1280, 700));
        setPreferredSize(new java.awt.Dimension(1700, 900));
        setSize(new java.awt.Dimension(1700, 900));

        pnlContent.setBackground(new java.awt.Color(250, 246, 240));
        pnlContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblTitle.setText("Reports");
        pnlContent.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, 400, 34));

        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSubtitle.setForeground(new java.awt.Color(113, 128, 140));
        lblSubtitle.setText("Management reports with charts. Preview on screen or export to PDF.");
        pnlContent.add(lblSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 48, 700, 20));

        pnlRange.setBackground(new java.awt.Color(255, 255, 255));
        pnlRange.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlRange.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capFrom.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        capFrom.setForeground(new java.awt.Color(74, 91, 106));
        capFrom.setText("From");
        pnlRange.add(capFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 13, 40, 20));

        spnFrom.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlRange.add(spnFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 8, 130, 30));

        capTo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        capTo.setForeground(new java.awt.Color(74, 91, 106));
        capTo.setText("To");
        pnlRange.add(capTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(212, 13, 30, 20));

        spnTo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlRange.add(spnTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(244, 8, 130, 30));

        pnlContent.add(pnlRange, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 84, 440, 46));

        pnlRevenue.setBackground(new java.awt.Color(255, 255, 255));
        pnlRevenue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlRevenue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capRevenueTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        capRevenueTitle.setForeground(new java.awt.Color(74, 91, 106));
        capRevenueTitle.setText("Revenue & Collections");
        pnlRevenue.add(capRevenueTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 16, 344, 28));

        capRevenueDesc.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        capRevenueDesc.setForeground(new java.awt.Color(113, 128, 140));
        capRevenueDesc.setText("<html>Invoices, payments and balances per customer, with a billed vs collected chart. Uses invoices, customers, pets and payments.</html>");
        pnlRevenue.add(capRevenueDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 52, 344, 90));

        btnRevenuePdf.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRevenuePdf.setForeground(new java.awt.Color(74, 91, 106));
        btnRevenuePdf.setBackground(new java.awt.Color(190, 210, 230));
        btnRevenuePdf.setText("Export PDF");
        pnlRevenue.add(btnRevenuePdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 210, 160, 38));

        btnRevenuePreview.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRevenuePreview.setForeground(new java.awt.Color(74, 91, 106));
        btnRevenuePreview.setBackground(new java.awt.Color(157, 201, 163));
        btnRevenuePreview.setText("Preview");
        pnlRevenue.add(btnRevenuePreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 210, 160, 38));

        pnlContent.add(pnlRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 380, 270));

        pnlServices.setBackground(new java.awt.Color(255, 255, 255));
        pnlServices.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlServices.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capServicesTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        capServicesTitle.setForeground(new java.awt.Color(74, 91, 106));
        capServicesTitle.setText("Service Performance");
        pnlServices.add(capServicesTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 16, 344, 28));

        capServicesDesc.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        capServicesDesc.setForeground(new java.awt.Color(113, 128, 140));
        capServicesDesc.setText("<html>Appointments grouped by service with totals and category / status charts. Uses appointments, services, staff, customers and pets.</html>");
        pnlServices.add(capServicesDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 52, 344, 90));

        btnServicesPdf.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnServicesPdf.setForeground(new java.awt.Color(74, 91, 106));
        btnServicesPdf.setBackground(new java.awt.Color(190, 210, 230));
        btnServicesPdf.setText("Export PDF");
        pnlServices.add(btnServicesPdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 210, 160, 38));

        btnServicesPreview.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnServicesPreview.setForeground(new java.awt.Color(74, 91, 106));
        btnServicesPreview.setBackground(new java.awt.Color(157, 201, 163));
        btnServicesPreview.setText("Preview");
        pnlServices.add(btnServicesPreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 210, 160, 38));

        pnlContent.add(pnlServices, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 150, 380, 270));

        pnlInvoice.setBackground(new java.awt.Color(255, 255, 255));
        pnlInvoice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlInvoice.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capInvoiceTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        capInvoiceTitle.setForeground(new java.awt.Color(74, 91, 106));
        capInvoiceTitle.setText("Customer Invoice");
        pnlInvoice.add(capInvoiceTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 16, 344, 28));

        capInvoiceDesc.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        capInvoiceDesc.setForeground(new java.awt.Color(113, 128, 140));
        capInvoiceDesc.setText("<html>Printable invoice with line items and payments. Pick an invoice below.</html>");
        pnlInvoice.add(capInvoiceDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 52, 344, 90));

        cmbInvoice.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbInvoice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1" }));
        pnlInvoice.add(cmbInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 150, 344, 30));

        btnInvoicePdf.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInvoicePdf.setForeground(new java.awt.Color(74, 91, 106));
        btnInvoicePdf.setBackground(new java.awt.Color(190, 210, 230));
        btnInvoicePdf.setText("Export PDF");
        pnlInvoice.add(btnInvoicePdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 210, 160, 38));

        btnInvoicePreview.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInvoicePreview.setForeground(new java.awt.Color(74, 91, 106));
        btnInvoicePreview.setBackground(new java.awt.Color(157, 201, 163));
        btnInvoicePreview.setText("Preview");
        pnlInvoice.add(btnInvoicePreview, new org.netbeans.lib.awtextra.AbsoluteConstraints(202, 210, 160, 38));

        pnlContent.add(pnlInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 150, 380, 270));

        lblMessage.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblMessage.setForeground(new java.awt.Color(113, 128, 140));
        lblMessage.setText(" ");
        pnlContent.add(lblMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 1300, 22));

        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInvoicePdf;
    private javax.swing.JButton btnInvoicePreview;
    private javax.swing.JButton btnRevenuePdf;
    private javax.swing.JButton btnRevenuePreview;
    private javax.swing.JButton btnServicesPdf;
    private javax.swing.JButton btnServicesPreview;
    private javax.swing.JLabel capFrom;
    private javax.swing.JLabel capInvoiceDesc;
    private javax.swing.JLabel capInvoiceTitle;
    private javax.swing.JLabel capRevenueDesc;
    private javax.swing.JLabel capRevenueTitle;
    private javax.swing.JLabel capServicesDesc;
    private javax.swing.JLabel capServicesTitle;
    private javax.swing.JLabel capTo;
    private javax.swing.JComboBox<String> cmbInvoice;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlInvoice;
    private javax.swing.JPanel pnlRange;
    private javax.swing.JPanel pnlRevenue;
    private javax.swing.JPanel pnlServices;
    private javax.swing.JSpinner spnFrom;
    private javax.swing.JSpinner spnTo;
    // End of variables declaration//GEN-END:variables
}
