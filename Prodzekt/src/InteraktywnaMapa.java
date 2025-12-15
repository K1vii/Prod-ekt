import javax.swing.*;
import java.awt.*;

public class InteraktywnaMapa extends JFrame {

    private final String MAP_IMAGE_PATH = "mapa.png";
    private final String MARKER_ICON_PATH = "marker_icon.png";

    public InteraktywnaMapa() {
        setTitle("Mapa baz rowerów");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapaPanel mapaPanel = new MapaPanel();
        add(mapaPanel);

        Image image = new ImageIcon(MAP_IMAGE_PATH).getImage();
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        mapaPanel.setPreferredSize(new Dimension(w > 0 ? w : 800, h > 0 ? h : 600));

        pack();
        setLocationRelativeTo(null);
    }

    class MapaPanel extends JPanel {
        private Image tloMapy = new ImageIcon(MAP_IMAGE_PATH).getImage();
        private ImageIcon markerIcon = new ImageIcon(MARKER_ICON_PATH);

        public MapaPanel() {
            // bazy
            setLayout(null);
            dodajMarker("Baza rowerów A", 470, 140);
            dodajMarker("Baza rowerów B", 240, 200);
            dodajMarker("Baza rowerów C", 350, 350);
        }
            // marker
        private void dodajMarker(String nazwa, int x, int y) {
            JButton marker = new JButton(markerIcon);
            marker.setToolTipText(nazwa);
            marker.setBorderPainted(false);
            marker.setFocusPainted(false);
            marker.setContentAreaFilled(false);

            int w = markerIcon.getIconWidth();
            int h = markerIcon.getIconHeight();
            marker.setBounds(x - w / 2, y - h, w, h);

            marker.addActionListener(e ->
                    JOptionPane.showMessageDialog(this, nazwa, "Informacja", JOptionPane.INFORMATION_MESSAGE)
            );
            add(marker);
        }

        @Override // mapka
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(tloMapy, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InteraktywnaMapa().setVisible(true));
    }
}
