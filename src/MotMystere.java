import java.util.Set;
import java.util.Random;
import java.util.HashSet;

/**
 * Modèle pour le jeu du pendu.
 */
public class MotMystere {
    // constantes pour gérer les différents niveaux de jeu
    /** Niveau FACILE : la première lettre et la dernière lettre du mot à trouver sont données ainsi que les éventuels caractères non alphabétiques (traits d'union par exemple)*/
    public final static int FACILE = 0;
    /** Niveau MOYEN : la première lettre du mot à trouver est donnée ainsi que les traits d'union si le mot à trouver en comporte */
    public final static int MOYEN = 1;
    /** Niveau DIFFICILE : seuls les traits d'union si le mot à trouver en comporte */
    public final static int DIFFICILE = 2;
    /** Niveau EXPERT : rien n'est donné, ni lettre ni trait d'union */
    public final static int EXPERT = 3;
   

    /**
     * le mot à trouver
     */
    private String motATrouver;
    /**
     * le niveau de jeu
     */
    private int niveau;
    /**
     * chaine contenant les lettres déjà trouvées et des * à la place des lettres non encore trouvées
     */
    private String motCrypte;
    /**
     * chaine contenant l'ensemble des lettres déjà essayées
     */
    private Set<String> lettresEssayees;
    /**
     * entier inquant le nombre de lettres restant à trouver
     */
    private int nbLettresRestantes;
    /**
     * le nombre d'essais déjà effectués
     */
    private int nbEssais;
    /**
     * le nombre d'erreurs encore possibles
     */
    public int nbErreursRestantes;
    /**
     * le nombre total de tentatives autorisées
     */
    private int nbEerreursMax;
    /**
     * Pour Clavier
     */
    private Clavier clavier;
    /**
     * dictionnaire dans lequel on choisit les mots
     */
    private Dictionnaire dict;


    /**
     * constructeur dans lequel on impose le mot à trouver
     * @param motATrouve mot à trouver
     * @param niveau niveau du jeu
     * @param nbErreursMax le nombre total d'essais autorisés
     */
    public MotMystere(String motATrouver, int niveau, int nbErreursMax, Clavier clavier) {
        super();
        this.initialiserJeu(motATrouver, niveau, nbErreursMax);
        this.clavier = clavier;
    }

    /**
     * Constructeur dans lequel on va initialiser un dictionnaire pour choisir les mots à trouver
     * @param nomFichier est le chemin vers le dictionnaire utilisé qui est un fichier texte
     *  contenant une liste de mots (un mot par ligne)
     * @param longMin longueur minimale des mots retenus dans le dictionnaire
     * @param longMax longueur maximale des mots retenus dans le dictionnaire
     * @param niveau niveau initial de jeu
     * @param nbErreursMax le nombre total d'essais autorisés
     */
    public MotMystere(String nomFichier, int longMin, int longMax, int niveau, int nbErreursMax, Clavier clavier) {
        super();
        this.dict = new Dictionnaire(nomFichier,longMin,longMax);
        String motATrouver = dict.choisirMot();
        this.initialiserJeu(motATrouver, niveau, nbErreursMax);
        this.clavier = clavier;
    }

        /**
     * Initialisation du jeu
     * @param motATrouver le mot à trouver
     * @param niveau le niveau de jeu
     * @param nbErreursMax le nombre total d'essais autorisés
     */
    private void initialiserJeu(String motATrouver, int niveau, int nbErreursMax) {
        this.niveau = niveau;
        this.nbEssais = 0;
        this.motATrouver = Dictionnaire.sansAccents(motATrouver).toUpperCase();
        this.motCrypte = "";
        this.lettresEssayees = new HashSet<>();

        this.nbLettresRestantes = this.motATrouver.length();
        this.masquerMot();
        this.nbEerreursMax = nbErreursMax;
        this.nbErreursRestantes = nbErreursMax;
        
        switch (this.niveau) {
            case FACILE:
                this.devoilerLettresAleatoires(3);
                break;
            case MOYEN:
                this.devoilerLettresAleatoires(2);
                break;
            case DIFFICILE:
                this.devoilerLettresAleatoires(1);
                break;
            case EXPERT:
                break;
            default:
                break;
        }
    }

    /**
     * Dévoile un nombre donné de lettres aléatoires
     * @param nb le nombre de lettres à dévoiler
     */
    private void devoilerLettresAleatoires(int nb) {
        int lettresDevoilees = 0;
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ-";
        Random random = new Random();
        
        while (lettresDevoilees < nb) {
            int indice = random.nextInt(alphabet.length());
            char lettre = alphabet.charAt(indice);
            
            if (devoilerLettre(lettre) > 0) {
                lettresDevoilees++;
                this.lettresEssayees.add(String.valueOf(lettre));
            }
        }
        
        System.out.println(clavier);
        if (clavier != null) {
            System.out.println(String.join("", this.lettresEssayees));
            this.clavier.desactiveTouches(String.join("", this.lettresEssayees));
        }
        
        this.masquerMot();
    }

