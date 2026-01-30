import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Logowanie");
        setSize(300, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel loginLabel = new JLabel("Login:");
        JTextField loginField = new JTextField();
        JLabel hasloLabel = new JLabel("Hasło:");
        JPasswordField hasloField = new JPasswordField();
        JButton zaloguj = new JButton("Zaloguj");

        panel.add(loginLabel);
        panel.add(loginField);
        panel.add(hasloLabel);
        panel.add(hasloField);
        panel.add(new JLabel()); // Pusty wypełniacz
        panel.add(zaloguj);

        add(panel);

        // 🔐 LOGIKA LOGOWANIA
        zaloguj.addActionListener(e -> {
            String login = loginField.getText();
            String haslo = new String(hasloField.getPassword());

            if (login.equals("admin") && haslo.equals("admin")) {
                dispose();
                new WypozyczalniaRowerowApp(true, "Admin", "Systemowy").setVisible(true);
            } else if (login.equals("user") && haslo.equals("user")) {
                dispose();
                // Jan Kowalski
                new WypozyczalniaRowerowApp(false, "Jan", "Kowalski").setVisible(true);
            } else if (login.equals("user2") && haslo.equals("user2")) {
                dispose();
                // Anna Nowak
                new WypozyczalniaRowerowApp(false, "Anna", "Nowak").setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Zły login lub hasło", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        // Tylko odpalenie GUI, żadnego syfu w konsoli
        SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true)
        );
    }
}