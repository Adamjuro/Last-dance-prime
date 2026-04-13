import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * JPanel pour ajouter un nouvel article.
 * Permet de remplir un formulaire avec les informations d'un article et
 * de l'insérer dans la base via le modèle.
 */
public class VueAjouterArticle extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;

    private JTextField txtDescription;
    private JTextField txtPrix;
    private JTextField txtPhoto;
    private JTextField txtStatut;

    private JComboBox<IndexLibelle> comboCategorie;
    private JComboBox<IndexLibelle> comboTaille;
    private JComboBox<IndexLibelle> comboCouleur;
    private JComboBox<IndexLibelle> comboGenre;
    private JComboBox<IndexLibelle> comboEtat;

    // =========================
    // Constructeur
    // =========================
    public VueAjouterArticle(Modele unModele, JPanel panneau) {
        this.monModele = unModele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Ajouter un Article", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));

        // Récupération des listes depuis le modèle
        ArrayList<Categorie> lesCategories = monModele.getLesCategories();
        ArrayList<Taille> lesTailles = monModele.getLesTailles();
        ArrayList<Couleur> lesCouleurs = monModele.getLesCouleurs();
        ArrayList<Genre> lesGenres = monModele.getLesGenres();
        ArrayList<Etat> lesEtats = monModele.getLesEtats();

        // === Panel Formulaire ===
        JPanel panelFormulaire = new JPanel(new GridLayout(9, 2, 10, 15));
        panelFormulaire.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Champs texte
        panelFormulaire.add(new JLabel("Description :"));
        txtDescription = new JTextField();
        panelFormulaire.add(txtDescription);

        panelFormulaire.add(new JLabel("Prix :"));
        txtPrix = new JTextField();
        panelFormulaire.add(txtPrix);

        panelFormulaire.add(new JLabel("Photo :"));
        txtPhoto = new JTextField();
        panelFormulaire.add(txtPhoto);

        panelFormulaire.add(new JLabel("Statut :"));
        txtStatut = new JTextField();
        panelFormulaire.add(txtStatut);

        // ComboBox pour les listes
        panelFormulaire.add(new JLabel("Catégorie :"));
        comboCategorie = new JComboBox<>();
        for (Categorie c : lesCategories) comboCategorie.addItem(new IndexLibelle(c.getIdCategorie(), c.getLibelle()));
        panelFormulaire.add(comboCategorie);

        panelFormulaire.add(new JLabel("Taille :"));
        comboTaille = new JComboBox<>();
        for (Taille t : lesTailles) comboTaille.addItem(new IndexLibelle(t.getIdTaille(), t.getLibelle()));
        panelFormulaire.add(comboTaille);

        panelFormulaire.add(new JLabel("Couleur :"));
        comboCouleur = new JComboBox<>();
        for (Couleur c : lesCouleurs) comboCouleur.addItem(new IndexLibelle(c.getIdCouleur(), c.getLibelle()));
        panelFormulaire.add(comboCouleur);

        panelFormulaire.add(new JLabel("Genre :"));
        comboGenre = new JComboBox<>();
        for (Genre g : lesGenres) comboGenre.addItem(new IndexLibelle(g.getIdGenre(), g.getLibelle()));
        panelFormulaire.add(comboGenre);

        panelFormulaire.add(new JLabel("État :"));
        comboEtat = new JComboBox<>();
        for (Etat e : lesEtats) comboEtat.addItem(new IndexLibelle(e.getIdEtat(), e.getLibelle()));
        panelFormulaire.add(comboEtat);

        // === Panel Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnValider = new JButton("Ajouter");
        JButton btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);

        // Ajout au panel principal
        this.add(titre, BorderLayout.NORTH);
        this.add(panelFormulaire, BorderLayout.CENTER);
        this.add(panelBoutons, BorderLayout.SOUTH);

        // Listeners des boutons
        btnValider.addActionListener(e -> ajouterArticle());
        btnAnnuler.addActionListener(e -> annuler());
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Récupère les données du formulaire et crée un article dans la BDD.
     */
    private void ajouterArticle() {
        try {
            System.out.println("=== Début ajouterArticle() ===");

            Article a = new Article();

            // Champs texte
            a.setDescription(txtDescription.getText());
            a.setPrix(Float.parseFloat(txtPrix.getText()));
            a.setPhoto(txtPhoto.getText());
            a.setStatut(txtStatut.getText());

            // Récupération des ID sélectionnés
            int idCategorie = ((IndexLibelle) comboCategorie.getSelectedItem()).getIndex();
            int idTaille = ((IndexLibelle) comboTaille.getSelectedItem()).getIndex();
            int idCouleur = ((IndexLibelle) comboCouleur.getSelectedItem()).getIndex();
            int idGenre = ((IndexLibelle) comboGenre.getSelectedItem()).getIndex();
            int idEtat = ((IndexLibelle) comboEtat.getSelectedItem()).getIndex();

            // Recherche des objets correspondant
            Categorie categorieSelectionnee = monModele.getCategorieById(idCategorie);
            Taille tailleSelectionnee = monModele.getTailleById(idTaille);
            Couleur couleurSelectionnee = monModele.getCouleurById(idCouleur);
            Genre genreSelectionne = monModele.getGenreById(idGenre);
            Etat etatSelectionne = monModele.getEtatById(idEtat);

            if (categorieSelectionnee == null || tailleSelectionnee == null || couleurSelectionnee == null
                    || genreSelectionne == null || etatSelectionne == null) {
                JOptionPane.showMessageDialog(this, "Erreur : Impossible de trouver tous les éléments sélectionnés",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Assignation des objets
            a.setCategorie(categorieSelectionnee);
            a.setTaille(tailleSelectionnee);
            a.setCouleur(couleurSelectionnee);
            a.setGenre(genreSelectionne);
            a.setEtat(etatSelectionne);

            // Insertion en BDD
            monModele.insererArticle(a);
            JOptionPane.showMessageDialog(this, "Article ajouté avec succès !", "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            // Retour à la gestion des articles
            panneauCentrale.removeAll();
            panneauCentrale.add(new VueGererArticles(monModele, panneauCentrale), BorderLayout.CENTER);
            panneauCentrale.revalidate();
            panneauCentrale.repaint();

            System.out.println("=== Fin ajouterArticle() ===");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide !", "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage(), "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Annule l'ajout et retourne à la vue de gestion des articles.
     */
    private void annuler() {
        panneauCentrale.removeAll();
        panneauCentrale.add(new VueGererArticles(monModele, panneauCentrale), BorderLayout.CENTER);
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }
}
