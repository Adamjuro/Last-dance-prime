/**
 * Représente un utilisateur de type Secrétaire.
 * Hérite de la classe Utilisateur.
 */
public class Secretaire extends Utilisateur {

    /**
     * Constructeur.
     * @param idUser Identifiant de l'utilisateur
     * @param login Login de l'utilisateur
     */
    public Secretaire(int idUser, String login) {
        super(idUser, login, "SECRETAIRE");
    }
}
