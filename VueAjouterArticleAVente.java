
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * JPanel pour ajouter des articles à une vente existante.
 * Permet de sélectionner une vente, afficher les articles disponibles et
 * ajouter les articles sélectionnés à la vente.
 */
public class VueAjouterArticleAVente extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;

    private JComboBox<Vente> comboVentes;
    private JTable tableArticles;
    private DefaultTableModel model;

    // =========================
    // Constructeur
    // =========================
    public VueAjouterArticleAVente(Modele modele, JPanel panneau) {
        this.monModele = modele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Ajouter des Articles à une Vente", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // === ComboBox Ventes ===
        JPanel panelVente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelVente.add(new JLabel("Sélectionner une vente :"));

        comboVentes = new JComboBox<>();
        ArrayList<Vente> lesVentes = monModele.getLesVentes();
        for (Vente v : lesVentes) comboVentes.addItem(v);
        panelVente.add(comboVentes);

        this.add(panelVente, BorderLayout.NORTH);

        // === Tableau des articles ===
        String[] entetes = {"ID", "Description", "Prix", "Sélectionner"};
        model = new DefaultTableModel(entetes, 0) {
        	/**
             * Méthode qui indique le type de chaque colonne.
             * Class<?> signifie "n'importe quel type d'objet" (wildcard)
             * Ici, on précise que la dernière colonne (index 3) contient des cases à cocher (Boolean)
             * et toutes les autres sont simplement des objets génériques (Object).
             */
            @Override
            public Class<?> getColumnClass(int column) { //Class<?> veut dire n'importe quel objet (wildcard)
                return column == 3 ? Boolean.class : Object.class;
            }
            /**
             * Méthode qui indique si une cellule est éditable ou non.
             * Seule la colonne "Sélectionner" (index 3) est modifiable par l'utilisateur.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // seule la colonne checkbox est éditable
            }
        };

        tableArticles = new JTable(model);
        remplirTableau(monModele.getLesArticles());
        this.add(new JScrollPane(tableArticles), BorderLayout.CENTER);

        // === Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAjouter = new JButton("Ajouter à la vente");
        JButton btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnAnnuler);
        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners ===
        btnAjouter.addActionListener(e -> ajouterArticlesSelectionnes());
        btnAnnuler.addActionListener(e -> retour());
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Remplit le tableau avec les articles disponibles.
     *
     * @param lesArticles Liste des articles à afficher.
     */
    private void remplirTableau(ArrayList<Article> lesArticles) {
        model.setRowCount(0);
        for (Article a : lesArticles) {
            model.addRow(new Object[]{a.getIdArticle(), a.getDescription(), a.getPrix(), false});
        }
    }

    /**
     * Ajoute les articles sélectionnés dans la vente choisie.
     */
    private void ajouterArticlesSelectionnes() {
        Vente venteSelectionnee = (Vente) comboVentes.getSelectedItem();
        if (venteSelectionnee == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une vente", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean auMoinsUnAjout = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean selected = (Boolean) model.getValueAt(i, 3);
            if (selected) {
                int idArticle = (int) model.getValueAt(i, 0);

                // Vérifie si l'article est déjà dans une vente
                if (!monModele.articleDejaDansUneVente(idArticle)) {
                    monModele.ajouterArticleAVente(venteSelectionnee.getIdVente(), idArticle);
                    auMoinsUnAjout = true;
                }
            }
        }

        if (auMoinsUnAjout) {
            JOptionPane.showMessageDialog(this, "Articles ajoutés à la vente !");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Aucun article n'a pu être ajouté (déjà présent dans une vente).",
                    "Information", JOptionPane.WARNING_MESSAGE);
        }

        retour();
    }

    /**
     * Retourne à la vue de gestion des ventes.
     */
    private void retour() {
        panneauCentrale.removeAll();
        panneauCentrale.add(new VueGererVentes(monModele, monModele.getLesVentes(), panneauCentrale),
                BorderLayout.CENTER);
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }
}
