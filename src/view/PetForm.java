package view;

import service.PetService;
import model.Pet;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class PetForm extends JFrame {

    private PetService petService;

    private JTextField txtPetId, txtPetName, txtBreed, txtDateOfBirth, txtWeight;
    private JTextArea txtNotes;
    private JComboBox<String> cmbCustomer, cmbSpecies, cmbGender;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable tblPets;
    private JLabel lblDogCount, lblCatCount, lblRabbitCount, lblBirdCount, lblOtherCount;

    // --- Fields added so the layout can be recomputed on resize. ---
    // (No existing variable names were changed - these are new references.)
    private JPanel mainPanel, sidebar, content, topBar, profile;
    private JPanel staffCard, goldAccent;
    private JPanel[] animalCards = new JPanel[5];
    private JButton newPatient;
    private JLabel lblSystemStatus, lblRecords, lblRecordsSub;
    private JScrollPane tableScroll, notesScroll;

    private JLabel lblFieldPetName, lblFieldSpecies, lblFieldBreed, lblFieldOwner, lblFieldGender;
    private JLabel lblFieldDob, lblFieldWeight, lblFieldNotes, lblFieldPatientId;

    private static final Color NAVY = new Color(24, 37, 46);
    private static final Color NAVY_LIGHT = new Color(34, 51, 61);
    private static final Color IVORY = new Color(248, 246, 240);
    private static final Color WHITE = new Color(255, 255, 253);
    private static final Color SAGE = new Color(164, 190, 143);
    private static final Color SAGE_DARK = new Color(92, 126, 101);
    private static final Color TEAL = new Color(73, 105, 115);
    private static final Color GOLD = new Color(194, 160, 99);
    private static final Color TEXT = new Color(39, 53, 61);
    private static final Color MUTED = new Color(116, 129, 135);
    private static final Color BORDER = new Color(224, 226, 219);
    private static final Color FIELD_BG = new Color(250, 250, 247);

    private static final int SIDEBAR_WIDTH = 250;
    private static final int TOPBAR_HEIGHT = 76;
    private static final int PROFILE_HEIGHT = 300;
    private static final int OUTER_MARGIN = 40;

    // The content area is wrapped in a JScrollPane (see createUI/relayoutMain)
    // so it can scroll instead of clipping content when the window is
    // smaller than the dashboard needs.
    private static final int MIN_CONTENT_WIDTH = 1000;
    private static final int MIN_TABLE_HEIGHT = 180;
    private JScrollPane contentScrollPane;

    public PetForm() {
        petService = new PetService();
        setResizable(false);
        setTitle("PawCare 360 - Veterinary Patient Registry");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setSize(1500, 900);
        setLocationRelativeTo(null);

        createUI();
        loadPets();

        // Recompute the whole layout as soon as the frame is realized, and
        // again every time it's resized/maximized/restored.
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                relayoutMain();
            }
        });
        SwingUtilities.invokeLater(this::relayoutMain);
    }

    private void createUI() {
        mainPanel = new JPanel(null);
        mainPanel.setBackground(IVORY);
        setContentPane(mainPanel);

        sidebar = createSidebar();
        mainPanel.add(sidebar);

        content = createContent();

        contentScrollPane = new JScrollPane(content,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentScrollPane.setBorder(null);
        contentScrollPane.getViewport().setBackground(IVORY);
        contentScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        mainPanel.add(contentScrollPane);

        relayoutMain();
    }

    // ------------------------------------------------------------------
    // Resize-aware layout
    // ------------------------------------------------------------------

    private void relayoutMain() {
        int w = mainPanel.getWidth();
        int h = mainPanel.getHeight();
        if (w <= 0) w = 1400;
        if (h <= 0) h = 900;

        sidebar.setBounds(0, 0, SIDEBAR_WIDTH, h);
        int viewportWidth = Math.max(w - SIDEBAR_WIDTH, 700);
        contentScrollPane.setBounds(SIDEBAR_WIDTH, 0, viewportWidth, h);

        relayoutSidebar(h);
        relayoutContent(viewportWidth, h);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void relayoutSidebar(int sidebarHeight) {
        if (staffCard == null) return;
        int y = Math.max(sidebarHeight - 165, 400);
        staffCard.setBounds(25, y, 200, 100);
    }

    private void relayoutContent(int viewportWidth, int viewportHeight) {
        int recordsY = 315 + PROFILE_HEIGHT + 20;
        int tableY = recordsY + 47;
        int minRequiredHeight = tableY + MIN_TABLE_HEIGHT + (OUTER_MARGIN / 2);

        int w = Math.max(viewportWidth, MIN_CONTENT_WIDTH);
        int h = Math.max(viewportHeight, minRequiredHeight);

        content.setPreferredSize(new Dimension(w, h));

        topBar.setBounds(0, 0, w, TOPBAR_HEIGHT);
        lblSystemStatus.setBounds(Math.max(w - 300, 400), 28, 260, 20);
        newPatient.setBounds(w - OUTER_MARGIN - 185, 125, 185, 46);

        int cardsAreaWidth = w - (2 * OUTER_MARGIN);
        int gap = 20;
        int cardWidth = (cardsAreaWidth - 4 * gap) / 5;
        for (int i = 0; i < animalCards.length; i++) {
            if (animalCards[i] != null) {
                animalCards[i].setBounds(OUTER_MARGIN + i * (cardWidth + gap), 210, cardWidth, 88);
            }
        }

        int profileWidth = w - (2 * OUTER_MARGIN);
        profile.setBounds(OUTER_MARGIN, 315, profileWidth, PROFILE_HEIGHT);
        goldAccent.setBounds(0, 0, 4, PROFILE_HEIGHT);
        relayoutProfileFields(profileWidth);

        lblRecords.setBounds(OUTER_MARGIN, recordsY, 350, 22);
        lblRecordsSub.setBounds(OUTER_MARGIN, recordsY + 23, 500, 18);

        int tableHeight = Math.max(MIN_TABLE_HEIGHT, h - tableY - OUTER_MARGIN);
        tableScroll.setBounds(OUTER_MARGIN, tableY, w - (2 * OUTER_MARGIN), tableHeight);

        content.revalidate();
    }

    private void relayoutProfileFields(int profileWidth) {
        int margin = 30;
        int gap = 20;
        int cols = 5;
        int colWidth = (profileWidth - (2 * margin) - (cols - 1) * gap) / cols;
        int[] xs = new int[cols];
        for (int i = 0; i < cols; i++) {
            xs[i] = margin + i * (colWidth + gap);
        }

        // Row 1
        lblFieldPetName.setBounds(xs[0], 78, colWidth, 17);
        txtPetName.setBounds(xs[0], 98, colWidth, 34);

        lblFieldSpecies.setBounds(xs[1], 78, colWidth, 17);
        cmbSpecies.setBounds(xs[1], 98, colWidth, 34);

        lblFieldBreed.setBounds(xs[2], 78, colWidth, 17);
        txtBreed.setBounds(xs[2], 98, colWidth, 34);

        lblFieldOwner.setBounds(xs[3], 78, colWidth, 17);
        cmbCustomer.setBounds(xs[3], 98, colWidth, 34);

        lblFieldGender.setBounds(xs[4], 78, colWidth, 17);
        cmbGender.setBounds(xs[4], 98, colWidth, 34);

        // Row 2
        lblFieldDob.setBounds(xs[0], 148, colWidth, 17);
        txtDateOfBirth.setBounds(xs[0], 168, colWidth, 34);

        lblFieldWeight.setBounds(xs[1], 148, colWidth, 17);
        txtWeight.setBounds(xs[1], 168, colWidth, 34);

        int notesWidth = colWidth * 2 + gap;
        lblFieldNotes.setBounds(xs[2], 148, notesWidth, 17);
        notesScroll.setBounds(xs[2], 168, notesWidth, 50);

        lblFieldPatientId.setBounds(xs[4], 148, colWidth, 17);
        txtPetId.setBounds(xs[4], 168, colWidth, 34);

        // Button row, anchored with an equal margin on both sides of the panel.
        int btnAddW = 140, btnUpdateW = 100, btnDeleteW = 100, btnClearW = 100, btnGap = 15;
        int totalButtonsWidth = btnAddW + btnUpdateW + btnDeleteW + btnClearW + 3 * btnGap;
        int bx = profileWidth - margin - totalButtonsWidth;
        int by = PROFILE_HEIGHT - 30 - 32;

        btnAdd.setBounds(bx, by, btnAddW, 32);
        bx += btnAddW + btnGap;
        btnUpdate.setBounds(bx, by, btnUpdateW, 32);
        bx += btnUpdateW + btnGap;
        btnDelete.setBounds(bx, by, btnDeleteW, 32);
        bx += btnDeleteW + btnGap;
        btnClear.setBounds(bx, by, btnClearW, 32);
    }

    // ------------------------------------------------------------------
    // Sidebar
    // ------------------------------------------------------------------

    private JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel(null);
        sidebarPanel.setBackground(NAVY);

        PawLogoPanel logo = new PawLogoPanel();
        logo.setBounds(28, 30, 55, 55);
        sidebarPanel.add(logo);

        JLabel brand = new JLabel("PAWCARE");
        brand.setBounds(94, 28, 140, 30);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 23));
        brand.setForeground(Color.WHITE);
        sidebarPanel.add(brand);

        JLabel number = new JLabel("360");
        number.setBounds(96, 56, 100, 20);
        number.setFont(new Font("Segoe UI", Font.BOLD, 12));
        number.setForeground(SAGE);
        sidebarPanel.add(number);

        JLabel clinic = new JLabel("VETERINARY • PET CARE");
        clinic.setBounds(30, 105, 190, 18);
        clinic.setFont(new Font("Segoe UI", Font.BOLD, 9));
        clinic.setForeground(new Color(139, 157, 164));
        sidebarPanel.add(clinic);

        JPanel line = new JPanel();
        line.setBounds(30, 132, 190, 1);
        line.setBackground(new Color(57, 72, 81));
        sidebarPanel.add(line);

        JLabel workspace = new JLabel("CLINIC WORKSPACE");
        workspace.setBounds(30, 157, 180, 18);
        workspace.setFont(new Font("Segoe UI", Font.BOLD, 9));
        workspace.setForeground(new Color(125, 145, 153));
        sidebarPanel.add(workspace);

        String[] items = {"DASHBOARD", "CUSTOMERS", "PET PATIENTS", "STAFF", "APPOINTMENTS",
                "TREATMENTS", "GROOMING", "BOARDING", "BILLING", "REPORTS"};
        for (int i = 0; i < items.length; i++) {
            sidebarPanel.add(createNavButton(items[i], 190 + (i * 48), i == 2));
        }

        staffCard = new RoundedPanel(NAVY_LIGHT, 16);
        staffCard.setLayout(null);
        staffCard.setBounds(25, 735, 200, 100);
        sidebarPanel.add(staffCard);

        JLabel userTitle = new JLabel("CURRENT USER");
        userTitle.setBounds(15, 13, 150, 15);
        userTitle.setFont(new Font("Segoe UI", Font.BOLD, 8));
        userTitle.setForeground(new Color(130, 149, 156));
        staffCard.add(userTitle);

        JLabel user = new JLabel("Reception Desk");
        user.setBounds(15, 34, 160, 20);
        user.setFont(new Font("Segoe UI", Font.BOLD, 11));
        user.setForeground(Color.WHITE);
        staffCard.add(user);

        JLabel status = new JLabel("● Online");
        status.setBounds(15, 61, 100, 18);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        status.setForeground(SAGE);
        staffCard.add(status);

        return sidebarPanel;
    }

    // ------------------------------------------------------------------
    // Main content
    // ------------------------------------------------------------------

    private JPanel createContent() {
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBackground(IVORY);

        topBar = new JPanel(null);
        topBar.setBounds(0, 0, 1150, 76);
        topBar.setBackground(WHITE);
        contentPanel.add(topBar);

        JLabel breadcrumb = new JLabel("CLINIC / PATIENT REGISTRY");
        breadcrumb.setBounds(40, 18, 300, 16);
        breadcrumb.setFont(new Font("Segoe UI", Font.BOLD, 9));
        breadcrumb.setForeground(new Color(113, 127, 134));
        topBar.add(breadcrumb);

        JLabel clinicName = new JLabel("PawCare 360 Veterinary Centre");
        clinicName.setBounds(40, 38, 320, 20);
        clinicName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        clinicName.setForeground(TEXT);
        topBar.add(clinicName);

        lblSystemStatus = new JLabel("●  CLINIC SYSTEM ONLINE");
        lblSystemStatus.setBounds(850, 28, 260, 20);
        lblSystemStatus.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSystemStatus.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lblSystemStatus.setForeground(SAGE_DARK);
        topBar.add(lblSystemStatus);

        JPanel topLine = new JPanel();
        topLine.setBounds(0, 75, 1150, 1);
        topLine.setBackground(BORDER);
        topBar.add(topLine);

        JLabel section = new JLabel("PATIENT REGISTRY");
        section.setBounds(40, 105, 300, 18);
        section.setFont(new Font("Segoe UI", Font.BOLD, 9));
        section.setForeground(GOLD);
        contentPanel.add(section);

        JLabel title = new JLabel("Pet Patients");
        title.setBounds(38, 125, 500, 45);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT);
        contentPanel.add(title);

        JLabel description = new JLabel("A complete clinical profile for every patient in our care.");
        description.setBounds(40, 168, 600, 20);
        description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        description.setForeground(MUTED);
        contentPanel.add(description);

        newPatient = createPremiumButton("+  NEW PATIENT", TEAL, Color.WHITE);
        newPatient.setBounds(925, 125, 185, 46);
        newPatient.addActionListener(e -> {
            clearFields();
            tblPets.clearSelection();
            txtPetName.requestFocusInWindow();
        });
        contentPanel.add(newPatient);

        animalCards[0] = createAnimalCard("DOGS", "Canine", "DOG", 40);
        animalCards[1] = createAnimalCard("CATS", "Feline", "CAT", 250);
        animalCards[2] = createAnimalCard("RABBITS", "Small animal", "RABBIT", 460);
        animalCards[3] = createAnimalCard("BIRDS", "Avian", "BIRD", 670);
        animalCards[4] = createAnimalCard("OTHER", "Other species", "OTHER", 880);
        for (JPanel card : animalCards) {
            contentPanel.add(card);
        }

        profile = new RoundedPanel(WHITE, 18);
        profile.setLayout(null);
        profile.setBounds(40, 315, 1070, PROFILE_HEIGHT);
        contentPanel.add(profile);

        goldAccent = new JPanel();
        goldAccent.setBounds(0, 0, 4, PROFILE_HEIGHT);
        goldAccent.setBackground(GOLD);
        profile.add(goldAccent);

        JLabel profileTitle = new JLabel("PATIENT PROFILE");
        profileTitle.setBounds(28, 18, 300, 24);
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        profileTitle.setForeground(TEXT);
        profile.add(profileTitle);

        JLabel profileSub = new JLabel("Register and maintain essential patient information");
        profileSub.setBounds(29, 44, 450, 18);
        profileSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        profileSub.setForeground(MUTED);
        profile.add(profileSub);

        lblFieldPetName = addFieldLabel(profile, "PATIENT NAME *", 30, 78);
        txtPetName = createInputField(30, 98, 190);
        txtPetName.setToolTipText("Enter the pet's name");
        profile.add(txtPetName);

        lblFieldSpecies = addFieldLabel(profile, "SPECIES *", 240, 78);
        cmbSpecies = createSpeciesCombo();
        cmbSpecies.setBounds(240, 98, 160, 34);
        profile.add(cmbSpecies);

        lblFieldBreed = addFieldLabel(profile, "BREED", 420, 78);
        txtBreed = createInputField(420, 98, 190);
        txtBreed.setToolTipText("Enter breed if known");
        profile.add(txtBreed);

        lblFieldOwner = addFieldLabel(profile, "OWNER *", 630, 78);
        cmbCustomer = new JComboBox<>();
        cmbCustomer.addItem("Select owner");
        styleComboBox(cmbCustomer);
        cmbCustomer.setEditable(true);
        cmbCustomer.setToolTipText("Select or type a Customer ID");
        cmbCustomer.setBounds(630, 98, 190, 34);
        profile.add(cmbCustomer);

        lblFieldGender = addFieldLabel(profile, "GENDER", 840, 78);
        cmbGender = new JComboBox<>(new String[]{"Select", "Male", "Female"});
        styleComboBox(cmbGender);
        cmbGender.setBounds(840, 98, 195, 34);
        profile.add(cmbGender);

        lblFieldDob = addFieldLabel(profile, "DATE OF BIRTH", 30, 148);
        txtDateOfBirth = createInputField(30, 168, 190);
        txtDateOfBirth.setToolTipText("Format: YYYY-MM-DD");
        profile.add(txtDateOfBirth);

        lblFieldWeight = addFieldLabel(profile, "WEIGHT (KG)", 240, 148);
        txtWeight = createInputField(240, 168, 160);
        txtWeight.setToolTipText("Enter a positive value. Example: 12.5");
        profile.add(txtWeight);

        lblFieldNotes = addFieldLabel(profile, "CLINICAL NOTES", 420, 148);
        txtNotes = new JTextArea();
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtNotes.setForeground(TEXT);
        txtNotes.setBackground(FIELD_BG);
        txtNotes.setCaretColor(TEXT);
        txtNotes.setBorder(new EmptyBorder(7, 9, 7, 9));

        notesScroll = new JScrollPane(txtNotes);
        notesScroll.setBounds(420, 168, 400, 50);
        notesScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        profile.add(notesScroll);

        lblFieldPatientId = addFieldLabel(profile, "PATIENT ID", 840, 148);
        txtPetId = createInputField(840, 168, 195);
        txtPetId.setEditable(false);
        txtPetId.setBackground(new Color(241, 242, 238));
        txtPetId.setToolTipText("Patient ID is generated automatically");
        profile.add(txtPetId);

        btnAdd = createPremiumButton("SAVE PATIENT", SAGE_DARK, Color.WHITE);
        btnAdd.setBounds(610, 225, 130, 32);
        btnAdd.setToolTipText("Save the current patient");
        profile.add(btnAdd);

        btnUpdate = createPremiumButton("UPDATE", new Color(218, 231, 232), TEXT);
        btnUpdate.setBounds(750, 225, 95, 32);
        btnUpdate.setToolTipText("Update the selected patient");
        profile.add(btnUpdate);

        btnDelete = createPremiumButton("DELETE", new Color(239, 225, 215), new Color(112, 76, 64));
        btnDelete.setBounds(855, 225, 95, 32);
        btnDelete.setToolTipText("Delete the selected patient");
        profile.add(btnDelete);

        btnClear = createPremiumButton("CLEAR", new Color(238, 239, 234), TEXT);
        btnClear.setBounds(960, 225, 100, 32);
        btnClear.setToolTipText("Clear the patient form");
        btnClear.addActionListener(e -> {
            clearFields();
            tblPets.clearSelection();
            setEditButtonsEnabled(false);
            txtPetName.requestFocusInWindow();
        });
        profile.add(btnClear);

        // =====================================================
        // DATABASE CRUD BUTTON ACTIONS
        // =====================================================

        btnAdd.addActionListener(e -> savePet());
        btnUpdate.addActionListener(e -> updatePet());
        btnDelete.addActionListener(e -> deletePet());

        lblRecords = new JLabel("RECENT PATIENT RECORDS");
        lblRecords.setBounds(40, 610, 350, 22);
        lblRecords.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblRecords.setForeground(TEXT);
        contentPanel.add(lblRecords);

        lblRecordsSub = new JLabel("Patient records currently registered at PawCare 360");
        lblRecordsSub.setBounds(40, 633, 500, 18);
        lblRecordsSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRecordsSub.setForeground(MUTED);
        contentPanel.add(lblRecordsSub);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "PATIENT", "SPECIES", "BREED", "GENDER", "OWNER", "DATE OF BIRTH", "WEIGHT", "NOTES"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPets = new JTable(model);
        styleTable();

        // Notes are stored in the table model but kept hidden from the UI.
        // This allows a selected row to restore its clinical notes correctly.
        tblPets.removeColumn(tblPets.getColumnModel().getColumn(8));

        tableScroll = new JScrollPane(tblPets);
        tableScroll.setBounds(40, 657, 1070, 190);
        tableScroll.setBorder(BorderFactory.createLineBorder(BORDER));
        tableScroll.getViewport().setBackground(WHITE);
        contentPanel.add(tableScroll);

        // Selecting a table row now provides normal form UX.
        tblPets.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRowToForm();
            }
        });

        setEditButtonsEnabled(false);

        return contentPanel;
    }

    private void styleTable() {
        tblPets.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tblPets.setRowHeight(36);
        tblPets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPets.setBackground(WHITE);
        tblPets.setForeground(TEXT);
        tblPets.setGridColor(new Color(232, 233, 228));
        tblPets.setShowVerticalLines(false);
        tblPets.setShowHorizontalLines(true);
        // Was setFocusable(false) - that silently broke keyboard/tab navigation
        // and arrow-key row selection. Restored normal focus behavior.
        tblPets.setFocusable(true);
        tblPets.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblPets.setSelectionBackground(new Color(224, 235, 218));
        tblPets.setSelectionForeground(TEXT);
        tblPets.setIntercellSpacing(new Dimension(0, 1));

        // Give columns sensible relative proportions instead of all-equal
        // widths; AUTO_RESIZE_ALL_COLUMNS preserves these ratios on resize.
        int[] relativeWidths = {70, 150, 100, 130, 90, 150, 120, 90};
        for (int i = 0; i < relativeWidths.length && i < tblPets.getColumnCount(); i++) {
            TableColumn col = tblPets.getColumnModel().getColumn(i);
            col.setPreferredWidth(relativeWidths[i]);
        }

        JTableHeaderHelper.style(tblPets, BORDER);
    }

    private JPanel createAnimalCard(String title, String subtitle, String type, int x) {
        RoundedPanel card = new RoundedPanel(WHITE, 16);
        card.setLayout(null);
        card.setBounds(x, 210, 190, 88);

        AnimalIconPanel icon = new AnimalIconPanel(type);
        icon.setBounds(15, 15, 52, 52);
        card.add(icon);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setBounds(78, 12, 100, 17);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitle.setForeground(TEXT);
        card.add(lblTitle);

        JLabel lblCount = new JLabel("0");
        lblCount.setBounds(78, 30, 80, 28);
        lblCount.setFont(new Font("Segoe UI", Font.BOLD, 21));
        lblCount.setForeground(SAGE_DARK);
        card.add(lblCount);

        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setBounds(78, 58, 100, 14);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        lblSubtitle.setForeground(MUTED);
        card.add(lblSubtitle);

        switch (type) {
            case "DOG" -> lblDogCount = lblCount;
            case "CAT" -> lblCatCount = lblCount;
            case "RABBIT" -> lblRabbitCount = lblCount;
            case "BIRD" -> lblBirdCount = lblCount;
            default -> lblOtherCount = lblCount;
        }

        return card;
    }

    private JButton createNavButton(String text, int y, boolean active) {
        JButton button = new JButton(text);
        button.setBounds(15, y, 220, 40);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);

        Color normalBg = active ? SAGE : NAVY;
        Color hoverBg = active ? new Color(177, 200, 158) : NAVY_LIGHT;
        Color normalFg = active ? new Color(31, 48, 56) : new Color(205, 216, 220);

        button.setBackground(normalBg);
        button.setForeground(normalFg);
        addHoverEffect(button, normalBg, hoverBg, normalFg);
        return button;
    }

    private JTextField createInputField(int x, int y, int width) {
        JTextField field = new JTextField();
        field.setBounds(x, y, width, 34);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        field.setForeground(TEXT);
        field.setBackground(FIELD_BG);
        field.setCaretColor(TEXT);
        field.setOpaque(true);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(0, 9, 0, 9)));
        return field;
    }

    private JComboBox<String> createSpeciesCombo() {
        JComboBox<String> combo = new JComboBox<>(new String[]{
                "Select species", "Dog", "Cat", "Rabbit", "Bird", "Hamster", "Turtle", "Other"
        });
        styleComboBox(combo);
        return combo;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        combo.setForeground(TEXT);
        combo.setBackground(FIELD_BG);
        // Was setFocusable(false) - that silently broke tab/keyboard access
        // to every combo box in the form. Restored normal focus behavior.
        combo.setFocusable(true);
        combo.setOpaque(true);
        combo.setBorder(BorderFactory.createLineBorder(BORDER));
        combo.setToolTipText("Select an option");
    }

    private JButton createPremiumButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 9));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color hover = brighten(background, 0.08f);
        addHoverEffect(button, background, hover, foreground);
        return button;
    }

    private void addHoverEffect(JButton button, Color normal, Color hover, Color foreground) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(normal);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(brighten(normal, -0.08f));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.isEnabled()) button.setBackground(button.contains(e.getPoint()) ? hover : normal);
            }
        });
    }

    private Color brighten(Color c, float amount) {
        int r = clamp((int) (c.getRed() + 255 * amount));
        int g = clamp((int) (c.getGreen() + 255 * amount));
        int b = clamp((int) (c.getBlue() + 255 * amount));
        return new Color(r, g, b);
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private JLabel addFieldLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 180, 17);
        label.setFont(new Font("Segoe UI", Font.BOLD, 8));
        label.setForeground(new Color(104, 117, 123));
        panel.add(label);
        return label;
    }

    // =========================================================
    // LOAD SELECTED TABLE ROW INTO FORM
    // =========================================================

    private void loadSelectedRowToForm() {
        int row = tblPets.getSelectedRow();

        if (row < 0) {
            setEditButtonsEnabled(false);
            return;
        }

        int modelRow = tblPets.convertRowIndexToModel(row);
        DefaultTableModel model = (DefaultTableModel) tblPets.getModel();

        setTextFromTable(txtPetId, model.getValueAt(modelRow, 0));
        setTextFromTable(txtPetName, model.getValueAt(modelRow, 1));
        setComboFromTable(cmbSpecies, model.getValueAt(modelRow, 2));
        setTextFromTable(txtBreed, model.getValueAt(modelRow, 3));
        setComboFromTable(cmbGender, model.getValueAt(modelRow, 4));
        setComboFromTable(cmbCustomer, model.getValueAt(modelRow, 5));
        setTextFromTable(txtDateOfBirth, model.getValueAt(modelRow, 6));
        setTextFromTable(txtWeight, model.getValueAt(modelRow, 7));

        // Column 8 is hidden from the JTable but remains in the model.
        setTextFromTable(txtNotes, model.getValueAt(modelRow, 8));

        setEditButtonsEnabled(true);
    }

    private void setTextFromTable(JTextField field, Object value) {
        field.setText(value == null ? "" : value.toString());
    }

    private void setTextFromTable(JTextArea area, Object value) {
        area.setText(value == null ? "" : value.toString());
    }

    private void setComboFromTable(JComboBox<String> combo, Object value) {
        if (value == null) {
            combo.setSelectedIndex(0);
            return;
        }

        String text = value.toString().trim();

        if (text.isEmpty()) {
            combo.setSelectedIndex(0);
            return;
        }

        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);

            if (item != null && text.equalsIgnoreCase(item.trim())) {
                combo.setSelectedIndex(i);
                return;
            }
        }

        if (combo == cmbCustomer) {
            combo.addItem(text);
            combo.setSelectedItem(text);
        } else {
            combo.setSelectedItem(text);
        }
    }

    // =========================================================
    // LOAD ALL PETS FROM DATABASE
    // =========================================================

    private void loadPets() {
        try {
            List<Pet> pets = petService.getAllPets();

            DefaultTableModel model =
                    (DefaultTableModel) tblPets.getModel();

            model.setRowCount(0);

            /*
             * Customer IDs are used in this PetForm because PetService/Pet
             * provide customerId, not the customer's name.
             *
             * The OWNER combo is editable, so a valid customer ID can also
             * be typed when registering the first pet for a customer.
             */
            Set<Integer> customerIds = new LinkedHashSet<>();

            if (pets != null) {
                for (Pet pet : pets) {
                    if (pet == null) {
                        continue;
                    }

                    if (pet.getCustomerId() > 0) {
                        customerIds.add(pet.getCustomerId());
                    }

                    model.addRow(new Object[]{
                        pet.getPetId(),
                        safe(pet.getPetName()),
                        safe(pet.getSpecies()),
                        safe(pet.getBreed()),
                        safe(pet.getGender()),
                        pet.getCustomerId(),
                        safe(pet.getDateOfBirth()),
                        pet.getWeight(),
                        safe(pet.getNotes())
                    });
                }
            }

            refreshCustomerCombo(customerIds);
            updateAnimalCountsFromTable();
            tblPets.clearSelection();
            setEditButtonsEnabled(false);

        } catch (Exception ex) {
            showError("Unable to load pet records.", ex);
        }
    }

    private void refreshCustomerCombo(Set<Integer> customerIds) {
        String currentValue = getSelectedCustomerText();

        cmbCustomer.removeAllItems();
        cmbCustomer.addItem("Select owner");

        if (customerIds != null) {
            for (Integer customerId : customerIds) {
                if (customerId != null && customerId > 0) {
                    cmbCustomer.addItem(String.valueOf(customerId));
                }
            }
        }

        if (currentValue != null
                && !currentValue.trim().isEmpty()
                && !currentValue.equalsIgnoreCase("Select owner")) {
            setComboFromTable(cmbCustomer, currentValue);
        } else {
            cmbCustomer.setSelectedIndex(0);
        }
    }

    // =========================================================
    // SAVE PET
    // =========================================================

    private void savePet() {
        try {
            Pet pet = buildPetFromForm(false);

            boolean saved = petService.addPet(pet);

            if (saved) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pet registered successfully.",
                        "Save Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadPets();
                clearFields();
                tblPets.clearSelection();
                setEditButtonsEnabled(false);
                txtPetName.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "The pet could not be saved.",
                        "Save Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (IllegalArgumentException ex) {
            showValidationError(ex.getMessage());
        } catch (Exception ex) {
            showError("An error occurred while saving the pet.", ex);
        }
    }

    // =========================================================
    // UPDATE PET
    // =========================================================

    private void updatePet() {
        try {
            if (tblPets.getSelectedRow() < 0) {
                showValidationError(
                        "Please select a pet record from the table first."
                );
                return;
            }

            Pet pet = buildPetFromForm(true);

            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to update this pet record?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (answer != JOptionPane.YES_OPTION) {
                return;
            }

            boolean updated = petService.updatePet(pet);

            if (updated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pet record updated successfully.",
                        "Update Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadPets();
                clearFields();
                tblPets.clearSelection();
                setEditButtonsEnabled(false);
                txtPetName.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "The pet record could not be updated.",
                        "Update Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (IllegalArgumentException ex) {
            showValidationError(ex.getMessage());
        } catch (Exception ex) {
            showError("An error occurred while updating the pet.", ex);
        }
    }

    // =========================================================
    // DELETE PET
    // =========================================================

    private void deletePet() {
        try {
            int petId = parsePositiveInt(
                    txtPetId.getText().trim(),
                    "Invalid patient ID."
            );

            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this pet record?\n"
                            + "Pet ID: " + petId,
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (answer != JOptionPane.YES_OPTION) {
                return;
            }

            boolean deleted = petService.deletePet(petId);

            if (deleted) {
                JOptionPane.showMessageDialog(
                        this,
                        "Pet record deleted successfully.",
                        "Delete Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadPets();
                clearFields();
                tblPets.clearSelection();
                setEditButtonsEnabled(false);
                txtPetName.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "The pet record could not be deleted.",
                        "Delete Failed",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (IllegalArgumentException ex) {
            showValidationError(ex.getMessage());
        } catch (Exception ex) {
            showError("An error occurred while deleting the pet.", ex);
        }
    }

    // =========================================================
    // BUILD PET OBJECT FROM FORM
    // =========================================================

    private Pet buildPetFromForm(boolean updating) {
        int petId = 0;

        if (updating) {
            petId = parsePositiveInt(
                    txtPetId.getText().trim(),
                    "Invalid patient ID."
            );
        }

        int customerId = parsePositiveInt(
                getSelectedCustomerText(),
                "Please select or enter a valid Customer ID."
        );

        String petName = txtPetName.getText().trim();
        String species = getSelectedComboValue(cmbSpecies);
        String breed = txtBreed.getText().trim();
        String gender = getSelectedComboValue(cmbGender);
        String dateOfBirth = txtDateOfBirth.getText().trim();
        String notes = txtNotes.getText().trim();

        if (petName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Pet name is required."
            );
        }

        if (species.isEmpty() || species.equalsIgnoreCase("Select species")) {
            throw new IllegalArgumentException(
                    "Please select a species."
            );
        }

        if (!dateOfBirth.isEmpty()) {
            validateDateOfBirth(dateOfBirth);
        }

        double weight = parseWeight(txtWeight.getText().trim());

        return new Pet(
                petId,
                customerId,
                petName,
                species,
                breed,
                gender,
                dateOfBirth,
                weight,
                notes
        );
    }

    private String getSelectedCustomerText() {
        Object selected = cmbCustomer.getSelectedItem();

        if (selected == null) {
            return "";
        }

        return selected.toString().trim();
    }

    private String getSelectedComboValue(JComboBox<String> combo) {
        Object selected = combo.getSelectedItem();

        if (selected == null) {
            return "";
        }

        String value = selected.toString().trim();

        if (value.equalsIgnoreCase("Select")
                || value.equalsIgnoreCase("Select species")) {
            return "";
        }

        return value;
    }

    private double parseWeight(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }

        try {
            double weight = Double.parseDouble(text.trim());

            if (Double.isNaN(weight) || Double.isInfinite(weight)) {
                throw new NumberFormatException();
            }

            if (weight < 0) {
                throw new IllegalArgumentException(
                        "Weight cannot be negative."
                );
            }

            return weight;

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Weight must be a valid number. Example: 12.5"
            );
        }
    }

    private int parsePositiveInt(String text, String message) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        try {
            int value = Integer.parseInt(text.trim());

            if (value <= 0) {
                throw new NumberFormatException();
            }

            return value;

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(message);
        }
    }

    private void validateDateOfBirth(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Date of birth must use YYYY-MM-DD format."
            );
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    // =========================================================
    // TABLE COUNTS
    // =========================================================

    private void updateAnimalCountsFromTable() {
        int dogs = 0;
        int cats = 0;
        int rabbits = 0;
        int birds = 0;
        int other = 0;

        DefaultTableModel model =
                (DefaultTableModel) tblPets.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, 2);

            if (value == null) {
                continue;
            }

            switch (value.toString().trim().toLowerCase()) {
                case "dog" -> dogs++;
                case "cat" -> cats++;
                case "rabbit" -> rabbits++;
                case "bird" -> birds++;
                default -> other++;
            }
        }

        if (lblDogCount != null) {
            lblDogCount.setText(String.valueOf(dogs));
        }

        if (lblCatCount != null) {
            lblCatCount.setText(String.valueOf(cats));
        }

        if (lblRabbitCount != null) {
            lblRabbitCount.setText(String.valueOf(rabbits));
        }

        if (lblBirdCount != null) {
            lblBirdCount.setText(String.valueOf(birds));
        }

        if (lblOtherCount != null) {
            lblOtherCount.setText(String.valueOf(other));
        }
    }

    // =========================================================
    // FORM CLEAR
    // =========================================================

    private void clearFields() {
        txtPetId.setText("");
        txtPetName.setText("");
        txtBreed.setText("");
        txtDateOfBirth.setText("");
        txtWeight.setText("");
        txtNotes.setText("");

        cmbSpecies.setSelectedIndex(0);
        cmbGender.setSelectedIndex(0);
        cmbCustomer.setSelectedIndex(0);

        setEditButtonsEnabled(false);
    }

    private void setEditButtonsEnabled(boolean enabled) {
        if (btnUpdate != null) {
            btnUpdate.setEnabled(enabled);
        }

        if (btnDelete != null) {
            btnDelete.setEnabled(enabled);
        }
    }

    // =========================================================
    // ERROR / VALIDATION MESSAGES
    // =========================================================

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message == null || message.trim().isEmpty()
                        ? "Please check the entered information."
                        : message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showError(String message, Exception ex) {
        String detail = ex == null ? "" : ex.getMessage();

        String finalMessage = message;

        if (detail != null && !detail.trim().isEmpty()) {
            finalMessage += "\n\nDetails: " + detail;
        }

        JOptionPane.showMessageDialog(
                this,
                finalMessage,
                "System Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static class RoundedPanel extends JPanel {
        private final Color backgroundColor;
        private final int radius;

        RoundedPanel(Color backgroundColor, int radius) {
            this.backgroundColor = backgroundColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.setColor(BORDER);
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class PawLogoPanel extends JPanel {
        PawLogoPanel() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SAGE);
            g2.fill(new Ellipse2D.Double(16, 25, 25, 22));
            g2.fill(new Ellipse2D.Double(3, 15, 11, 13));
            g2.fill(new Ellipse2D.Double(13, 5, 11, 13));
            g2.fill(new Ellipse2D.Double(27, 5, 11, 13));
            g2.fill(new Ellipse2D.Double(38, 15, 11, 13));
            g2.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class AnimalIconPanel extends JPanel {
        private final String type;

        AnimalIconPanel(String type) {
            this.type = type;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(240, 244, 237));
            g2.fillOval(1, 1, 50, 50);
            g2.setColor(new Color(76, 101, 109));
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            if (type.equals("DOG")) {
                g2.drawOval(14, 15, 25, 24);
                g2.drawOval(8, 14, 10, 15);
                g2.drawOval(35, 14, 10, 15);
                g2.fillOval(25, 28, 5, 5);
            } else if (type.equals("CAT")) {
                Polygon leftEar = new Polygon();
                leftEar.addPoint(12, 17); leftEar.addPoint(17, 7); leftEar.addPoint(22, 17);
                Polygon rightEar = new Polygon();
                rightEar.addPoint(29, 17); rightEar.addPoint(34, 7); rightEar.addPoint(40, 17);
                g2.drawPolygon(leftEar); g2.drawPolygon(rightEar);
                g2.drawOval(14, 14, 25, 25);
                g2.fillOval(24, 27, 5, 4);
            } else if (type.equals("RABBIT")) {
                g2.drawOval(15, 18, 24, 24);
                g2.drawOval(17, 4, 8, 20);
                g2.drawOval(29, 4, 8, 20);
                g2.fillOval(25, 29, 5, 4);
            } else if (type.equals("BIRD")) {
                g2.drawOval(14, 15, 25, 25);
                Polygon beak = new Polygon();
                beak.addPoint(38, 25); beak.addPoint(47, 29); beak.addPoint(38, 32);
                g2.drawPolygon(beak);
                g2.drawArc(16, 20, 18, 16, 300, 240);
            } else {
                g2.drawOval(13, 13, 26, 26);
                g2.drawString("+", 22, 32);
            }

            g2.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class JTableHeaderHelper {
        static void style(JTable table, Color border) {
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 10));
            table.getTableHeader().setForeground(new Color(83, 97, 103));
            table.getTableHeader().setBackground(new Color(239, 240, 235));
            table.getTableHeader().setPreferredSize(new Dimension(0, 40));
            table.getTableHeader().setReorderingAllowed(false);
            table.getTableHeader().setResizingAllowed(true);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use Swing default look and feel if system LAF is unavailable.
            }
            new PetForm().setVisible(true);
        });
    }
}
