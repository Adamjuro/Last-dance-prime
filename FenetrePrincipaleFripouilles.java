import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Fenêtre principale de l'application Fripouilles.
 * Affiche le menu et les vues selon le rôle de l'utilisateur connecté. [cite: 24, 41]
 */
public class FenetrePrincipaleFripouilles extends JFrame {

    private Modele modele;
    private JPanel panelCentrale;
    private Utilisateur utilisateurConnecte; // utilisateur actuellement connecté

    // Barre utilisateur
    private JLabel lblUtilisateur;

    // Polices globales
    private final Font fontTitre = new Font("Segoe UI", Font.BOLD, 26);
    private final Font fontNormal = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Constructeur principal.
     * @param utilisateur Utilisateur connecté
     */
    public FenetrePrincipaleFripouilles(Utilisateur utilisateur) {
        this.modele = new Modele();
        this.modele.connect_database();

        this.utilisateurConnecte = utilisateur;

        initFenetre();
        setJMenuBar(creerMenuBar());
        setVisible(true);
    }

    /**
     * Initialise la fenêtre principale.
     */
    private void initFenetre() {
        setTitle("Gestion Fripouilles");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        // =========================
        // BARRE UTILISATEUR (HAUT)
        // =========================
        JPanel panelUtilisateur = new JPanel(new BorderLayout());
        panelUtilisateur.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelUtilisateur.setBackground(new Color(240, 240, 240));

        // Affichage du login + rôle [cite: 27, 59]
        lblUtilisateur = new JLabel(
                "Connecté : " + utilisateurConnecte.getLogin()
                        + " (" + utilisateurConnecte.getRole() + ")"
        );
        lblUtilisateur.setFont(fontNormal);

        // Bouton déconnexion
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setFont(fontNormal);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeconnexion.addActionListener(e -> deconnexion());

        panelUtilisateur.add(lblUtilisateur, BorderLayout.WEST);
        panelUtilisateur.add(btnDeconnexion, BorderLayout.EAST);

        add(panelUtilisateur, BorderLayout.NORTH);

        // =========================
        // PANNEAU CENTRAL
        // =========================
        panelCentrale = new JPanel(new BorderLayout());
        add(panelCentrale, BorderLayout.CENTER);

        afficherAccueil();

        // Fermeture propre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                modele.close_connexion();
                dispose();
            }
        });
    }

    // =========================
    // PAGE D'ACCUEIL
    // =========================
    private void afficherAccueil() {
        panelCentrale.removeAll();

        JPanel accueil = new JPanel();
        accueil.setLayout(new BoxLayout(accueil, BoxLayout.Y_AXIS));
        accueil.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titre = new JLabel("Gestion Fripouilles");
        titre.setFont(fontTitre);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sousTitre = new JLabel("Application de gestion des articles et ventes");
        sousTitre.setFont(fontNormal);
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnAjouterArticle = new JButton("Ajouter un article");
        JButton btnAfficherArticles = new JButton("Afficher les articles");
        JButton btnAjouterVente = new JButton("Ajouter une vente");
        JButton btnAfficherVentes = new JButton("Afficher les ventes");
        JButton btnAjouterArticleAVente = new JButton("Ajouter article à une vente");
        JButton btnAjouterCategorie = new JButton("Ajouter une catégorie de vêtements");
        JButton btnAjouterCompte = new JButton("Ajouter un compte"); 
        JButton btnAfficherCompte = new JButton("Afficher un compte"); 

        JButton[] boutons = {
                btnAjouterArticle, btnAfficherArticles,
                btnAjouterVente, btnAfficherVentes,
                btnAjouterArticleAVente,
                btnAjouterCategorie,
                btnAjouterCompte, btnAfficherCompte
        };

        for (JButton b : boutons) {
            b.setFont(fontNormal);
            b.setFocusPainted(false);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Listeners
        btnAjouterArticle.addActionListener(e -> afficherVueAjouterArticle());
        btnAfficherArticles.addActionListener(e -> afficherVueAfficherArticle());
        btnAjouterVente.addActionListener(e -> afficherVueAjouterVente());
        btnAfficherVentes.addActionListener(e -> afficherVueAfficherVentes());
        btnAjouterArticleAVente.addActionListener(e -> afficherVueAjouterArticleAVente());
        btnAjouterCategorie.addActionListener(e -> afficherVueAjouterCategorie());
        btnAjouterCompte.addActionListener(e -> afficherVueAjouterComptes());
        btnAfficherCompte.addActionListener(e -> afficherVueAfficherComptes());

        // Ajout des composants
        accueil.add(titre);
        accueil.add(Box.createVerticalStrut(10));
        accueil.add(sousTitre);
        accueil.add(Box.createVerticalStrut(30));

        // Affichage selon rôle [cite: 42, 43, 44]
        if (utilisateurConnecte instanceof Benevole) {
            accueil.add(btnAjouterArticle);
            accueil.add(btnAfficherArticles);
        }

        if (utilisateurConnecte instanceof Secretaire) {
            accueil.add(btnAjouterVente);
            accueil.add(btnAfficherVentes);
            accueil.add(Box.createVerticalStrut(20));
            accueil.add(btnAjouterArticleAVente);
            accueil.add(btnAjouterCategorie);
            accueil.add(btnAjouterCompte); // Seul secrétaire peut enregistrer bénévoles [cite: 60]
        }

        if (utilisateurConnecte instanceof Maire) {
            accueil.add(btnAfficherArticles);
            accueil.add(btnAfficherVentes);
        }

        panelCentrale.add(accueil, BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    // =========================
    // CHANGEMENT DE VUE
    // =========================
    private void afficherVueAjouterArticle() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueAjouterArticle(modele, panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    private void afficherVueAfficherArticle() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueGererArticles(modele, panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    private void afficherVueAjouterVente() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueAjouterVente(modele, panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    private void afficherVueAfficherVentes() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueGererVentes(modele, modele.getLesVentes(), panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    private void afficherVueAjouterArticleAVente() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueAjouterArticleAVente(modele, panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }
    
    private void afficherVueAjouterCategorie() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueAjouterCategorie(modele, panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    private void afficherVueAjouterComptes() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueAjouterComptes(modele, panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    private void afficherVueAfficherComptes() {
        panelCentrale.removeAll();
        panelCentrale.add(new VueGererComptes(modele, modele.getLesComptes(), panelCentrale), BorderLayout.CENTER);
        panelCentrale.revalidate();
        panelCentrale.repaint();
    }

    // =========================
    // MENU BAR
    // =========================
    private JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Articles
        JMenu menuArticles = new JMenu("Articles");
        JMenuItem itemAjouterArticle = new JMenuItem("Ajouter un article");
        JMenuItem itemAfficherArticles = new JMenuItem("Afficher les articles");
        JMenu menuAccueil = new JMenu("Accueil");
        JMenuItem itemAccueil = new JMenuItem("Page d'accueil");

        itemAjouterArticle.addActionListener(e -> afficherVueAjouterArticle());
        itemAfficherArticles.addActionListener(e -> afficherVueAfficherArticle());
        itemAccueil.addActionListener(e -> afficherAccueil());

        menuArticles.add(itemAjouterArticle);
        menuArticles.add(itemAfficherArticles);
        menuAccueil.add(itemAccueil);

        // Menu Ventes
        JMenu menuVentes = new JMenu("Ventes");
        JMenuItem itemAjouterVente = new JMenuItem("Ajouter une vente");
        JMenuItem itemAfficherVentes = new JMenuItem("Afficher les ventes");
        JMenuItem itemAjouterArticleAVente = new JMenuItem("Ajouter article à une vente");
        

        itemAjouterVente.addActionListener(e -> afficherVueAjouterVente());
        itemAfficherVentes.addActionListener(e -> afficherVueAfficherVentes());
        itemAjouterArticleAVente.addActionListener(e -> afficherVueAjouterArticleAVente());
       

        menuVentes.add(itemAjouterVente);
        menuVentes.add(itemAfficherVentes);
        menuVentes.add(itemAjouterArticleAVente);
        

        // Menu Catégories
        JMenu menuCategories = new JMenu("Catégories");
        JMenuItem itemAjouterCategorie = new JMenuItem("Ajouter une catégorie");
        itemAjouterCategorie.addActionListener(e -> afficherVueAjouterCategorie());
        menuCategories.add(itemAjouterCategorie);
        
        // Menu Comptes
        JMenu menuComptes = new JMenu("Comptes");
        JMenuItem itemAjouterComptes = new JMenuItem("Ajouter un compte");
        JMenuItem itemAfficherComptes = new JMenuItem("Afficher les comptes");
        
        itemAjouterComptes.addActionListener(e -> afficherVueAjouterComptes());
        itemAfficherComptes.addActionListener(e -> afficherVueAfficherComptes());
        menuComptes.add(itemAjouterComptes);
        menuComptes.add(itemAfficherComptes);

        // =========================
        // VOTRE FILTRAGE D'ORIGINE
        // =========================
        if (utilisateurConnecte instanceof Benevole) {
            itemAjouterVente.setEnabled(false);
            itemAfficherVentes.setEnabled(false);
            itemAjouterArticleAVente.setEnabled(false);
            itemAjouterCategorie.setEnabled(false);
            itemAfficherComptes.setEnabled(false);
            itemAjouterComptes.setEnabled(false);
        }

        if (utilisateurConnecte instanceof Secretaire) {
            itemAjouterArticle.setEnabled(false);
            itemAfficherArticles.setEnabled(false);
        }

        if (utilisateurConnecte instanceof Maire) {
            itemAjouterArticle.setEnabled(false);
            itemAjouterVente.setEnabled(false);
            itemAjouterArticleAVente.setEnabled(false);
            itemAjouterCategorie.setEnabled(false);
            itemAfficherComptes.setEnabled(false);
            itemAjouterComptes.setEnabled(false);
        }

        // Ajout final
        menuBar.add(menuArticles);
        menuBar.add(menuVentes);
        menuBar.add(menuCategories);
        menuBar.add(menuAccueil, 0);
        menuBar.add(menuComptes);

        return menuBar;
    }

    private void deconnexion() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            utilisateurConnecte = null;
            dispose();
            new LoginFrame();
        }
    }
}