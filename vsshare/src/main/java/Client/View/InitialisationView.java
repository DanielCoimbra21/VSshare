package Client.View;

public class InitialisationView {
    // création des variables impossibles à créer dans la classe Smartphone
    private MainPanel mainPanel ;
    private TopBar topBar;

    /**
     * Constructeur qui sera appelé une fois uniquement à l'initialisation du smartphone.
     * Est utile car si l'on crée un HomeScreen dans la classe Smartphone et que l'on crée un Smartphone dans la classe HomeScreen, cela crée des classes en boucle
     * Afin d'éviter ceci, on fait appel à la classe HomeScreen dans une classe tierce (celle-ci) de manière à ne pas devoir l'appeler dans la classe Smartphone
     * Fait de même avec la topBar (car même soucis)
     */

    public InitialisationView() {
        mainPanel = new MainPanel() ;
        topBar = new TopBar() ;
    }

    /**
     * Méthode permettant à la classe Smartphone de Get le HomeScreen lors de l'initialisation
     * @return
     * retourne le panel de HomeScreen
     */

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Méthode permettant à la classe Smartphone de Get la topBar lors de l'initialisation
     * @return
     * retourne le panel de la topBar
     */

    public TopBar getTopBar(){
        return topBar;
    }
}
