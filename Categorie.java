/**
 * Classe représentant une catégorie d'article.
 * Chaque article appartient à une catégorie (ex : Vêtements, Chaussures, Accessoires, etc.).
 */
public class Categorie {

    // ========================
    // Attributs
    // ========================
    private int idCategorie;  // Identifiant unique de la catégorie
    private String libelle;   // Nom de la catégorie

    // ========================
    // Constructeurs
    // ========================

    /**
     * Constructeur par défaut.
     */
    public Categorie() {
    }

    /**
     * Constructeur avec initialisation des attributs.
     *
     * @param unIdCategorie Identifiant unique de la catégorie
     * @param unLibelle     Nom de la catégorie
     */
    public Categorie(int unIdCategorie, String unLibelle) {
        this.idCategorie = unIdCategorie;
        this.libelle = unLibelle;
    }

    // ========================
    // Getters et Setters
    // ========================

    /**
     * @return L'identifiant unique de la catégorie
     */
    public int getIdCategorie() {
        return idCategorie;
    }

    /**
     * Définit l'identifiant unique de la catégorie
     *
     * @param unIdCategorie Nouvel identifiant
     */
    public void setIdCategorie(int unIdCategorie) {
        this.idCategorie = unIdCategorie;
    }

    /**
     * @return Le libellé (nom) de la catégorie
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libellé (nom) de la catégorie
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
     * @return Le libellé de la catégorie
     */
    @Override
    public String toString() {
        return this.libelle;
    }
}
