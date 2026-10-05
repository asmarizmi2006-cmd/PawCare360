# formgen - make NetBeans designer forms from a spec
`python3 tools/formgen/formgen.py` is a library. Write a spec script (see tools/formspec/*.py):

    import sys; sys.path.insert(0,'tools/formgen')
    from formgen import *
    f = Form('XForm', title='..', size=(1500,900), minsize=(1100,700), children=[
        BEAN('sidebarPanel1','view.SidebarPanel',pos='West'),
        P('pnlContent', pos='Center', layout='abs', bg=(250,246,240), children=[
            L('lblTitle','Text', x=20,y=15,w=300,h=35, font=('Segoe UI',1,24), fg=(74,91,106)),
            B('btnAdd','Add', x=20,y=60,w=120,h=36, bg=(157,201,163), fg=(74,91,106), font=('Segoe UI',1,12)),
            T('txtName', x=..,y=..,w=..,h=..), PW(..), CB('cmbX',['A','B'],x=..), TB('tblX',['ID','Name'],x=..),
            TA('txtNotes',..., bools={'lineWrap':True}), SPN(..), CK('chk','text',..),
            P('pnlCard', x,y,w,h, bg=..., border=('line',(217,217,217)), children=[...]),
        ])])
    write(f, 'src/view/XForm', ctor_body='        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);\n',
          members=getters(f) + EXTRA_JAVA)

It writes BOTH src/view/XForm.form and src/view/XForm.java (GEN-guarded initComponents + variables).
Parent layouts: 'abs' (x,y,w,h) or 'border' (pos='North|South|East|West|Center'). Root is border layout.
Tables / text areas are auto-wrapped in JScrollPane (constraints are of the table).
Kinds: JLabel, JButton, JTextField, JPasswordField, JComboBox, JTable, JTextArea, JSpinner, JCheckBox, JPanel, BEAN(custom class, needs public no-arg ctor).
Generic props: bools={'opaque':False,'editable':False,'lineWrap':True}, ints={'rows':3}, align=0|2|4, border=('line',(r,g,b)) or ('empty',(t,l,b,r)), size=(w,h) preferred, minsize.
Dialogs: Form('XDialog', kind='JDialog', close=2) -> ctor (java.awt.Frame parent, boolean modal).
NEVER hand-edit the generated GEN blocks: change the spec and regenerate (keeps .form and .java in sync).
