
import javax.swing.*;
import java.awt.*;

public class PanelNaszeRowery extends JPanel {

    public PanelNaszeRowery() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.NONE;

        // ===== GÓRA =====
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(kartaRoweru("Rower miejski", "rower1.jfif"), gbc);

        gbc.gridx = 1;
        add(kartaRoweru("Rower górski", "rower2.jfif"), gbc);

        // ===== DÓŁ (ŚRODEK) =====
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // zajmuje 2 kolumny
        gbc.anchor = GridBagConstraints.CENTER;

        add(kartaRoweru("Rower elektryczny", "rower3.jfif"), gbc);
    }

    private JPanel kartaRoweru(String nazwa, String imagePath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel label = new JLabel(nazwa, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(label, BorderLayout.SOUTH);

        return panel;
    }
}