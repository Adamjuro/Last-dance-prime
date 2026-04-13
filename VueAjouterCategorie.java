
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * JPanel pour ajouter une nouvelle catégorie.
 * L'ID est calculé automatiquement pour éviter les conflits.
 */
public class VueAjouterCategorie extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;
    private JTextField txtLibelle;

    // =========================
    // Constructeur
    // =========================
    public VueAjouterCategorie(Modele modele, JPanel panneau) {
        this.monModele = modele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Ajouter une Catégorie", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // === Formulaire ===
        JPanel panelFormulaire = new JPanel(new GridLayout(1, 2, 10, 15));
        panelFormulaire.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        panelFormulaire.add(new JLabel("Libellé :"));
        txtLibelle = new JTextField();
        panelFormulaire.add(txtLibelle);

        this.add(panelFormulaire, BorderLayout.CENTER);

        // === Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnValider = new JButton("Ajouter");
        JButton btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);

        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners ===
        btnValider.addActionListener(e -> ajouterCategorie());
        btnAnnuler.addActionListener(e -> retour());
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Ajoute la catégorie saisie dans la base via le modèle.
     * L'ID est automatiquement calculé.
     */
    private void ajouterCategorie() {
        String libelle = txtLibelle.getText().trim();

        if (libelle.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Le libellé ne peut pas être vide !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idCat = monModele.getNextIdCategorie(); // 🔹 ID calculé automatiquement
            monModele.ajouterCategorie(idCat, libelle);
            JOptionPane.showMessageDialog(this,
                    "Catégorie ajoutée avec succès ! (ID=" + idCat + ")",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            retour();
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this,
                    "Cet ID existe déjà !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout en base !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retourne à la vue précédente (liste des catégories ou accueil).
     */
    private void retour() {
        panneauCentrale.removeAll();
        // Ici tu peux ajouter la vue liste des catégories si nécessaire
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }
}
