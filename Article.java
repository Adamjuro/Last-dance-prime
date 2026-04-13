/**
 * Représente un article du catalogue des ventes Fripouilles.
 * Chaque article possède des informations sur sa description, son prix, sa photo,
 * son statut ainsi que ses caractéristiques : catégorie, taille, couleur, genre et état.
 */
public class Article {

    // =========================
    // Attributs
    // =========================
    private int idArticle; 
    private String description;
    private float prix;
    private String photo;
    private String statut;
    private Categorie uneCategorie;
    private Taille uneTaille;
    private Couleur uneCouleur;
    private Genre unGenre;
    private Etat unEtat;
    private int idEtat; // Sert éventuellement pour la persistance

    // =========================
    // Constructeurs
    // =========================

    /** Constructeur par défaut */
    public Article() {}

    /**
     * Constructeur complet sans idEtat.
     */
    public Article(int idArticle, String description, float prix, String photo, String statut,
                   Categorie categorie, Taille taille, Couleur couleur, Genre genre, Etat etat) {
        this.idArticle = idArticle;
        this.description = description;
        this.prix = prix;
        this.photo = photo;
        this.statut = statut;
        this.uneCategorie = categorie;
        this.uneTaille = taille;
        this.uneCouleur = couleur;
        this.unGenre = genre;
        this.unEtat = etat;
    }

    /**
     * Constructeur complet avec idEtat.
     */
    public Article(int idArticle, String description, float prix, String photo, String statut,
                   Categorie categorie, Taille taille, Couleur couleur, Genre genre, Etat etat, int idEtat) {
        this(idArticle, description, prix, photo, statut, categorie, taille, couleur, genre, etat);
        this.idEtat = idEtat;
    }

    // =========================
    // Getters et Setters
    // =========================

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Categorie getCategorie() {
        return uneCategorie;
    }

    public void setCategorie(Categorie categorie) {
        this.uneCategorie = categorie;
    }

    public int getIdCategorie() {
        return (uneCategorie != null) ? uneCategorie.getIdCategorie() : 0;
    }

    public Taille getTaille() {
        return uneTaille;
    }

    public void setTaille(Taille taille) {
        this.uneTaille = taille;
    }

    public int getIdTaille() {
        return (uneTaille != null) ? uneTaille.getIdTaille() : 0;
    }

    public Couleur getCouleur() {
        return uneCouleur;
    }

    public void setCouleur(Couleur couleur) {
        this.uneCouleur = couleur;
    }

    public int getIdCouleur() {
        return (uneCouleur != null) ? uneCouleur.getIdCouleur() : 0;
    }

    public Genre getGenre() {
        return unGenre;
    }

    public void setGenre(Genre genre) {
        this.unGenre = genre;
    }

    public int getIdGenre() {
        return (unGenre != null) ? unGenre.getIdGenre() : 0;
    }

    public Etat getEtat() {
        return unEtat;
    }

    public void setEtat(Etat etat) {
        this.unEtat = etat;
    }

    public int getIdEtat() {
        return (unEtat != null) ? unEtat.getIdEtat() : idEtat;
    }

    public void setIdEtat(int idEtat) {
        this.idEtat = idEtat;
        System.out.println("setIdEtat appelé avec : " + idEtat); // Debug
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Retourne une représentation complète de l'article sous forme de chaîne de caractères.
     */
    public String afficherUnArticle() {
        return "Article [idArticle=" + idArticle +
               ", description=" + description +
               ", prix=" + prix +
               ", photo=" + photo +
               ", statut=" + statut +
               ", idCategorie=" + getIdCategorie() +
               ", taille=" + (uneTaille != null ? uneTaille.getLibelle() : "N/A") +
               ", idCouleur=" + getIdCouleur() +
               ", genre=" + (unGenre != null ? unGenre.getLibelle() : "N/A") +
               ", etat=" + (unEtat != null ? unEtat.getLibelle() : "N/A") + "]";
    }
}
