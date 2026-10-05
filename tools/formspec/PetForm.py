import sys; sys.path.insert(0, 'tools/formgen')
from formgen import *

BG = (250, 246, 240); WHITE = (255, 255, 255); LINE = (217, 217, 217)
TXT = (74, 91, 106); MUTED = (113, 128, 140)
GREEN = (157, 201, 163); BLUE = (190, 210, 230); RED = (220, 170, 170); GREY = (235, 235, 230)
FONT = 'Segoe UI'


def lab(name, text, x, y, w):
    return L(name, text, x=x, y=y, w=w, h=18, font=(FONT, 1, 11), fg=MUTED)


def fld(name, x, y, w):
    return T(name, x=x, y=y, w=w, h=32, font=(FONT, 0, 12), fg=TXT)


def combo(name, items, x, y, w, **kw):
    return CB(name, items, x=x, y=y, w=w, h=32, font=(FONT, 0, 12), fg=TXT, **kw)


def btn(name, text, x, y, w, bg):
    return B(name, text, x=x, y=y, w=w, h=36, bg=bg, fg=TXT, font=(FONT, 1, 12))


cards = []
spec = [('Dog', 'DOGS', 'Canine'), ('Cat', 'CATS', 'Feline'), ('Rabbit', 'RABBITS', 'Small animal'),
        ('Bird', 'BIRDS', 'Avian'), ('Other', 'OTHER', 'Other species')]
for i, (k, t, s) in enumerate(spec):
    cards.append(P('pnl%sCard' % k, x=20 + i * 244, y=75, w=224, h=88, bg=WHITE, border=('line', LINE), children=[
        L('lbl%sTitle' % k, t, x=16, y=10, w=190, h=18, font=(FONT, 1, 11), fg=TXT),
        L('lbl%sCount' % k, '0', x=16, y=28, w=190, h=32, font=(FONT, 1, 24), fg=(92, 126, 101)),
        L('lbl%sSub' % k, s, x=16, y=62, w=190, h=16, font=(FONT, 0, 11), fg=MUTED)]))

details = P('pnlDetails', x=20, y=180, w=1200, h=290, bg=WHITE, border=('line', LINE), children=[
    L('lblDetailsTitle', 'Patient Profile', x=20, y=12, w=400, h=28, font=(FONT, 1, 18), fg=TXT),
    L('lblDetailsSub', 'Register and maintain essential patient information', x=20, y=40, w=500, h=18, font=(FONT, 0, 12), fg=MUTED),
    lab('lblName', 'Patient Name *', 20, 70, 220), fld('txtPetName', 20, 90, 220),
    lab('lblSpecies', 'Species *', 260, 70, 200),
    combo('cmbSpecies', ['Select species', 'Dog', 'Cat', 'Rabbit', 'Bird', 'Hamster', 'Turtle', 'Other'], 260, 90, 200),
    lab('lblBreed', 'Breed', 480, 70, 220), fld('txtBreed', 480, 90, 220),
    lab('lblOwner', 'Owner *', 720, 70, 260),
    combo('cmbCustomer', ['Select owner'], 720, 90, 260, bools={'editable': True}, tooltip='Select owner or type a Customer ID'),
    lab('lblGender', 'Gender', 1000, 70, 180),
    combo('cmbGender', ['Select', 'Male', 'Female'], 1000, 90, 180),
    lab('lblDob', 'Date of Birth (YYYY-MM-DD)', 20, 135, 220), fld('txtDateOfBirth', 20, 155, 220),
    lab('lblWeight', 'Weight (kg)', 260, 135, 200), fld('txtWeight', 260, 155, 200),
    lab('lblNotes', 'Clinical Notes', 480, 135, 220),
    TA('txtNotes', x=480, y=155, w=500, h=60, font=(FONT, 0, 12), fg=TXT, bools={'lineWrap': True, 'wrapStyleWord': True}),
    lab('lblPetId', 'Patient ID', 1000, 135, 180), T('txtPetId', x=1000, y=155, w=180, h=32, font=(FONT, 0, 12), fg=TXT,
                                                       bg=GREY, bools={'editable': False}),
    btn('btnAdd', 'Save Patient', 560, 235, 150, GREEN), btn('btnUpdate', 'Update', 730, 235, 140, BLUE),
    btn('btnDelete', 'Delete', 890, 235, 140, RED), btn('btnClear', 'Clear', 1040, 235, 140, GREY),
])

f = Form('PetForm', title='PawCare 360 - Pet Patients', size=(1500, 900), minsize=(1200, 800), close=2, children=[
    BEAN('sidebarPanel1', 'view.SidebarPanel', pos='West'),
    P('pnlContent', pos='Center', layout='abs', bg=BG, children=[
        L('lblTitle', 'Pet Patients', x=20, y=15, w=400, h=40, font=(FONT, 1, 26), fg=TXT),
        B('btnNew', '+ New Patient', x=1020, y=20, w=200, h=40, bg=BLUE, fg=TXT, font=(FONT, 1, 12)),
    ] + cards + [
        details,
        L('lblRecords', 'Recent Patient Records', x=20, y=482, w=400, h=24, font=(FONT, 1, 16), fg=TXT),
        TB('tblPets', ['ID', 'Patient', 'Species', 'Breed', 'Gender', 'Owner', 'Date of Birth', 'Weight', 'Notes'],
           x=20, y=512, w=1200, h=270, font=(FONT, 0, 12), fg=TXT),
    ])])

