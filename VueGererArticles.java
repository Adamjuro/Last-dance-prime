import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * JPanel permettant de gérer le catalogue des articles.
 * Affiche les articles en grille et permet de les ajouter, modifier ou supprimer.
 * Inclut une barre de recherche et des filtres (catégorie, couleur).
 */
public class VueGererArticles extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;

    // Sélection de l'article
    private Article articleSelectionne = null;
    private JPanel panelSelectionne = null;

    // === Filtres ===
    private JTextField txtRecherche;
    private JComboBox<Categorie> comboCategorie;
    private JComboBox<Couleur> comboCouleur;

    // Données
    private ArrayList<Article> tousLesArticles;
    private JPanel panelCatalogue;

    // =========================
    // Constructeur
    // =========================
    public VueGererArticles(Modele modele, JPanel panneau) {
        this.monModele = modele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Catalogue des Articles", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // =========================
        // BARRE DE RECHERCHE & FILTRES
        // =========================
        JPanel panelFiltres = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        txtRecherche = new JTextField(20);
        panelFiltres.add(new JLabel("Recherche :"));
        panelFiltres.add(txtRecherche);

        // Catégories
        comboCategorie = new JComboBox<>();
        comboCategorie.addItem(null); // Toutes les catégories
        for (Categorie c : monModele.getLesCategories()) {
            comboCategorie.addItem(c);
        }
        panelFiltres.add(new JLabel("Catégorie :"));
        panelFiltres.add(comboCategorie);

        // Couleurs
        comboCouleur = new JComboBox<>();
        comboCouleur.addItem(null); // Toutes les couleurs
        for (Couleur c : monModele.getLesCouleurs()) {
            comboCouleur.addItem(c);
        }
        panelFiltres.add(new JLabel("Couleur :"));
        panelFiltres.add(comboCouleur);

        this.add(panelFiltres, BorderLayout.BEFORE_FIRST_LINE);

        // =========================
        // LISTENERS DES FILTRES
        // =========================

        /**
         * KeyAdapter permet d'écouter les événements clavier.
         * Ici, on l'utilise pour relancer le filtrage à chaque fois
         * que l'utilisateur tape ou supprime un caractère dans la barre de recherche.
         * 
         * La méthode keyReleased est appelée une fois que la touche est relâchée,
         * ce qui garantit que le texte du champ est bien à jour.
         */
        txtRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrerArticles();
            }
        });

        // Mise à jour automatique du catalogue lors d'un changement de filtre
        comboCategorie.addActionListener(e -> filtrerArticles());
        comboCouleur.addActionListener(e -> filtrerArticles());

        // =========================
        // Catalogue des articles
        // =========================
        panelCatalogue = new JPanel(new GridLayout(0, 3, 10, 10));
        panelCatalogue.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tousLesArticles = monModele.getLesArticles();
        afficherArticles(tousLesArticles);

        JScrollPane scroll = new JScrollPane(panelCatalogue);
        this.add(scroll, BorderLayout.CENTER);

        // =========================
        // Boutons
        // =========================
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners boutons ===
        btnAjouter.addActionListener(e -> {
            panneauCentrale.removeAll();
            panneauCentrale.add(new VueAjouterArticle(monModele, panneauCentrale), BorderLayout.CENTER);
            panneauCentrale.revalidate();
            panneauCentrale.repaint();
        });

        btnModifier.addActionListener(e -> modifierArticle());
        btnSupprimer.addActionListener(e -> supprimerArticle());
    }

    // =========================
    // PANEL ARTICLE CLIQUABLE
    // =========================
    private JPanel creerPanelArticle(Article article) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblPhoto;
        try {
        	// Chargement de l'image de l'article à partir de son URL
        	// ImageIcon permet de créer une image utilisable dans les composants Swing (JLabel, JButton, etc.)
        	URL url = new URL(article.getPhoto());
        	ImageIcon icon = new ImageIcon(url);

        	// Récupération de l'image et redimensionnement pour un affichage uniforme
        	// SCALE_SMOOTH applique un lissage afin d'obtenir une meilleure qualité visuelle
        	Image img = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            lblPhoto = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            lblPhoto = new JLabel("Image indisponible", SwingConstants.CENTER);
        }
        panel.add(lblPhoto, BorderLayout.CENTER);

        JLabel lblDesc = new JLabel(
                "<html><center>" + article.getDescription() + "<br/>"
                        + article.getPrix() + " €</center></html>",
                SwingConstants.CENTER
        );
        panel.add(lblDesc, BorderLayout.SOUTH);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectionnerArticle(article, panel);
            }
        });

        return panel;
    }

    // =========================
    // GESTION DE LA SÉLECTION
    // =========================
    private void selectionnerArticle(Article article, JPanel panel) {
        if (panelSelectionne != null) {
            panelSelectionne.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }
        articleSelectionne = article;
        panelSelectionne = panel;
        panel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
    }

    // =========================
    // MODIFIER UN ARTICLE
    // =========================
    private void modifierArticle() {
        if (articleSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article.");
            return;
        }

        panneauCentrale.removeAll();
        panneauCentrale.add(
                new VueModifierArticle(monModele, articleSelectionne, panneauCentrale),
                BorderLayout.CENTER
        );
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }

    // =========================
    // SUPPRIMER UN ARTICLE
    // =========================
    private void supprimerArticle() {
        if (articleSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un article.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Supprimer cet article ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            monModele.deleteArticle(articleSelectionne.getIdArticle());

            panneauCentrale.removeAll();
            panneauCentrale.add(new VueGererArticles(monModele, panneauCentrale), BorderLayout.CENTER);
            panneauCentrale.revalidate();
            panneauCentrale.repaint();
        }
    }

    // =========================
    // AFFICHAGE & FILTRAGE
    // =========================
    private void afficherArticles(ArrayList<Article> articles) {
        panelCatalogue.removeAll();
        articleSelectionne = null;
        panelSelectionne = null;

        for (Article a : articles) {
            panelCatalogue.add(creerPanelArticle(a));
        }

        panelCatalogue.revalidate();
        panelCatalogue.repaint();
    }

    private void filtrerArticles() {
        String recherche = txtRecherche.getText().toLowerCase().trim();
        Categorie categorie = (Categorie) comboCategorie.getSelectedItem();
        Couleur couleur = (Couleur) comboCouleur.getSelectedItem();

        ArrayList<Article> filtres = new ArrayList<>();

        for (Article a : tousLesArticles) {
            boolean ok = true;

            if (!recherche.isEmpty() &&
                !a.getDescription().toLowerCase().contains(recherche)) {
                ok = false;
            }

            if (categorie != null &&
                a.getCategorie().getIdCategorie() != categorie.getIdCategorie()) {
                ok = false;
            }

            if (couleur != null &&
                a.getCouleur().getIdCouleur() != couleur.getIdCouleur()) {
                ok = false;
            }

            if (ok) {
                filtres.add(a);
            }
        }

        afficherArticles(filtres);
    }
}