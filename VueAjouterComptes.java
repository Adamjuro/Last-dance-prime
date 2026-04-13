import javax.swing.*;
import java.awt.*;

/**
 * JPanel permettant aux secrétaires d'ajouter un nouveau compte utilisateur.
 * Gère l'insertion dans UTILISATEUR et BENEVOLE.
 */
public class VueAjouterComptes extends JPanel {

    // =========================
    // Attributs
    // =========================
    private Modele monModele;
    private JPanel panneauCentrale;

    private JTextField txtId;
    private JTextField txtLogin;
    private JPasswordField txtMdp;
    private JComboBox<String> comboRole;

    // =========================
    // Constructeur
    // =========================
    public VueAjouterComptes(Modele modele, JPanel panneau) {
        this.monModele = modele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // === Titre ===
        JLabel titre = new JLabel("Ajouter un Nouveau Compte", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titre, BorderLayout.NORTH);

        // === Formulaire ===
        JPanel panelFormulaire = new JPanel(new GridLayout(4, 2, 10, 20));
        panelFormulaire.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        panelFormulaire.add(new JLabel("ID Utilisateur (Numérique) :"));
        txtId = new JTextField();
        panelFormulaire.add(txtId);

        panelFormulaire.add(new JLabel("Login :"));
        txtLogin = new JTextField();
        panelFormulaire.add(txtLogin);

        panelFormulaire.add(new JLabel("Mot de passe :"));
        txtMdp = new JPasswordField();
        panelFormulaire.add(txtMdp);

        panelFormulaire.add(new JLabel("Rôle :"));
        // Selon le CDC, seul l'enregistrement des bénévoles est mentionné explicitement pour les secrétaires
        String[] roles = {"Benevole"};
        comboRole = new JComboBox<>(roles);
        panelFormulaire.add(comboRole);

        this.add(panelFormulaire, BorderLayout.CENTER);

        // === Boutons ===
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnValider = new JButton("Créer le compte");
        JButton btnAnnuler = new JButton("Annuler");
        
        btnValider.setBackground(new Color(46, 204, 113)); // Vert
        btnValider.setForeground(Color.WHITE);
        
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);

        this.add(panelBoutons, BorderLayout.SOUTH);

        // === Listeners ===
        btnValider.addActionListener(e -> enregistrerCompte());
        btnAnnuler.addActionListener(e -> retour());
    }

    // =========================
    // Méthodes
    // =========================

    private void enregistrerCompte() {
        try {
            String idStr = txtId.getText().trim();
            String login = txtLogin.getText().trim();
            String mdp = new String(txtMdp.getPassword()).trim();
            String role = (String) comboRole.getSelectedItem();

            // Vérification des champs
            if (idStr.isEmpty() || login.isEmpty() || mdp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idUser = Integer.parseInt(idStr);

            // Appel au modèle pour l'insertion
            // Le modèle doit gére l'INSERT INTO UTILISATEUR 
            // ET l'INSERT INTO BENEVOLE si le rôle est 'Benevole'
            boolean succes = monModele.ajouterUtilisateur(idUser, login, mdp, role);

            if (succes) {
                JOptionPane.showMessageDialog(this, "Compte " + role + " créé avec succès !");
                retour();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la création (ID peut-être déjà utilisé).", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'ID doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retour() {
        panneauCentrale.removeAll();
       
    }
}