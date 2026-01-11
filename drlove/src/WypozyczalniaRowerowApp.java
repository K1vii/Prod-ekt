import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WypozyczalniaRowerowApp extends JFrame {

    private ArrayList<Rower> rowery = new ArrayList<>();
    private DefaultListModel<Rower> modelListy = new DefaultListModel<>();

    public WypozyczalniaRowerowApp() {
        setTitle("Wypożyczalnia Rowerów");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Dane testowe
        dodajRower(new Rower("R1", "Miejski"));
        dodajRower(new Rower("R2", "Górski"));
        dodajRower(new Rower("R3", "Elektryczny"));

        JTabbedPane zakladki = new JTabbedPane();
        zakladki.addTab("🗺️ Mapa", new InteraktywnaMapaPanel());
        zakladki.addTab("🚲 Wypożycz", panelWypozyczania());
        zakladki.addTab("📋 Lista rowerów", panelListyRowerow());

        add(zakladki);
    }

    private void dodajRower(Rower r) {
        rowery.add(r);
        modelListy.addElement(r);
    }

    private JPanel panelListyRowerow() {
        JPanel panel = new JPanel(new BorderLayout());
        JList<Rower> lista = new JList<>(modelListy);
        panel.add(new JScrollPane(lista), BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelWypozyczania() {
        JPanel panel = new JPanel(new BorderLayout());

        JList<Rower> lista = new JList<>(modelListy);
        JScrollPane scroll = new JScrollPane(lista);

        JButton wypozycz = new JButton("Wypożycz");
        JButton zwroc = new JButton("Zwróć");

        wypozycz.addActionListener(e -> {
            Rower r = lista.getSelectedValue();
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Nie wybrano roweru");
                return;
            }
            if (!r.isDostepny()) {
                JOptionPane.showMessageDialog(this, "Rower już wypożyczony");
                return;
            }
            r.wypozycz();
            lista.repaint();
        });

        zwroc.addActionListener(e -> {
            Rower r = lista.getSelectedValue();
            if (r == null) {
                JOptionPane.showMessageDialog(this, "Nie wybrano roweru");
                return;
            }
            r.zwroc();
            lista.repaint();
        });

        JPanel przyciski = new JPanel();
        przyciski.add(wypozycz);
        przyciski.add(zwroc);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(przyciski, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new WypozyczalniaRowerowApp().setVisible(true)
        );
    }
}