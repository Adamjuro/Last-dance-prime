/**
 * Classe représentant une couleur d'article.
 * Chaque article peut avoir une couleur (ex : Rouge, Bleu, Vert, etc.).
 */
public class Couleur {

    // ========================
    // Attributs
    // ========================
    private int idCouleur;  // Identifiant unique de la couleur
    private String libelle; // Nom de la couleur

    // ========================
    // Constructeurs
    // ========================

    /**
     * Constructeur par défaut.
     */
    public Couleur() {
    }

    /**
     * Constructeur avec initialisation des attributs.
     *
     * @param unIdCouleur Identifiant unique de la couleur
     * @param unLibelle   Nom de la couleur
     */
    public Couleur(int unIdCouleur, String unLibelle) {
        this.idCouleur = unIdCouleur;
        this.libelle = unLibelle;
    }

    // ========================
    // Getters et Setters
    // ========================

    /**
     * @return L'identifiant unique de la couleur
     */
    public int getIdCouleur() {
        return idCouleur;
    }

    /**
     * Définit l'identifiant unique de la couleur
     *
     * @param unIdCouleur Nouvel identifiant
     */
    public void setIdCouleur(int unIdCouleur) {
        this.idCouleur = unIdCouleur;
    }

    /**
     * @return Le libellé (nom) de la couleur
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libellé (nom) de la couleur
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
     * @return Le libellé de la couleur
     */
    @Override
    public String toString() {
        return this.libelle;
    }
}
