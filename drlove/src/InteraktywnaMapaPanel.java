import javax.swing.*;
import java.awt.*;

public class InteraktywnaMapaPanel extends JPanel {

    private final String MAP_IMAGE_PATH = "mapa.png";
    private final String MARKER_ICON_PATH = "marker_icon.png";

    private Image mapa;
    private ImageIcon markerIcon;

    private boolean czyAdmin;

    public InteraktywnaMapaPanel(boolean czyAdmin) {
        this.czyAdmin = czyAdmin;

        setLayout(null); // ręczne pozycjonowanie markerów

        // 🔹 rozmiar panelu – KLUCZOWE
        setPreferredSize(new Dimension(800, 600));

        // 🔹 wczytanie mapy
        mapa = new ImageIcon(MAP_IMAGE_PATH).getImage();

        // 🔹 wczytanie markera
        markerIcon = new ImageIcon(MARKER_ICON_PATH);

        // 🔹 markery
        dodajMarker("Baza rowerów A", 470, 140);
        dodajMarker("Baza rowerów B", 240, 200);
        dodajMarker("Baza rowerów C", 350, 350);
    }

    private void dodajMarker(String nazwa, int x, int y) {
        JButton marker = new JButton(markerIcon);

        marker.setBorderPainted(false);
        marker.setContentAreaFilled(false);
        marker.setFocusPainted(false);
        marker.setToolTipText(nazwa);

        int w = markerIcon.getIconWidth();
        int h = markerIcon.getIconHeight();

        // „czubek” markera trafia w punkt (x, y)
        marker.setBounds(x - w / 2, y - h, w, h);

        marker.addActionListener(e -> {
            if (czyAdmin) {
                // 🛑 SCENARIUSZ ADMINA
                // Admin NIE MOŻE wypożyczać. Widzi tylko panel informacyjny.
                JOptionPane.showMessageDialog(
                        this,
                        "Jesteś zalogowany jako Administrator.\n" +
                                "Opcja wypożyczania jest zablokowana.\n" +
                                "Możesz jedynie zarządzać stacją: " + nazwa,
                        "Tryb Administratora",
                        JOptionPane.WARNING_MESSAGE // Ikona ostrzeżenia
                );
            } else {
                // ✅ SCENARIUSZ UŻYTKOWNIKA (USER)
                // Tylko User widzi pytanie o wypożyczenie roweru.
                int decyzja = JOptionPane.showConfirmDialog(
                        this,
                        "Czy chcesz wypożyczyć rower z: " + nazwa + "?",
                        "Wypożyczenie roweru",
                        JOptionPane.YES_NO_OPTION // Przyciski TAK / NIE
                );

                // Jeśli użytkownik kliknął TAK (YES_OPTION)
                if (decyzja == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Rower został pomyślnie wypożyczony!\nMiłej jazdy!",
                            "Sukces",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    // Tutaj w przyszłości dodasz kod, który np. zmniejsza liczbę rowerów w bazie
                }
            }
        });

        add(marker);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapa != null) {
            g.drawImage(mapa, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.RED);
            g.drawString("Nie załadowano mapa.png", 20, 20);
        }
    }
}
