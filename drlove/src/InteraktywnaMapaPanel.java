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

    private final int BASE_WIDTH = 800;
    private final int BASE_HEIGHT = 600;
    private final List<MarkerData> listaMarkerow = new ArrayList<>();

    public InteraktywnaMapaPanel(boolean czyAdmin, WypozyczalniaRowerowApp parentApp) {
        this.parentApp = parentApp;
        this.czyAdmin = czyAdmin;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));

        // Wczyty  obrazków
        this.mapa = new ImageIcon("mapa.png").getImage();
        ImageIcon originalIcon = new ImageIcon("marker_icon.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        this.markerIcon = new ImageIcon(scaledImage);

        // stacje
        dodajMarker("Baza rowerów A", 750, 270);
        dodajMarker("Baza rowerów B", 250, 155);
        dodajMarker("Baza rowerów C", 330, 450);

        // Listener do przeliczania pozycji przy zmianie rozmiaru okna
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { aktualizujPozycjeMarkerow(); }
        });
    }

    private void dodajMarker(String nazwa, int x, int y) {
        JButton markerBtn = new JButton(this.markerIcon);
        markerBtn.setContentAreaFilled(false);
        markerBtn.setBorderPainted(false);
        markerBtn.setFocusPainted(false);
        markerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        markerBtn.addActionListener((e) -> {
            if (this.czyAdmin) {
                JOptionPane.showMessageDialog(this, "Tryb Administratora: " + nazwa);
            } else {
                if (parentApp != null) parentApp.pokazPanelStacji(nazwa);
            }
        });

        listaMarkerow.add(new MarkerData(markerBtn, x, y));
        this.add(markerBtn);
    }

    private void aktualizujPozycjeMarkerow() {
        double scaleX = (double) getWidth() / BASE_WIDTH;
        double scaleY = (double) getHeight() / BASE_HEIGHT;
        int w = (markerIcon.getIconWidth() <= 0) ? 32 : markerIcon.getIconWidth();
        int h = (markerIcon.getIconHeight() <= 0) ? 32 : markerIcon.getIconHeight();

        for (MarkerData data : listaMarkerow) {
            int newX = (int) (data.baseX * scaleX);
            int newY = (int) (data.baseY * scaleY);
            data.button.setBounds(newX - w / 2, newY - h, w, h);
        }
    }
//
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.mapa != null) {
            g.drawImage(this.mapa, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private static class MarkerData {
        JButton button;
        int baseX, baseY;
        MarkerData(JButton b, int x, int y) { this.button = b; this.baseX = x; this.baseY = y; }
    }
}