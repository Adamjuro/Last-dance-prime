/**
 * Classe représentant un utilisateur de type Bénévole.
 * Hérite de la classe Utilisateur.
 * 
 * Les bénévoles peuvent enregistrer les articles à vendre
 * et consulter le catalogue des articles.
 */
public class Benevole extends Utilisateur {

    /**
     * Constructeur pour créer un bénévole avec son identifiant et login.
     * Le rôle est automatiquement défini à "BENEVOLE".
     *
     * @param idUser Identifiant unique de l'utilisateur
     * @param login  Nom de connexion de l'utilisateur
     */
    public Benevole(int idUser, String login) {
        super(idUser, login, "BENEVOLE");
    }
}
