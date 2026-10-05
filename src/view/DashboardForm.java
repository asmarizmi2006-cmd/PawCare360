package view;


/**
 * DashboardForm: passive view (designer form).
 * Logic lives in the controller.
 */
public class DashboardForm extends javax.swing.JFrame {


    public DashboardForm() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    public DashboardForm(String username) {
        this();
    }

    // Stat card text
    public void setStat(int index, String text) {
        javax.swing.JLabel[] cards = {lblCardCustomer, lblCardPet, lblCardStaff, lblCardRevenue, lblCardOutstanding};
        cards[index].setText(text);
    }

    public void setUserInfo(String text) {
        lblUserInfo.setText(text);
    }

    public void setRecentCustomers(java.util.List<String> lines) {
        fill(tblRecentCustomers, lines);
    }

    public void setRecentPets(java.util.List<String> lines) {
        fill(tblRecentPets, lines);
    }

    public void setRecentAppointments(java.util.List<String> lines) {
        fill(tblRecentAppointments, lines);
    }

    public void setRecentStaff(java.util.List<String> lines) {
        fill(tblRecentStaff, lines);
    }

    public void setBarData(int[] data) {
        BarChartPanel bar = getBarChartPanel();
        if (bar != null)
        {
            bar.setData(data);
        }
    }

    public void setDonutData(java.util.Map<String, Integer> data) {
        DonutChartPanel donut = getDonutChartPanel();
        if (donut != null)
        {
            donut.setData(data);
        }
    }

    // List rows
    private void fill(javax.swing.JTable table, java.util.List<String> lines) {
        Object title = table.getColumnModel().getColumn(0).getHeaderValue();
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(new Object[]{title}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String line : lines) {
            model.addRow(new Object[]{line});
        }
        table.setModel(model);
    }

    public view.SidebarPanel getSidebarPanel1() {
        return BeanFinder.find(getContentPane(), SidebarPanel.class);
    }

    public view.BarChartPanel getBarChartPanel() {
        return BeanFinder.find(getContentPane(), BarChartPanel.class);
    }

    public view.DonutChartPanel getDonutChartPanel() {
        return BeanFinder.find(getContentPane(), DonutChartPanel.class);
    }

    public javax.swing.JButton getBtnNewAppointment() {
        return btnNewAppointment;
    }

    public javax.swing.JButton getBtnRegisterCustomer() {
        return btnRegisterCustomer;
    }

    public javax.swing.JButton getBtnAddPet() {
        return btnAddPet;
    }

    public javax.swing.JButton getBtnRecordPayment() {
        return btnRecordPayment;
    }

    public javax.swing.JTable getTblRecentCustomers() {
        return tblRecentCustomers;
    }

    public javax.swing.JTable getTblRecentPets() {
        return tblRecentPets;
    }

    public javax.swing.JTable getTblRecentAppointments() {
        return tblRecentAppointments;
    }

