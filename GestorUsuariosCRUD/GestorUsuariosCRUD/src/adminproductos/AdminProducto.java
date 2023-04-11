package adminproductos;

import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminProducto extends JFrame{
    private JTable table1;
    private JButton btnDelete;
    private JButton btnAgregar;
    private JButton btnBuscar;
    private JPanel panelAP;
    private JButton btnSalir;
    private JTextField txtBuscarNombre;
    private JButton btnEditar;
    private JTextField txtDescripcion;
    private JTextField txtNombreProducto;
    private JTextField txtPrecio;
    private JTextField txtCantidad;
    private JButton btnBorrar;
    private  JTextField txtStock;
    private JScrollPane scrollPane_table1;

    PreparedStatement pst;
    Connection conecBdAp;

    public static void main(String[] args) {
        JFrame frame = new JFrame("AdminProducto");
        frame.setContentPane(new AdminProducto().panelAP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public AdminProducto(){
        connect();
        table_load();
        txtStock.setEnabled(false);

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!productoExistente()){
                    agregarProducto();
                    table_load();
                }else{
                    JOptionPane.showMessageDialog(null,"El producto ya existe, editelo");
                }
            }
        });
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String nombreProducto = txtBuscarNombre.getText();

                    pst = conecBdAp.prepareStatement("SELECT nombre_producto,stock,precio_unidad,cantidad_ingresada,descripcion FROM productos WHERE nombre_producto= '" + nombreProducto + "'");
                    ResultSet rs = pst.executeQuery();

                    if(rs.next()){
                        String nombre_producto = rs.getString(1);
                        String stock = rs.getString(2);
                        String precio_unidad = rs.getString(3);
                        String cantidad_ingresada = rs.getString(4);
                        String descripcion = rs.getString(5);

                        txtNombreProducto.setText(nombre_producto);
                        txtStock.setText(stock);
                        txtPrecio.setText(precio_unidad);
                        txtCantidad.setText(cantidad_ingresada);
                        txtDescripcion.setText(descripcion);

                    }else{
                        txtNombreProducto.setText("");
                        txtStock.setText("");
                        txtPrecio.setText("");
                        txtCantidad.setText("");
                        txtDescripcion.setText("");
                        JOptionPane.showMessageDialog(null,"Producto no existente");
                    }
                }catch(SQLException e1){

                }

            }
        });
        btnBorrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String nombreProducto = txtBuscarNombre.getText();
                    pst = conecBdAp.prepareStatement("DELETE FROM productos WHERE nombre_producto = '" + nombreProducto + "'");
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null,"Producto borrado exitosamente");
                    table_load();
                    txtBuscarNombre.setText("");

                }catch(SQLException e2){
                    JOptionPane.showMessageDialog(null,"Error al conectar con bd");
                }

            }
        });
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(productoExistente()){
                    editarProducto();
                }else{
                    JOptionPane.showMessageDialog(null,"No puede editar un producto que no existe, agreguelo");
                }
            }
        });
    }

    void table_load(){

        try{
            pst = conecBdAp.prepareStatement("SELECT * FROM productos");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void connect() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conecBdAp = DriverManager.getConnection("jdbc:mysql://localhost:33065/gestor_usuarios", "root","");
            System.out.println("Conexion exitosa");

        }catch(ClassNotFoundException ex){
        }catch(SQLException ex){
            System.out.println("Error al intentar conectar con la bd HomePage");
        }
    }


    private void agregarProducto(){

        String nombre_producto,precio_unidad,cantidad,descripcion;

        nombre_producto = txtNombreProducto.getText();
        precio_unidad = txtPrecio.getText();
        cantidad = txtCantidad.getText();
        descripcion = txtDescripcion.getText();

        try{
            pst = conecBdAp.prepareStatement("INSERT  INTO productos(nombre_producto,stock,precio_unidad,cantidad_ingresada,descripcion) VALUES (?,?,?,?,?)");
            pst.setString(1,nombre_producto);
            //Cuando agrego un NUEVO producto, el stock es igual a la cantidad ingresada
            pst.setString(2,cantidad);
            pst.setString(3,precio_unidad);
            pst.setString(4,cantidad);
            pst.setString(5,descripcion);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,"Producto agregado exitosamente!!");

            txtNombreProducto.setText("");
            txtPrecio.setText("");
            txtCantidad.setText("");
            txtDescripcion.setText("");
            txtNombreProducto.requestFocus();

        }catch(SQLException e){
            e.printStackTrace();
        }

    }
    public boolean productoExistente(){
        boolean res=false;

        try{
            pst = conecBdAp.prepareStatement("SELECT nombre_producto FROM productos WHERE nombre_producto = '" + getNombreProducto() + "'");
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                res=true;
            }
        }catch(SQLException e){
            System.err.println("Error en validacion del producto " + e);
            JOptionPane.showMessageDialog(null,"Error con la base contacte admin");
        }
        return res;
    }

    private void editarProducto(){

        String nombre_producto,precio_unidad,cantidad,descripcion;


        nombre_producto = txtNombreProducto.getText();
        precio_unidad = txtPrecio.getText();
        cantidad = txtCantidad.getText();
        descripcion = txtDescripcion.getText();

        try{
            pst = conecBdAp.prepareStatement("UPDATE  productos SET nombre_producto=?,precio_unidad=?,cantidad_ingresada=?,descripcion=? WHERE nombre_producto= '" + nombre_producto + "'");
            pst.setString(1,nombre_producto);
            pst.setString(2,precio_unidad);
            pst.setString(3,cantidad);
            pst.setString(4,descripcion);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null,"Producto editado exitosamente");
            table_load();

            txtNombreProducto.setText("");
            txtPrecio.setText("");
            txtCantidad.setText("");
            txtDescripcion.setText("");
            txtNombreProducto.requestFocus();

        }catch(SQLException e){
            e.printStackTrace();
        }

    }
    public JPanel getPanelAP(){
        return panelAP;
    }
    public String getNombreProducto(){
        return txtNombreProducto.getText();
    }

}
