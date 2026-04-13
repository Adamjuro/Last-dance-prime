import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de connexion à l'application Fripouilles.
 * Permet à l'utilisateur de saisir son login et mot de passe,
 * et d'être redirigé vers la fenêtre principale selon son rôle.
 */
public class LoginFrame extends JFrame {

    private Modele modele; // Accès aux méthodes de la couche modèle

    /**
     * Constructeur : initialise la fenêtre de login.
     */
    public LoginFrame() {
        // Connexion à la base de données via le modèle
        modele = new Modele();
        modele.connect_database();

        // Paramètres de la fenêtre
        setTitle("Connexion");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10)); // 3 lignes x 2 colonnes

        // Champs de saisie
        JTextField txtLogin = new JTextField();
        JPasswordField txtMdp = new JPasswordField();
        JButton btnConnexion = new JButton("Connexion");

        // Ajout des composants dans la fenêtre
        add(new JLabel("Login :"));
        add(txtLogin);
        add(new JLabel("Mot de passe :"));
        add(txtMdp);
        add(new JLabel()); // placeholder vide
        add(btnConnexion);

        // Action lors du clic sur le bouton "Connexion"
        btnConnexion.addActionListener(e -> {
            String login = txtLogin.getText();
            String mdp = new String(txtMdp.getPassword());

            // Tentative de connexion via le modèle
            Utilisateur utilisateurConnecte = modele.connecter(login, mdp);

            if (utilisateurConnecte == null) {
                // Login ou mot de passe incorrect
                JOptionPane.showMessageDialog(this,
                        "Login ou mot de passe incorrect",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                // Login réussi : fermeture de la fenêtre de login
                dispose();

                // Ouverture de la fenêtre principale adaptée au rôle de l'utilisateur
                new FenetrePrincipaleFripouilles(utilisateurConnecte);
            }
        });

        setVisible(true);
    }
}