    public javax.swing.JTable getTblRecentStaff() {
        return tblRecentStaff;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDashboardContent = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        lblUserInfo = new javax.swing.JLabel();
        pnlCardCustomer = new javax.swing.JPanel();
        pnlAccentCustomer = new javax.swing.JPanel();
        lblCardTitleCustomer = new javax.swing.JLabel();
        lblCardCustomer = new javax.swing.JLabel();
        lblCardSubCustomer = new javax.swing.JLabel();
        pnlCardPet = new javax.swing.JPanel();
        pnlAccentPet = new javax.swing.JPanel();
        lblCardTitlePet = new javax.swing.JLabel();
        lblCardPet = new javax.swing.JLabel();
        lblCardSubPet = new javax.swing.JLabel();
        pnlCardStaff = new javax.swing.JPanel();
        pnlAccentStaff = new javax.swing.JPanel();
        lblCardTitleStaff = new javax.swing.JLabel();
        lblCardStaff = new javax.swing.JLabel();
        lblCardSubStaff = new javax.swing.JLabel();
        pnlCardRevenue = new javax.swing.JPanel();
        pnlAccentRevenue = new javax.swing.JPanel();
        lblCardTitleRevenue = new javax.swing.JLabel();
        lblCardRevenue = new javax.swing.JLabel();
        lblCardSubRevenue = new javax.swing.JLabel();
        pnlCardOutstanding = new javax.swing.JPanel();
        pnlAccentOutstanding = new javax.swing.JPanel();
        lblCardTitleOutstanding = new javax.swing.JLabel();
        lblCardOutstanding = new javax.swing.JLabel();
        lblCardSubOutstanding = new javax.swing.JLabel();
        lblBarTitle = new javax.swing.JLabel();
        lblPieTitle = new javax.swing.JLabel();
        lblQuickActions = new javax.swing.JLabel();
        btnNewAppointment = new javax.swing.JButton();
        btnRegisterCustomer = new javax.swing.JButton();
        btnAddPet = new javax.swing.JButton();
        btnRecordPayment = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRecentCustomers = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblRecentPets = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRecentAppointments = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblRecentStaff = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare 360 - Dashboard");
        setMinimumSize(new java.awt.Dimension(1300, 800));
        setPreferredSize(new java.awt.Dimension(1500, 900));
        setSize(new java.awt.Dimension(1500, 900));

        pnlDashboardContent.setBackground(new java.awt.Color(247, 248, 250));
        pnlDashboardContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblWelcome.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(44, 62, 80));
        lblWelcome.setText("Dashboard");
        pnlDashboardContent.add(lblWelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 15, 500, 38));

        lblUserInfo.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblUserInfo.setForeground(new java.awt.Color(130, 140, 150));
        lblUserInfo.setText("Welcome back");
        pnlDashboardContent.add(lblUserInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 53, 700, 20));

        pnlCardCustomer.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardCustomer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)));
        pnlCardCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAccentCustomer.setBackground(new java.awt.Color(90, 140, 220));
        pnlAccentCustomer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlCardCustomer.add(pnlAccentCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 34, 4));

        lblCardTitleCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCardTitleCustomer.setForeground(new java.awt.Color(44, 62, 80));
        lblCardTitleCustomer.setText("Customers");
        pnlCardCustomer.add(lblCardTitleCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 27, 190, 18));

        lblCardCustomer.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCardCustomer.setForeground(new java.awt.Color(44, 62, 80));
        lblCardCustomer.setText("0");
        pnlCardCustomer.add(lblCardCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 50, 190, 36));

        lblCardSubCustomer.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblCardSubCustomer.setForeground(new java.awt.Color(130, 140, 150));
        lblCardSubCustomer.setText("Registered customers");
        pnlCardCustomer.add(lblCardSubCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 88, 190, 16));

        pnlDashboardContent.add(pnlCardCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 85, 220, 115));

        pnlCardPet.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardPet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)));
        pnlCardPet.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAccentPet.setBackground(new java.awt.Color(95, 185, 130));
        pnlAccentPet.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlCardPet.add(pnlAccentPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 34, 4));

        lblCardTitlePet.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCardTitlePet.setForeground(new java.awt.Color(44, 62, 80));
        lblCardTitlePet.setText("Pets");
        pnlCardPet.add(lblCardTitlePet, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 27, 190, 18));

        lblCardPet.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCardPet.setForeground(new java.awt.Color(44, 62, 80));
        lblCardPet.setText("0");
        pnlCardPet.add(lblCardPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 50, 190, 36));

        lblCardSubPet.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblCardSubPet.setForeground(new java.awt.Color(130, 140, 150));
        lblCardSubPet.setText("Registered pets");
        pnlCardPet.add(lblCardSubPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 88, 190, 16));

        pnlDashboardContent.add(pnlCardPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 85, 220, 115));

        pnlCardStaff.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardStaff.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)));
        pnlCardStaff.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAccentStaff.setBackground(new java.awt.Color(230, 160, 70));
        pnlAccentStaff.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlCardStaff.add(pnlAccentStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 34, 4));

        lblCardTitleStaff.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCardTitleStaff.setForeground(new java.awt.Color(44, 62, 80));
        lblCardTitleStaff.setText("Staff");
        pnlCardStaff.add(lblCardTitleStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 27, 190, 18));

        lblCardStaff.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCardStaff.setForeground(new java.awt.Color(44, 62, 80));
        lblCardStaff.setText("0");
        pnlCardStaff.add(lblCardStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 50, 190, 36));

        lblCardSubStaff.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblCardSubStaff.setForeground(new java.awt.Color(130, 140, 150));
        lblCardSubStaff.setText("Clinic staff members");
        pnlCardStaff.add(lblCardSubStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 88, 190, 16));

        pnlDashboardContent.add(pnlCardStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 85, 220, 115));

        pnlCardRevenue.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardRevenue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)));
        pnlCardRevenue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAccentRevenue.setBackground(new java.awt.Color(150, 110, 210));
        pnlAccentRevenue.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlCardRevenue.add(pnlAccentRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 34, 4));

        lblCardTitleRevenue.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCardTitleRevenue.setForeground(new java.awt.Color(44, 62, 80));
        lblCardTitleRevenue.setText("Revenue (LKR)");
        pnlCardRevenue.add(lblCardTitleRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 27, 190, 18));

        lblCardRevenue.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCardRevenue.setForeground(new java.awt.Color(44, 62, 80));
        lblCardRevenue.setText("0");
        pnlCardRevenue.add(lblCardRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 50, 190, 36));

        lblCardSubRevenue.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblCardSubRevenue.setForeground(new java.awt.Color(130, 140, 150));
        lblCardSubRevenue.setText("Payments collected");
        pnlCardRevenue.add(lblCardSubRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 88, 190, 16));

        pnlDashboardContent.add(pnlCardRevenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 85, 220, 115));

        pnlCardOutstanding.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardOutstanding.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(225, 225, 225)));
        pnlCardOutstanding.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlAccentOutstanding.setBackground(new java.awt.Color(60, 175, 170));
        pnlAccentOutstanding.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        pnlCardOutstanding.add(pnlAccentOutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 13, 34, 4));

        lblCardTitleOutstanding.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCardTitleOutstanding.setForeground(new java.awt.Color(44, 62, 80));
        lblCardTitleOutstanding.setText("Outstanding (LKR)");
        pnlCardOutstanding.add(lblCardTitleOutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 27, 190, 18));

        lblCardOutstanding.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblCardOutstanding.setForeground(new java.awt.Color(44, 62, 80));
        lblCardOutstanding.setText("0");
        pnlCardOutstanding.add(lblCardOutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 50, 190, 36));

        lblCardSubOutstanding.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblCardSubOutstanding.setForeground(new java.awt.Color(130, 140, 150));
        lblCardSubOutstanding.setText("Unpaid balances");
        pnlCardOutstanding.add(lblCardSubOutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 88, 190, 16));

        pnlDashboardContent.add(pnlCardOutstanding, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 85, 220, 115));

        lblBarTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBarTitle.setForeground(new java.awt.Color(44, 62, 80));
        lblBarTitle.setText("Appointments · Last 7 Days");
        pnlDashboardContent.add(lblBarTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 215, 400, 22));

        lblPieTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPieTitle.setForeground(new java.awt.Color(44, 62, 80));
        lblPieTitle.setText("Pets by Species");
        pnlDashboardContent.add(lblPieTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 215, 400, 22));

        lblQuickActions.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblQuickActions.setForeground(new java.awt.Color(130, 140, 150));
        lblQuickActions.setText("QUICK ACTIONS");
        pnlDashboardContent.add(lblQuickActions, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 300, 20));

        btnNewAppointment.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNewAppointment.setForeground(new java.awt.Color(44, 62, 80));
        btnNewAppointment.setBackground(new java.awt.Color(157, 201, 163));
        btnNewAppointment.setText("New Appointment");
        pnlDashboardContent.add(btnNewAppointment, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 445, 290, 50));

        btnRegisterCustomer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRegisterCustomer.setForeground(new java.awt.Color(44, 62, 80));
        btnRegisterCustomer.setBackground(new java.awt.Color(157, 201, 163));
        btnRegisterCustomer.setText("Register Customer");
        pnlDashboardContent.add(btnRegisterCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 445, 290, 50));

        btnAddPet.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAddPet.setForeground(new java.awt.Color(44, 62, 80));
        btnAddPet.setBackground(new java.awt.Color(157, 201, 163));
        btnAddPet.setText("Add Pet");
        pnlDashboardContent.add(btnAddPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 445, 290, 50));

        btnRecordPayment.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRecordPayment.setForeground(new java.awt.Color(44, 62, 80));
        btnRecordPayment.setBackground(new java.awt.Color(157, 201, 163));
        btnRecordPayment.setText("Record Payment");
        pnlDashboardContent.add(btnRecordPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 445, 290, 50));

        tblRecentCustomers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Recent Customers"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblRecentCustomers);

        pnlDashboardContent.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 288, 200));

        tblRecentPets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Recent Pet Patients"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblRecentPets);

        pnlDashboardContent.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 510, 288, 200));

        tblRecentAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Recent Appointments"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblRecentAppointments);

        pnlDashboardContent.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 510, 288, 200));

        tblRecentStaff.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Recent Staff"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblRecentStaff);

        pnlDashboardContent.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(944, 510, 288, 200));

        getContentPane().add(pnlDashboardContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddPet;
    private javax.swing.JButton btnNewAppointment;
    private javax.swing.JButton btnRecordPayment;
    private javax.swing.JButton btnRegisterCustomer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblBarTitle;
    private javax.swing.JLabel lblCardCustomer;
    private javax.swing.JLabel lblCardOutstanding;
    private javax.swing.JLabel lblCardPet;
    private javax.swing.JLabel lblCardRevenue;
    private javax.swing.JLabel lblCardStaff;
    private javax.swing.JLabel lblCardSubCustomer;
    private javax.swing.JLabel lblCardSubOutstanding;
    private javax.swing.JLabel lblCardSubPet;
    private javax.swing.JLabel lblCardSubRevenue;
    private javax.swing.JLabel lblCardSubStaff;
    private javax.swing.JLabel lblCardTitleCustomer;
    private javax.swing.JLabel lblCardTitleOutstanding;
    private javax.swing.JLabel lblCardTitlePet;
    private javax.swing.JLabel lblCardTitleRevenue;
    private javax.swing.JLabel lblCardTitleStaff;
    private javax.swing.JLabel lblPieTitle;
    private javax.swing.JLabel lblQuickActions;
    private javax.swing.JLabel lblUserInfo;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnlAccentCustomer;
    private javax.swing.JPanel pnlAccentOutstanding;
    private javax.swing.JPanel pnlAccentPet;
    private javax.swing.JPanel pnlAccentRevenue;
    private javax.swing.JPanel pnlAccentStaff;
    private javax.swing.JPanel pnlCardCustomer;
    private javax.swing.JPanel pnlCardOutstanding;
    private javax.swing.JPanel pnlCardPet;
    private javax.swing.JPanel pnlCardRevenue;
    private javax.swing.JPanel pnlCardStaff;
    private javax.swing.JPanel pnlDashboardContent;
    private javax.swing.JTable tblRecentAppointments;
    private javax.swing.JTable tblRecentCustomers;
    private javax.swing.JTable tblRecentPets;
    private javax.swing.JTable tblRecentStaff;
    // End of variables declaration//GEN-END:variables
}
