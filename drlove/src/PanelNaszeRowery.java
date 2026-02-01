import javax.swing.*;
import java.awt.*;

public class PanelNaszeRowery extends JPanel {

    public PanelNaszeRowery() {
        // Ustawiamy układ pionowy (lista elementów jeden pod drugim)
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        // Dodajemy margines dookoła całej listy
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- LISTA ROWERÓW ---

        // Rower 1
        add(kartaRoweru("Rower Miejski Gazelle", "rower1.png",
                "Idealny towarzysz do codziennych dojazdów do pracy czy szkoły. <br>" +
                        "Posiada wygodne, szerokie siodełko oraz pojemny <b>koszyk na zakupy</b>. " +
                        "Wyposażony w pełne oświetlenie LED i błotniki."));

        add(Box.createVerticalStrut(30)); // Większy odstęp między kartami (było 20)

        // Rower 2
        add(kartaRoweru("Góral Kross Hexagon", "rower2.png",
                "Stworzony do jazdy w trudnym terenie. <br>" +
                        "Szerokie opony z głębokim bieżnikiem i <b>przedni amortyzator</b> " +
                        "zapewniają przyczepność na piasku i błocie. Lekka aluminiowa rama."));

        add(Box.createVerticalStrut(30));

        // Rower 3
        add(kartaRoweru("E-Bike Specialized", "rower3.png",
                "Nowoczesny rower ze wspomaganiem elektrycznym.<br>" +
                        "Bateria pozwala przejechać nawet <b>100 km</b> na jednym ładowaniu. " +
                        "Świetny wybór dla osób, które chcą unikać zmęczenia na podjazdach."));

        add(Box.createVerticalStrut(30));

        // Rower 4
        add(kartaRoweru("Szosa Trek Domane+", "rower4.png",
                "Połączenie klasycznej kolarzówki z dyskretnym silnikiem elektrycznym.<br>" +
                        "Karbonowa rama, <b>kierownica typu baranek</b> i hydrauliczne hamulce tarczowe. " +
                        "Najszybszy rower w naszej ofercie, idealny na asfalt."));

        // "Klej" na dole
        add(Box.createVerticalGlue());
    }

    private JPanel kartaRoweru(String nazwa, String imagePath, String opisHtml) {
        JPanel card = new JPanel(new BorderLayout(25, 0)); // 25px odstępu między fotką a tekstem
        card.setBackground(Color.WHITE);

        // --- ZMIANA 1: Zwiększamy wysokość karty z 160 na 220 pikseli ---
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        card.setPreferredSize(new Dimension(0, 220));

        // Ramka
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // 1. ZDJĘCIE (Po lewej stronie)
        ImageIcon icon = new ImageIcon(imagePath);

        // --- ZMIANA 2: Skalujemy zdjęcie do rozmiaru 260x190 (było 180x130) ---
        Image img = icon.getImage().getScaledInstance(260, 190, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));

        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        card.add(imageLabel, BorderLayout.WEST);

        // 2. OPIS (Po prawej stronie)
        String htmlContent = "<html>" +
                "<body style='width: 350px;'>" + // Zmniejszyłem szerokość tekstu, żeby nie był za szeroki
                "<h1 style='color: #2c3e50; margin-bottom: 10px; font-family: sans-serif; font-size: 18px;'>" + nazwa + "</h1>" +
                "<div style='font-family: sans-serif; font-size: 13px; color: #555; line-height: 1.5;'>" +
                opisHtml +
                "</div>" +
                "</body></html>";

        JLabel textLabel = new JLabel(htmlContent);
        textLabel.setVerticalAlignment(SwingConstants.TOP); // Tekst zaczyna się od góry

        card.add(textLabel, BorderLayout.CENTER);

        return card;
    }
}