    /**
     * Définit le clavier à utiliser
     * @param clavier l'objet Clavier à utiliser
     */
    public void definirClavier(Clavier clavier) {
        this.clavier = clavier;
    }

    /**
     * @return le mot à trouver
     */
    public String getMotATrouve() {
        return this.motATrouver;
    }

    /**
     * @return le niveau de jeu
     */
    public int getNiveau(){
        return this.niveau;
    }

    /** réinitialise le jeu avec un nouveau à trouver
     * @param motATrouver le nouveau mot à trouver
     */
    public void setMotATrouver(String motATrouver) {
        this.initialiserJeu(motATrouver, this.niveau, this.nbEerreursMax);
    }

    /**
     * Réinitialise le jeu avec un nouveau mot à trouver choisi au hasard dans le dictionnaire
     */
    public void setMotATrouver() {
        this.initialiserJeu(this.dict.choisirMot(), this.niveau, this.nbEerreursMax);
    }

    /**
     * change le niveau de jeu (n'a pas d'effet en cours de partie)
     * @param niveau le nouveau niveu de jeu
     */
    public void setNiveau(int niveau){
        this.niveau = niveau;
    }

    /**
     * @return le mot avec les lettres trouvées affichées et des étoiles pour les lettres non trouvées
     */
    public String getMotCrypte() {
        return this.motCrypte;
    }

    /**
     * @return les lettres déjà essayées
     */
    public Set<String> getLettresEssayees() {
        return this.lettresEssayees;
    }

    /**
     * @return le nombre de lettres restant à trouver
     */
    public int getNbLettresRestantes() {
        return this.nbLettresRestantes;
    }

    /**
     * @return le nombre d'essais déjà effectués
     */
    public int getNbEssais(){
        return this.nbEssais;
    }

    /**
     * @return le nombre total de tentatives autorisées
     */
    public int getNbErreursMax(){
        return this.nbEerreursMax;
    }

    /**
     * @return le nombre d'erreurs encore autorisées
     */
    public int getNbErreursRestants(){
        return this.nbErreursRestantes;
    }

    /**
     * @return un booléen indiquant si le joueur a perdu
     */
    public boolean perdu(){
        return this.nbErreursRestantes == 0;
    }

    /**
     * @return un booléen indiquant si le joueur a gangé
     */
    public boolean gagne(){
        return this.nbLettresRestantes == 0;
    }

    /**
     * Dévoile une lettre et met à jour le nombre d'erreurs restantes
     * @param lettre la lettre à révéler
     * @return le nombre de nouvelles lettres révélées
     */
    public int devoilerLettre(char lettre) {
        int nouvellesDecouvertes = 0;
        char[] motTemporaire = this.motCrypte.toCharArray();
        for (int index = 0; index < this.motATrouver.length(); index++) {
            if (this.motATrouver.charAt(index) == lettre && this.motCrypte.charAt(index) == '*') {
                nouvellesDecouvertes++;
                motTemporaire[index] = lettre;
            }
        }
        this.motCrypte = String.valueOf(motTemporaire);
        this.nbLettresRestantes -= nouvellesDecouvertes;
        return nouvellesDecouvertes;
    }

    /**
     * Crypte le mot à trouver en masquant les lettres non devinées
     */
    public void masquerMot() {
        char[] motCache = new char[this.motATrouver.length()];
        for (int index = 0; index < this.motATrouver.length(); index++) {
            if (this.lettresEssayees.contains(String.valueOf(this.motATrouver.charAt(index)))) {
                motCache[index] = this.motATrouver.charAt(index);
            } else {
                motCache[index] = '*';
            }
        }
        this.motCrypte = String.valueOf(motCache);
    }

    /**
     * permet au joueur d'essayer une lettre
     * @param lettre la lettre essayée par le joueur
     * @return le nombre de fois où la lettre apparait dans le mot à trouver
     */
    public int essaiLettre(char lettre){
        this.nbEssais += 1;
        int nbNewLetter = this.devoilerLettre(lettre);
        this.lettresEssayees.add(lettre+"");
        this.masquerMot();
        if (nbNewLetter == 0){
            this.nbErreursRestantes-= 1;
    }
    return nbNewLetter;
   }

    /**
     * @return une chaine de caractère donnant l'état du jeu
     */
    public String toString(){
        return "Mot a trouve: "+this.motATrouver+" Lettres trouvees: "+
               this.motCrypte+" nombre de lettres restantes "+this.nbLettresRestantes+
               " nombre d'essais restents: "+this.nbErreursRestantes;
    }

}
