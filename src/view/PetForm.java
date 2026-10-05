package view;


/**
 * PetForm: passive view (designer form).
 * Logic lives in the controller.
 */
public class PetForm extends javax.swing.JFrame {


    public PetForm() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        readOnlyTable();
        setEditEnabled(false);
    }

    public view.SidebarPanel getSidebarPanel1() {
        return BeanFinder.find(getContentPane(), SidebarPanel.class);
    }

    public javax.swing.JPanel getPnlContent() {
        return pnlContent;
    }

    public javax.swing.JLabel getLblTitle() {
        return lblTitle;
    }

    public javax.swing.JButton getBtnNew() {
        return btnNew;
    }

    public javax.swing.JPanel getPnlDogCard() {
        return pnlDogCard;
    }

    public javax.swing.JLabel getLblDogTitle() {
        return lblDogTitle;
    }

    public javax.swing.JLabel getLblDogCount() {
        return lblDogCount;
    }

    public javax.swing.JLabel getLblDogSub() {
        return lblDogSub;
    }

    public javax.swing.JPanel getPnlCatCard() {
        return pnlCatCard;
    }

    public javax.swing.JLabel getLblCatTitle() {
        return lblCatTitle;
    }

    public javax.swing.JLabel getLblCatCount() {
        return lblCatCount;
    }

    public javax.swing.JLabel getLblCatSub() {
        return lblCatSub;
    }

    public javax.swing.JPanel getPnlRabbitCard() {
        return pnlRabbitCard;
    }

    public javax.swing.JLabel getLblRabbitTitle() {
        return lblRabbitTitle;
    }

    public javax.swing.JLabel getLblRabbitCount() {
        return lblRabbitCount;
    }

    public javax.swing.JLabel getLblRabbitSub() {
        return lblRabbitSub;
    }

    public javax.swing.JPanel getPnlBirdCard() {
        return pnlBirdCard;
    }

    public javax.swing.JLabel getLblBirdTitle() {
        return lblBirdTitle;
    }

    public javax.swing.JLabel getLblBirdCount() {
        return lblBirdCount;
    }

    public javax.swing.JLabel getLblBirdSub() {
        return lblBirdSub;
    }

    public javax.swing.JPanel getPnlOtherCard() {
        return pnlOtherCard;
    }

    public javax.swing.JLabel getLblOtherTitle() {
        return lblOtherTitle;
    }

    public javax.swing.JLabel getLblOtherCount() {
        return lblOtherCount;
    }

    public javax.swing.JLabel getLblOtherSub() {
        return lblOtherSub;
    }

    public javax.swing.JPanel getPnlDetails() {
        return pnlDetails;
    }

    public javax.swing.JLabel getLblDetailsTitle() {
        return lblDetailsTitle;
    }

    public javax.swing.JLabel getLblDetailsSub() {
        return lblDetailsSub;
    }

    public javax.swing.JLabel getLblName() {
        return lblName;
    }

    public javax.swing.JTextField getTxtPetName() {
        return txtPetName;
    }

    public javax.swing.JLabel getLblSpecies() {
        return lblSpecies;
    }

    public javax.swing.JComboBox<String> getCmbSpecies() {
        return cmbSpecies;
    }

    public javax.swing.JLabel getLblBreed() {
        return lblBreed;
    }

    public javax.swing.JTextField getTxtBreed() {
        return txtBreed;
    }

    public javax.swing.JLabel getLblOwner() {
        return lblOwner;
    }

    public javax.swing.JComboBox<String> getCmbCustomer() {
        return cmbCustomer;
    }

    public javax.swing.JLabel getLblGender() {
        return lblGender;
    }

    public javax.swing.JComboBox<String> getCmbGender() {
        return cmbGender;
    }

    public javax.swing.JLabel getLblDob() {
        return lblDob;
    }

    public javax.swing.JTextField getTxtDateOfBirth() {
        return txtDateOfBirth;
    }

    public javax.swing.JLabel getLblWeight() {
        return lblWeight;
    }

    public javax.swing.JTextField getTxtWeight() {
        return txtWeight;
    }

    public javax.swing.JLabel getLblNotes() {
        return lblNotes;
    }

    public javax.swing.JTextArea getTxtNotes() {
        return txtNotes;
    }

    public javax.swing.JLabel getLblPetId() {
        return lblPetId;
    }

    public javax.swing.JTextField getTxtPetId() {
        return txtPetId;
    }

    public javax.swing.JButton getBtnAdd() {
        return btnAdd;
    }

    public javax.swing.JButton getBtnUpdate() {
        return btnUpdate;
    }

    public javax.swing.JButton getBtnDelete() {
        return btnDelete;
    }

    public javax.swing.JButton getBtnClear() {
        return btnClear;
    }

    public javax.swing.JLabel getLblRecords() {
        return lblRecords;
    }

    public javax.swing.JTable getTblPets() {
        return tblPets;
    }

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


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContent = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        pnlDogCard = new javax.swing.JPanel();
        lblDogTitle = new javax.swing.JLabel();
        lblDogCount = new javax.swing.JLabel();
        lblDogSub = new javax.swing.JLabel();
        pnlCatCard = new javax.swing.JPanel();
        lblCatTitle = new javax.swing.JLabel();
        lblCatCount = new javax.swing.JLabel();
        lblCatSub = new javax.swing.JLabel();
        pnlRabbitCard = new javax.swing.JPanel();
        lblRabbitTitle = new javax.swing.JLabel();
        lblRabbitCount = new javax.swing.JLabel();
        lblRabbitSub = new javax.swing.JLabel();
        pnlBirdCard = new javax.swing.JPanel();
        lblBirdTitle = new javax.swing.JLabel();
        lblBirdCount = new javax.swing.JLabel();
        lblBirdSub = new javax.swing.JLabel();
        pnlOtherCard = new javax.swing.JPanel();
        lblOtherTitle = new javax.swing.JLabel();
        lblOtherCount = new javax.swing.JLabel();
        lblOtherSub = new javax.swing.JLabel();
        pnlDetails = new javax.swing.JPanel();
        lblDetailsTitle = new javax.swing.JLabel();
        lblDetailsSub = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        txtPetName = new javax.swing.JTextField();
        lblSpecies = new javax.swing.JLabel();
        cmbSpecies = new javax.swing.JComboBox<>();
        lblBreed = new javax.swing.JLabel();
        txtBreed = new javax.swing.JTextField();
        lblOwner = new javax.swing.JLabel();
        cmbCustomer = new javax.swing.JComboBox<>();
        lblGender = new javax.swing.JLabel();
        cmbGender = new javax.swing.JComboBox<>();
        lblDob = new javax.swing.JLabel();
        txtDateOfBirth = new javax.swing.JTextField();
        lblWeight = new javax.swing.JLabel();
        txtWeight = new javax.swing.JTextField();
        lblNotes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtNotes = new javax.swing.JTextArea();
        lblPetId = new javax.swing.JLabel();
        txtPetId = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblRecords = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPets = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PawCare 360 - Pet Patients");
        setMinimumSize(new java.awt.Dimension(1200, 800));
        setPreferredSize(new java.awt.Dimension(1500, 900));
        setSize(new java.awt.Dimension(1500, 900));

        pnlContent.setBackground(new java.awt.Color(250, 246, 240));
        pnlContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblTitle.setText("Pet Patients");
        pnlContent.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 400, 40));

        btnNew.setBackground(new java.awt.Color(60, 90, 100));
        btnNew.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNew.setForeground(new java.awt.Color(255, 255, 255));
        btnNew.setText("+ NEW PATIENT");
        btnNew.addActionListener(this::btnNewActionPerformed);
        pnlContent.add(btnNew, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, 210, 42));

        pnlDogCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlDogCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlDogCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDogTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblDogTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblDogTitle.setText("DOGS");
        pnlDogCard.add(lblDogTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, 190, 18));

        lblDogCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblDogCount.setForeground(new java.awt.Color(92, 126, 101));
        lblDogCount.setText("0");
        pnlDogCard.add(lblDogCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 28, 190, 32));

        lblDogSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDogSub.setForeground(new java.awt.Color(113, 128, 140));
        lblDogSub.setText("Canine");
        pnlDogCard.add(lblDogSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 62, 190, 16));

        pnlContent.add(pnlDogCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 75, 224, 88));

        pnlCatCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlCatCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCatCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCatTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCatTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblCatTitle.setText("CATS");
        pnlCatCard.add(lblCatTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, 190, 18));

        lblCatCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCatCount.setForeground(new java.awt.Color(92, 126, 101));
        lblCatCount.setText("0");
        pnlCatCard.add(lblCatCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 28, 190, 32));

        lblCatSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblCatSub.setForeground(new java.awt.Color(113, 128, 140));
        lblCatSub.setText("Feline");
        pnlCatCard.add(lblCatSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 62, 190, 16));

        pnlContent.add(pnlCatCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 75, 224, 88));

        pnlRabbitCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlRabbitCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlRabbitCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRabbitTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblRabbitTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblRabbitTitle.setText("RABBITS");
        pnlRabbitCard.add(lblRabbitTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, 190, 18));

        lblRabbitCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblRabbitCount.setForeground(new java.awt.Color(92, 126, 101));
        lblRabbitCount.setText("0");
        pnlRabbitCard.add(lblRabbitCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 28, 190, 32));

        lblRabbitSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblRabbitSub.setForeground(new java.awt.Color(113, 128, 140));
        lblRabbitSub.setText("Small animal");
        pnlRabbitCard.add(lblRabbitSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 62, 190, 16));

        pnlContent.add(pnlRabbitCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 75, 224, 88));

        pnlBirdCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlBirdCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlBirdCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblBirdTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblBirdTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblBirdTitle.setText("BIRDS");
        pnlBirdCard.add(lblBirdTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, 190, 18));

        lblBirdCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblBirdCount.setForeground(new java.awt.Color(92, 126, 101));
        lblBirdCount.setText("0");
        pnlBirdCard.add(lblBirdCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 28, 190, 32));

        lblBirdSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblBirdSub.setForeground(new java.awt.Color(113, 128, 140));
        lblBirdSub.setText("Avian");
        pnlBirdCard.add(lblBirdSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 62, 190, 16));

        pnlContent.add(pnlBirdCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(752, 75, 224, 88));

        pnlOtherCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlOtherCard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlOtherCard.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblOtherTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblOtherTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblOtherTitle.setText("OTHER");
        pnlOtherCard.add(lblOtherTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 10, 190, 18));

        lblOtherCount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblOtherCount.setForeground(new java.awt.Color(92, 126, 101));
        lblOtherCount.setText("0");
        pnlOtherCard.add(lblOtherCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 28, 190, 32));

        lblOtherSub.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblOtherSub.setForeground(new java.awt.Color(113, 128, 140));
        lblOtherSub.setText("Other species");
        pnlOtherCard.add(lblOtherSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 62, 190, 16));

        pnlContent.add(pnlOtherCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(996, 75, 224, 88));

        pnlDetails.setBackground(new java.awt.Color(255, 255, 255));
        pnlDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblDetailsTitle.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDetailsTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblDetailsTitle.setText("Patient Profile");
        pnlDetails.add(lblDetailsTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 12, 400, 28));

        lblDetailsSub.setForeground(new java.awt.Color(113, 128, 140));
        lblDetailsSub.setText("Register and maintain essential patient information");
        pnlDetails.add(lblDetailsSub, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 500, 18));

        lblName.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblName.setForeground(new java.awt.Color(113, 128, 140));
        lblName.setText("Patient Name *");
        pnlDetails.add(lblName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 220, 18));

        txtPetName.setForeground(new java.awt.Color(74, 91, 106));
        pnlDetails.add(txtPetName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 220, 32));

        lblSpecies.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblSpecies.setForeground(new java.awt.Color(113, 128, 140));
        lblSpecies.setText("Species *");
        pnlDetails.add(lblSpecies, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 200, 18));

        cmbSpecies.setForeground(new java.awt.Color(74, 91, 106));
        cmbSpecies.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select species", "Dog", "Cat", "Rabbit", "Bird", "Hamster", "Turtle", "Other" }));
        pnlDetails.add(cmbSpecies, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 200, 32));

        lblBreed.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblBreed.setForeground(new java.awt.Color(113, 128, 140));
        lblBreed.setText("Breed");
        pnlDetails.add(lblBreed, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, 220, 18));

        txtBreed.setForeground(new java.awt.Color(74, 91, 106));
        pnlDetails.add(txtBreed, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 90, 220, 32));

        lblOwner.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblOwner.setForeground(new java.awt.Color(113, 128, 140));
        lblOwner.setText("Owner *");
        pnlDetails.add(lblOwner, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 70, 260, 18));

        cmbCustomer.setForeground(new java.awt.Color(74, 91, 106));
        cmbCustomer.setEditable(true);
        cmbCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select owner" }));
        cmbCustomer.setToolTipText("Select owner or type a Customer ID");
        pnlDetails.add(cmbCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 90, 260, 32));

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblGender.setForeground(new java.awt.Color(113, 128, 140));
        lblGender.setText("Gender *");
        pnlDetails.add(lblGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 70, 180, 18));

        cmbGender.setForeground(new java.awt.Color(74, 91, 106));
        cmbGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Male", "Female" }));
        pnlDetails.add(cmbGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 90, 180, 32));

        lblDob.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblDob.setForeground(new java.awt.Color(113, 128, 140));
        lblDob.setText("Date of Birth (YYYY-MM-DD *");
        pnlDetails.add(lblDob, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 135, 220, 18));

        txtDateOfBirth.setForeground(new java.awt.Color(74, 91, 106));
        pnlDetails.add(txtDateOfBirth, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 155, 220, 32));

        lblWeight.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblWeight.setForeground(new java.awt.Color(113, 128, 140));
        lblWeight.setText("Weight (kg)");
        pnlDetails.add(lblWeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 135, 200, 18));

        txtWeight.setForeground(new java.awt.Color(74, 91, 106));
        pnlDetails.add(txtWeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 155, 200, 32));

        lblNotes.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblNotes.setForeground(new java.awt.Color(113, 128, 140));
        lblNotes.setText("Clinical Notes");
        pnlDetails.add(lblNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 135, 220, 18));

        txtNotes.setForeground(new java.awt.Color(74, 91, 106));
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtNotes);

        pnlDetails.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 155, 500, 32));

        lblPetId.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblPetId.setForeground(new java.awt.Color(113, 128, 140));
        lblPetId.setText("Patient ID");
        pnlDetails.add(lblPetId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 135, 180, 18));

        txtPetId.setEditable(false);
        txtPetId.setBackground(new java.awt.Color(235, 235, 230));
        txtPetId.setForeground(new java.awt.Color(74, 91, 106));
        pnlDetails.add(txtPetId, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 155, 180, 32));

        btnAdd.setBackground(new java.awt.Color(62, 107, 82));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Save Patient");
        pnlDetails.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 235, 200, 42));

        btnUpdate.setBackground(new java.awt.Color(190, 210, 230));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(74, 91, 106));
        btnUpdate.setText("Update");
        pnlDetails.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 235, 200, 42));

        btnDelete.setBackground(new java.awt.Color(220, 170, 170));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(74, 91, 106));
        btnDelete.setText("Delete");
        pnlDetails.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 235, 200, 42));

        btnClear.setBackground(new java.awt.Color(235, 235, 230));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClear.setForeground(new java.awt.Color(74, 91, 106));
        btnClear.setText("Clear");
        pnlDetails.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 235, 200, 42));

        pnlContent.add(pnlDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 1200, 290));

        lblRecords.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblRecords.setForeground(new java.awt.Color(74, 91, 106));
        lblRecords.setText("Recent Patient Records");
        pnlContent.add(lblRecords, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 482, 400, 24));

        tblPets.setForeground(new java.awt.Color(74, 91, 106));
        tblPets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Patient", "Species", "Breed", "Gender", "Owner", "Date of Birth", "Weight", "Notes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblPets);

        pnlContent.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 512, 1200, 270));

        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbCustomer;
    private javax.swing.JComboBox<String> cmbGender;
    private javax.swing.JComboBox<String> cmbSpecies;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBirdCount;
    private javax.swing.JLabel lblBirdSub;
    private javax.swing.JLabel lblBirdTitle;
    private javax.swing.JLabel lblBreed;
    private javax.swing.JLabel lblCatCount;
    private javax.swing.JLabel lblCatSub;
    private javax.swing.JLabel lblCatTitle;
    private javax.swing.JLabel lblDetailsSub;
    private javax.swing.JLabel lblDetailsTitle;
    private javax.swing.JLabel lblDob;
    private javax.swing.JLabel lblDogCount;
    private javax.swing.JLabel lblDogSub;
    private javax.swing.JLabel lblDogTitle;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNotes;
    private javax.swing.JLabel lblOtherCount;
    private javax.swing.JLabel lblOtherSub;
    private javax.swing.JLabel lblOtherTitle;
    private javax.swing.JLabel lblOwner;
    private javax.swing.JLabel lblPetId;
    private javax.swing.JLabel lblRabbitCount;
    private javax.swing.JLabel lblRabbitSub;
    private javax.swing.JLabel lblRabbitTitle;
    private javax.swing.JLabel lblRecords;
    private javax.swing.JLabel lblSpecies;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblWeight;
    private javax.swing.JPanel pnlBirdCard;
    private javax.swing.JPanel pnlCatCard;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JPanel pnlDogCard;
    private javax.swing.JPanel pnlOtherCard;
    private javax.swing.JPanel pnlRabbitCard;
    private javax.swing.JTable tblPets;
    private javax.swing.JTextField txtBreed;
    private javax.swing.JTextField txtDateOfBirth;
    private javax.swing.JTextArea txtNotes;
    private javax.swing.JTextField txtPetId;
    private javax.swing.JTextField txtPetName;
    private javax.swing.JTextField txtWeight;
    // End of variables declaration//GEN-END:variables
}
