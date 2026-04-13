/**
 * Classe représentant un couple index/libellé.
 * Utile pour les JComboBox ou autres listes déroulantes
 * où l'on souhaite stocker à la fois un identifiant et un texte affiché.
 */
public class IndexLibelle {

    // =========================
    // Attributs
    // =========================
    private int index;       // Identifiant ou position associé au libellé
    private String libelle;  // Texte affiché dans la liste

    // =========================
    // Constructeur
    // =========================

    /**
     * Constructeur complet.
     *
     * @param index  L'index ou identifiant associé
     * @param libelle Le libellé affiché
     */
    public IndexLibelle(int index, String libelle) {
        this.index = index;
        this.libelle = libelle;
    }

    // =========================
    // Getters
    // =========================

    /**
     * Retourne l'index associé à ce libellé.
     *
     * @return index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Retourne le libellé associé à cet index.
     *
     * @return libellé
     */
    public String getLibelle() {
        return this.libelle;
    }

    // =========================
    // Méthodes utilitaires
    // =========================

    /**
     * Retourne le libellé pour l'affichage.
     * Cette méthode est appelée automatiquement par les JComboBox.
     *
     * @return libellé
     */
    @Override
    public String toString() {
        return this.libelle;
    }
}
