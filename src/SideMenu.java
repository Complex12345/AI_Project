import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SideMenu extends JPanel implements ActionListener{
    public SideMenu() {
        this.setLayout(null);
        JLabel titleLabel = new JLabel("Side Menu");
        add(titleLabel);
        this.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Side Menu", 10, 10);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
