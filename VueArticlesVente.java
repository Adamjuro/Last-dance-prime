import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * JPanel affichant les articles d'une vente spécifique.
 * Permet de consulter et de retirer des articles de la vente.
 */
public class VueArticlesVente extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;
    private int idVente;
    private JTable tableArticles;
    private DefaultTableModel model;

    // =========================
    // Constructeur
    // =========================
    public VueArticlesVente(Modele modele, JPanel panneau, int idVente) {
        this.monModele = modele;
        this.panneauCentrale = panneau;
        this.idVente = idVente;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Articles de la vente " + idVente, SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(titre, BorderLayout.NORTH);

        // === Tableau ===
        String[] entetes = {"ID", "Description", "Prix", "Catégorie", "Taille", "Couleur", "Genre", "État"};
        model = new DefaultTableModel(entetes, 0);
        tableArticles = new JTable(model);
        remplirTableau();

        JScrollPane scrollPane = new JScrollPane(tableArticles);
        this.add(scrollPane, BorderLayout.CENTER);

        // === Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSupprimer = new JButton("Supprimer de la vente");
        JButton btnRetour = new JButton("Retour");
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnRetour);
        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners ===
        btnSupprimer.addActionListener(e -> supprimerArticle());
        btnRetour.addActionListener(e -> retour());
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Remplit le tableau avec les articles de la vente.
     */
    private void remplirTableau() {
        model.setRowCount(0);
        ArrayList<Article> lesArticles = monModele.getArticlesByVente(idVente);
        for (Article a : lesArticles) {
            model.addRow(new Object[]{
                a.getIdArticle(),
                a.getDescription(),
                a.getPrix(),
                a.getCategorie().getLibelle(),
                a.getTaille(),
                a.getCouleur().getLibelle(),
                a.getGenre(),
                a.getEtat()
            });
        }
    }

    /**
     * Supprime l'article sélectionné de la vente.
     */
    private void supprimerArticle() {
        int ligne = tableArticles.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un article à supprimer",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idArticle = (int) model.getValueAt(ligne, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment retirer cet article de la vente ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            monModele.supprimerArticleDuneVente(idVente, idArticle);
            remplirTableau(); // rafraîchit le tableau
        }
    }

    /**
     * Retourne à la vue de gestion des ventes.
     */
    private void retour() {
        panneauCentrale.removeAll();
        panneauCentrale.add(new VueGererVentes(monModele, monModele.getLesVentes(), panneauCentrale), BorderLayout.CENTER);
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }
}
