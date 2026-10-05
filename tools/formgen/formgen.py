"""NetBeans GUI-builder generator: one component tree -> .form XML + GEN java.
Usage: see README at bottom / FORMGEN.md. Keeps .form and initComponents in sync."""
import re
from xml.sax.saxutils import escape as _xesc

ABS = 'org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout'
BRD = 'org.netbeans.modules.form.compat2.layouts.DesignBorderLayout'
DIMED = 'org.netbeans.beaninfo.editors.DimensionEditor'
JNAME = {'JPanel': 'javax.swing.JPanel', 'JLabel': 'javax.swing.JLabel', 'JButton': 'javax.swing.JButton',
         'JTextField': 'javax.swing.JTextField', 'JPasswordField': 'javax.swing.JPasswordField',
         'JComboBox': 'javax.swing.JComboBox', 'JTable': 'javax.swing.JTable', 'JTextArea': 'javax.swing.JTextArea',
         'JScrollPane': 'javax.swing.JScrollPane', 'JSpinner': 'javax.swing.JSpinner',
         'JCheckBox': 'javax.swing.JCheckBox', 'JSeparator': 'javax.swing.JSeparator'}


def xesc(s):
    return _xesc(str(s), {'"': '&quot;'})


def jesc(s):
    return str(s).replace('\\', '\\\\').replace('"', '\\"').replace('\n', '\\n')


class Node:
    def __init__(self, kind, name, x=0, y=0, w=0, h=0, pos=None, text=None, font=None, fg=None, bg=None,
                 border=None, tooltip=None, align=None, bools=None, ints=None, size=None, minsize=None,
                 items=None, columns=None, children=None, layout='abs', cls=None, scroll_bg=None, handles_scroll=False):
        self.kind = kind; self.name = name
        self.x, self.y, self.w, self.h, self.pos = x, y, w, h, pos
        self.text = text; self.font = font; self.fg = fg; self.bg = bg; self.border = border
        self.tooltip = tooltip; self.align = align; self.bools = bools or {}; self.ints = ints or {}
        self.size = size; self.minsize = minsize
        self.items = items; self.columns = columns
        self.children = children or []; self.layout = layout; self.cls = cls
        self.scroll = None  # scrollpane name when wrapped

    def classname(self):
        return self.cls or JNAME[self.kind]


# ---- convenience constructors -------------------------------------------------
def P(name, children=(), **kw): return Node('JPanel', name, children=list(children), **kw)
def L(name, text, **kw): return Node('JLabel', name, text=text, **kw)
def B(name, text, **kw): return Node('JButton', name, text=text, **kw)
def T(name, **kw): return Node('JTextField', name, **kw)
def PW(name, **kw): return Node('JPasswordField', name, **kw)
def CB(name, items=('Item 1',), **kw): return Node('JComboBox', name, items=list(items), **kw)
def TB(name, columns, **kw): return Node('JTable', name, columns=list(columns), **kw)
def TA(name, **kw): return Node('JTextArea', name, **kw)
def SPN(name, **kw): return Node('JSpinner', name, **kw)
def CK(name, text, **kw): return Node('JCheckBox', name, text=text, **kw)
def BEAN(name, cls, **kw): return Node('BEAN', name, cls=cls, **kw)