MEMBERS = r'''
    // Read-only model
    private void readOnlyTable() {
        javax.swing.table.DefaultTableModel old = (javax.swing.table.DefaultTableModel) tblPets.getModel();
        String[] cols = new String[old.getColumnCount()];
        for (int i = 0; i < cols.length; i++) {
            cols[i] = old.getColumnName(i);
        }
        tblPets.setModel(new javax.swing.table.DefaultTableModel(new Object[0][], cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblPets.setRowHeight(30);
        tblPets.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPets.getTableHeader().setReorderingAllowed(false);
        tblPets.removeColumn(tblPets.getColumnModel().getColumn(8)); // Hidden notes
    }

    // Replace rows
    public void setRows(java.util.List<Object[]> rows) {
        javax.swing.table.DefaultTableModel m = (javax.swing.table.DefaultTableModel) tblPets.getModel();
        m.setRowCount(0);
        for (Object[] r : rows) {
            m.addRow(r);
        }
    }

    // Cell text
    public String cellText(int viewRow, int col) {
        Object v = tblPets.getModel().getValueAt(tblPets.convertRowIndexToModel(viewRow), col);
        return v == null ? "" : v.toString();
    }

    // Owner choices
    public void setOwners(java.util.List<String> labels) {
        Object keep = cmbCustomer.getSelectedItem();
        cmbCustomer.removeAllItems();
        cmbCustomer.addItem("Select owner");
        for (String s : labels) {
            cmbCustomer.addItem(s);
        }
        if (keep != null && !keep.toString().trim().isEmpty()) {
            cmbCustomer.setSelectedItem(keep.toString());
        } else {
            cmbCustomer.setSelectedIndex(0);
        }
    }

    // Stat cards
    public void setCounts(int dogs, int cats, int rabbits, int birds, int other) {
        lblDogCount.setText(String.valueOf(dogs));
        lblCatCount.setText(String.valueOf(cats));
        lblRabbitCount.setText(String.valueOf(rabbits));
        lblBirdCount.setText(String.valueOf(birds));
        lblOtherCount.setText(String.valueOf(other));
    }

    // Fill fields
    public void fillForm(String id, String name, String species, String breed, String gender,
            String owner, String dob, String weight, String notes) {
        txtPetId.setText(id);
        txtPetName.setText(name);
        selectItem(cmbSpecies, species);
        txtBreed.setText(breed);
        selectItem(cmbGender, gender);
        if (owner.isEmpty()) {
            cmbCustomer.setSelectedIndex(0);
        } else {
            cmbCustomer.setSelectedItem(owner);
        }
        txtDateOfBirth.setText(dob);
        txtWeight.setText(weight);
        txtNotes.setText(notes);
        setEditEnabled(true);
    }

    // Reset fields
    public void clearForm() {
        txtPetId.setText("");
        txtPetName.setText("");
        txtBreed.setText("");
        txtDateOfBirth.setText("");
        txtWeight.setText("");
        txtNotes.setText("");
        cmbSpecies.setSelectedIndex(0);
        cmbGender.setSelectedIndex(0);
        cmbCustomer.setSelectedIndex(0);
        tblPets.clearSelection();
        setEditEnabled(false);
    }

    // Toggle buttons
    public void setEditEnabled(boolean on) {
        btnUpdate.setEnabled(on);
        btnDelete.setEnabled(on);
    }

    // Combo match
    private void selectItem(javax.swing.JComboBox<String> c, String text) {
        String t = text == null ? "" : text.trim();
        for (int i = 0; i < c.getItemCount(); i++) {
            if (c.getItemAt(i).equalsIgnoreCase(t)) {
                c.setSelectedIndex(i);
                return;
            }
        }
        c.setSelectedIndex(0);
    }

    // Field readers
    public String getPetIdText() { return txtPetId.getText().trim(); }
    public String getPetName() { return txtPetName.getText().trim(); }
    public String getBreed() { return txtBreed.getText().trim(); }
    public String getDob() { return txtDateOfBirth.getText().trim(); }
    public String getWeightText() { return txtWeight.getText().trim(); }
    public String getNotes() { return txtNotes.getText().trim(); }
    public String getOwnerText() {
        Object o = cmbCustomer.getSelectedItem();
        return o == null ? "" : o.toString().trim();
    }
    public String getSpecies() { return cmbSpecies.getSelectedIndex() <= 0 ? "" : cmbSpecies.getSelectedItem().toString(); }
    public String getGender() { return cmbGender.getSelectedIndex() <= 0 ? "" : cmbGender.getSelectedItem().toString(); }

'''
write(f, 'src/view/PetForm', ctor_body='        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);\n        setLocationRelativeTo(null);\n        readOnlyTable();\n        setEditEnabled(false);\n',
      members=getters(f) + MEMBERS)
