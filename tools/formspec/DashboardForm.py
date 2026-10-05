import sys; sys.path.insert(0, 'tools/formgen')
from formgen import *

NAVY = (44, 62, 80); GREY = (130, 140, 150); IVORY = (247, 248, 250); SAGE = (157, 201, 163)
LINE = (225, 225, 225)
ACC = [(90, 140, 220), (95, 185, 130), (230, 160, 70), (150, 110, 210), (60, 175, 170)]
titles = ['Customers', 'Pets', 'Staff', 'Revenue (LKR)', 'Outstanding (LKR)']
subs = ['Registered customers', 'Registered pets', 'Clinic staff members', 'Payments collected', 'Unpaid balances']
names = ['Customer', 'Pet', 'Staff', 'Revenue', 'Outstanding']
xs = [20, 260, 500, 740, 980]

cards = []
for i in range(5):
    cards.append(P('pnlCard%s' % names[i], x=xs[i], y=85, w=220, h=115, bg=(255, 255, 255), border=('line', LINE), children=[
        P('pnlAccent%s' % names[i], x=18, y=13, w=34, h=4, bg=ACC[i]),
        L('lblCardTitle%s' % names[i], titles[i], x=18, y=27, w=190, h=18, font=('Segoe UI', 1, 12), fg=NAVY),
        L('lblCard%s' % names[i], '0', x=18, y=50, w=190, h=36, font=('Segoe UI', 1, 24), fg=NAVY),
        L('lblCardSub%s' % names[i], subs[i], x=18, y=88, w=190, h=16, font=('Segoe UI', 0, 10), fg=GREY),
    ]))

lists = []
lx = [20, 328, 636, 944]
for i, (nm, t) in enumerate([('RecentCustomers', 'Recent Customers'), ('RecentPets', 'Recent Pet Patients'),
                              ('RecentAppointments', 'Recent Appointments'), ('RecentStaff', 'Recent Staff')]):
    lists.append(TB('tbl' + nm, [t], x=lx[i], y=510, w=288, h=200, font=('Segoe UI', 0, 12)))

acts = []
for i, (nm, t) in enumerate([('NewAppointment', 'New Appointment'), ('RegisterCustomer', 'Register Customer'),
                              ('AddPet', 'Add Pet'), ('RecordPayment', 'Record Payment')]):
    acts.append(B('btn' + nm, t, x=[20, 330, 640, 950][i], y=445, w=290, h=50, bg=SAGE, fg=NAVY, font=('Segoe UI', 1, 12)))

f = Form('DashboardForm', title='PawCare 360 - Dashboard', size=(1500, 900), minsize=(1300, 800), children=[
    BEAN('sidebarPanel1', 'view.SidebarPanel', pos='West', minsize=(250, 900)),
    P('pnlDashboardContent', pos='Center', layout='abs', bg=IVORY, children=[
        L('lblWelcome', 'Dashboard', x=30, y=15, w=500, h=38, font=('Segoe UI', 1, 26), fg=NAVY),
        L('lblUserInfo', 'Welcome back', x=30, y=53, w=700, h=20, font=('Segoe UI', 0, 13), fg=GREY),
        *cards,
        L('lblBarTitle', 'Appointments · Last 7 Days', x=20, y=215, w=400, h=22, font=('Segoe UI', 1, 14), fg=NAVY),
        L('lblPieTitle', 'Pets by Species', x=630, y=215, w=400, h=22, font=('Segoe UI', 1, 14), fg=NAVY),
        BEAN('barChartPanel', 'view.BarChartPanel', x=20, y=240, w=590, h=160),
        BEAN('donutChartPanel', 'view.DonutChartPanel', x=630, y=240, w=590, h=160),
        L('lblQuickActions', 'QUICK ACTIONS', x=20, y=420, w=300, h=20, font=('Segoe UI', 1, 11), fg=GREY),
        *acts,
        *lists,
    ])])

extra = '''    public DashboardForm(String username) {
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
        barChartPanel.setData(data);
    }

    public void setDonutData(java.util.Map<String, Integer> data) {
        donutChartPanel.setData(data);
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

'''
write(f, 'src/view/DashboardForm',
      ctor_body='        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);\n        setLocationRelativeTo(null);\n',
      members=extra + getters(f, include=('JButton', 'JTable', 'BEAN')))
