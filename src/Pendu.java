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
    private Button boutonParametres = new Button();

    private Button boutonInfo = new Button();
    /**
     * le bouton Accueil / Maison
     */    
    private Button boutonMaison = new Button();
    /**
     * le bouton qui permet de (lancer ou relancer une partie
     */ 
    private Button bJouer;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10, this.clavier);
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
        fenetre.setCenter(this.panelCentral);
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
        boutonMaison.setGraphic(i1);
        boutonMaison.setOnAction(new RetourAccueil(this));

        ImageView i2 = new ImageView("file:img/parametres.png");
        i2.setFitHeight(40);
        i2.setFitWidth(40);
        boutonParametres.setGraphic(i2);

        ImageView i3 = new ImageView("file:img/info.png");
        i3.setFitHeight(40);
        i3.setFitWidth(40);
        boutonInfo.setGraphic(i3);

        boutonMaison.setOnAction(null);
        boutonParametres.setOnAction(null);
        boutonInfo.setOnAction(null);
        h.getChildren().addAll(boutonMaison, boutonParametres, boutonInfo);

        Text text = new Text ("Jeu du pendu") ;
        text.setFont(Font.font("Arial",FontWeight.BOLD,30)) ;        
        bp.setLeft(text);
        bp.setRight(h);
        return bp;
    }

    // /**
     // * @return le panel du chronomètre
     // */
    private TitledPane leChrono(){
        // A implementer
        TitledPane res = new TitledPane();
        res.setText("Chronomètre");
        res.setCollapsible(false);
        res.setContent(this.chrono);
        return res;
    }

    // /**
     // * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
     // *         de progression et le clavier
     // */
    private Pane fenetreJeu(){
        // A implementer
        BorderPane res = new BorderPane();
        VBox left = new VBox(10);
        VBox right = new VBox(50);
        right.setPadding(new Insets(5));
        left.setPadding(new Insets(5));
        res.setLeft(left);
        res.setRight(right);
        left.setAlignment(Pos.BASELINE_CENTER);
        this.leNiveau = new Text("Niveau : "+this.niveaux.get(this.modelePendu.getNiveau()));
        this.leNiveau.setFont(new Font(20));
        right.getChildren().add(this.leNiveau);
        this.motCrypte = new Text(this.modelePendu.getMotCrypte());
        this.motCrypte.setFont(new Font(20));
        left.getChildren().add(this.motCrypte);
        this.dessin = new ImageView(this.lesImages.get(0));
        left.getChildren().add(this.dessin);
        this.pg = new ProgressBar();
        this.pg.setProgress(0);
        left.getChildren().add(this.pg);
        this.clavier = new Clavier("abcdefghijklmnopqrstuvwxyz-", new ControleurLettres(this.modelePendu, this));
        left.getChildren().add(clavier);

        TitledPane chrono = this.leChrono();
        right.getChildren().add(chrono);
    
        Button nouveau = new Button("Nouveau mot");
        nouveau.setOnAction(new ControleurLancerPartie(modelePendu, this));
        right.getChildren().add(nouveau);
        return res;
    }

    // /**
     // * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     // */
    private Pane fenetreAccueil(){
        // A implementer    
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        TitledPane difficulte = new TitledPane();
        difficulte.setText("Niveau de difficulté");
        difficulte.setCollapsible(false);
        VBox pane = new VBox(10);
        difficulte.setContent(pane);
        this.bJouer = new Button("Lancer une partie !");
        this.bJouer.setOnAction(new ControleurLancerPartie(this.modelePendu, this));

        ToggleGroup group = new ToggleGroup();
        RadioButton facile = new RadioButton(niveaux.get(0));
        facile.setToggleGroup(group);
        facile.setOnAction(new ControleurNiveau(this.modelePendu, this));
        group.selectToggle(facile);
        RadioButton medium = new RadioButton(niveaux.get(1));
        medium.setToggleGroup(group);
        medium.setOnAction(new ControleurNiveau(this.modelePendu, this));
        RadioButton difficile = new RadioButton(niveaux.get(2));
        difficile.setToggleGroup(group);
        difficile.setOnAction(new ControleurNiveau(this.modelePendu, this));
        RadioButton expert = new RadioButton(niveaux.get(3));
        expert.setToggleGroup(group);
        expert.setOnAction(new ControleurNiveau(this.modelePendu, this));
        pane.getChildren().addAll(facile, medium, difficile, expert);
        box.getChildren().addAll(this.bJouer, difficulte);

        return box;

    }

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

    public void modeAccueil(){
        // A implementer
        this.panelCentral.setCenter(this.fenetreAccueil());
        this.boutonMaison.setDisable(true);
        this.boutonParametres.setDisable(false);
    }
    
    public void modeJeu(){
        // A implementer
        this.panelCentral.setCenter(this.fenetreJeu());
        this.boutonMaison.setDisable(false);
        this.boutonParametres.setDisable(true);

    }
    
    public void modeParametres(){
        // A implémenter
    }

    /** lance une partie */
    public void lancePartie(){
        // A implementer
        this.modeJeu();
        this.modelePendu.definirClavier(this.clavier);
        this.modelePendu.setMotATrouver();
        this.chrono.resetTime();
        this.chrono.start();
        this.majAffichage();

    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin.setImage(this.lesImages.get(this.modelePendu.getNbErreursMax()-this.modelePendu.getNbErreursRestants()));
        double progress = Double.valueOf(this.modelePendu.getNbErreursMax()-this.modelePendu.getNbErreursRestants())/Double.valueOf(this.modelePendu.getNbErreursMax());
        this.pg.setProgress(progress);

        if(this.modelePendu.perdu()){
            this.popUpMessagePerdu().showAndWait();}
        else if(this.modelePendu.gagne()){
            this.popUpMessageGagne().showAndWait();}
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
        alert.setHeaderText("https://fr.wikipedia.org/wiki/Pendu_(jeu)");
        return alert;
    }
    
    public Alert popUpMessageGagne(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);    
        alert.setTitle("Felicitations");    
        alert.setHeaderText("Trop trop simple");    
        this.chrono.stop(); 
        return alert;
    }
    
    public Alert popUpMessagePerdu(){
        // A implementer    
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nullos");
        alert.setHeaderText("aie aie aie...");
        this.chrono.stop();
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        this.panelCentral = new BorderPane();
        this.niveaux = Arrays.asList("Facile", "Médium", "Difficile", "Expert");
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
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
