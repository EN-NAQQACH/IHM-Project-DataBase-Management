/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package panel;

/**
 *
 * @author YK
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DatabaseLogin extends JFrame implements ActionListener {
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField addressField;
    private JButton loginButton;

    public DatabaseLogin() {
        // set up the form
        setTitle("Login Form");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // create the form components
        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        nameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        passwordField = new JPasswordField(20);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
        addressField = new JTextField(20);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Helvetica", Font.BOLD, 16));
        loginButton.setBackground(new Color(45, 156, 219));
        loginButton.setForeground(Color.WHITE);

        // add the components to the form
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameLabel, gbc);
        gbc.gridx++;
        panel.add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(passwordLabel, gbc);
        gbc.gridx++;
        panel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(addressLabel, gbc);
        gbc.gridx++;
        panel.add(addressField, gbc);
        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        add(panel);

        // register the button's action listener
        loginButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        // handle button click
        String name = nameField.getText();
        String passwordp = new String(passwordField.getPassword());
        String address = addressField.getText();
        try {

            String url = "jdbc:mysql://"+address+"/";//localhost/
            String user = name;
            String password = passwordp;
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("password:"+password);
            DatabaseGUI Databaseinterface = new DatabaseGUI(conn,password, user);
            this.setVisible(false);
        } catch (SQLException E) {
            E.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database 99");
        
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DatabaseLogin form = new DatabaseLogin();
        form.setVisible(true);
    }
}
