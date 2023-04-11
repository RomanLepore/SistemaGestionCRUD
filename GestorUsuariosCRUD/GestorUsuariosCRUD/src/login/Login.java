package login;

import adminproductos.AdminProducto;
import registerpage.RegisterPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame{
    private JLabel txtInicio;
    private JTextField txtUser;
    private JButton btnSignIn;
    private JButton btnRegister;
    private JPanel panel;
    private JButton cancelarButton;
    private JPasswordField txtPassword;
    RegisterPage rp = new RegisterPage();
    AdminProducto adminProducto = new AdminProducto();
    Connection conexionBd;
    PreparedStatement pst;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new Login().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setSize(250,250);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void connect() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexionBd = DriverManager.getConnection("jdbc:mysql://localhost:33065/gestor_usuarios", "root","");
            System.out.println("Conexion exitosa");

        }catch(ClassNotFoundException ex){
        }catch(SQLException ex){
            System.out.println("Error al intentar conectar con la bd");
        }
    }
    public Login() {
        connect();
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rp.setContentPane(new RegisterPage().getPanelRegister());
                rp.setSize(250,250);
                rp.setVisible(true);
                rp.setLocationRelativeTo(null);
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnSignIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(usuarioValido()){
                    JOptionPane.showMessageDialog(null,"Sesión Iniciada!!");
                    adminProducto.setContentPane(new AdminProducto().getPanelAP());
                    adminProducto.setSize(1200,600);
                    adminProducto.setVisible(true);
                    adminProducto.setLocationRelativeTo(null);
                }else{
                    JOptionPane.showMessageDialog(null,"Usuario NO válido, registresé");
                }
            }
        });
    }
    private boolean usuarioValido(){
        boolean res=false;

        // tiene que validar que tanto user como pass sean validos en la bd
        try{
            connect();
            pst = conexionBd.prepareStatement("SELECT usuario,contraseña FROM usuarios WHERE usuario='" + getUsuario() + "' AND contraseña='" + getPassword() +"'");
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                res=true;
            }
        }catch(SQLException e2){
            System.err.println("Error en inicio de sesión " + e2);
            JOptionPane.showMessageDialog(null,"Error con la base contacte admin");
            //e2.printStackTrace();
        }

        return res;
    }
    public String getUsuario(){
        return txtUser.getText();
    }
    public String getPassword(){
        return txtPassword.getText();
    }

    
}
