package Client;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {

    public Gui()
    {
        setTitle("Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);

        add(new JLabel("To close the server click on the red x"), BorderLayout.SOUTH);
    }

}
