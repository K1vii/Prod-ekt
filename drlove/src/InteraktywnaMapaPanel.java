import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class InteraktywnaMapaPanel extends JPanel {
    private WypozyczalniaRowerowApp parentApp;

    private Image mapa;
    private ImageIcon markerIcon;
    private boolean czyAdmin;

    // Rozmiar bazowy, dla którego ustaliłeś oryginalne współrzędne (470, 140 itd.)
    private final int BASE_WIDTH = 800;
    private final int BASE_HEIGHT = 600;

    // Lista do przechowywania danych o markerach, aby móc je przeliczać
    private final List<MarkerData> listaMarkerow = new ArrayList<>();

    public InteraktywnaMapaPanel(boolean czyAdmin, WypozyczalniaRowerowApp parentApp) {
        this.parentApp = parentApp;
        this.czyAdmin = czyAdmin;
        this.setLayout(null); // Pozostajemy przy null, ale będziemy nim zarządzać
        this.setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));

        this.mapa = new ImageIcon("mapa.png").getImage();
        this.markerIcon = new ImageIcon("marker_icon.png");

        // Dodajemy dane markerów do listy
        dodajMarker("Baza rowerów A", 470, 140);
        dodajMarker("Baza rowerów B", 240, 200);
        dodajMarker("Baza rowerów C", 350, 350);

        // KLUCZ: Listener, który reaguje na zmianę rozmiaru panelu (np. przy wysuwaniu paska)
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                aktualizujPozycjeMarkerow();
            }
        });
    }

    private void dodajMarker(String nazwa, int x, int y) {
        JButton markerBtn = new JButton(this.markerIcon);
        // ... reszta stylu przycisku ...

        markerBtn.addActionListener((e) -> {
            System.out.println(">>> KLIKNIĘTO MARKER: " + nazwa); // LOG 1

            if (this.czyAdmin) {
                System.out.println(">>> TRYB ADMIN: Pokazuję okno dialogowe");
                JOptionPane.showMessageDialog(this, "Tryb Administratora: " + nazwa);
            } else {
                if (parentApp != null) {
                    System.out.println(">>> TRYB USER: Wywołuję pokazPanelStacji()");
                    parentApp.pokazPanelStacji(nazwa);
                } else {
                    System.err.println(">>> BŁĄD: parentApp jest NULL! Zapomniałeś przekazać 'this' w konstruktorze mapy?");
                }
            }
        });

        listaMarkerow.add(new MarkerData(markerBtn, x, y));
        this.add(markerBtn);
    }

    private void aktualizujPozycjeMarkerow() {
        double scaleX = (double) getWidth() / BASE_WIDTH;
        double scaleY = (double) getHeight() / BASE_HEIGHT;

        // Zabezpieczenie: jeśli ikona się nie załadowała, dajemy domyślny rozmiar 32x32
        int w = (markerIcon.getIconWidth() <= 0) ? 32 : markerIcon.getIconWidth();
        int h = (markerIcon.getIconHeight() <= 0) ? 32 : markerIcon.getIconHeight();

        for (MarkerData data : listaMarkerow) {
            int newX = (int) (data.baseX * scaleX);
            int newY = (int) (data.baseY * scaleY);
            data.button.setBounds(newX - w / 2, newY - h, w, h);
            System.out.println("Pozycjonowanie markera: " + data.button.getToolTipText() + " na: " + newX + "," + newY);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.mapa != null) {
            // Skalowanie mapy do pełnego rozmiaru panelu
            g.drawImage(this.mapa, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Klasa pomocnicza do przechowywania danych o markerze
    private static class MarkerData {
        JButton button;
        int baseX;
        int baseY;

        MarkerData(JButton button, int x, int y) {
            this.button = button;
            this.baseX = x;
            this.baseY = y;
        }
    }
}