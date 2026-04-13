import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * JPanel pour ajouter une nouvelle vente.
 * Permet de saisir le titre, la date, l'heure de début, l'heure de fin et le lieu.
 */
public class VueAjouterVente extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;

    private JTextField txtTitre;
    private JTextField txtDate;
    private JTextField txtHeureDebut;
    private JTextField txtHeureFin;
    private JTextField txtLieu;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    // =========================
    // Constructeur
    // =========================
    public VueAjouterVente(Modele modele, JPanel panneau) {
        this.monModele = modele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Ajouter une Vente", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // === Formulaire ===
        JPanel panelFormulaire = new JPanel(new GridLayout(5, 2, 10, 15));
        panelFormulaire.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        panelFormulaire.add(new JLabel("Titre :"));
        txtTitre = new JTextField();
        panelFormulaire.add(txtTitre);

        panelFormulaire.add(new JLabel("Date (yyyy-MM-dd) :"));
        txtDate = new JTextField();
        panelFormulaire.add(txtDate);

        panelFormulaire.add(new JLabel("Heure début (HH:mm:ss) :"));
        txtHeureDebut = new JTextField();
        panelFormulaire.add(txtHeureDebut);

        panelFormulaire.add(new JLabel("Heure fin (HH:mm:ss) :"));
        txtHeureFin = new JTextField();
        panelFormulaire.add(txtHeureFin);

        panelFormulaire.add(new JLabel("Lieu :"));
        txtLieu = new JTextField();
        panelFormulaire.add(txtLieu);

        this.add(panelFormulaire, BorderLayout.CENTER);

        // === Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnValider = new JButton("Ajouter");
        JButton btnAnnuler = new JButton("Annuler");
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);

        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners ===
        btnValider.addActionListener(e -> ajouterVente());
        btnAnnuler.addActionListener(e -> retour());
    }

    // =========================
    // Méthodes
    // =========================

    /**
     * Récupère les données du formulaire et ajoute la vente via le modèle.
     */
    private void ajouterVente() {
        try {
            String titre = txtTitre.getText().trim();
            String dateStr = txtDate.getText().trim();
            String debutStr = txtHeureDebut.getText().trim();
            String finStr = txtHeureFin.getText().trim();
            String lieu = txtLieu.getText().trim();

            if (titre.isEmpty() || dateStr.isEmpty() || debutStr.isEmpty() || finStr.isEmpty() || lieu.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Tous les champs doivent être remplis !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date dateVente = new Date(dateFormat.parse(dateStr).getTime());
            Time heureDebut = new Time(timeFormat.parse(debutStr).getTime());
            Time heureFin = new Time(timeFormat.parse(finStr).getTime());

            monModele.ajouterVente(titre, dateVente, heureDebut, heureFin, lieu);

            JOptionPane.showMessageDialog(this,
                    "Vente ajoutée avec succès !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            retour();

        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Format de date ou heure invalide !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retourne à la vue précédente (par exemple, la liste des ventes).
     */
    private void retour() {
        panneauCentrale.removeAll();
        // Ici tu peux ajouter la vue liste des ventes si nécessaire
        panneauCentrale.revalidate();
        panneauCentrale.repaint();
    }
}
