import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;

/**
 * Controleur des radio boutons gérant le niveau
 */
public class ControleurNiveau implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;

    private Pendu vueDuPendu;


    /**
     * @param modelePendu modèle du jeu
     */
    public ControleurNiveau(MotMystere modelePendu, Pendu vueDuPendu) {
        // A implémenter
        this.modelePendu = modelePendu;
        this.vueDuPendu = vueDuPendu;
    }

    /**
     * gère le changement de niveau
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // A implémenter
        RadioButton radiobouton = (RadioButton) actionEvent.getTarget();
        String nomDuRadiobouton = radiobouton.getText();

        for (int i = 0; i < vueDuPendu.niveaux.size(); i++) {
            if (nomDuRadiobouton.equals(vueDuPendu.niveaux.get(i))) {
                this.modelePendu.setNiveau(i);
            }
        }
    }
}
