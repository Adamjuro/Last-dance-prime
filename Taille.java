import java.util.ArrayList;
import java.util.List;

/**
 * Représente une taille pour un article.
 * Contient un identifiant et un libellé.
 * Permet également de gérer une liste statique de tailles.
 */
public class Taille {

    // =========================
    // Attributs
    // =========================
    private int idTaille;
    private String libelle;

    // =========================
    // Constructeurs
    // =========================

    /** Constructeur par défaut */
    public Taille() {
    }

    /**
     * Constructeur complet
     * @param unIdTaille Identifiant de la taille
     * @param unLibelle Libellé de la taille
     */
    public Taille(int unIdTaille, String unLibelle) {
        this.idTaille = unIdTaille;
        this.libelle = unLibelle;
    }

    // =========================
    // Getters et Setters
    // =========================
    public int getIdTaille() {
        return idTaille;
    }

    public void setIdTaille(int unIdTaille) {
        this.idTaille = unIdTaille;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String unLibelle) {
        this.libelle = unLibelle;
    }

    // =========================
    // Gestion de la liste statique de tailles
    // =========================
    private static List<Taille> lesTailles = new ArrayList<>();

    public static void setLesTailles(List<Taille> lesTailles) {
        Taille.lesTailles = lesTailles;
    }

    public static List<Taille> getLesTailles() {
        return Taille.lesTailles;
    }

    /**
     * Retourne la taille correspondant à l'identifiant donné
     * @param idTaille Identifiant recherché
     * @return Objet Taille ou null si non trouvé
     */
    public static Taille getTailleById(int idTaille) {
        for (Taille taille : lesTailles) {
            if (taille.getIdTaille() == idTaille) {
                return taille;
            }
        }
        return null;
    }

    // =========================
    // Méthodes
    // =========================

    @Override
    public String toString() {
        return this.libelle;
    }
}
