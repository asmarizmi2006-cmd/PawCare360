import sys
sys.path.insert(0, 'tools/formgen')
from formgen import *

BG = (250, 246, 240); LINE = (217, 217, 217); TXT = (74, 91, 106); MUT = (113, 128, 140)
GREEN = (157, 201, 163); BLUE = (190, 210, 230); RED = (220, 170, 170); GREY = (235, 235, 230)
DRED = (120, 40, 40); WHITE = (255, 255, 255)
SEG = 'Segoe UI'
CAP = (SEG, 1, 11)
BODY = (SEG, 0, 13)
BTN = (SEG, 1, 12)


def cap(name, text, x, y, w):
    return L(name, text, x=x, y=y, w=w, h=16, font=CAP, fg=MUT)


def fld(name, text, x, y, w):
    return T(name, text=text, x=x, y=y, w=w, h=30, font=BODY)


def cmb(name, items, x, y, w):
    return CB(name, items, x=x, y=y, w=w, h=30, font=BODY)


def btn(name, text, x, y, w, h, bg, fg=TXT):
    return B(name, text, x=x, y=y, w=w, h=h, bg=bg, fg=fg, font=BTN)


def stat(pn, capn, valn, text, val, x):
    return P(pn, x=x, y=82, w=335, h=70, bg=WHITE, border=('line', LINE), children=[
        L(capn, text, x=15, y=8, w=300, h=16, font=CAP, fg=(110, 125, 135)),
        L(valn, val, x=15, y=28, w=300, h=34, font=(SEG, 1, 24), fg=TXT)])


left = P('pnlNewInvoice', x=20, y=168, w=700, h=650, bg=WHITE, border=('line', LINE), children=[
    L('lblInvoiceTitle', 'NEW INVOICE', x=16, y=12, w=400, h=22, font=(SEG, 1, 16), fg=TXT),
    L('lblInvoiceSub', 'Pick a customer, add items, then save', x=16, y=36, w=400, h=18, font=BODY, fg=MUT),
    cap('capCustomer', 'CUSTOMER', 16, 66, 300), cmb('cmbCustomer', ['Item 1'], 16, 84, 322),
    cap('capPet', 'PET', 362, 66, 300), cmb('cmbPet', ['Item 1'], 362, 84, 322),
    cap('capAppointment', 'APPOINTMENT (OPTIONAL)', 16, 124, 300), cmb('cmbAppointment', ['Item 1'], 16, 142, 530),
    btn('btnAddCharges', 'Add Charges', 560, 142, 124, 30, BLUE),
    cap('capType', 'TYPE', 16, 182, 100), cmb('cmbType', ['Service', 'Medicine', 'Boarding Stay', 'Other'], 16, 200, 120),
    cap('capItem', 'ITEM', 146, 182, 100), cmb('cmbItem', ['Item 1'], 146, 200, 270),
    cap('capQty', 'QTY', 426, 182, 60), fld('txtQty', '1', 426, 200, 60),
    cap('capPrice', 'UNIT PRICE', 496, 182, 100), fld('txtPrice', '', 496, 200, 90),
    btn('btnAddItem', 'Add Item', 596, 200, 88, 30, GREEN),
    TB('tblCart', ['Type', 'Description', 'Qty', 'Unit Price', 'Total'], x=16, y=240, w=668, h=170),
    btn('btnRemoveItem', 'Remove Item', 16, 420, 130, 30, RED, DRED),
    L('capSubtotal', 'Subtotal:', x=320, y=427, w=60, h=16, font=BODY, fg=TXT),
    L('lblSubtotal', '0.00', x=384, y=427, w=90, h=16, font=BTN, fg=TXT),
    L('capTotal', 'Total (LKR):', x=474, y=424, w=90, h=22, font=BODY, fg=TXT),
    L('lblTotal', '0.00', x=568, y=424, w=116, h=22, font=(SEG, 1, 16), fg=TXT),
    cap('capDiscount', 'DISCOUNT (LKR)', 16, 466, 150), fld('txtDiscount', '0', 16, 484, 150),
    cap('capTax', 'TAX (%)', 178, 466, 150), fld('txtTaxRate', '0', 178, 484, 150),
    cap('capMethod', 'PAYMENT METHOD', 340, 466, 170), cmb('cmbMethod', ['Cash', 'Card', 'Online'], 340, 484, 170),
    cap('capPaid', 'PAID NOW (LKR)', 522, 466, 160), fld('txtPaidNow', '0', 522, 484, 162),
    cap('capNotes', 'NOTES', 16, 524, 200), fld('txtNotes', '', 16, 542, 668),
    btn('btnSave', 'Save Invoice', 16, 592, 326, 38, GREEN),
    btn('btnClear', 'Clear', 358, 592, 326, 38, GREY),
])

