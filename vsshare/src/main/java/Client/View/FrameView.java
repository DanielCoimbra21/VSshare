package Client.View;

import javax.swing.*;
import java.awt.*;

public class FrameView extends JFrame {

    // création de la taille et de la forme du smartphone
    private static final Dimension SIZE = new Dimension(1000, 800) ;

    // création des diverses vues
    private BottomBar bottomBar = new BottomBar() ;
    private static JPanel contenuApp = new JPanel() ;
    private static JPanel app ;
    private static JPanel topBarTP ;
    private static JPanel contenuTopBar = new JPanel() ;

    // Cardlayout va spécifier comment le smartphone va fonctionner
    private static CardLayout cardLayout = new CardLayout() ;
    private static CardLayout cardLayoutTP = new CardLayout() ;
    private InitialisationView initialisationView = new InitialisationView();


    public FrameView(JPanel newApp, JPanel newTopBar){
        if(newApp == null){
            app = initialisationView.getMainPanel() ;
            contenuApp.add(app);
            topBarTP = initialisationView.getTopBar();
            contenuTopBar.add(topBarTP) ;

            setPreferredSize(SIZE);
            setVisible(true);

            contenuApp.setLayout(cardLayout);
            contenuTopBar.setLayout(cardLayoutTP);

            setLayout(new BorderLayout());
            add(contenuTopBar, BorderLayout.NORTH) ;
            add(contenuApp, BorderLayout.CENTER) ;
            add(bottomBar, BorderLayout.SOUTH) ;

            pack();
            setLocationRelativeTo(null);
        }
        else{
            switchApplication(newApp, newTopBar);
        }
    }

    public void switchApplication(JPanel newApp, JPanel newTopBar){
        // changement de l'app
        contenuApp.remove(app);
        app = newApp ;
        contenuApp.add(app);
        cardLayout.next(contenuApp);

        // changement de la topBar
        contenuTopBar.remove(topBarTP);
        topBarTP = newTopBar ;
        contenuTopBar.add(topBarTP);
        cardLayoutTP.next(contenuTopBar);
    }
}
