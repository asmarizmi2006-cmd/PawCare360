# Spec: PrescriptionDialog
import sys
sys.path.insert(0, 'tools/formgen')
from formgen import *

BG = (250, 246, 240)
TEXT = (74, 91, 106)
SAGE = (157, 201, 163)
BLUE = (190, 210, 230)
RED = (220, 170, 170)
REDTXT = (120, 40, 40)
GREY = (235, 235, 230)
BORDER = (217, 217, 217)
LBL = (113, 128, 140)
FONT = ('Segoe UI', 0, 13)
BOLD = ('Segoe UI', 1, 12)


def lbl(name, text, x, y, w):
    return L(name, text, x=x, y=y, w=w, h=18, font=BOLD, fg=LBL)


def fld(name, x, y, w):
    return T(name, x=x, y=y, w=w, h=30, font=FONT)


def btn(name, text, x, y, bg, fg=TEXT, w=150):
    return B(name, text, x=x, y=y, w=w, h=36, bg=bg, fg=fg, font=BOLD)


card = dict(bg=(255, 255, 255), border=('line', BORDER))

presc = P('pnlPrescription', x=20, y=96, w=940, h=540, layout='abs', **card, children=[
    lbl('lblMedicine', 'MEDICINE', 14, 12, 300),
    CB('cmbMedicine', ['Medicine'], x=14, y=32, w=300, h=30, font=FONT),
    lbl('lblQuantity', 'QUANTITY', 330, 12, 120),
    fld('txtQuantity', 330, 32, 120),
    lbl('lblDosage', 'DOSAGE', 466, 12, 200),
    fld('txtDosage', 466, 32, 200),
    lbl('lblInstructions', 'INSTRUCTIONS', 14, 72, 300),
    fld('txtInstructions', 14, 92, 652),
    btn('btnAddMedicine', 'Add Medicine', 14, 136, SAGE),
    btn('btnRemoveLine', 'Remove Selected', 172, 136, RED, REDTXT, 180),
    btn('btnClose', 'Close', 360, 136, GREY),
    TB('tblLines', ['Medicine', 'Qty', 'Dosage', 'Instructions', 'Unit price', 'Line total'],
       x=14, y=186, w=910, h=300, font=FONT, fg=TEXT, ints={'rowHeight': 24}),
    L('lblTotal', 'Total medicine cost: 0.00', x=14, y=496, w=500, h=28, font=('Segoe UI', 1, 14), fg=TEXT),
])

stock = P('pnlStock', x=20, y=96, w=940, h=540, layout='abs', **card, children=[
    lbl('lblName', 'NAME', 14, 12, 200),
    fld('txtName', 14, 32, 200),
    lbl('lblCategory', 'CATEGORY', 226, 12, 160),
    fld('txtCategory', 226, 32, 160),
    lbl('lblPrice', 'UNIT PRICE', 398, 12, 120),
    fld('txtPrice', 398, 32, 120),
    lbl('lblStock', 'STOCK', 530, 12, 100),
    fld('txtStock', 530, 32, 100),
    lbl('lblStatus', 'STATUS', 642, 12, 140),
    CB('cmbStatus', ['Active', 'Inactive'], x=642, y=32, w=140, h=30, font=FONT),
    btn('btnAddStock', 'Add', 14, 76, SAGE),
    btn('btnUpdateStock', 'Update', 172, 76, BLUE),
    btn('btnDeleteStock', 'Delete', 330, 76, RED, REDTXT),
    btn('btnClearStock', 'Clear', 488, 76, GREY),
    TB('tblMedicines', ['ID', 'Name', 'Category', 'Unit Price', 'Stock', 'Status'],
       x=14, y=126, w=910, h=398, font=FONT, fg=TEXT, ints={'rowHeight': 24}),
])

f = Form('PrescriptionDialog', title='Medicines', kind='JDialog', close=2, size=(980, 680), children=[
    P('pnlRoot', pos='Center', layout='abs', bg=BG, children=[
        L('lblHeader', 'Medicines for Treatment', x=20, y=14, w=900, h=34, font=('Segoe UI', 1, 20), fg=TEXT),
        B('btnTabPrescription', 'Prescription', x=20, y=56, w=160, h=32, bg=SAGE, fg=TEXT, font=BOLD),
        B('btnTabStock', 'Medicine Stock', x=188, y=56, w=160, h=32, bg=GREY, fg=TEXT, font=BOLD),
        presc, stock,
    ])])

MEMBERS = '''
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
'''

CTOR = '''        setLocationRelativeTo(parent);
        java.awt.Color sage = new java.awt.Color(157, 201, 163);
        for (javax.swing.JTable t : new javax.swing.JTable[]{tblLines, tblMedicines}) {
            t.getTableHeader().setFont(new java.awt.Font("Segoe UI", 1, 12));
            t.setSelectionBackground(sage);
            t.setSelectionForeground(new java.awt.Color(74, 91, 106));
        }
        showStockTab(false);
'''

write(f, 'src/view/PrescriptionDialog', ctor_body=CTOR, members=getters(f) + MEMBERS)
