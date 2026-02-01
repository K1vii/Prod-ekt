import javax.swing.*;
import java.awt.*;

public class PanelNaszeRowery extends JPanel {

    public PanelNaszeRowery() {
        // Ustawienia wizualne kontener listy
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Katalog rowerów

        add(kartaRoweru("Rower Miejski Gazelle", "rower1.png",
                "Idealny towarzysz do codziennych dojazdów do pracy czy szkoły. <br>" +
                        "Posiada wygodne, szerokie siodełko oraz pojemny <b>koszyk na zakupy</b>. " +
                        "Wyposażony w pełne oświetlenie LED i błotniki."));

        add(Box.createVerticalStrut(30));

        add(kartaRoweru("Góral Kross Hexagon", "rower2.png",
                "Stworzony do jazdy w trudnym terenie. <br>" +
                        "Szerokie opony z głębokim bieżnikiem i <b>przedni amortyzator</b> " +
                        "zapewniają przyczepność na piasku i błocie. Lekka aluminiowa rama."));

        add(Box.createVerticalStrut(30));

        add(kartaRoweru("E-Bike Specialized", "rower3.png",
                "Nowoczesny rower ze wspomaganiem elektrycznym.<br>" +
                        "Bateria pozwala przejechać nawet <b>100 km</b> na jednym ładowaniu. " +
                        "Świetny wybór dla osób, które chcą unikać zmęczenia na podjazdach."));

        add(Box.createVerticalStrut(30));

        add(kartaRoweru("Szosa Trek Domane+", "rower4.png",
                "Połączenie klasycznej kolarzówki z dyskretnym silnikiem elektrycznym.<br>" +
                        "Karbonowa rama, <b>kierownica typu baranek</b> i hydrauliczne hamulce tarczowe. " +
                        "Najszybszy rower w naszej ofercie, idealny na asfalt."));

        add(Box.createVerticalGlue());
    }

    // Cały opis rowerka w sidebarze
    private JPanel kartaRoweru(String nazwa, String imagePath, String opisHtml) {
        JPanel card = new JPanel(new BorderLayout(25, 0));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        card.setPreferredSize(new Dimension(0, 220));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(260, 190, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
        card.add(imageLabel, BorderLayout.WEST);

        String htmlContent = "<html>" +
                "<body style='width: 350px;'>" +
                "<h1 style='color: #2c3e50; margin-bottom: 10px; font-family: sans-serif; font-size: 18px;'>" + nazwa + "</h1>" +
                "<div style='font-family: sans-serif; font-size: 13px; color: #555; line-height: 1.5;'>" +
                opisHtml +
                "</div>" +
                "</body></html>";

        JLabel textLabel = new JLabel(htmlContent);
        textLabel.setVerticalAlignment(SwingConstants.TOP);
        card.add(textLabel, BorderLayout.CENTER);

        return card;
    }
}