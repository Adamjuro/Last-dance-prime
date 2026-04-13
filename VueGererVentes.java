import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * JPanel permettant de gérer les ventes.
 * Affiche les ventes dans un tableau et permet de les ajouter, modifier, supprimer ou consulter leurs articles.
 */
public class VueGererVentes extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;
    private JTable tableVentes;
    private DefaultTableModel model;

    // =========================
    // Constructeur
    // =========================
    public VueGererVentes(Modele unModele, ArrayList<Vente> lesVentes, JPanel panneau) {
        this.monModele = unModele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Gestion des Ventes", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // === Tableau des ventes ===
        String[] entetes = {"ID", "Titre", "Date", "Heure Début", "Heure Fin", "Lieu"};
        model = new DefaultTableModel(entetes, 0);
        tableVentes = new JTable(model);
        remplirTableau(lesVentes);

        JScrollPane scrollPane = new JScrollPane(tableVentes);
        this.add(scrollPane, BorderLayout.CENTER);

        // === Panel boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAjouter = new JButton("Ajouter");
        JButton btnModifier = new JButton("Modifier");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnVoirArticles = new JButton("Voir les articles");

        panelBoutons.add(btnVoirArticles);
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);

        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners ===
        btnAjouter.addActionListener(e -> {
            panneauCentrale.removeAll();
            panneauCentrale.add(new VueAjouterVente(monModele, panneauCentrale), BorderLayout.CENTER);
            panneauCentrale.revalidate();
            panneauCentrale.repaint();
        });

        btnModifier.addActionListener(e -> modifierVente());
        btnSupprimer.addActionListener(e -> supprimerVente());
        btnVoirArticles.addActionListener(e -> voirArticlesVente());
    }

    // =========================
    // Remplir le tableau des ventes
    // =========================
    private void remplirTableau(ArrayList<Vente> lesVentes) {
        model.setRowCount(0); // vide le tableau
        for (Vente v : lesVentes) {
            model.addRow(new Object[]{
                    v.getIdVente(),
                    v.getTitre(),
                    v.getDateVente(),
                    v.getHeureDebut(),
                    v.getHeureFin(),
                    v.getLieu()
            });
        }
    }

    // =========================
    // Modifier une vente
    // =========================
    private void modifierVente() {
        int ligne = tableVentes.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une vente à modifier", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idVente = (int) model.getValueAt(ligne, 0);
        Vente v = monModele.getVenteById(idVente);

        panneauCentrale.removeAll();
        panneauCentrale.add(new VueModifierVente(monModele, v, panneauCentrale), BorderLayout.CENTER);
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }

    // =========================
    // Supprimer une vente
    // =========================
    private void supprimerVente() {
        int ligne = tableVentes.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une vente à supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idVente = (int) model.getValueAt(ligne, 0);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment supprimer cette vente ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            monModele.deleteVente(idVente);

            // Rafraîchir le tableau
            ArrayList<Vente> lesVentes = monModele.getLesVentes();
            remplirTableau(lesVentes);
        }
    }

    // =========================
    // Voir les articles d'une vente
    // =========================
    private void voirArticlesVente() {
        int ligne = tableVentes.getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une vente", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idVente = (int) model.getValueAt(ligne, 0);
        ArrayList<Article> lesArticles = monModele.getArticlesByVente(idVente);

        if (lesArticles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun article dans cette vente", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Crée un tableau pour afficher les articles
        String[] entetes = {"ID", "Description", "Prix", "Catégorie", "Taille", "Couleur", "Genre", "État"};
        Object[][] data = new Object[lesArticles.size()][entetes.length];

        for (int i = 0; i < lesArticles.size(); i++) {
            Article a = lesArticles.get(i);
            data[i][0] = a.getIdArticle();
            data[i][1] = a.getDescription();
            data[i][2] = a.getPrix();
            data[i][3] = a.getCategorie().getLibelle();
            data[i][4] = a.getTaille();
            data[i][5] = a.getCouleur().getLibelle();
            data[i][6] = a.getGenre();
            data[i][7] = a.getEtat();
        }

        JTable tableArticles = new JTable(data, entetes);
        JScrollPane scrollPane = new JScrollPane(tableArticles);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Articles de la vente " + idVente, JOptionPane.INFORMATION_MESSAGE);
    }
}
