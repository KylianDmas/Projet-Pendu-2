import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;


/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */    
    private Button boutonMaison;
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */ 
    private Button bJouer;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./img");
        // A terminer d'implementer
        this.chrono = new Chronometre();
        this.modelePendu.nbErreursRestantes = 0;
         
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.modeAccueil());
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private BorderPane titre(){
        BorderPane bp = new BorderPane();
        bp.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, null, null)));

        HBox h = new HBox();

        ImageView i1 = new ImageView("file:img/home.png");
        i1.setFitHeight(40);
        i1.setFitWidth(40);
        Button b1 = new Button("", i1);

        ImageView i2 = new ImageView("file:img/parametres.png");
        i2.setFitHeight(40);
        i2.setFitWidth(40);
        Button b2 = new Button("", i2);

        ImageView i3 = new ImageView("file:img/info.png");
        i3.setFitHeight(40);
        i3.setFitWidth(40);
        Button b3 = new Button("", i3);

        b1.setOnAction(null);
        b2.setOnAction(null);
        b3.setOnAction(null);
        h.getChildren().addAll(b1, b2, b3);

        Text text = new Text ("Jeu du pendu") ;
        text.setFont(Font.font("Arial",FontWeight.BOLD,30)) ;        
        bp.setLeft(text);
        bp.setRight(h);
        return bp;
    }

    // /**
     // * @return le panel du chronomètre
     // */
    // private TitledPane leChrono(){
        // A implementer
        // TitledPane res = new TitledPane();
        // return res;
    // }

    // /**
     // * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     // *         de progression et le clavier
     // */
    // private Pane fenetreJeu(){
        // A implementer
        // Pane res = new Pane();
        // return res;
    // }

    // /**
     // * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     // */
    // private Pane fenetreAccueil(){
        // A implementer    
        // Pane res = new Pane();
        // return res;
    // }

    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire){
        for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File(repertoire+"/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public BorderPane modeAccueil(){
        // A implementer
        this.panelCentral = new BorderPane(); 
        VBox vbb = new VBox();
        bJouer = new Button("Lancer une partie");
        bJouer.setOnAction(new ControleurLancerPartie(modelePendu, this));
        vbb.getChildren().add(bJouer);
        this.panelCentral.setTop(bJouer);

        VBox vb = new VBox();
        ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("FACILE");
        RadioButton rb2 = new RadioButton("MOYEN");
        RadioButton rb3 = new RadioButton("DIFFICILE");
        RadioButton rb4 = new RadioButton("EXPERT");
        rb1.setToggleGroup(group);
        rb2.setToggleGroup(group);
        rb3.setToggleGroup(group);
        rb4.setToggleGroup(group);

        vb.getChildren().addAll(rb1, rb2, rb3, rb4);
        TitledPane tp = new TitledPane("Niveau de difficulté", vb);
        vbb.getChildren().add(tp);
        this.panelCentral.setCenter(vbb);
        return this.panelCentral;
    }
    
    public void modeJeu(){
        // A implementer
        BorderPane pagejeu = new BorderPane();
        VBox vb = new VBox();
        this.dessin = new ImageView("file:img/pendu0.png");
        this.clavier = new Clavier("abcdefghijklmnopqrstuvwxyz", new ControleurLettres(modelePendu, this));
        this.pg = new ProgressBar(modelePendu.getMotATrouve().length());
        Label lmot = new Label(modelePendu.getMotCrypte());
        vb.getChildren().addAll(lmot, dessin, pg, clavier);
        pagejeu.setLeft(vb);
        VBox vb2 = new VBox();
        this.chrono = new Chronometre();
        bJouer.setText("Nouveau mot");
        /*Text tlvl = this.leNiveau; */
        vb2.getChildren().addAll(chrono, bJouer);
        pagejeu.setRight(vb2);
        this.panelCentral.setCenter(pagejeu);

    }
    
    public void modeParametres(){
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie(){
        // A implementer
        this.modeJeu();

    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){
        
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono(){
        // A implémenter
        return this.chrono;
    }

    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }
        
    public Alert popUpReglesDuJeu(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Regles du jeu");
        return alert;
    }
    
    public Alert popUpMessageGagne(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);    
        alert.setTitle("Felicitations");    
        return alert;
    }
    
    public Alert popUpMessagePerdu(){
        // A implementer    
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nullos");
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