right = P('pnlInvoices', x=735, y=168, w=685, h=650, bg=WHITE, border=('line', LINE), children=[
    L('lblListTitle', 'INVOICES', x=16, y=12, w=400, h=22, font=(SEG, 1, 16), fg=TXT),
    L('lblListSub', 'Recent invoices and payment status', x=16, y=36, w=400, h=18, font=BODY, fg=MUT),
    cap('capSearch', 'SEARCH (INVOICE #, CUSTOMER, PET)', 16, 66, 400), fld('txtSearch', '', 16, 84, 480),
    cap('capStatus', 'STATUS', 506, 66, 100), cmb('cmbFilter', ['All', 'Unpaid', 'Partial', 'Paid', 'Cancelled'], 506, 84, 163),
    TB('tblInvoices', ['Invoice #', 'Date', 'Customer', 'Pet', 'Total', 'Paid', 'Balance', 'Status'], x=16, y=124, w=653, h=452),
    btn('btnPay', 'Record Payment', 16, 592, 152, 38, GREEN),
    btn('btnPrint', 'Print Invoice', 183, 592, 152, 38, BLUE),
    btn('btnCancel', 'Cancel Invoice', 350, 592, 152, 38, RED, DRED),
    btn('btnRefresh', 'Refresh', 517, 592, 152, 38, GREY),
])

f = Form('BillingForm', title='PawCare 360 - Billing', size=(1700, 900), minsize=(1280, 760), children=[
    BEAN('sidebarPanel1', 'view.SidebarPanel', pos='West'),
    P('pnlContent', pos='Center', layout='abs', bg=BG, children=[
        L('lblTitle', 'Billing', x=20, y=14, w=400, h=34, font=(SEG, 1, 24), fg=TXT),
        L('lblSubtitle', 'Create invoices and record payments', x=20, y=48, w=500, h=20, font=BODY, fg=MUT),
        stat('pnlStatTotal', 'capStatTotal', 'lblTotalCount', 'TOTAL INVOICES', '0', 20),
        stat('pnlStatRevenue', 'capStatRevenue', 'lblRevenueCount', 'REVENUE COLLECTED (LKR)', '0.00', 375),
        stat('pnlStatOutstanding', 'capStatOutstanding', 'lblOutstandingCount', 'OUTSTANDING (LKR)', '0.00', 730),
        stat('pnlStatUnpaid', 'capStatUnpaid', 'lblUnpaidCount', 'UNPAID / PARTIAL', '0', 1085),
        left, right,
        L('lblMessage', ' ', x=20, y=826, w=1400, h=22, font=BODY, fg=MUT),
    ])])

EXTRA = r'''
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
'''
CTOR = '''        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); // Full screen
        setLocationRelativeTo(null);
        readOnly(tblCart, 24);
        readOnly(tblInvoices, 26);
        sorter = new javax.swing.table.TableRowSorter<>((javax.swing.table.DefaultTableModel) tblInvoices.getModel());
        tblInvoices.setRowSorter(sorter);
'''


def my_getters(form):
    out = []
    for n in form.all_nodes():
        if n.kind in ('JButton', 'JTextField', 'JComboBox', 'JTable', 'JSpinner') or (n.kind == 'JLabel' and n.name.startswith('lbl')):
            t = 'javax.swing.JComboBox<String>' if n.kind == 'JComboBox' else n.classname()
            out.append(f'    public {t} get{n.name[0].upper()+n.name[1:]}() {{\n        return {n.name};\n    }}\n')
    return '\n'.join(out)


# Fit narrower screens
def shrink(node, k=0.9):
    for c in node.children:
        if c.kind != 'JLabel' or c.w > 150:
            c.x = round(c.x * k)
        else:
            c.x = round(c.x * k)
        if c.kind == 'JButton':
            c.w = max(round(c.w * k), len(c.text) * 9 + 34)
        elif c.kind != 'JLabel':
            c.w = round(c.w * k)
        shrink(c, k)


for _n in f.children:
    if _n.name == 'pnlContent':
        shrink(_n)
left.w += 26
right.x += 26
f.size = (1450, 900)


if __name__ == '__main__':
    write(f, 'src/view/BillingForm', ctor_body=CTOR, members=my_getters(f) + EXTRA)
