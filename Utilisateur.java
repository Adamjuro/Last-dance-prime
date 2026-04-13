/**
 * Classe abstraite représentant un utilisateur de l'application.
 * Elle contient les informations communes à tous les utilisateurs : 
 * identifiant, login et rôle.
 */
public abstract class Utilisateur {

    // =========================
    // Attributs
    // =========================
    protected int idUser;   // Identifiant unique de l'utilisateur
    protected String login; // Nom de connexion
    protected String role;  // Rôle de l'utilisateur (Benevole, Secretaire, Maire, etc.)

    // =========================
    // Constructeur
    // =========================
    /**
     * Constructeur complet
     * @param idUser Identifiant unique de l'utilisateur
     * @param login Nom de connexion
     * @param role Rôle de l'utilisateur
     */
    public Utilisateur(int idUser, String login, String role) {
        this.idUser = idUser;
        this.login = login;
        this.role = role;
    }

    // =========================
    // Getters
    // =========================
    /**
     * Retourne l'identifiant de l'utilisateur
     * @return idUser
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * Retourne le login de l'utilisateur
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Retourne le rôle de l'utilisateur
     * @return role
     */
    public String getRole() {
        return role;
    }
}
