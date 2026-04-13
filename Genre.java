/**
 * Classe représentant un Genre d'article (ex : Homme, Femme, Enfant).
 * Chaque genre possède un identifiant unique et un libellé.
 */
public class Genre {

    // =========================
    // Attributs
    // =========================
    private int idGenre;      // Identifiant unique du genre
    private String libelle;   // Libellé du genre (ex : Homme, Femme)

    // =========================
    // Constructeurs
    // =========================

    /**
     * Constructeur par défaut.
     */
    public Genre() {
    }

    /**
     * Constructeur complet.
     *
     * @param unIdGenre Identifiant du genre
     * @param unLibelle Libellé du genre
     */
    public Genre(int unIdGenre, String unLibelle) {
        this.idGenre = unIdGenre;
        this.libelle = unLibelle;
    }

    // =========================
    // Getters et Setters
    // =========================

    /**
     * Retourne l'identifiant du genre.
     *
     * @return identifiant du genre
     */
    public int getIdGenre() {
        return idGenre;
    }

    /**
     * Définit l'identifiant du genre.
     *
     * @param unIdGenre identifiant à définir
     */
    public void setIdGenre(int unIdGenre) {
        this.idGenre = unIdGenre;
    }

    /**
     * Retourne le libellé du genre.
     *
     * @return libellé
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libellé du genre.
     *
     * @param unLibelle libellé à définir
     */
    public void setLibelle(String unLibelle) {
        this.libelle = unLibelle;
    }

    // =========================
    // Méthodes utilitaires
    // =========================

    /**
     * Retourne le libellé du genre pour l'affichage.
     *
     * @return libellé
     */
    @Override
    public String toString() {
        return this.libelle;
    }
}
