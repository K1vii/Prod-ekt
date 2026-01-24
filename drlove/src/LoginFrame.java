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
        panel.add(new JLabel()); // pusty
        panel.add(zaloguj);

        add(panel);

        // 🔐 LOGIKA LOGOWANIA
        zaloguj.addActionListener(e -> {
            String login = loginField.getText();
            String haslo = new String(hasloField.getPassword());

            // 👉 NA SZTYWNO (najprościej)
            if (login.equals("admin") && haslo.equals("admin")) {
                dispose(); // zamyka login
                // Przekazujemy "true" -> to jest ADMIN
                new WypozyczalniaRowerowApp(true).setVisible(true);
            } else if (login.equals("user") && haslo.equals("user")) {
                dispose();
                // Przekazujemy "false" -> to jest ZWYKŁY USER
                new WypozyczalniaRowerowApp(false).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Zły login lub hasło",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new LoginFrame().setVisible(true)
        );
        Wypozyczalnia w1 = new Wypozyczalnia("Stacja Centrum");
        Wypozyczalnia w2 = new Wypozyczalnia("Stacja Plaża");
        Wypozyczalnia w3 = new Wypozyczalnia("Stacja Park");

        // Dodajemy rowery do baz poszczególnych wypożyczalni
        w1.dodajRower(new Rower(101, "Góral Kross"));
        w1.dodajRower(new Rower(102, "Miejski Gazelle"));
        w2.dodajRower(new Rower(201, "Szosa Trek"));

        // 2. Tworzymy System i Klienta
        SystemWypozyczalni systemApp = new SystemWypozyczalni();
        Klient jan = new Klient("Jan", "Kowalski");

        // 3. Symulacja wypożyczenia
        // Jan wypożycza rower ID 101 z Centrum
        systemApp.wypozyczRower(jan, w1, 101);

        // Jan próbuje wypożyczyć ten sam rower (powinien być błąd/zajęty)
        systemApp.wypozyczRower(jan, w1, 101);

        // Jan wypożycza inny rower z Plaży
        systemApp.wypozyczRower(jan, w2, 201);

        // 4. Sprawdzenie zakładki "Moje wypożyczenia"
        jan.pokazMojeWypozyczenia();
    }

}
