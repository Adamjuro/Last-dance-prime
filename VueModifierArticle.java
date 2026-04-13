import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Vue pour modifier un article existant dans le catalogue.
 * Cette vue permet de modifier toutes les propriétés d'un article
 * et met à jour la base via le modèle.
 */
public class VueModifierArticle extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;
    private Article article;

    private JTextField txtDescription;
    private JTextField txtPrix;
    private JTextField txtPhoto;
    private JTextField txtStatut;

    private JComboBox<Categorie> comboCategorie;
    private JComboBox<Taille> comboTaille;
    private JComboBox<Couleur> comboCouleur;
    private JComboBox<Genre> comboGenre;
    private JComboBox<Etat> comboEtat;

    // =========================
    // Constructeur
    // =========================
    /**
     * Crée la vue pour modifier un article existant.
     *
     * @param unModele  le modèle permettant l'accès à la base de données
     * @param unArticle l'article à modifier
     * @param panneau   le panneau central de la fenêtre principale pour les changements de vue
     */
    public VueModifierArticle(Modele unModele, Article unArticle, JPanel panneau) {
        this.monModele = unModele;
        this.panneauCentrale = panneau;
        this.article = unArticle;

        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Modifier un Article", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));

        // Récupération des listes depuis le modèle
        ArrayList<Categorie> lesCategories = monModele.getLesCategories();
        ArrayList<Taille> lesTailles = monModele.getLesTailles();
        ArrayList<Couleur> lesCouleurs = monModele.getLesCouleurs();
        ArrayList<Genre> lesGenres = monModele.getLesGenres();
        ArrayList<Etat> lesEtats = monModele.getLesEtats();

        // === Panel formulaire ===
        JPanel panelFormulaire = new JPanel(new GridLayout(9, 2, 10, 15));
        panelFormulaire.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Champs texte pré-remplis
        panelFormulaire.add(new JLabel("Description :"));
        txtDescription = new JTextField(article.getDescription());
        panelFormulaire.add(txtDescription);

        panelFormulaire.add(new JLabel("Prix :"));
        txtPrix = new JTextField(String.valueOf(article.getPrix()));
        panelFormulaire.add(txtPrix);

        panelFormulaire.add(new JLabel("Photo :"));
        txtPhoto = new JTextField(article.getPhoto());
        panelFormulaire.add(txtPhoto);

        panelFormulaire.add(new JLabel("Statut :"));
        txtStatut = new JTextField(article.getStatut());
        panelFormulaire.add(txtStatut);

        // Combobox pour les propriétés liées aux objets
        panelFormulaire.add(new JLabel("Catégorie :"));
        comboCategorie = new JComboBox<>();
        for (Categorie c : lesCategories) {
            comboCategorie.addItem(c);
            if (c.getIdCategorie() == article.getIdCategorie()) comboCategorie.setSelectedItem(c);
        }
        panelFormulaire.add(comboCategorie);

        panelFormulaire.add(new JLabel("Taille :"));
        comboTaille = new JComboBox<>();
        for (Taille t : lesTailles) {
            comboTaille.addItem(t);
            if (t.getIdTaille() == article.getIdTaille()) comboTaille.setSelectedItem(t);
        }
        panelFormulaire.add(comboTaille);

        panelFormulaire.add(new JLabel("Couleur :"));
        comboCouleur = new JComboBox<>();
        for (Couleur c : lesCouleurs) {
            comboCouleur.addItem(c);
            if (c.getIdCouleur() == article.getIdCouleur()) comboCouleur.setSelectedItem(c);
        }
        panelFormulaire.add(comboCouleur);

        panelFormulaire.add(new JLabel("Genre :"));
        comboGenre = new JComboBox<>();
        for (Genre g : lesGenres) {
            comboGenre.addItem(g);
            if (g.getIdGenre() == article.getIdGenre()) comboGenre.setSelectedItem(g);
        }
        panelFormulaire.add(comboGenre);

        panelFormulaire.add(new JLabel("État :"));
        comboEtat = new JComboBox<>();
        for (Etat e : lesEtats) {
            comboEtat.addItem(e);
            if (e.getIdEtat() == article.getIdEtat()) comboEtat.setSelectedItem(e);
        }
        panelFormulaire.add(comboEtat);

        // === Panel Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnValider = new JButton("Modifier");
        JButton btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);

        // Ajout des composants
        this.add(titre, BorderLayout.NORTH);
        this.add(panelFormulaire, BorderLayout.CENTER);
        this.add(panelBoutons, BorderLayout.SOUTH);

        // Listeners
        btnValider.addActionListener(e -> modifierArticle());
        btnAnnuler.addActionListener(e -> annulerModification());
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Valide les modifications apportées à l'article et met à jour la base.
     */
    private void modifierArticle() {
        try {
            article.setDescription(txtDescription.getText());
            article.setPrix(Float.parseFloat(txtPrix.getText()));
            article.setPhoto(txtPhoto.getText());
            article.setStatut(txtStatut.getText());
            article.setCategorie((Categorie) comboCategorie.getSelectedItem());
            article.setTaille((Taille) comboTaille.getSelectedItem());
            article.setCouleur((Couleur) comboCouleur.getSelectedItem());
            article.setGenre((Genre) comboGenre.getSelectedItem());
            article.setEtat((Etat) comboEtat.getSelectedItem());

            monModele.updateArticle(article);

            JOptionPane.showMessageDialog(this, "Article modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);

            // Retour à la vue de gestion
            panneauCentrale.removeAll();
            panneauCentrale.add(new VueGererArticles(monModele, panneauCentrale), BorderLayout.CENTER);
            panneauCentrale.revalidate();
            panneauCentrale.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Le prix doit être un nombre valide !", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Annule la modification et revient à la vue de gestion des articles.
     */
    private void annulerModification() {
        panneauCentrale.removeAll();
        panneauCentrale.add(new VueGererArticles(monModele, panneauCentrale), BorderLayout.CENTER);
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }
}
