import javax.swing.*;
import java.awt.*;

public class InteraktywnaMapaPanel extends JPanel {

    private final String MAP_IMAGE_PATH = "mapa.png";
    private final String MARKER_ICON_PATH = "marker_icon.png";

    private Image mapa;
    private ImageIcon markerIcon;

    public InteraktywnaMapaPanel() {
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

        marker.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Kliknięto: " + nazwa,
                        "Baza",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

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
