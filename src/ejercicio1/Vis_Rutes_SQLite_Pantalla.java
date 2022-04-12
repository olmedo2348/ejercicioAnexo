package ejercicio1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class Vis_Rutes_SQLite_Pantalla extends JFrame implements ActionListener {

    JComboBox combo;
    JButton salir = new JButton("Salir");
    JTextArea area = new JTextArea();
    Connection con;

    public void iniciar() throws SQLException {
        // sentències per a fer la connexió
        String url = "jdbc:sqlite:Rutes.sqlite";
        con = DriverManager.getConnection(url);

        this.setBounds(100, 100, 450, 300);
        this.setLayout(new BorderLayout());

        JPanel panell1 = new JPanel(new FlowLayout());
        JPanel panell2 = new JPanel(new BorderLayout());
        this.add(panell1, BorderLayout.NORTH);
        this.add(panell2, BorderLayout.CENTER);

        ArrayList<String> llista_rutes = new ArrayList<String>();
        // sentències per a omplir l'ArrayList amb el nom de les rutes
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from rutes");
        while (rs.next()) {
            llista_rutes.add(rs.getString(2));
        }
        st.close();

        combo = new JComboBox(llista_rutes.toArray());

        panell1.add(combo);
        panell1.add(salir);

        panell2.add(new JLabel("LLista de punts de la ruta:"), BorderLayout.NORTH);
        panell2.add(area, BorderLayout.CENTER);

        this.setVisible(true);
        combo.addActionListener(this);
        salir.addActionListener(this);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == combo) {
            //accions quan s'ha seleccionat un element del combobox, i que han de consistir en omplir el JTextArea
           try {
               area.setText("");
               int ind=combo.getSelectedIndex()+1;
               System.out.println(ind);
               Statement st1= con.createStatement();
               String sql1 = "select * from punts where num_r=" + ind;
               System.out.println(sql1);
               ResultSet rs1 = st1.executeQuery(sql1);
               while (rs1.next()) {
                  area.append(rs1.getString(3) + " (" + rs1.getDouble(4) + " , "+ rs1.getDouble(5) + ")"+ "\n");
               }

           //    ResultSet rs1 = st1.executeQuery("select * from punts where num");
           } catch (SQLException ex){
               ex.printStackTrace();
           }
        }

        if (e.getSource() == salir) {
            //accions quan s'ha apretat el botó d'eixir
            try{
                con.close();
                System.exit(0);
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }
}