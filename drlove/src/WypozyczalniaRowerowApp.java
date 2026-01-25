import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WypozyczalniaRowerowApp extends JFrame {
    // === POLA KLASY ===
    private final Map<String, Wypozyczalnia> stacje = new HashMap<>();
    private final Klient aktualnyKlient = new Klient("Jan", "Kowalski");

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final JPanel sidebar;
    private final JPanel rightSidebar;

    private boolean sidebarVisible = true;
    private int sidebarWidth = 200;
    private int rightSidebarWidth = 0;

    private Timer sidebarTimer;
    private Timer rightSidebarTimer;

    private final int SIDEBAR_EXPANDED = 200;
    private final int SIDEBAR_COLLAPSED = 50;
    private final int RIGHT_SIDEBAR_TARGET_WIDTH = 300;

    public WypozyczalniaRowerowApp(boolean czyAdmin) {
        setTitle("System Rowerowy - " + (czyAdmin ? "ADMIN" : "UŻYTKOWNIK"));
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. INICJALIZACJA DANYCH
        inicjalizujStacje();

        // 2. SIDEBAR (LEWY)
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(sidebarWidth, getHeight()));
        sidebar.setBackground(new Color(45, 45, 45));

        setupSidebar(czyAdmin);

        // 3. RIGHT SIDEBAR (DYNAMICZNY)
        rightSidebar = new JPanel(new BorderLayout());
        rightSidebar.setPreferredSize(new Dimension(0, getHeight()));
        rightSidebar.setBackground(new Color(245, 245, 245));
        rightSidebar.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

        // 4. CONTENT PANEL
        setupContentPanel(czyAdmin);

        // DODANIE DO OKNA
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);

        cardLayout.show(contentPanel, "MAPA");
    }

    private void inicjalizujStacje() {
        Wypozyczalnia stacjaA = new Wypozyczalnia("Baza rowerów A");
        stacjaA.dodajRower(new Rower(101, "Góral Kross", "rower2.jfif", "Super amortyzatory."));
        stacjaA.dodajRower(new Rower(102, "Miejski Gazelle", "rower1.jfif", "Koszyk na zakupy."));

        Wypozyczalnia stacjaB = new Wypozyczalnia("Baza rowerów B");
        stacjaB.dodajRower(new Rower(201, "Szosa Trek", "rower3.jfif", "Lekka rama, szybkie opony."));

        Wypozyczalnia stacjaC = new Wypozyczalnia("Baza rowerów C");
        stacjaC.dodajRower(new Rower(301, "Elektryczny Specialized", "rower1.jfif", "Zasięg do 100km."));

        stacje.put("Baza rowerów A", stacjaA);
        stacje.put("Baza rowerów B", stacjaB);
        stacje.put("Baza rowerów C", stacjaC);
    }

    private void setupSidebar(boolean czyAdmin) {
        JButton toggle = createSidebarButton("☰");
        toggle.addActionListener(e -> toggleSidebar());

        sidebar.add(toggle);
        sidebar.add(menuButton("Mapa", "MAPA"));
        sidebar.add(menuButton("Moje wypożyczenia", "WYPOZYCZENIA"));
        sidebar.add(menuButton("Nasze rowery", "ROWERY"));
        sidebar.add(menuButton("Regulamin", "REGULAMIN"));
        sidebar.add(menuButton("Kontakt", "KONTAKT"));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(wylogujButton());
    }

    private void setupContentPanel(boolean czyAdmin) {
        contentPanel.add(new InteraktywnaMapaPanel(czyAdmin, this), "MAPA");
        contentPanel.add(new PanelNaszeRowery(), "ROWERY");

        JPanel wypozyczeniaWrapper = new JPanel(new BorderLayout());
        contentPanel.add(wypozyczeniaWrapper, "WYPOZYCZENIA");

        contentPanel.add(simplePanel("Kontakt:\nemail: kontakt@rowery.pl\ntel: 123 456 789"), "KONTAKT");
        contentPanel.add(simplePanel("REGULAMIN\n1. Dbaj o rower.\n2. Zwracaj w terminie.\n3. Miłej jazdy!"), "REGULAMIN");

        odswiezMojeWypozyczenia(); // Pierwsze ładowanie
    }

    // === LOGIKA UI ===

    public void pokazPanelStacji(String nazwaStacji) {
        Wypozyczalnia stacja = stacje.get(nazwaStacji);
        if (stacja == null) return;

        rightSidebar.removeAll();

        // Nagłówek
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(60, 60, 60));
        JLabel title = new JLabel("  " + nazwaStacji, JLabel.LEFT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(title, BorderLayout.CENTER);

        JButton closeBtn = new JButton("X");
        closeBtn.addActionListener(e -> schowajPrawyPanel());
        header.add(closeBtn, BorderLayout.EAST);
        rightSidebar.add(header, BorderLayout.NORTH);

        // Lista rowerów
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Color.WHITE);

        for (Rower r : stacja.getFlota()) {
            lista.add(stworzWizualnyRower(r, stacja));
        }

        rightSidebar.add(new JScrollPane(lista), BorderLayout.CENTER);

        // Animacja wysuwania
        if (rightSidebarWidth == 0) {
            runRightSidebarAnimation(true);
        } else {
            rightSidebar.revalidate();
            rightSidebar.repaint();
        }
    }

    private void runRightSidebarAnimation(boolean open) {
        if (rightSidebarTimer != null && rightSidebarTimer.isRunning()) rightSidebarTimer.stop();

        rightSidebarTimer = new Timer(10, e -> {
            if (open) {
                rightSidebarWidth += 20;
                if (rightSidebarWidth >= RIGHT_SIDEBAR_TARGET_WIDTH) {
                    rightSidebarWidth = RIGHT_SIDEBAR_TARGET_WIDTH;
                    rightSidebarTimer.stop();
                }
            } else {
                rightSidebarWidth -= 20;
                if (rightSidebarWidth <= 0) {
                    rightSidebarWidth = 0;
                    rightSidebarTimer.stop();
                    rightSidebar.removeAll();
                }
            }
            rightSidebar.setPreferredSize(new Dimension(rightSidebarWidth, getHeight()));
            this.revalidate();
        });
        rightSidebarTimer.start();
    }

    public void schowajPrawyPanel() {
        runRightSidebarAnimation(false);
    }

    public void odswiezMojeWypozyczenia() {
        // Wyciągamy panel WYPOZYCZENIA (indeks 2)
        JPanel wrapper = (JPanel) contentPanel.getComponent(2);
        wrapper.removeAll();

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (aktualnyKlient.getMojeWypozyczenia().isEmpty()) {
            lista.add(new JLabel("<html><h2>Brak aktywnych wypożyczeń.</h2></html>"));
        } else {
            for (Wypozyczenie w : aktualnyKlient.getMojeWypozyczenia()) {
                JPanel item = new JPanel(new BorderLayout(10, 10));
                item.setMaximumSize(new Dimension(800, 50));
                item.add(new JLabel("🚲 " + w.getRower().getModel() + " [" + w.getWypozyczalnia().getNazwa() + "]"), BorderLayout.CENTER);

                JButton zwrotBtn = new JButton("Zwróć");
                zwrotBtn.addActionListener(e -> {
                    w.getRower().setDostepny(true);
                    aktualnyKlient.usunWypozyczenie(w);
                    JOptionPane.showMessageDialog(this, "Rower zwrócony!");
                    odswiezMojeWypozyczenia();
                });
                item.add(zwrotBtn, BorderLayout.EAST);
                lista.add(item);
                lista.add(Box.createVerticalStrut(10));
            }
        }
        wrapper.add(new JScrollPane(lista), BorderLayout.CENTER);
        wrapper.revalidate();
        wrapper.repaint();
    }

    private void toggleSidebar() {
        if (sidebarTimer != null && sidebarTimer.isRunning()) return;
        int target = sidebarVisible ? SIDEBAR_COLLAPSED : SIDEBAR_EXPANDED;

        sidebarTimer = new Timer(5, e -> {
            if (sidebarWidth != target) {
                sidebarWidth += (sidebarVisible ? -10 : 10);
                if ((sidebarVisible && sidebarWidth <= target) || (!sidebarVisible && sidebarWidth >= target)) {
                    sidebarWidth = target;
                    sidebarVisible = !sidebarVisible;
                    sidebarTimer.stop();
                }
                sidebar.setPreferredSize(new Dimension(sidebarWidth, getHeight()));
                sidebar.revalidate();
            }
        });
        sidebarTimer.start();
    }

    // === HELPERY (PRZYCISKI I PANELE) ===

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton menuButton(String text, String card) {
        JButton btn = createSidebarButton(text);
        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, card);
            if (card.equals("WYPOZYCZENIA")) odswiezMojeWypozyczenia();
        });
        return btn;
    }

    private JButton wylogujButton() {
        JButton btn = createSidebarButton("Wyloguj");
        btn.setForeground(new Color(255, 100, 100));
        btn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        return btn;
    }

    private JPanel stworzWizualnyRower(Rower r, Wypozyczalnia stacja) {
        JPanel p = new JPanel(new BorderLayout(15, 5));
        p.setMaximumSize(new Dimension(RIGHT_SIDEBAR_TARGET_WIDTH, 100));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        // Obrazek
        ImageIcon icon = new ImageIcon(new ImageIcon(r.getImagePath()).getImage().getScaledInstance(70, 45, Image.SCALE_SMOOTH));
        JLabel imgLabel = new JLabel(icon);
        p.add(imgLabel, BorderLayout.WEST);

        // Opis
        JLabel nameLabel = new JLabel("<html><b>" + r.getModel() + "</b></html>");
        p.add(nameLabel, BorderLayout.CENTER);

        // Przycisk
        JButton rentBtn = new JButton(r.isDostepny() ? "Wypożycz" : "Zajęty");
        rentBtn.setEnabled(r.isDostepny());
        rentBtn.addActionListener(e -> {
            if (aktualnyKlient.czyPosiadaRower(r.getId())) {
                JOptionPane.showMessageDialog(this, "Masz już ten rower!");
            } else {
                r.setDostepny(false);
                aktualnyKlient.dodajDoHistorii(new Wypozyczenie(r, stacja));
                JOptionPane.showMessageDialog(this, "Wypożyczono: " + r.getModel());
                pokazPanelStacji(stacja.getNazwa());
                odswiezMojeWypozyczenia();
            }
        });
        p.add(rentBtn, BorderLayout.EAST);

        return p;
    }

    private JPanel simplePanel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Monospaced", Font.PLAIN, 16));
        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }
}