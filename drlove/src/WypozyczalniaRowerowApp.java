import javax.swing.*;
import java.awt.*;

public class WypozyczalniaRowerowApp extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel contentPanel = new JPanel(cardLayout);
    private JPanel sidebar;
    private boolean sidebarVisible = true;

    public WypozyczalniaRowerowApp() {
        setTitle("Wypożyczalnia Rowerów");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== SIDEBAR =====
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(new Color(45, 45, 45));

        JButton toggle = createSidebarButton("☰");
        toggle.addActionListener(e -> toggleSidebar());

        sidebar.add(toggle);
        sidebar.add(menuButton("Mapa", "MAPA"));
        sidebar.add(menuButton("Moje wypożyczenia", "WYPOZYCZENIA"));
        sidebar.add(menuButton("Kontakt", "KONTAKT"));
        sidebar.add(menuButton("Regulamin", "REGULAMIN"));
        sidebar.add(menuButton("Współpraca", "WSPOLPRACA"));
        sidebar.add(menuButton("Nasze rowery", "ROWERY"));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(wylogujButton());

        // ===== CONTENT =====
        contentPanel.add(new InteraktywnaMapaPanel(), "MAPA");
        contentPanel.add(new PanelNaszeRowery(), "ROWERY");
        contentPanel.add(simplePanel("Moje wypożyczenia"), "WYPOZYCZENIA");
        contentPanel.add(simplePanel("Kontakt:\nemail@kontakt.pl"), "KONTAKT");
        contentPanel.add(simplePanel("""
REGULAMIN WYPOŻYCZALNI ROWERÓW

1. Wypożyczenie roweru jest możliwe po zalogowaniu.
2. Użytkownik ponosi pełną odpowiedzialność za rower.
3. Zabrania się przekazywania roweru osobom trzecim.
4. Rower należy zwrócić w stanie niepogorszonym.
5. W przypadku uszkodzenia naliczana jest opłata.
6. Firma nie ponosi odpowiedzialności za wypadki.
7. Regulamin obowiązuje od momentu wypożyczenia.
"""), "REGULAMIN");

        contentPanel.add(simplePanel("""
WSPÓŁPRACA

Zapraszamy do współpracy:
- hotele
- uczelnie
- firmy
- samorządy
- organizatorów eventów

Oferujemy:
✔ flotę rowerów
✔ system rezerwacji
✔ serwis techniczny
✔ branding rowerów

Kontakt:
wspolpraca@rowery.pl
"""), "WSPOLPRACA");



        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "MAPA");
    }

    // ====== METODY ======

    private JButton menuButton(String text, String card) {
        JButton btn = createSidebarButton(text);
        btn.addActionListener(e -> cardLayout.show(contentPanel, card));
        return btn;
    }

    private JButton wylogujButton() {
        JButton btn = createSidebarButton("Wyloguj");
        btn.setForeground(Color.RED);
        btn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        return btn;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void toggleSidebar() {
        sidebarVisible = !sidebarVisible;
        sidebar.setPreferredSize(new Dimension(sidebarVisible ? 200 : 50, getHeight()));
        sidebar.revalidate();
    }

    private JPanel simplePanel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 18));
        p.add(area, BorderLayout.CENTER);
        return p;
    }
}