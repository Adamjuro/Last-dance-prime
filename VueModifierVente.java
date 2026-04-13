import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Vue pour modifier une vente existante.
 * Permet d'éditer le titre, la date, l'heure et le lieu d'une vente.
 */
public class VueModifierVente extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;
    private Vente vente;
    private JTextField txtTitre;
    private JTextField txtDateVente;
    private JTextField txtHeureDebut;
    private JTextField txtHeureFin;
    private JTextField txtLieu;

    // =========================
    // Constructeur
    // =========================
    public VueModifierVente(Modele modele, Vente vente, JPanel panneau) {
        this.monModele = modele;
        this.vente = vente;
        this.panneauCentrale = panneau;

        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Modifier la Vente", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // === Formulaire ===
        JPanel panelFormulaire = new JPanel(new GridLayout(5, 2, 10, 15));
        panelFormulaire.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        panelFormulaire.add(new JLabel("Titre :"));
        txtTitre = new JTextField(vente.getTitre());
        panelFormulaire.add(txtTitre);

        panelFormulaire.add(new JLabel("Date (YYYY-MM-DD) :"));
        txtDateVente = new JTextField(vente.getDateVente() != null ? vente.getDateVente().toString() : "");
        panelFormulaire.add(txtDateVente);

        panelFormulaire.add(new JLabel("Heure début (HH:MM) :"));
        txtHeureDebut = new JTextField(vente.getHeureDebut() != null ? vente.getHeureDebut().toString() : "");
        panelFormulaire.add(txtHeureDebut);

        panelFormulaire.add(new JLabel("Heure fin (HH:MM) :"));
        txtHeureFin = new JTextField(vente.getHeureFin() != null ? vente.getHeureFin().toString() : "");
        panelFormulaire.add(txtHeureFin);

        panelFormulaire.add(new JLabel("Lieu :"));
        txtLieu = new JTextField(vente.getLieu());
        panelFormulaire.add(txtLieu);

        this.add(panelFormulaire, BorderLayout.CENTER);

        // === Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnValider = new JButton("Modifier");
        JButton btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);
        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners ===
        btnValider.addActionListener(e -> modifierVente());
        btnAnnuler.addActionListener(e -> retour());
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Valide les modifications et met à jour la vente dans le modèle.
     */
    private void modifierVente() {
        try {
            String titre = txtTitre.getText().trim();
            LocalDate date = txtDateVente.getText().isEmpty() ? null : LocalDate.parse(txtDateVente.getText().trim());
            LocalTime debut = txtHeureDebut.getText().isEmpty() ? null : LocalTime.parse(txtHeureDebut.getText().trim());
            LocalTime fin = txtHeureFin.getText().isEmpty() ? null : LocalTime.parse(txtHeureFin.getText().trim());
            String lieu = txtLieu.getText().trim();

            vente.setTitre(titre);
            vente.setDateVente(date);
            vente.setHeureDebut(debut);
            vente.setHeureFin(fin);
            vente.setLieu(lieu);

            monModele.modifierVente(vente); // méthode à implémenter dans le modèle

            JOptionPane.showMessageDialog(this, "Vente modifiée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            retour();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Annule la modification et revient à la vue de gestion des ventes.
     */
    private void retour() {
        panneauCentrale.removeAll();
        panneauCentrale.add(new VueGererVentes(monModele, monModele.getLesVentes(), panneauCentrale), BorderLayout.CENTER);
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }
}