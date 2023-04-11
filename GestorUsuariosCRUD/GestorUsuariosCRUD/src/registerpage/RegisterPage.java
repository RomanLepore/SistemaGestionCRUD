package registerpage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterPage extends JFrame{
    private JLabel txtRegisterPage;
    private JTextField txtRegisterUser;
    private JTextField txtRegisterEmail;
    private JTextField txtRegisterPass;
    private JButton btnRegister;
    private JPanel panelRegister;
    Connection conexionBdReg;
    PreparedStatement pst;

    public static void main(String[] args) {
        JFrame frame1 = new JFrame("RegisterPage");
        frame1.setContentPane(new RegisterPage().panelRegister);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame1.setSize(250,250);
        frame1.pack();
        frame1.setVisible(true);
    }
    public RegisterPage() {

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!campoCompletado()){
                    JOptionPane.showMessageDialog(null,"Complete todos los campos");
                } else if(!usuarioExistente()){
                    registrarUsuario();
                }else{
                    JOptionPane.showMessageDialog(null,"El usuario ya está registrado");
                }

            }
        });
    }
    private void registrarUsuario(){
        String user,password,email;

        user = txtRegisterUser.getText();
        password = txtRegisterPass.getText();
        email = txtRegisterEmail.getText();

        try{
            pst = conexionBdReg.prepareStatement("INSERT INTO usuarios(usuario,contraseña,email) VALUES (?,?,?)");
            pst.setString(1,user);
            pst.setString(2,password);
            pst.setString(3,email);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,"Registro exitoso!!");

            txtRegisterUser.setText("");
            txtRegisterPass.setText("");
            txtRegisterEmail.setText("");
            txtRegisterUser.requestFocus();

        }catch(SQLException e1){
            e1.printStackTrace();
        }
    }
    public boolean usuarioExistente(){
        boolean res = false;

            try {
                connect();
                PreparedStatement pst = conexionBdReg.prepareStatement("SELECT usuario FROM usuarios WHERE usuario = '" + getRegisterUser() + "'");
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    res = true;
                }
            }catch(SQLException ex){
                    System.err.println("Error en validacion de usuario" + ex);
                    JOptionPane.showMessageDialog(null,"Error con la base contacte admin");
            }
        return res;
    }

    private boolean campoCompletado(){
        boolean res = false;

        if( !( (getRegisterUser().equals("")) || (getRegisterPassword().equals("")) || (getRegisterEmail().equals(""))  ) ) {
            res = true;
        }

        return res;
    }

    private void connect() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexionBdReg = DriverManager.getConnection("jdbc:mysql://localhost:33065/gestor_usuarios", "root","");
            System.out.println("Conexion exitosa");

        }catch(ClassNotFoundException ex){
        }catch(SQLException ex){
            System.out.println("Error al intertar conectar con la bd");
        }
    }

    // GETTERS
    public String getRegisterUser(){
        return txtRegisterUser.getText();
    }
    public String getRegisterPassword(){
        return txtRegisterPass.getText();
    }
    public String getRegisterEmail(){
        return txtRegisterEmail.getText();
    }
    public JPanel getPanelRegister(){
        return panelRegister;
    }


}
