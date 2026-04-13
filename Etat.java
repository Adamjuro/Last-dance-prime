/**
 * Classe représentant l'état d'un article.
 * Par exemple : Neuf, Comme Neuf, Usagé, etc.
 */
public class Etat {

    // ========================
    // Attributs
    // ========================
    private int idEtat;    // Identifiant unique de l'état
    private String libelle; // Nom de l'état

    // ========================
    // Constructeurs
    // ========================

    /**
     * Constructeur par défaut.
     */
    public Etat() {
    }

    /**
     * Constructeur avec initialisation des attributs.
     *
     * @param unIdEtat Identifiant unique de l'état
     * @param unLibelle Nom de l'état
     */
    public Etat(int unIdEtat, String unLibelle) {
        this.idEtat = unIdEtat;
        this.libelle = unLibelle;
    }

    // ========================
    // Getters et Setters
    // ========================

    /**
     * @return L'identifiant unique de l'état
     */
    public int getIdEtat() {
        return idEtat;
    }

    /**
     * Définit l'identifiant unique de l'état
     *
     * @param unIdEtat Nouvel identifiant
     */
    public void setIdEtat(int unIdEtat) {
        this.idEtat = unIdEtat;
    }

    /**
     * @return Le libellé (nom) de l'état
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libellé (nom) de l'état
     *
     * @param unLibelle Nouveau libellé
     */
    public void setLibelle(String unLibelle) {
        this.libelle = unLibelle;
    }

    // ========================
    // Méthodes
    // ========================

    /**
     * Permet d'afficher directement le libellé dans les JComboBox ou logs
     *
     * @return Le libellé de l'état
     */
    @Override
    public String toString() {
        return this.libelle;
    }
}
