package view;


/**
 * BillingForm: passive view (designer form).
 * Logic lives in the controller.
 */
public class BillingForm extends javax.swing.JFrame {


    public BillingForm() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); // Full screen
        setLocationRelativeTo(null);
        readOnly(tblCart, 24);
        readOnly(tblInvoices, 26);
        sorter = new javax.swing.table.TableRowSorter<>((javax.swing.table.DefaultTableModel) tblInvoices.getModel());
        tblInvoices.setRowSorter(sorter);
    }

    public javax.swing.JLabel getLblTitle() {
        return lblTitle;
    }

    public javax.swing.JLabel getLblSubtitle() {
        return lblSubtitle;
    }

    public javax.swing.JLabel getLblTotalCount() {
        return lblTotalCount;
    }

    public javax.swing.JLabel getLblRevenueCount() {
        return lblRevenueCount;
    }

    public javax.swing.JLabel getLblOutstandingCount() {
        return lblOutstandingCount;
    }

    public javax.swing.JLabel getLblUnpaidCount() {
        return lblUnpaidCount;
    }

    public javax.swing.JLabel getLblInvoiceTitle() {
        return lblInvoiceTitle;
    }

    public javax.swing.JLabel getLblInvoiceSub() {
        return lblInvoiceSub;
    }

    public javax.swing.JComboBox<String> getCmbCustomer() {
        return cmbCustomer;
    }

    public javax.swing.JComboBox<String> getCmbPet() {
        return cmbPet;
    }

    public javax.swing.JComboBox<String> getCmbAppointment() {
        return cmbAppointment;
    }

    public javax.swing.JButton getBtnAddCharges() {
        return btnAddCharges;
    }

    public javax.swing.JComboBox<String> getCmbType() {
        return cmbType;
    }

    public javax.swing.JComboBox<String> getCmbItem() {
        return cmbItem;
    }

    public javax.swing.JTextField getTxtQty() {
        return txtQty;
    }

    public javax.swing.JTextField getTxtPrice() {
        return txtPrice;
    }

    public javax.swing.JButton getBtnAddItem() {
        return btnAddItem;
    }

    public javax.swing.JTable getTblCart() {
        return tblCart;
    }

    public javax.swing.JButton getBtnRemoveItem() {
        return btnRemoveItem;
    }

    public javax.swing.JLabel getLblSubtotal() {
        return lblSubtotal;
    }

    public javax.swing.JLabel getLblTotal() {
        return lblTotal;
    }

    public javax.swing.JTextField getTxtDiscount() {
        return txtDiscount;
    }

    public javax.swing.JTextField getTxtTaxRate() {
        return txtTaxRate;
    }

    public javax.swing.JComboBox<String> getCmbMethod() {
        return cmbMethod;
    }

    public javax.swing.JTextField getTxtPaidNow() {
        return txtPaidNow;
    }

    public javax.swing.JTextField getTxtNotes() {
        return txtNotes;
    }

    public javax.swing.JButton getBtnSave() {
        return btnSave;
    }

    public javax.swing.JButton getBtnClear() {
        return btnClear;
    }

    public javax.swing.JLabel getLblListTitle() {
        return lblListTitle;
    }

    public javax.swing.JLabel getLblListSub() {
        return lblListSub;
    }

    public javax.swing.JTextField getTxtSearch() {
        return txtSearch;
    }

    public javax.swing.JComboBox<String> getCmbFilter() {
        return cmbFilter;
    }

    public javax.swing.JTable getTblInvoices() {
        return tblInvoices;
    }

    public javax.swing.JButton getBtnPay() {
        return btnPay;
    }

    public javax.swing.JButton getBtnPrint() {
        return btnPrint;
    }

    public javax.swing.JButton getBtnCancel() {
        return btnCancel;
    }

    public javax.swing.JButton getBtnRefresh() {
        return btnRefresh;
    }

    public javax.swing.JLabel getLblMessage() {
        return lblMessage;
    }

    private javax.swing.table.TableRowSorter<javax.swing.table.DefaultTableModel> sorter;

    // Read-only tables
    private void readOnly(javax.swing.JTable t, int rowHeight)
    {
        javax.swing.table.DefaultTableModel old = (javax.swing.table.DefaultTableModel) t.getModel();
        String[] cols = new String[old.getColumnCount()];
        for (int i = 0; i < cols.length; i++)
        {
            cols[i] = old.getColumnName(i);
        }
        t.setModel(new javax.swing.table.DefaultTableModel(new Object[0][], cols)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        });
        t.setRowHeight(rowHeight);
        t.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    }

    // Stat cards
    public void setStats(String total, String revenue, String outstanding, String unpaid)
    {
        lblTotalCount.setText(total);
        lblRevenueCount.setText(revenue);
        lblOutstandingCount.setText(outstanding);
        lblUnpaidCount.setText(unpaid);
    }

    // Invoice rows
    public void setInvoiceRows(java.util.List<Object[]> rows)
    {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tblInvoices.getModel();
        m.setRowCount(0);
        for (Object[] r : rows)
        {
            m.addRow(r);
        }
    }

    // Cart rows
    public void setCart(java.util.List<Object[]> rows)
    {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tblCart.getModel();
        m.setRowCount(0);
        for (Object[] r : rows)
        {
            m.addRow(r);
        }
    }

    public void setTotals(String subtotal, String total)
    {
        lblSubtotal.setText(subtotal);
        lblTotal.setText(total);
    }

    public void setMessage(String text)
    {
        lblMessage.setText(text);
    }

    // Row filter
    public void filterInvoices(String text, String status)
    {
        java.util.List<javax.swing.RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
        if (!text.isEmpty())
        {
            filters.add(javax.swing.RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text), 0, 2, 3));
        }
        if (!"All".equals(status))
        {
            filters.add(javax.swing.RowFilter.regexFilter("^" + status + "$", 7));
        }
        sorter.setRowFilter(filters.isEmpty() ? null : javax.swing.RowFilter.andFilter(filters));
    }

    // Selected invoice id
    public int getSelectedInvoiceId()
    {
        int row = tblInvoices.getSelectedRow();
        if (row < 0)
        {
            return -1;
        }
        return (int) tblInvoices.getModel().getValueAt(tblInvoices.convertRowIndexToModel(row), 0);
    }

    public int getSelectedCartRow()
    {
        return tblCart.getSelectedRow();
    }

    // Fill combo
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setCombo(javax.swing.JComboBox<String> box, java.util.List<util.ComboItem> items)
    {
        javax.swing.JComboBox raw = box;
        raw.removeAllItems();
        for (util.ComboItem ci : items)
        {
            raw.addItem(ci);
        }
    }

    // Selected id
    public int selectedId(javax.swing.JComboBox<String> box)
    {
        Object o = box.getSelectedItem();
        return o instanceof util.ComboItem ? ((util.ComboItem) o).getId() : 0;
    }

    // Select by id
    public void selectId(javax.swing.JComboBox<String> box, int id)
    {
        for (int i = 0; i < box.getItemCount(); i++)
        {
            Object o = ((javax.swing.JComboBox) box).getItemAt(i);
            if (o instanceof util.ComboItem && ((util.ComboItem) o).getId() == id)
            {
                box.setSelectedIndex(i);
            }
        }
    }

    // Reset fields
    public void clearForm()
    {
        txtDiscount.setText("0");
        txtTaxRate.setText("0");
        txtPaidNow.setText("0");
        txtNotes.setText("");
        txtQty.setText("1");
        if (cmbCustomer.getItemCount() > 0)
        {
            cmbCustomer.setSelectedIndex(0);
        }
        lblMessage.setText(" ");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContent = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlStatTotal = new javax.swing.JPanel();
        capStatTotal = new javax.swing.JLabel();
        lblTotalCount = new javax.swing.JLabel();
        pnlStatRevenue = new javax.swing.JPanel();
        capStatRevenue = new javax.swing.JLabel();
        lblRevenueCount = new javax.swing.JLabel();
        pnlStatOutstanding = new javax.swing.JPanel();
        capStatOutstanding = new javax.swing.JLabel();
        lblOutstandingCount = new javax.swing.JLabel();
        pnlStatUnpaid = new javax.swing.JPanel();
        capStatUnpaid = new javax.swing.JLabel();
        lblUnpaidCount = new javax.swing.JLabel();
        pnlNewInvoice = new javax.swing.JPanel();
        lblInvoiceTitle = new javax.swing.JLabel();
        lblInvoiceSub = new javax.swing.JLabel();
        capCustomer = new javax.swing.JLabel();
        cmbCustomer = new javax.swing.JComboBox<>();
        capPet = new javax.swing.JLabel();
        cmbPet = new javax.swing.JComboBox<>();
        capAppointment = new javax.swing.JLabel();
        cmbAppointment = new javax.swing.JComboBox<>();
        btnAddCharges = new javax.swing.JButton();
        capType = new javax.swing.JLabel();
        cmbType = new javax.swing.JComboBox<>();
        capItem = new javax.swing.JLabel();
        cmbItem = new javax.swing.JComboBox<>();
        capQty = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        capPrice = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        btnAddItem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCart = new javax.swing.JTable();
        btnRemoveItem = new javax.swing.JButton();
        capSubtotal = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        capTotal = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        capDiscount = new javax.swing.JLabel();
        txtDiscount = new javax.swing.JTextField();
        capTax = new javax.swing.JLabel();
        txtTaxRate = new javax.swing.JTextField();
        capMethod = new javax.swing.JLabel();
        cmbMethod = new javax.swing.JComboBox<>();
        capPaid = new javax.swing.JLabel();
        txtPaidNow = new javax.swing.JTextField();
        capNotes = new javax.swing.JLabel();
        txtNotes = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        pnlInvoices = new javax.swing.JPanel();
        lblListTitle = new javax.swing.JLabel();
        lblListSub = new javax.swing.JLabel();
        capSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        capStatus = new javax.swing.JLabel();
        cmbFilter = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblInvoices = new javax.swing.JTable();
        btnPay = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        lblMessage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare 360 - Billing");
        setMinimumSize(new java.awt.Dimension(1280, 760));
        setPreferredSize(new java.awt.Dimension(1450, 900));
        setSize(new java.awt.Dimension(1450, 900));

        pnlContent.setBackground(new java.awt.Color(250, 246, 240));
        pnlContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblTitle.setText("Billing");
        pnlContent.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 14, 400, 34));

        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblSubtitle.setForeground(new java.awt.Color(113, 128, 140));
        lblSubtitle.setText("Create invoices and record payments");
        pnlContent.add(lblSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 48, 500, 20));

        pnlStatTotal.setBackground(new java.awt.Color(255, 255, 255));
        pnlStatTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlStatTotal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capStatTotal.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capStatTotal.setForeground(new java.awt.Color(110, 125, 135));
        capStatTotal.setText("TOTAL INVOICES");
        pnlStatTotal.add(capStatTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 8, 300, 16));

        lblTotalCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTotalCount.setForeground(new java.awt.Color(74, 91, 106));
        lblTotalCount.setText("0");
        pnlStatTotal.add(lblTotalCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 28, 300, 34));

        pnlContent.add(pnlStatTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 82, 302, 70));

        pnlStatRevenue.setBackground(new java.awt.Color(255, 255, 255));
        pnlStatRevenue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlStatRevenue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capStatRevenue.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capStatRevenue.setForeground(new java.awt.Color(110, 125, 135));
        capStatRevenue.setText("REVENUE COLLECTED (LKR)");
        pnlStatRevenue.add(capStatRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 8, 300, 16));

        lblRevenueCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblRevenueCount.setForeground(new java.awt.Color(74, 91, 106));
        lblRevenueCount.setText("0.00");
        pnlStatRevenue.add(lblRevenueCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 28, 300, 34));

        pnlContent.add(pnlStatRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(338, 82, 302, 70));

        pnlStatOutstanding.setBackground(new java.awt.Color(255, 255, 255));
        pnlStatOutstanding.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlStatOutstanding.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capStatOutstanding.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capStatOutstanding.setForeground(new java.awt.Color(110, 125, 135));
        capStatOutstanding.setText("OUTSTANDING (LKR)");
        pnlStatOutstanding.add(capStatOutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 8, 300, 16));

        lblOutstandingCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblOutstandingCount.setForeground(new java.awt.Color(74, 91, 106));
        lblOutstandingCount.setText("0.00");
        pnlStatOutstanding.add(lblOutstandingCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 28, 300, 34));

        pnlContent.add(pnlStatOutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(657, 82, 302, 70));

        pnlStatUnpaid.setBackground(new java.awt.Color(255, 255, 255));
        pnlStatUnpaid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlStatUnpaid.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        capStatUnpaid.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capStatUnpaid.setForeground(new java.awt.Color(110, 125, 135));
        capStatUnpaid.setText("UNPAID / PARTIAL");
        pnlStatUnpaid.add(capStatUnpaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 8, 300, 16));

        lblUnpaidCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblUnpaidCount.setForeground(new java.awt.Color(74, 91, 106));
        lblUnpaidCount.setText("0");
        pnlStatUnpaid.add(lblUnpaidCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 28, 300, 34));

        pnlContent.add(pnlStatUnpaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(976, 82, 302, 70));

        pnlNewInvoice.setBackground(new java.awt.Color(255, 255, 255));
        pnlNewInvoice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlNewInvoice.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblInvoiceTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblInvoiceTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblInvoiceTitle.setText("NEW INVOICE");
        pnlNewInvoice.add(lblInvoiceTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 12, 400, 22));

        lblInvoiceSub.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblInvoiceSub.setForeground(new java.awt.Color(113, 128, 140));
        lblInvoiceSub.setText("Pick a customer, add items, then save");
        pnlNewInvoice.add(lblInvoiceSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 36, 400, 18));

        capCustomer.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capCustomer.setForeground(new java.awt.Color(113, 128, 140));
        capCustomer.setText("CUSTOMER");
        pnlNewInvoice.add(capCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 66, 300, 16));

        cmbCustomer.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1" }));
        pnlNewInvoice.add(cmbCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 84, 290, 30));

        capPet.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capPet.setForeground(new java.awt.Color(113, 128, 140));
        capPet.setText("PET");
        pnlNewInvoice.add(capPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 66, 300, 16));

        cmbPet.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbPet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1" }));
        pnlNewInvoice.add(cmbPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 84, 290, 30));

        capAppointment.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capAppointment.setForeground(new java.awt.Color(113, 128, 140));
        capAppointment.setText("APPOINTMENT (OPTIONAL)");
        pnlNewInvoice.add(capAppointment, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 124, 300, 16));

        cmbAppointment.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbAppointment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1" }));
        pnlNewInvoice.add(cmbAppointment, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 142, 477, 30));

        btnAddCharges.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddCharges.setForeground(new java.awt.Color(74, 91, 106));
        btnAddCharges.setBackground(new java.awt.Color(190, 210, 230));
        btnAddCharges.setText("Add Charges");
        pnlNewInvoice.add(btnAddCharges, new org.netbeans.lib.awtextra.AbsoluteConstraints(504, 142, 133, 30));

        capType.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capType.setForeground(new java.awt.Color(113, 128, 140));
        capType.setText("TYPE");
        pnlNewInvoice.add(capType, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 182, 100, 16));

        cmbType.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Service", "Medicine", "Boarding Stay", "Other" }));
        pnlNewInvoice.add(cmbType, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 200, 108, 30));

        capItem.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capItem.setForeground(new java.awt.Color(113, 128, 140));
        capItem.setText("ITEM");
        pnlNewInvoice.add(capItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 182, 100, 16));

        cmbItem.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1" }));
        pnlNewInvoice.add(cmbItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 200, 243, 30));

        capQty.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capQty.setForeground(new java.awt.Color(113, 128, 140));
        capQty.setText("QTY");
        pnlNewInvoice.add(capQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(383, 182, 60, 16));

        txtQty.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtQty.setText("1");
        pnlNewInvoice.add(txtQty, new org.netbeans.lib.awtextra.AbsoluteConstraints(383, 200, 54, 30));

        capPrice.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capPrice.setForeground(new java.awt.Color(113, 128, 140));
        capPrice.setText("UNIT PRICE");
        pnlNewInvoice.add(capPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 182, 100, 16));

        txtPrice.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlNewInvoice.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(446, 200, 81, 30));

        btnAddItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddItem.setForeground(new java.awt.Color(74, 91, 106));
        btnAddItem.setBackground(new java.awt.Color(157, 201, 163));
        btnAddItem.setText("Add Item");
        pnlNewInvoice.add(btnAddItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(536, 200, 106, 30));

        tblCart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Description", "Qty", "Unit Price", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblCart);

        pnlNewInvoice.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 240, 601, 110));

        btnRemoveItem.setBackground(new java.awt.Color(220, 170, 170));
        btnRemoveItem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRemoveItem.setForeground(new java.awt.Color(120, 40, 40));
        btnRemoveItem.setText("Remove Item");
        pnlNewInvoice.add(btnRemoveItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 370, 133, 30));

        capSubtotal.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        capSubtotal.setForeground(new java.awt.Color(74, 91, 106));
        capSubtotal.setText("Subtotal:");
        pnlNewInvoice.add(capSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(288, 370, 60, 16));

        lblSubtotal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(74, 91, 106));
        lblSubtotal.setText("0.00");
        pnlNewInvoice.add(lblSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(346, 370, 90, 16));

        capTotal.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        capTotal.setForeground(new java.awt.Color(74, 91, 106));
        capTotal.setText("Total (LKR):");
        pnlNewInvoice.add(capTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 370, 90, 22));

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(74, 91, 106));
        lblTotal.setText("0.00");
        pnlNewInvoice.add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 370, 116, 22));

        capDiscount.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capDiscount.setForeground(new java.awt.Color(113, 128, 140));
        capDiscount.setText("DISCOUNT (LKR)");
        pnlNewInvoice.add(capDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 420, 150, 16));

        txtDiscount.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtDiscount.setText("0");
        pnlNewInvoice.add(txtDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 440, 135, 30));

        capTax.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capTax.setForeground(new java.awt.Color(113, 128, 140));
        capTax.setText("TAX (%)");
        pnlNewInvoice.add(capTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 420, 150, 16));

        txtTaxRate.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtTaxRate.setText("0");
        pnlNewInvoice.add(txtTaxRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 440, 135, 30));

        capMethod.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capMethod.setForeground(new java.awt.Color(113, 128, 140));
        capMethod.setText("PAYMENT METHOD");
        pnlNewInvoice.add(capMethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 420, 170, 16));

        cmbMethod.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Card", "Online" }));
        pnlNewInvoice.add(cmbMethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 440, 153, 30));

        capPaid.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capPaid.setForeground(new java.awt.Color(113, 128, 140));
        capPaid.setText("PAID NOW (LKR)");
        pnlNewInvoice.add(capPaid, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 420, 160, 16));

        txtPaidNow.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtPaidNow.setText("0");
        pnlNewInvoice.add(txtPaidNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 440, 146, 30));

        capNotes.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capNotes.setForeground(new java.awt.Color(113, 128, 140));
        capNotes.setText("NOTES");
        pnlNewInvoice.add(capNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 490, 200, 16));

        txtNotes.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        txtNotes.addActionListener(this::txtNotesActionPerformed);
        pnlNewInvoice.add(txtNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 510, 601, 30));

        btnSave.setBackground(new java.awt.Color(157, 201, 163));
        btnSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSave.setForeground(new java.awt.Color(74, 91, 106));
        btnSave.setText("Save Invoice");
        btnSave.addActionListener(this::btnSaveActionPerformed);
        pnlNewInvoice.add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 570, 293, 38));

        btnClear.setBackground(new java.awt.Color(235, 235, 230));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClear.setForeground(new java.awt.Color(74, 91, 106));
        btnClear.setText("Clear");
        pnlNewInvoice.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(322, 570, 293, 38));

        pnlContent.add(pnlNewInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 168, 656, 650));

        pnlInvoices.setBackground(new java.awt.Color(255, 255, 255));
        pnlInvoices.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlInvoices.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblListTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblListTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblListTitle.setText("INVOICES");
        pnlInvoices.add(lblListTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 12, 400, 22));

        lblListSub.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblListSub.setForeground(new java.awt.Color(113, 128, 140));
        lblListSub.setText("Recent invoices and payment status");
        pnlInvoices.add(lblListSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 36, 400, 18));

        capSearch.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capSearch.setForeground(new java.awt.Color(113, 128, 140));
        capSearch.setText("SEARCH (INVOICE #, CUSTOMER, PET)");
        pnlInvoices.add(capSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 66, 400, 16));

        txtSearch.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        pnlInvoices.add(txtSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 84, 380, 30));

        capStatus.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        capStatus.setForeground(new java.awt.Color(113, 128, 140));
        capStatus.setText("STATUS");
        pnlInvoices.add(capStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 66, 100, 16));

        cmbFilter.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        cmbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Unpaid", "Partial", "Paid", "Cancelled" }));
        pnlInvoices.add(cmbFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 84, 147, 30));

        tblInvoices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice #", "Date", "Customer", "Pet", "Total", "Paid", "Balance", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvoices.setRowHeight(28);
        jScrollPane2.setViewportView(tblInvoices);

        pnlInvoices.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 124, 560, 380));

        btnPay.setBackground(new java.awt.Color(157, 201, 163));
        btnPay.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPay.setForeground(new java.awt.Color(74, 91, 106));
        btnPay.setText("Record Payment");
        pnlInvoices.add(btnPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 570, 130, 38));

        btnPrint.setBackground(new java.awt.Color(190, 210, 230));
        btnPrint.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(74, 91, 106));
        btnPrint.setText("Print Invoice");
        pnlInvoices.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 570, 130, 38));

        btnCancel.setBackground(new java.awt.Color(220, 170, 170));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(120, 40, 40));
        btnCancel.setText("Cancel Invoice");
        pnlInvoices.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 570, 130, 38));

        btnRefresh.setBackground(new java.awt.Color(235, 235, 230));
        btnRefresh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(74, 91, 106));
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);
        pnlInvoices.add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 570, 130, 38));

        pnlContent.add(pnlInvoices, new org.netbeans.lib.awtextra.AbsoluteConstraints(688, 168, 590, 650));

        lblMessage.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblMessage.setForeground(new java.awt.Color(113, 128, 140));
        lblMessage.setText(" ");
        pnlContent.add(lblMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 826, 1400, 22));

        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNotesActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCharges;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnRemoveItem;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel capAppointment;
    private javax.swing.JLabel capCustomer;
    private javax.swing.JLabel capDiscount;
    private javax.swing.JLabel capItem;
    private javax.swing.JLabel capMethod;
    private javax.swing.JLabel capNotes;
    private javax.swing.JLabel capPaid;
    private javax.swing.JLabel capPet;
    private javax.swing.JLabel capPrice;
    private javax.swing.JLabel capQty;
    private javax.swing.JLabel capSearch;
    private javax.swing.JLabel capStatOutstanding;
    private javax.swing.JLabel capStatRevenue;
    private javax.swing.JLabel capStatTotal;
    private javax.swing.JLabel capStatUnpaid;
    private javax.swing.JLabel capStatus;
    private javax.swing.JLabel capSubtotal;
    private javax.swing.JLabel capTax;
    private javax.swing.JLabel capTotal;
    private javax.swing.JLabel capType;
    private javax.swing.JComboBox<String> cmbAppointment;
    private javax.swing.JComboBox<String> cmbCustomer;
    private javax.swing.JComboBox<String> cmbFilter;
    private javax.swing.JComboBox<String> cmbItem;
    private javax.swing.JComboBox<String> cmbMethod;
    private javax.swing.JComboBox<String> cmbPet;
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblInvoiceSub;
    private javax.swing.JLabel lblInvoiceTitle;
    private javax.swing.JLabel lblListSub;
    private javax.swing.JLabel lblListTitle;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblOutstandingCount;
    private javax.swing.JLabel lblRevenueCount;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalCount;
    private javax.swing.JLabel lblUnpaidCount;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlInvoices;
    private javax.swing.JPanel pnlNewInvoice;
    private javax.swing.JPanel pnlStatOutstanding;
    private javax.swing.JPanel pnlStatRevenue;
    private javax.swing.JPanel pnlStatTotal;
    private javax.swing.JPanel pnlStatUnpaid;
    private javax.swing.JTable tblCart;
    private javax.swing.JTable tblInvoices;
    private javax.swing.JTextField txtDiscount;
    private javax.swing.JTextField txtNotes;
    private javax.swing.JTextField txtPaidNow;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTaxRate;
    // End of variables declaration//GEN-END:variables
}
