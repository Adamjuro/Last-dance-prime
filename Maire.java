/**
 * Classe représentant un utilisateur de type Maire.
 * Hérite de la classe Utilisateur.
 */
public class Maire extends Utilisateur {

    /**
     * Constructeur.
     * @param idUser Identifiant de l'utilisateur
     * @param login Login de l'utilisateur
     */
    public Maire(int idUser, String login) {
        // On fixe le rôle à "MAIRE"
        super(idUser, login, "MAIRE");
    }
}
