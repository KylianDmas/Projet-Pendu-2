import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Génère la vue d'un clavier et associe le contrôleur aux touches
 * le choix ici est d'un faire un héritié d'un TilePane
 */
public class Clavier extends TilePane{
    /**
     * il est conseillé de stocker les touches dans un ArrayList
     */
    private List<Button> clavier;

    /**
     * constructeur du clavier
     * @param touches une chaine de caractères qui contient les lettres à mettre sur les touches
     * @param actionTouches le contrôleur des touches
     * @param tailleLigne nombre de touches par ligne
     */
    public Clavier(String touches, EventHandler<ActionEvent> actionTouches) {
        // A implémenter
        this.setAlignment(Pos.CENTER);
        //this.setPadding(new Insets(10));
        this.clavier = new ArrayList<>();
        char[] chars = touches.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            Button b = new Button(Character.toString(c).toUpperCase());
            b.setOnAction(actionTouches);
            this.getChildren().add(b);
            this.clavier.add(b);
        }
    }

    /**
     * permet de désactiver certaines touches du clavier (et active les autres)
     * @param touchesDesactivees une chaine de caractères contenant la liste des touches désactivées
     */
    public void desactiveTouches(String touchesDesactivees){
        // A implémenter
        Set<Character> touches = new HashSet<>();
        for (char lettre : touchesDesactivees.toCharArray()) {
            touches.add(lettre);
        }
        for (Button bouton : clavier) {
            if (touches.contains(bouton.getText().toCharArray()[0])) {
                bouton.setDisable(true);
            } else {
                bouton.setDisable(false);
            }
        }
    }
}
