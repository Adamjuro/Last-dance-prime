import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Représente une vente dans l'application Fripouilles.
 * Une vente possède un identifiant, un titre, une date, des horaires, un lieu
 * et peut contenir plusieurs articles.
 */
public class Vente {

    // =========================
    // Attributs
    // =========================
    private int idVente;                 // Identifiant unique de la vente
    private String titre;                 // Titre de la vente
    private LocalDate dateVente;          // Date de la vente
    private LocalTime heureDebut;         // Heure de début
    private LocalTime heureFin;           // Heure de fin
    private String lieu;                  // Lieu de la vente
    private ArrayList<Article> lesArticles; // Liste des articles associés

    // =========================
    // Constructeurs
    // =========================
    /** Constructeur par défaut */
    public Vente() {
        this.lesArticles = new ArrayList<>();
    }

    /**
     * Constructeur complet
     * @param unIdVente Identifiant de la vente
     * @param unTitre Titre de la vente
     * @param uneDateVente Date de la vente
     * @param uneHeureDebut Heure de début
     * @param uneHeureFin Heure de fin
     * @param unLieu Lieu de la vente
     */
    public Vente(int unIdVente, String unTitre, LocalDate uneDateVente, LocalTime uneHeureDebut,
                 LocalTime uneHeureFin, String unLieu) {
        this.idVente = unIdVente;
        this.titre = unTitre;
        this.dateVente = uneDateVente;
        this.heureDebut = uneHeureDebut;
        this.heureFin = uneHeureFin;
        this.lieu = unLieu;
        this.lesArticles = new ArrayList<>();
    }

    // =========================
    // Getters et Setters
    // =========================
    public int getIdVente() {
        return idVente;
    }

    public void setIdVente(int unIdVente) {
        this.idVente = unIdVente;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String unTitre) {
        this.titre = unTitre;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate uneDateVente) {
        this.dateVente = uneDateVente;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime uneHeureDebut) {
        this.heureDebut = uneHeureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime uneHeureFin) {
        this.heureFin = uneHeureFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String unLieu) {
        this.lieu = unLieu;
    }

    public ArrayList<Article> getLesArticles() {
        return lesArticles;
    }

    // =========================
    // Méthodes pour gérer les articles
    // =========================
    /**
     * Ajoute un article à la vente s'il n'est pas déjà présent
     * @param unArticle Article à ajouter
     */
    public void ajouterUnArticle(Article unArticle) {
        if (!this.lesArticles.contains(unArticle)) {
            this.lesArticles.add(unArticle);
        }
    }

    /**
     * Supprime un article de la vente
     * @param unArticle Article à supprimer
     */
    public void supprimerUnArticle(Article unArticle) {
        this.lesArticles.remove(unArticle);
    }

    /** Supprime tous les articles de la vente */
    public void supprimerTousLesArticles() {
        this.lesArticles.clear();
    }

    // =========================
    // Affichage
    // =========================
    /**
     * Affiche les informations de la vente
     * @return String décrivant la vente
     */
    public String afficherUneVente() {
        return "Vente [idVente=" + idVente + ", titre=" + titre + ", dateVente=" + dateVente
                + ", heureDebut=" + heureDebut + ", heureFin=" + heureFin + ", lieu=" + lieu + "]";
    }

    @Override
    public String toString() {
        return titre; // Affiche le titre pour les listes déroulantes ou JComboBox
    }
}
