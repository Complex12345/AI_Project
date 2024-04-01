import javax.swing.*;
import java.awt.*;

public class Program {
    JFrame frame;

    private static final int DEFAULT_WIDTH = 1050;
    private static final int DEFAULT_HEIGHT = 800;

    public Program(){
        frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.21;
        //frame.add(new SideMenu(), gbc);
        gbc.weightx = 0.75;
        gbc.weighty = 1;
        frame.add(new Hospital(),gbc);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }


    public static void main(String[] args) {
        new Program();
    }


}