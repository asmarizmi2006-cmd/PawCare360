package view;

/**
 * LoginForm: passive view (designer form).
 * Logic lives in LoginController.
 */
public class LoginForm extends javax.swing.JFrame {

    public LoginForm() {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        lblError.setText(" ");
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public char[] getPassword() {
        return txtPassword.getPassword();
    }

    // Blank = no role
    public String getRole() {
        Object item = cmbRole.getSelectedItem();
        return item == null ? "" : item.toString().trim();
    }

    public void showError(String message) {
        lblError.setText(message);
    }

    public void clearPassword() {
        txtPassword.setText("");
    }

    public javax.swing.JButton getBtnLogin() {
        return btnLogin;
    }

    public javax.swing.JTextField getTxtUsername() {
        return txtUsername;
    }

    public javax.swing.JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public javax.swing.JComboBox<String> getCmbRole() {
        return cmbRole;
    }

    /**
     * App entry point.
     */
    public static void main(String[] args) {
        util.GlobalExceptionHandler.install();
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> controller.LoginController.open().setVisible(true));
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        lblUsernameLabel = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        lblError = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        lblRole = new javax.swing.JLabel();
        cmbRole = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PawCare 360 - Login");
        setMinimumSize(new java.awt.Dimension(1500, 900));
        setPreferredSize(new java.awt.Dimension(1500, 900));
        setResizable(false);
        setSize(new java.awt.Dimension(1500, 900));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblTitle.setText("PawCare 360");
        getContentPane().add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 209, 600, 55));

        lblSubtitle.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblSubtitle.setForeground(new java.awt.Color(110, 125, 135));
        lblSubtitle.setText("Veterinary Clinic Management");
        getContentPane().add(lblSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 264, 600, 30));

        lblUsernameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsernameLabel.setForeground(new java.awt.Color(74, 91, 106));
        lblUsernameLabel.setText("Username");
        getContentPane().add(lblUsernameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 329, 600, 25));

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(74, 91, 106));
        lblPassword.setText("Password");
        getContentPane().add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 421, 600, 25));

        lblError.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblError.setForeground(java.awt.Color.red);
        getContentPane().add(lblError, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 600, 600, 25));

        txtUsername.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        getContentPane().add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 356, 600, 50));
        getContentPane().add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 448, 600, 50));

        btnLogin.setBackground(new java.awt.Color(157, 201, 163));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(74, 91, 106));
        btnLogin.setText("Login");
        getContentPane().add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 635, 600, 55));

        lblRole.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblRole.setForeground(new java.awt.Color(74, 91, 106));
        lblRole.setText("Role");
        getContentPane().add(lblRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 513, 600, 25));

        cmbRole.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Veterinarian", "Nurse", "Receptionist", " " }));
        getContentPane().add(cmbRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 540, 600, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsernameLabel;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
