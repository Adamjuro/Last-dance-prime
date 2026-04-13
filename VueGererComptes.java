import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class VueGererComptes extends JPanel {
    private Modele monModele;
    private JPanel panneauCentrale;
    private JTable tableComptes;
    private DefaultTableModel tableModel;

    public VueGererComptes(Modele modele, ArrayList<Utilisateur> listeComptes, JPanel panneau) {
        this.monModele = modele;
        this.panneauCentrale = panneau;
        this.setLayout(new BorderLayout(10, 10));

        // Titre
        JLabel titre = new JLabel("Gestion des Comptes Utilisateurs", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(titre, BorderLayout.NORTH);

        // Modèle de table
        String[] colonnes = {"ID", "Login", "Rôle"};
        tableModel = new DefaultTableModel(colonnes, 0);

        // Remplissage automatique
        for (Utilisateur u : listeComptes) {
            Object[] ligne = {
                u.getIdUser(),
                u.getLogin(),
                u.getRole() // Assurez-vous d'avoir getRole() dans votre classe Utilisateur
            };
            tableModel.addRow(ligne);
        }

        tableComptes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableComptes);
        this.add(scrollPane, BorderLayout.CENTER);

   
    }
}