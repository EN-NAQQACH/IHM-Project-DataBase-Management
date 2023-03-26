/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panel;

/**
 *
 * @author Yassine KADER
 */
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DatabaseGUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String CREATE_DATABASE_TEMPLATE = "CREATE DATABASE %s";
    private static final String CREATE_TABLE_TEMPLATE = "CREATE TABLE %s (%s)";

    private Connection conn;

    private JTextField dbField;
    private JTextField tableField;
    private JTextField columnField;
    private JTextField infoField;
    private JTextArea resultArea;
    private String password;
    private String user;

    public DatabaseGUI(Connection con, String password, String user) {
        super("Database App");
        this.user = user;
        this.password = password;
        initDB(con);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Input"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Database:"), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        dbField = new JTextField(10);
        inputPanel.add(dbField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Table:"), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        tableField = new JTextField(10);
        inputPanel.add(tableField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Columns:"), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        columnField = new JTextField(20);
        inputPanel.add(columnField, c);

        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.NONE;
        inputPanel.add(new JLabel("Information:"), c);

        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        infoField = new JTextField(20);
        inputPanel.add(infoField, c);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton createDBButton = new JButton("Create Database");

        createDBButton.addActionListener(this);
        buttonPanel.add(createDBButton);

        JButton createTableButton = new JButton("Create Table");
        createTableButton.addActionListener(this);
        buttonPanel.add(createTableButton);

        JButton addInfoButton = new JButton("Add Information");
        addInfoButton.addActionListener(this);
        buttonPanel.add(addInfoButton);

        JButton showInfoButton = new JButton("Show Information");
        showInfoButton.addActionListener(this);
        buttonPanel.add(showInfoButton);

        JButton showtableButton = new JButton("Show Tables");
        buttonPanel.add(showtableButton);
        showtableButton.addActionListener(this);

        JButton showDbButton = new JButton("Show Databases");
        showDbButton.addActionListener(this);
        buttonPanel.add(showDbButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(contentPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initDB(Connection con) {
        /*try {*/

            /*String url = "jdbc:mysql://"+urlp+"/";//localhost/
            String user = userp;
            String password = passwordp;*/
            conn = con;
        /*} catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database");
            System.exit(1);
        }*/
    }

    private void createDatabase() {
        String dbName = dbField.getText().trim();
        if (dbName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter database name");
            return;
        }

        String sql = String.format(CREATE_DATABASE_TEMPLATE, dbName);
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            JOptionPane.showMessageDialog(this, "Database created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to create database");
        }
    }

    private void createTable() {
        String tableName = tableField.getText().trim();
        String columns = columnField.getText().trim();
        String datbase = dbField.getText();
        String url = "jdbc:mysql://localhost:3306/" + datbase;
        if (tableName.isEmpty() || columns.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter table name and columns");
            return;
        }

        String sql = String.format(CREATE_TABLE_TEMPLATE, tableName, columns);
        try {
            reInitDB(url, password, user);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            JOptionPane.showMessageDialog(this, "Table created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, sql);
        }
    }
    private void reInitDB(String url, String password, String user){
        try {
            System.out.println(url+user+password);
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void addInformation() {
        String tableName = tableField.getText().trim();
        String info = infoField.getText().trim();
        String datbase = dbField.getText();
        String url = "jdbc:mysql://localhost:3306/" + datbase;
        if (tableName.isEmpty() || info.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter table name and information");
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES (%s)", tableName, info);
        try {
            reInitDB(url, password, user);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            JOptionPane.showMessageDialog(this, "Information added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add information"+sql);
        }
    }

    private void showInformation() {
        String tableName = tableField.getText().trim();
        String datbase = dbField.getText();
        String url = "jdbc:mysql://localhost:3306/" + datbase;
        if (tableName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter table name");
            return;
        }

        String sql = String.format("SELECT * FROM %s", tableName);
        try {
            reInitDB(url, password, user);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= numCols; i++) {
                sb.append(rsmd.getColumnLabel(i)).append("\t");
            }
            sb.append("\n");

            while (rs.next()) {
                for (int i = 1; i <= numCols; i++) {
                    sb.append(rs.getString(i)).append("\t");
                }
                sb.append("\n");
            }

            resultArea.setText(sb.toString());
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve information");
        }
    }
    
    public void showTables(){
        String datbase = dbField.getText();
        String url = "jdbc:mysql://localhost:3306/" + datbase;
        String resultTables = new String();
        reInitDB(url, password, user);
        try {
         Connection conn = DriverManager.getConnection(url, user, password);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SHOW TABLES");
         while (rs.next()) {
           resultTables += rs.getString(1)+"\n";
         }
         resultArea.setText(resultTables);
         rs.close();
         stmt.close();
         conn.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
    }
    public void showDB(){
        String datbase = dbField.getText();
        String url = "jdbc:mysql://localhost:3306/" + datbase;
        String resultDB = new String();
        reInitDB(url, password, user);
        try {
         Connection conn = DriverManager.getConnection(url, user, password);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SHOW DATABASES");
         while (rs.next()) {
           resultDB += rs.getString(1)+"\n";
         }
         resultArea.setText(resultDB);
         rs.close();
         stmt.close();
         conn.close();
      } catch (SQLException e) {
         e.printStackTrace();
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Create Database":
                createDatabase();
                break;
            case "Create Table":
                createTable();
                break;
            case "Add Information":
                addInformation();
                break;
            case "Show Information":
                showInformation();
                break;
            case "Show Tables":
                showTables();
                break;
            case "Show Databases":
                showDB();
                break;
            default:
                System.out.println("something clicked");
                break;
        }
        
    }
    /*
    public static void main(String[] args) {
        new DatabaseGUI();
    }*/
}