class Form:
    def __init__(self, class_name, title='', kind='JFrame', size=(1500, 900), minsize=None, close=3,
                 children=(), package='view'):
        self.class_name = class_name; self.title = title; self.kind = kind
        self.size = size; self.minsize = minsize; self.close = close
        self.children = list(children); self.package = package
        self._n = 0
        self._prep()

    # wrap table/textarea in scroll panes, assign scroll names
    def _prep(self):
        def walk(node):
            new = []
            for c in node.children:
                if c.kind in ('JTable', 'JTextArea'):
                    self._n += 1
                    c.scroll = 'jScrollPane%d' % self._n
                new.append(c)
                walk(c)
            node.children = new
        root = Node('ROOT', 'root', children=self.children, layout='border')
        walk(root)
        self.root = root

    def all_nodes(self):
        out = []
        def walk(n, parent_layout):
            for c in n.children:
                out.append(c)
                walk(c, c.layout)
        walk(self.root, 'border')
        return out

    # ------------------------------------------------------------------ XML
    def _constraint_xml(self, n, parent_layout, ind):
        sp = ' ' * ind
        if parent_layout == 'border':
            d = {'West': 'West', 'Center': 'Center', 'North': 'North', 'South': 'South', 'East': 'East'}[n.pos or 'Center']
            return (f'{sp}<Constraints>\n{sp}  <Constraint layoutClass="{BRD}" value="{BRD}$BorderConstraintsDescription">\n'
                    f'{sp}    <BorderConstraints direction="{d}"/>\n{sp}  </Constraint>\n{sp}</Constraints>\n')
        return (f'{sp}<Constraints>\n{sp}  <Constraint layoutClass="{ABS}" value="{ABS}$AbsoluteConstraintsDescription">\n'
                f'{sp}    <AbsoluteConstraints x="{n.x}" y="{n.y}" width="{n.w}" height="{n.h}"/>\n{sp}  </Constraint>\n{sp}</Constraints>\n')

    @staticmethod
    def _color_xml(prop, c, ind):
        sp = ' ' * ind
        r, g, b = c
        return (f'{sp}<Property name="{prop}" type="java.awt.Color" editor="org.netbeans.beaninfo.editors.ColorEditor">\n'
                f'{sp}  <Color blue="{b:02x}" green="{g:02x}" red="{r:02x}" type="rgb"/>\n{sp}</Property>\n')

    def _props_xml(self, n, ind):
        sp = ' ' * ind
        s = ''
        if n.font:
            f = n.font
            s += (f'{sp}<Property name="font" type="java.awt.Font" editor="org.netbeans.beaninfo.editors.FontEditor">\n'
                  f'{sp}  <Font name="{f[0]}" size="{f[2]}" style="{f[1]}"/>\n{sp}</Property>\n')
        if n.fg: s += self._color_xml('foreground', n.fg, ind)
        if n.bg: s += self._color_xml('background', n.bg, ind)
        for k, v in n.bools.items():
            s += f'{sp}<Property name="{k}" type="boolean" value="{"true" if v else "false"}"/>\n'
        for k, v in n.ints.items():
            s += f'{sp}<Property name="{k}" type="int" value="{v}"/>\n'
        if n.align is not None:
            s += f'{sp}<Property name="horizontalAlignment" type="int" value="{n.align}"/>\n'
        if n.border:
            kind = n.border[0]
            if kind == 'line':
                r, g, b = n.border[1]
                s += (f'{sp}<Property name="border" type="javax.swing.border.Border" editor="org.netbeans.modules.form.editors2.BorderEditor">\n'
                      f'{sp}  <Border info="org.netbeans.modules.form.compat2.border.LineBorderInfo">\n'
                      f'{sp}    <LineBorder>\n{sp}      <Color PropertyName="color" blue="{b:02x}" green="{g:02x}" red="{r:02x}" type="rgb"/>\n'
                      f'{sp}    </LineBorder>\n{sp}  </Border>\n{sp}</Property>\n')
            elif kind == 'empty':
                t, l, bt, rt = n.border[1]
                s += (f'{sp}<Property name="border" type="javax.swing.border.Border" editor="org.netbeans.modules.form.editors2.BorderEditor">\n'
                      f'{sp}  <Border info="org.netbeans.modules.form.compat2.border.EmptyBorderInfo">\n'
                      f'{sp}    <EmptyBorder bottom="{bt}" left="{l}" right="{rt}" top="{t}"/>\n{sp}  </Border>\n{sp}</Property>\n')
        for nm, dim in (('minimumSize', n.minsize), ('preferredSize', n.size)):
            if dim:
                s += (f'{sp}<Property name="{nm}" type="java.awt.Dimension" editor="{DIMED}">\n'
                      f'{sp}  <Dimension value="[{dim[0]}, {dim[1]}]"/>\n{sp}</Property>\n')
        if n.kind == 'JComboBox':
            its = n.items
            s += (f'{sp}<Property name="model" type="javax.swing.ComboBoxModel" editor="org.netbeans.modules.form.editors2.ComboBoxModelEditor">\n'
                  f'{sp}  <StringArray count="{len(its)}">\n')
            for i, it in enumerate(its):
                s += f'{sp}    <StringItem index="{i}" value="{xesc(it)}"/>\n'
            s += f'{sp}  </StringArray>\n{sp}</Property>\n'
        if n.kind == 'JTable':
            cols = n.columns
            s += (f'{sp}<Property name="model" type="javax.swing.table.TableModel" editor="org.netbeans.modules.form.editors2.TableModelEditor">\n'
                  f'{sp}  <Table columnCount="{len(cols)}" rowCount="0">\n')
            for c in cols:
                s += f'{sp}    <Column editable="false" title="{xesc(c)}" type="java.lang.Object"/>\n'
            s += f'{sp}  </Table>\n{sp}</Property>\n'
        if n.text is not None and n.kind in ('JLabel', 'JButton', 'JTextField', 'JPasswordField', 'JCheckBox', 'JTextArea'):
            s += f'{sp}<Property name="text" type="java.lang.String" value="{xesc(n.text)}"/>\n'
        if n.tooltip:
            s += f'{sp}<Property name="toolTipText" type="java.lang.String" value="{xesc(n.tooltip)}"/>\n'
        return s

    def _node_xml(self, n, parent_layout, ind):
        sp = ' ' * ind
        if n.scroll:
            # scroll pane wrapper
            inner = self._leaf_xml(n, 'scroll', ind + 4)
            return (f'{sp}<Container class="javax.swing.JScrollPane" name="{n.scroll}">\n'
                    f'{sp}  <AuxValues>\n{sp}    <AuxValue name="autoScrollPane" type="java.lang.Boolean" value="true"/>\n{sp}  </AuxValues>\n'
                    + self._constraint_xml(n, parent_layout, ind + 2) +
                    f'\n{sp}  <Layout class="org.netbeans.modules.form.compat2.layouts.support.JScrollPaneSupportLayout"/>\n'
                    f'{sp}  <SubComponents>\n{inner}{sp}  </SubComponents>\n{sp}</Container>\n')
        if n.kind == 'JPanel':
            props = self._props_xml(n, ind + 4)
            s = f'{sp}<Container class="javax.swing.JPanel" name="{n.name}">\n'
            if props: s += f'{sp}  <Properties>\n{props}{sp}  </Properties>\n'
            s += self._constraint_xml(n, parent_layout, ind + 2)
            if n.layout == 'border':
                s += f'\n{sp}  <Layout class="{BRD}"/>\n'
            else:
                s += (f'\n{sp}  <Layout class="{ABS}">\n{sp}    <Property name="useNullLayout" type="boolean" value="false"/>\n{sp}  </Layout>\n')
            s += f'{sp}  <SubComponents>\n'
            for c in n.children:
                s += self._node_xml(c, n.layout, ind + 4)
            s += f'{sp}  </SubComponents>\n{sp}</Container>\n'
            return s
        return self._leaf_xml(n, parent_layout, ind)

    def _leaf_xml(self, n, parent_layout, ind):
        sp = ' ' * ind
        props = self._props_xml(n, ind + 4)
        s = f'{sp}<Component class="{n.classname()}" name="{n.name}">\n'
        if props: s += f'{sp}  <Properties>\n{props}{sp}  </Properties>\n'
        if n.kind == 'JComboBox':
            s += (f'{sp}  <AuxValues>\n{sp}    <AuxValue name="JavaCodeGenerator_TypeParameters" type="java.lang.String" value="&lt;String&gt;"/>\n{sp}  </AuxValues>\n')
        if parent_layout != 'scroll':
            s += self._constraint_xml(n, parent_layout, ind + 2)
        s += f'{sp}</Component>\n'
        return s

    def form_xml(self):
        info = 'JFrameFormInfo' if self.kind == 'JFrame' else 'JDialogFormInfo'
        w, h = self.size
        s = ('<?xml version="1.0" encoding="UTF-8" ?>\n\n'
             f'<Form version="1.5" maxVersion="1.9" type="org.netbeans.modules.form.forminfo.{info}">\n  <Properties>\n')
        s += f'    <Property name="defaultCloseOperation" type="int" value="{self.close}"/>\n'
        if self.title:
            s += f'    <Property name="title" type="java.lang.String" value="{xesc(self.title)}"/>\n'
        for nm, dim in (('minimumSize', self.minsize), ('preferredSize', self.size), ('size', self.size)):
            if dim:
                s += (f'    <Property name="{nm}" type="java.awt.Dimension" editor="{DIMED}">\n'
                      f'      <Dimension value="[{dim[0]}, {dim[1]}]"/>\n    </Property>\n')
        s += ('  </Properties>\n  <SyntheticProperties>\n    <SyntheticProperty name="formSizePolicy" type="int" value="1"/>\n'
              '    <SyntheticProperty name="generateCenter" type="boolean" value="false"/>\n  </SyntheticProperties>\n  <AuxValues>\n'
              '    <AuxValue name="FormSettings_autoResourcing" type="java.lang.Integer" value="0"/>\n'
              '    <AuxValue name="FormSettings_autoSetComponentName" type="java.lang.Boolean" value="false"/>\n'
              '    <AuxValue name="FormSettings_generateFQN" type="java.lang.Boolean" value="true"/>\n'
              '    <AuxValue name="FormSettings_generateMnemonicsCode" type="java.lang.Boolean" value="false"/>\n'
              '    <AuxValue name="FormSettings_i18nAutoMode" type="java.lang.Boolean" value="false"/>\n'
              '    <AuxValue name="FormSettings_layoutCodeTarget" type="java.lang.Integer" value="1"/>\n'
              '    <AuxValue name="FormSettings_listenerGenerationStyle" type="java.lang.Integer" value="3"/>\n'
              '    <AuxValue name="FormSettings_variablesLocal" type="java.lang.Boolean" value="false"/>\n'
              '    <AuxValue name="FormSettings_variablesModifier" type="java.lang.Integer" value="2"/>\n  </AuxValues>\n\n'
              f'  <Layout class="{BRD}"/>\n  <SubComponents>\n')
        for c in self.children:
            s += self._node_xml(c, 'border', 4)
        s += '  </SubComponents>\n</Form>\n'
        return s

    # ------------------------------------------------------------------ Java
    @staticmethod
    def _col(c): return f'new java.awt.Color({c[0]}, {c[1]}, {c[2]})'

    def _prop_java(self, n, var):
        o = []
        if n.font: o.append(f'{var}.setFont(new java.awt.Font("{n.font[0]}", {n.font[1]}, {n.font[2]})); // NOI18N')
        if n.fg: o.append(f'{var}.setForeground({self._col(n.fg)});')
        if n.bg: o.append(f'{var}.setBackground({self._col(n.bg)});')
        for k, v in n.bools.items():
            o.append(f'{var}.set{k[0].upper()+k[1:]}({"true" if v else "false"});')
        for k, v in n.ints.items():
            o.append(f'{var}.set{k[0].upper()+k[1:]}({v});')
        if n.align is not None:
            nm = {0: 'CENTER', 2: 'LEFT', 4: 'RIGHT'}[n.align]
            o.append(f'{var}.setHorizontalAlignment(javax.swing.SwingConstants.{nm});')
        if n.border:
            if n.border[0] == 'line':
                o.append(f'{var}.setBorder(javax.swing.BorderFactory.createLineBorder({self._col(n.border[1])}));')
            elif n.border[0] == 'empty':
                t, l, b, r = n.border[1]
                o.append(f'{var}.setBorder(javax.swing.BorderFactory.createEmptyBorder({t}, {l}, {b}, {r}));')
        if n.minsize: o.append(f'{var}.setMinimumSize(new java.awt.Dimension({n.minsize[0]}, {n.minsize[1]}));')
        if n.size: o.append(f'{var}.setPreferredSize(new java.awt.Dimension({n.size[0]}, {n.size[1]}));')
        if n.kind == 'JComboBox':
            its = ', '.join('"%s"' % jesc(i) for i in n.items)
            o.append(f'{var}.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {{ {its} }}));')
        if n.kind == 'JTable':
            cols = ', '.join('"%s"' % jesc(c) for c in n.columns)
            o.append(f'{var}.setModel(new javax.swing.table.DefaultTableModel(\n            new Object [][] {{\n\n            }},\n            new String [] {{\n                {cols}\n            }}\n        ));')
        if n.text is not None and n.kind in ('JLabel', 'JButton', 'JTextField', 'JPasswordField', 'JCheckBox', 'JTextArea'):
            o.append(f'{var}.setText("{jesc(n.text)}");')
        if n.tooltip: o.append(f'{var}.setToolTipText("{jesc(n.tooltip)}");')
        return o

    def _add_java(self, parent_var, parent_layout, comp_var, n):
        if parent_layout == 'border':
            return f'{parent_var}.add({comp_var}, java.awt.BorderLayout.{(n.pos or "Center").upper()});'
        return f'{parent_var}.add({comp_var}, new org.netbeans.lib.awtextra.AbsoluteConstraints({n.x}, {n.y}, {n.w}, {n.h}));'

    def java_blocks(self):
        decls, lines, vars_ = [], [], []
        def vtype(n):
            if n.kind == 'JComboBox': return 'javax.swing.JComboBox<String>'
            return n.classname()
        def ctor(n):
            if n.kind == 'JComboBox': return 'new javax.swing.JComboBox<>()'
            return f'new {n.classname()}()'
        def walk(node, parent_var, parent_layout, is_root):
            for c in node.children:
                decls.append(f'        {c.name} = {ctor(c)};')
                if c.scroll: decls.append(f'        {c.scroll} = new javax.swing.JScrollPane();')
                vars_.append((vtype(c), c.name))
                if c.scroll: vars_.append(('javax.swing.JScrollPane', c.scroll))
        # declarations in tree order (pre-order, scroll pane before its component like NetBeans)
        def pre(node):
            for c in node.children:
                if c.scroll:
                    decls.append(f'        {c.scroll} = new javax.swing.JScrollPane();')
                    vars_.append(('javax.swing.JScrollPane', c.scroll))
                decls.append(f'        {c.name} = {ctor(c)};')
                vars_.append((vtype(c), c.name))
                pre(c)
        pre(self.root)
        def emit(node, parent_var, parent_layout):
            for c in node.children:
                lines.append('')
                var = c.name
                if c.kind == 'JPanel':
                    lines.extend('        ' + l for l in self._prop_java(c, var))
                    if c.layout == 'abs':
                        lines.append(f'        {var}.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());')
                    else:
                        pass
                    emit(c, var, c.layout)
                    lines.append('')
                    lines.append('        ' + self._add_java(parent_var, parent_layout, var, c))
                else:
                    lines.extend('        ' + l for l in self._prop_java(c, var))
                    if c.scroll:
                        lines.append(f'        {c.scroll}.setViewportView({var});')
                        lines.append('')
                        lines.append('        ' + self._add_java(parent_var, parent_layout, c.scroll, c))
                    else:
                        lines.append('        ' + self._add_java(parent_var, parent_layout, var, c))
        emit_lines_start = len(lines)
        emit(self.root, 'getContentPane()', 'border')
        # Java body
        head = ['        ' + l.strip() for l in []]
        init = ['    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents',
                '    private void initComponents() {', '']
        init += decls
        init.append('')
        init.append(f'        setDefaultCloseOperation({"javax.swing.WindowConstants.EXIT_ON_CLOSE" if self.close == 3 else "javax.swing.WindowConstants.DISPOSE_ON_CLOSE" if self.close == 2 else "javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE" if self.close == 0 else "javax.swing.WindowConstants.HIDE_ON_CLOSE"});')
        if self.title: init.append(f'        setTitle("{jesc(self.title)}");')
        if self.minsize: init.append(f'        setMinimumSize(new java.awt.Dimension({self.minsize[0]}, {self.minsize[1]}));')
        if self.size:
            init.append(f'        setPreferredSize(new java.awt.Dimension({self.size[0]}, {self.size[1]}));')
            init.append(f'        setSize(new java.awt.Dimension({self.size[0]}, {self.size[1]}));')
        # strip first blank of lines
        body = lines[:]
        if body and body[0] == '': body = body[1:]
        init += [l for l in body]
        init.append('')
        init.append('        pack();')
        init.append('    }// </editor-fold>//GEN-END:initComponents')
        order = sorted(set(vars_), key=lambda t: (t[0], t[1]))
        vb = ['    // Variables declaration - do not modify//GEN-BEGIN:variables']
        vb += [f'    private {t} {n};' for t, n in order]
        vb.append('    // End of variables declaration//GEN-END:variables')
        return '\n'.join(init), '\n'.join(vb)

    def java_file(self, extra_imports='', class_doc='', fields='', ctor_body='', members=''):
        base = 'javax.swing.JFrame' if self.kind == 'JFrame' else 'javax.swing.JDialog'
        init, vb = self.java_blocks()
        if self.kind == 'JFrame':
            ctor = f'    public {self.class_name}() {{\n        initComponents();\n{ctor_body}    }}\n'
        else:
            ctor = (f'    public {self.class_name}(java.awt.Frame parent, boolean modal) {{\n        super(parent, modal);\n'
                    f'        initComponents();\n{ctor_body}    }}\n')
        return (f'package {self.package};\n\n{extra_imports}\n/**\n * {class_doc or self.class_name}: passive view (designer form).\n * Logic lives in the controller.\n */\n'
                f'public class {self.class_name} extends {base} {{\n\n{fields}\n{ctor}\n{members}\n{init}\n\n{vb}\n}}\n')


def write(form, base_path, **kw):
    """base_path without extension, writes .form and .java"""
    open(base_path + '.form', 'w', encoding='utf-8', newline='\n').write(form.form_xml())
    open(base_path + '.java', 'w', encoding='utf-8', newline='\n').write(form.java_file(**kw))


def getters(form, include=('JButton', 'JTextField', 'JPasswordField', 'JComboBox', 'JTable', 'JTextArea', 'JLabel', 'JSpinner', 'JCheckBox', 'BEAN', 'JPanel')):
    """Auto getter source for named components (JLabel only when name starts with lblv/lblMessage etc: pass names list)"""
    out = []
    for n in form.all_nodes():
        if n.kind in include:
            t = 'javax.swing.JComboBox<String>' if n.kind == 'JComboBox' else n.classname()
            out.append(f'    public {t} get{n.name[0].upper()+n.name[1:]}() {{\n        return {n.name};\n    }}\n')
    return '\n'.join(out)
