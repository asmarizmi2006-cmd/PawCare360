import sys
sys.path.insert(0, 'tools/formgen')
from formgen import *

BG = (250, 246, 240); LINE = (217, 217, 217); TXT = (74, 91, 106); MUT = (113, 128, 140)
GREEN = (157, 201, 163); BLUE = (190, 210, 230); WHITE = (255, 255, 255)
SEG = 'Segoe UI'
BODY = (SEG, 0, 13)
BTN = (SEG, 1, 12)


def card(pn, x, title_n, title, desc_n, desc, pdf_n, prev_n, extra=()):
    return P(pn, x=x, y=150, w=380, h=270, bg=WHITE, border=('line', LINE), children=[
        L(title_n, title, x=18, y=16, w=344, h=28, font=(SEG, 1, 18), fg=TXT),
        L(desc_n, '<html>' + desc + '</html>', x=18, y=52, w=344, h=90, font=BODY, fg=MUT),
        *extra,
        B(pdf_n, 'Export PDF', x=18, y=210, w=160, h=38, bg=BLUE, fg=TXT, font=BTN),
        B(prev_n, 'Preview', x=202, y=210, w=160, h=38, bg=GREEN, fg=TXT, font=BTN)])


f = Form('ReportsForm', title='PawCare 360 - Reports', size=(1700, 900), minsize=(1280, 700), children=[
    BEAN('sidebarPanel1', 'view.SidebarPanel', pos='West'),
    P('pnlContent', pos='Center', layout='abs', bg=BG, children=[
        L('lblTitle', 'Reports', x=20, y=14, w=400, h=34, font=(SEG, 1, 24), fg=TXT),
        L('lblSubtitle', 'Management reports with charts. Preview on screen or export to PDF.', x=20, y=48, w=700, h=20, font=BODY, fg=MUT),
        P('pnlRange', x=20, y=84, w=440, h=46, bg=WHITE, border=('line', LINE), children=[
            L('capFrom', 'From', x=14, y=13, w=40, h=20, font=BTN, fg=TXT),
            SPN('spnFrom', x=56, y=8, w=130, h=30, font=BODY),
            L('capTo', 'To', x=212, y=13, w=30, h=20, font=BTN, fg=TXT),
            SPN('spnTo', x=244, y=8, w=130, h=30, font=BODY)]),
        card('pnlRevenue', 20, 'capRevenueTitle', 'Revenue & Collections', 'capRevenueDesc',
             'Invoices, payments and balances per customer, with a billed vs collected chart. Uses invoices, customers, pets and payments.',
             'btnRevenuePdf', 'btnRevenuePreview'),
        card('pnlServices', 420, 'capServicesTitle', 'Service Performance', 'capServicesDesc',
             'Appointments grouped by service with totals and category / status charts. Uses appointments, services, staff, customers and pets.',
             'btnServicesPdf', 'btnServicesPreview'),
        card('pnlInvoice', 820, 'capInvoiceTitle', 'Customer Invoice', 'capInvoiceDesc',
             'Printable invoice with line items and payments. Pick an invoice below.',
             'btnInvoicePdf', 'btnInvoicePreview',
             extra=[CB('cmbInvoice', ['Item 1'], x=18, y=150, w=344, h=30, font=BODY)]),
        L('lblMessage', ' ', x=20, y=440, w=1300, h=22, font=BODY, fg=MUT),
    ])])

EXTRA = r'''
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
'''
CTOR = '''        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); // Full screen
        setLocationRelativeTo(null);
'''


def my_getters(form):
    out = []
    for n in form.all_nodes():
        if n.kind in ('JButton', 'JComboBox', 'JSpinner') or (n.kind == 'JLabel' and n.name.startswith('lbl')):
            t = 'javax.swing.JComboBox<String>' if n.kind == 'JComboBox' else n.classname()
            out.append(f'    public {t} get{n.name[0].upper()+n.name[1:]}() {{\n        return {n.name};\n    }}\n')
    return '\n'.join(out)


if __name__ == '__main__':
    write(f, 'src/view/ReportsForm', ctor_body=CTOR, members=my_getters(f) + EXTRA)
