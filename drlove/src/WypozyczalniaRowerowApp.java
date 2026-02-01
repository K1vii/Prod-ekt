import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class WypozyczalniaRowerowApp extends JFrame {
    private static final Map<String, Klient> bazaKlientow = new HashMap<>();
    private static boolean klienciZainicjalizowani = false;
    private static final Map<String, Wypozyczalnia> stacje = new HashMap<>();
    private static boolean stacjeZainicjalizowane = false;
    private Klient aktualnyKlient; // Pole inicjalizowane w konstruktorze

    // Globalna historia dla Admina - static, aby była wspólna dla wszystkich sesji
    private static final List<Wypozyczenie> historiaGlobalna = new ArrayList<>();

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final JPanel sidebar;
    private final JPanel rightSidebar;

    private boolean sidebarVisible = true;
    private int sidebarWidth = 200;
    private int rightSidebarWidth = 0;
    private boolean czyAdmin;

    private Timer sidebarTimer;
    private Timer rightSidebarTimer;

    private final int SIDEBAR_EXPANDED = 200;
    private final int SIDEBAR_COLLAPSED = 50;
    private final int RIGHT_SIDEBAR_TARGET_WIDTH = 300;

    public WypozyczalniaRowerowApp(boolean czyAdmin, String imie, String nazwisko) {
        this.czyAdmin = czyAdmin;

        setTitle("System Rowerowy - " + (czyAdmin ? "ADMIN" : "UŻYTKOWNIK"));
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        inicjalizujStacje();
        inicjalizujKlientow();

        String klucz = imie + "_" + nazwisko;
        this.aktualnyKlient = bazaKlientow.getOrDefault(klucz, new Klient(imie, nazwisko));

        wczytajWypozyczeniaZPliku();

        // ===== SIDEBAR (LEWY) =====
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(sidebarWidth, getHeight()));
        sidebar.setBackground(new Color(45, 45, 45));

        JButton toggle = createSidebarButton("☰");
        toggle.addActionListener(e -> toggleSidebar());
        sidebar.add(toggle);

        sidebar.add(menuButton("Mapa", "MAPA"));

        // Nazwa przycisku zależna od roli
        String tekstWypozyczen = czyAdmin ? "Monitor Wypożyczeń" : "Moje wypożyczenia";
        sidebar.add(menuButton(tekstWypozyczen, "WYPOZYCZENIA"));

        sidebar.add(menuButton("Nasze rowery", "ROWERY"));
        sidebar.add(menuButton("Regulamin", "REGULAMIN"));
        sidebar.add(menuButton("Kontakt", "KONTAKT"));
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(wylogujButton());

        // ===== RIGHT SIDEBAR (DYNAMICZNY) =====
        rightSidebar = new JPanel(new BorderLayout());
        rightSidebar.setPreferredSize(new Dimension(0, getHeight()));
        rightSidebar.setBackground(new Color(245, 245, 245));
        rightSidebar.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));

        // ===== CONTENT PANEL =====
        setupContentPanel();

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        add(rightSidebar, BorderLayout.EAST);

        cardLayout.show(contentPanel, "MAPA");
    }

    private void inicjalizujStacje() {
        if (stacjeZainicjalizowane) return; // Jeśli stacje już są w pamięci, nic nie rób

        Wypozyczalnia stacjaA = new Wypozyczalnia("Baza rowerów A");
        stacjaA.dodajRower(new Rower(101, "Góral Kross", "rower2.png", "Super amortyzatory."));
        stacjaA.dodajRower(new Rower(102, "Miejski Gazelle", "rower1.png", "Koszyk na zakupy."));

        Wypozyczalnia stacjaB = new Wypozyczalnia("Baza rowerów B");
        stacjaB.dodajRower(new Rower(201, "Trek E-Bike", "rower4.png", "Lekka rama, szybkie opony, electrico."));

        Wypozyczalnia stacjaC = new Wypozyczalnia("Baza rowerów C");
        stacjaC.dodajRower(new Rower(301, "Elektryczny Specialized", "rower3.png", "Zasięg do 100km."));

        stacje.put("Baza rowerów A", stacjaA);
        stacje.put("Baza rowerów B", stacjaB);
        stacje.put("Baza rowerów C", stacjaC);

        stacjeZainicjalizowane = true; // Zaznaczamy, że baza jest gotowa
    }
    private void inicjalizujKlientow() {
        if (klienciZainicjalizowani) return;

        // Tworzymy stałe obiekty klientów, które będą żyć przez cały czas działania programu
        bazaKlientow.put("Jan_Kowalski", new Klient("Jan", "Kowalski"));
        bazaKlientow.put("Anna_Nowak", new Klient("Anna", "Nowak"));
        bazaKlientow.put("Admin_Systemowy", new Klient("Admin", "Systemowy"));

        klienciZainicjalizowani = true;
    }

    private void setupContentPanel() {
        contentPanel.add(new InteraktywnaMapaPanel(czyAdmin, this), "MAPA");

        //scroll
        PanelNaszeRowery panelRowery = new PanelNaszeRowery();
        JScrollPane scrollRowery = new JScrollPane(panelRowery);
        scrollRowery.setBorder(null);
        scrollRowery.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollRowery, "ROWERY");

        JPanel wypozyczeniaWrapper = new JPanel(new BorderLayout());
        contentPanel.add(wypozyczeniaWrapper, "WYPOZYCZENIA");

        StringBuilder trescKontakt = new StringBuilder();
        File plikKontakt = new File("kontakt.txt");

        if (plikKontakt.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(plikKontakt))) {
                String linia;
                while ((linia = br.readLine()) != null) {
                    trescKontakt.append(linia).append("\n");
                }
            } catch (IOException e) {
                trescKontakt.append("Błąd odczytu danych kontaktowych.");
            }
        } else {
            trescKontakt.append("Brak pliku kontakt.txt! Stwórz go w folderze projektu.");
        }

        contentPanel.add(simplePanel(trescKontakt.toString()), "KONTAKT");


        //regulamin
        StringBuilder trescRegulaminu = new StringBuilder();
        File plikRegulaminu = new File("regulamin.txt");

        if (plikRegulaminu.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(plikRegulaminu))) {
                String linia;
                while ((linia = br.readLine()) != null) {
                    trescRegulaminu.append(linia).append("\n");
                }
            } catch (IOException e) {
                trescRegulaminu.append("Błąd odczytu regulaminu.");
            }
        } else {
            trescRegulaminu.append("Brak pliku regulamin.txt w folderze projektu.\nUpewnij się, że plik jest obok folderu src.");
        }

        contentPanel.add(simplePanel(trescRegulaminu.toString()), "REGULAMIN");

        odswiezMojeWypozyczenia();
    }

    public void odswiezMojeWypozyczenia() {
        JPanel wrapper = (JPanel) contentPanel.getComponent(2);
        wrapper.removeAll();
        wrapper.setLayout(new BorderLayout());

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Color.WHITE);
        lista.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (czyAdmin) {
            // === WIDOK ADMINA ===
            JLabel title = new JLabel("<html><h1>📊 Monitor Systemu (ADMIN)</h1></html>");
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            lista.add(title);

            // === TO JEST TWÓJ PUNKT 3 ===
            JButton refreshBtn = new JButton("🔄 Synchronizuj z bazą plikową");
            refreshBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            refreshBtn.addActionListener(e -> {
                wczytajWypozyczeniaZPliku(); // Metoda odczytująca plik
                odswiezMojeWypozyczenia();    // Odświeżenie widoku
                JOptionPane.showMessageDialog(this, "Dane odświeżone!");
            });
            lista.add(refreshBtn);
            lista.add(Box.createVerticalStrut(20));

            if (historiaGlobalna.isEmpty()) {
                lista.add(new JLabel("Brak aktywnych wypożyczeń."));
            } else {
                JPanel header = new JPanel(new GridLayout(1, 3));
                header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
                header.setBackground(new Color(230, 230, 230));
                header.add(new JLabel("  Użytkownik"));
                header.add(new JLabel("  Rower"));
                header.add(new JLabel("  Lokalizacja"));
                header.setAlignmentX(Component.LEFT_ALIGNMENT);
                lista.add(header);
                lista.add(Box.createVerticalStrut(10));

                for (Wypozyczenie w : historiaGlobalna) {
                    JPanel row = new JPanel(new GridLayout(1, 3));
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
                    row.setBackground(Color.WHITE);
                    row.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                            BorderFactory.createEmptyBorder(0, 10, 0, 10)
                    ));


                    // POBIERANIE DANYCH KLIENTA Z WYPOZYCZENIA
                    row.add(new JLabel("👤 " + w.getKlient().getImie() + " " + w.getKlient().getNazwisko()));
                    row.add(new JLabel("🚲 " + w.getRower().getModel()));
                    row.add(new JLabel("📍 " + w.getWypozyczalnia().getNazwa()));

                    row.setAlignmentX(Component.LEFT_ALIGNMENT);
                    lista.add(row);
                    lista.add(Box.createVerticalStrut(5));
                }
            }
        } else {
            // === WIDOK USERA ===
            JLabel title = new JLabel("<html><h1>Moje aktywne wypożyczenia</h1></html>");
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            lista.add(title);
            lista.add(Box.createVerticalStrut(20));

            if (aktualnyKlient.getMojeWypozyczenia().isEmpty()) {
                lista.add(new JLabel("Nie masz obecnie wypożyczonych rowerów."));
            } else {
                for (Wypozyczenie w : aktualnyKlient.getMojeWypozyczenia()) {
                    lista.add(stworzWierszWypozyczenia(w));
                    lista.add(Box.createVerticalStrut(10));
                }
            }
        }

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        wrapper.add(scroll, BorderLayout.CENTER);

        wrapper.revalidate();
        wrapper.repaint();
    }

    private JPanel stworzWierszWypozyczenia(Wypozyczenie w) {
        JPanel item = new JPanel(new BorderLayout(15, 0));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        item.setPreferredSize(new Dimension(900, 60));
        item.setBackground(new Color(245, 245, 245));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel desc = new JLabel("🚲 " + w.getRower().getModel() + " | Lokalizacja: " + w.getWypozyczalnia().getNazwa());
        item.add(desc, BorderLayout.CENTER);

        JButton zwrotBtn = new JButton("Zwróć");
        zwrotBtn.addActionListener(e -> {
            w.getRower().setDostepny(true);
            aktualnyKlient.usunWypozyczenie(w);
            historiaGlobalna.remove(w);
            zapiszWszystkoDoPliku();
            JOptionPane.showMessageDialog(this, "Rower zwrócony pomyślnie!");
            odswiezMojeWypozyczenia();
        });
        item.add(zwrotBtn, BorderLayout.EAST);

        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        return item;
    }

    public void pokazPanelStacji(String nazwaStacji) {
        Wypozyczalnia stacja = stacje.get(nazwaStacji);
        if (stacja == null) return;

        rightSidebar.removeAll();
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(60, 60, 60));
        JLabel title = new JLabel("  " + nazwaStacji);
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);

        JButton closeBtn = new JButton("X");
        closeBtn.addActionListener(e -> schowajPrawyPanel());
        header.add(closeBtn, BorderLayout.EAST);
        rightSidebar.add(header, BorderLayout.NORTH);

        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        for (Rower r : stacja.getFlota()) {
            lista.add(stworzWizualnyRower(r, stacja));
        }
        rightSidebar.add(new JScrollPane(lista), BorderLayout.CENTER);

        if (rightSidebarWidth == 0) runRightSidebarAnimation(true);
        else { rightSidebar.revalidate(); rightSidebar.repaint(); }
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

    public void schowajPrawyPanel() { runRightSidebarAnimation(false); }

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

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
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
        btn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        return btn;
    }

    private JPanel stworzWizualnyRower(Rower r, Wypozyczalnia stacja) {
        // 1. Główna karta
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        // Zwiększamy nieco wysokość karty (np. do 130), żeby wszystko się ładnie zmieściło
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setPreferredSize(new Dimension(0, 160));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // 2. OBRAZEK (Lewa strona)
        ImageIcon icon = new ImageIcon(r.getImagePath());
        Image img = icon.getImage().getScaledInstance(150, 110, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(img));
        // Wyrównanie obrazka do góry
        iconLabel.setVerticalAlignment(SwingConstants.TOP);
        card.add(iconLabel, BorderLayout.WEST);

        // 3. PRAWA STRONA (Panel zawierający Tekst i Przycisk pod spodem)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 5)); // 5px odstępu między tekstem a przyciskiem
        rightPanel.setBackground(Color.WHITE);

        // --- TEKST ---
        // Teraz tekst ma dużo miejsca, więc zwiększamy szerokość w HTML do 160px
        String opisHtml = "<html><div style='width: 150px;'>" +
                "<b style='font-size:13px; color:#2c3e50;'>" + r.getModel() + "</b><br>" +
                "<div style='margin-top:4px; font-size:10px; color:#7f8c8d;'>" + r.getOpis() + "</div>" +
                "</div></html>";

        JLabel textLabel = new JLabel(opisHtml);
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        rightPanel.add(textLabel, BorderLayout.CENTER);

        // --- PRZYCISK ---
        JButton rentBtn = new JButton(r.isDostepny() ? "WYPOŻYCZ TERAZ" : "OBECNIE ZAJĘTY");
        rentBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        rentBtn.setFocusPainted(false);

        // Usuwamy obramowanie przycisku dla nowocześniejszego wyglądu (płaski design)
        rentBtn.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        if (r.isDostepny()) {
            rentBtn.setBackground(new Color(39, 174, 96)); // Zieleń
            rentBtn.setForeground(Color.WHITE);
            rentBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            rentBtn.setBackground(new Color(149, 165, 166)); // Szary
            rentBtn.setForeground(Color.WHITE);
            rentBtn.setEnabled(false);
        }

        rightPanel.add(rentBtn, BorderLayout.SOUTH);

        card.add(rightPanel, BorderLayout.CENTER);

        // Logika przycisku (bez zmian)
        rentBtn.addActionListener(e -> {
            r.setDostepny(false);
            Wypozyczenie nowe = new Wypozyczenie(r, stacja, aktualnyKlient);
            aktualnyKlient.dodajDoHistorii(nowe);
            historiaGlobalna.add(nowe);
            zapiszWszystkoDoPliku();
            pokazPanelStacji(stacja.getNazwa());
            odswiezMojeWypozyczenia();
            JOptionPane.showMessageDialog(this, "Wypożyczono rower: " + r.getModel());
        });

        return card;
    }

    // dzieki temu moge uzyc html w regulaminie
    private JPanel simplePanel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Marginesy

        // Zmiana na JEditorPane pozwala używać HTML
        JEditorPane area = new JEditorPane();
        area.setContentType("text/html"); // Ważne: tryb HTML
        area.setEditable(false);
        area.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Domyślna czcionka

        // Automatyczne dodanie znaczników HTML, jeśli ich nie ma w pliku
        if (!text.trim().startsWith("<html>")) {
            text = "<html><body style='font-family: sans-serif; padding: 10px;'>"
                    + text.replace("\n", "<br>")
                    + "</body></html>";
        }

        area.setText(text);
        area.setCaretPosition(0); // Przewiń na samą górę

        p.add(new JScrollPane(area), BorderLayout.CENTER);
        return p;
    }
    private void zapiszWszystkoDoPliku() {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("wypozyczenia.txt", false)))) { // false = nadpisz plik
            for (Wypozyczenie w : historiaGlobalna) {
                out.println(w.getKlient().getImie() + ";" +
                        w.getKlient().getNazwisko() + ";" +
                        w.getRower().getModel() + ";" +
                        w.getWypozyczalnia().getNazwa());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void wczytajWypozyczeniaZPliku() {
        // 1. CZYŚCIMY WSZYSTKO, żeby uniknąć duplikatów przy przelogowaniu
        historiaGlobalna.clear();

        // Czyścimy listy u każdego klienta w statycznej bazie
        for (Klient k : bazaKlientow.values()) {
            // Zakładam, że masz metodę getMojeWypozyczenia() zwracającą listę
            k.getMojeWypozyczenia().clear();
        }

        // Resetujemy statusy rowerów na stacjach (żeby nie były "Zajęte" na stałe)
        for (Wypozyczalnia s : stacje.values()) {
            for (Rower r : s.getFlota()) {
                r.setDostepny(true);
            }
        }

        File plik = new File("wypozyczenia.txt");
        if (!plik.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(plik))) {
            String linia;
            while ((linia = br.readLine()) != null) {
                String[] d = linia.split(";");
                if (d.length < 4) continue;

                Wypozyczalnia st = stacje.get(d[3]);
                if (st != null) {
                    for (Rower r : st.getFlota()) {
                        if (r.getModel().equals(d[2])) {
                            r.setDostepny(false);

                            String kluczKlienta = d[0] + "_" + d[1];
                            Klient kl = bazaKlientow.get(kluczKlienta);

                            if (kl == null) {
                                kl = new Klient(d[0], d[1]);
                                bazaKlientow.put(kluczKlienta, kl);
                            }

                            Wypozyczenie w = new Wypozyczenie(r, st, kl);

                            // Dodajemy do historii tylko jeśli jeszcze jej tam nie ma
                            // (choć clear() powyżej już to załatwił)
                            historiaGlobalna.add(w);
                            kl.dodajDoHistorii(w);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}