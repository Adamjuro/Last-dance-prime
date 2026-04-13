
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Couche d'accès aux données pour l'ensemble de l'application.
 * Elle centralise les opérations CRUD sur les entités (prison, bâtiment,
 * cellule, détenu) et est invoquée par la couche contrôleur.
 */
public class Modele {
    private Connection connexion; 
    private Statement st;
    private ResultSet rs;
    private PreparedStatement pst;

    /**
     * Ouvre la connexion JDBC vers la base MySQL distante.
     * Doit être appelée avant toute opération nécessitant la BDD.
     */
    public void connect_database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connexion = DriverManager.getConnection(
                "jdbc:mysql://172.16.203.109/fripouilles?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", 
                "sio", 
                "slam"
            );
            this.st = connexion.createStatement();    
        } catch(ClassNotFoundException erreur) {
            System.out.println("Driver non chargé");
        } catch(SQLException erreur) {
            System.out.println("Problème de connexion à la BDD: " + erreur.getMessage());    
        }
    }

    /**
     * Ferme proprement la connexion JDBC lorsque la fenêtre principale se ferme.
     */
    public void close_connexion() {
        try {
            if (this.connexion != null) {
                this.connexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
    
      
    	// Get les articles
    public ArrayList<Article> getLesArticles() {
        ArrayList<Article> lesArticles = new ArrayList<>();
        try {
            String req = "SELECT a.idArticle, a.description, a.prix, a.photo, a.statut, " +
                         "c.idCategorie, c.libelle AS libelleCategorie, " +
                         "t.idTaille, t.libelle AS libelleTaille, " +
                         "co.idCouleur, co.libelle AS libelleCouleur, " +
                         "g.idGenre, g.libelle AS libelleGenre, " +
                         "e.idEtat, e.libelle AS libelleEtat " +
                         "FROM ARTICLE a " +
                         "LEFT JOIN CATEGORIE c ON a.idCategorie = c.idCategorie " +
                         "LEFT JOIN TAILLE t ON a.idTaille = t.idTaille " +
                         "LEFT JOIN COULEUR co ON a.idCouleur = co.idCouleur " +
                         "LEFT JOIN GENRE g ON a.idGenre = g.idGenre " +
                         "LEFT JOIN ETAT e ON a.idEtat = e.idEtat";

            rs = st.executeQuery(req);
            while (rs.next()) {
                Categorie categorie = new Categorie(rs.getInt("idCategorie"), rs.getString("libelleCategorie"));
                Taille taille = new Taille(rs.getInt("idTaille"), rs.getString("libelleTaille"));
                Couleur couleur = new Couleur(rs.getInt("idCouleur"), rs.getString("libelleCouleur"));
                Genre genre = new Genre(rs.getInt("idGenre"), rs.getString("libelleGenre"));
                Etat etat = new Etat(rs.getInt("idEtat"), rs.getString("libelleEtat"));

                Article a = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("description"),
                    rs.getFloat("prix"),
                    rs.getString("photo"),
                    rs.getString("statut"),
                    categorie,
                    taille,
                    couleur,
                    genre,
                    etat
                );
                lesArticles.add(a);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lesArticles;
    }



    	// Article By ID
    public Article getArticleById(int idArticle) {
        Article article = null;
        try {
            String req = "SELECT a.idArticle, a.description, a.prix, a.photo, a.statut, " +
                         "c.idCategorie, c.libelle AS libelleCategorie, " +
                         "t.idTaille, t.libelle AS libelleTaille, " +
                         "co.idCouleur, co.libelle AS libelleCouleur, " +
                         "g.idGenre, g.libelle AS libelleGenre, " +
                         "e.idEtat, e.libelle AS libelleEtat " +
                         "FROM ARTICLE a " +
                         "LEFT JOIN CATEGORIE c ON a.idCategorie = c.idCategorie " +
                         "LEFT JOIN TAILLE t ON a.idTaille = t.idTaille " +
                         "LEFT JOIN COULEUR co ON a.idCouleur = co.idCouleur " +
                         "LEFT JOIN GENRE g ON a.idGenre = g.idGenre " +
                         "LEFT JOIN ETAT e ON a.idEtat = e.idEtat " +
                         "WHERE a.idArticle = ?";

            PreparedStatement pst = connexion.prepareStatement(req);
            pst.setInt(1, idArticle);
            rs = pst.executeQuery();

            if (rs.next()) {
                Categorie categorie = new Categorie(rs.getInt("idCategorie"), rs.getString("libelleCategorie"));
                Taille taille = new Taille(rs.getInt("idTaille"), rs.getString("libelleTaille"));
                Couleur couleur = new Couleur(rs.getInt("idCouleur"), rs.getString("libelleCouleur"));
                Genre genre = new Genre(rs.getInt("idGenre"), rs.getString("libelleGenre"));
                Etat etat = new Etat(rs.getInt("idEtat"), rs.getString("libelleEtat"));

                article = new Article(
                    rs.getInt("idArticle"),
                    rs.getString("description"),
                    rs.getFloat("prix"),
                    rs.getString("photo"),
                    rs.getString("statut"),
                    categorie,
                    taille,
                    couleur,
                    genre,
                    etat
                );
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    	//ajouter une catégorie
    public void ajouterCategorie(int idCat, String libelle) throws SQLIntegrityConstraintViolationException, SQLException {
        String requete = "INSERT INTO CATEGORIE (idCategorie, libelle) VALUES (?, ?)";
        try (PreparedStatement ps = connexion.prepareStatement(requete)) {
            ps.setInt(1, idCat);
            ps.setString(2, libelle);
            ps.executeUpdate();
        }
    }




    
    
        // Ajouter un article
    public void insererArticle(Article a) {
        try {
            // DEBUG : Afficher toutes les valeurs AVANT insertion
            System.out.println("=== INSERTION ARTICLE ===");
            System.out.println("Description : " + a.getDescription());
            System.out.println("Prix : " + a.getPrix());
            System.out.println("Photo : " + a.getPhoto());
            System.out.println("Statut : " + a.getStatut());
            System.out.println("ID Catégorie : " + a.getIdCategorie());
            System.out.println("ID Taille : " + a.getIdTaille());
            System.out.println("ID Couleur : " + a.getIdCouleur());
            System.out.println("ID Genre : " + a.getIdGenre());
            System.out.println("ID Etat : " + a.getIdEtat());  // ← Celui-ci affiche 0 !
            
            String requete = "INSERT INTO ARTICLE (description, prix, photo, statut, idCategorie, idTaille, idCouleur, idGenre, idEtat) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = connexion.prepareStatement(requete);
            stmt.setString(1, a.getDescription());
            stmt.setFloat(2, a.getPrix());
            stmt.setString(3, a.getPhoto());
            stmt.setString(4, a.getStatut());
            stmt.setInt(5, a.getIdCategorie());
            stmt.setInt(6, a.getIdTaille());
            stmt.setInt(7, a.getIdCouleur());
            stmt.setInt(8, a.getIdGenre());
            stmt.setInt(9, a.getIdEtat());
            
            // DEBUG : Afficher la requête préparée
            System.out.println("Requête SQL : " + stmt.toString());
            
            stmt.executeUpdate();
            stmt.close();
            
            System.out.println("Article inséré avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        // Modifier un article
        public void updateArticle(Article unArticle) {
            try {
                String req = "UPDATE ARTICLE SET description=?, prix=?, photo=?, statut=?, idCategorie=?, idTaille=?, idCouleur=?, idGenre=?, idEtat=? "
                           + "WHERE idArticle=?";
                PreparedStatement ps = connexion.prepareStatement(req);
                ps.setString(1, unArticle.getDescription());
                ps.setFloat(2, unArticle.getPrix());
                ps.setString(3, unArticle.getPhoto());
                ps.setString(4, unArticle.getStatut());
                ps.setInt(5, unArticle.getIdCategorie());
                ps.setInt(6, unArticle.getIdTaille());
                ps.setInt(7, unArticle.getIdCouleur());
                ps.setInt(8, unArticle.getIdGenre());
                ps.setInt(9, unArticle.getIdEtat());
                ps.setInt(10, unArticle.getIdArticle());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Supprimer un article
        public void deleteArticle(int idArticle) {
            try {
                String req = "DELETE FROM ARTICLE WHERE idArticle = ?";
                PreparedStatement ps = connexion.prepareStatement(req);
                ps.setInt(1, idArticle);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
     // ==========================
     // Catégorie
     // ==========================
     public ArrayList<Categorie> getLesCategories() {
         ArrayList<Categorie> lesCategories = new ArrayList<>();
         try {
             String req = "SELECT * FROM CATEGORIE";
             rs = st.executeQuery(req);
             while (rs.next()) {
                 Categorie c = new Categorie(
                     rs.getInt("idCategorie"),
                     rs.getString("libelle")
                 );
                 lesCategories.add(c);
             }
             rs.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return lesCategories;
     }
     
     public int getNextIdCategorie() {
    	    int nextId = 1; // par défaut si pas de catégorie
    	    try {
    	        String sql = "SELECT MAX(idCategorie) AS maxId FROM CATEGORIE";
    	        ResultSet rs = st.executeQuery(sql);
    	        if (rs.next()) {
    	            nextId = rs.getInt("maxId") + 1; // prend le max + 1
    	        }
    	        rs.close();
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	    return nextId;
    	}
     
  // ==========================
  // Taille
  // ==========================
     public ArrayList<Taille> getLesTailles() {
    	    ArrayList<Taille> lesTailles = new ArrayList<>();
    	    try {
    	        String req = "SELECT idTaille, libelle FROM TAILLE";
    	        rs = st.executeQuery(req);
    	        while (rs.next()) {
    	            Taille t = new Taille(rs.getInt("idTaille"), rs.getString("libelle"));
    	            lesTailles.add(t);
    	        }
    	        rs.close();
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	    return lesTailles;
    	}

  
     public ArrayList<Couleur> getLesCouleurs() {
    	    ArrayList<Couleur> lesCouleurs = new ArrayList<>();
    	    try {
    	        String req = "SELECT idCouleur, libelle FROM COULEUR";
    	        rs = st.executeQuery(req);
    	        while (rs.next()) {
    	            Couleur c = new Couleur(rs.getInt("idCouleur"), rs.getString("libelle"));
    	            lesCouleurs.add(c);
    	        }
    	        rs.close();
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	    }
    	    return lesCouleurs;
    	}

	// ==========================
	// Genre
	// ==========================
  public ArrayList<Genre> getLesGenres() {
	    ArrayList<Genre> lesGenres = new ArrayList<>();
	    try {
	        String req = "SELECT idGenre, libelle FROM GENRE";
	        rs = st.executeQuery(req);
	        while (rs.next()) {
	            Genre g = new Genre(rs.getInt("idGenre"), rs.getString("libelle"));
	            lesGenres.add(g);
	        }
	        rs.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return lesGenres;
	}

	// ==========================
	// Etat
	// ==========================
  public ArrayList<Etat> getLesEtats() {
	    ArrayList<Etat> lesEtats = new ArrayList<>();
	    try {
	        String requete = "SELECT idEtat, libelle FROM ETAT";  // ← Vérifiez les noms de colonnes
	        PreparedStatement stmt = connexion.prepareStatement(requete);
	        ResultSet rs = stmt.executeQuery();
	        
	        while (rs.next()) {
	            int id = rs.getInt("idEtat");  // ← Vérifiez que "idEtat" est le bon nom
	            String libelle = rs.getString("libelle");
	            System.out.println("BDD : ID=" + id + ", Libellé=" + libelle);  // DEBUG
	            lesEtats.add(new Etat(id, libelle));
	        }
	        
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return lesEtats;
	}
	
  
	//Ajouter vente
	public void ajouterVente(String titre, java.sql.Date dateVente, java.sql.Time heureDebut, java.sql.Time heureFin, String lieu) {
	    String requete = "INSERT INTO VENTE (titre, dateVente, heureDebut, heureFin, lieu) VALUES (?, ?, ?, ?, ?)";
	    try (PreparedStatement ps = connexion.prepareStatement(requete)) {
	        ps.setString(1, titre);
	        ps.setDate(2, dateVente);
	        ps.setTime(3, heureDebut);
	        ps.setTime(4, heureFin);
	        ps.setString(5, lieu);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	//VenteById
	public Vente getVenteById(int idVente) {
	    Vente vente = null;
	    String requete = "SELECT * FROM VENTE WHERE idVente = ?";
	    try (PreparedStatement ps = connexion.prepareStatement(requete)) {
	        ps.setInt(1, idVente);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            vente = new Vente();
	            vente.setIdVente(rs.getInt("idVente"));
	            vente.setTitre(rs.getString("titre"));

	            java.sql.Date dateSQL = rs.getDate("dateVente");
	            if (dateSQL != null) {
	                vente.setDateVente(dateSQL.toLocalDate());
	            }

	            java.sql.Time heureDebutSQL = rs.getTime("heureDebut");
	            if (heureDebutSQL != null) {
	                vente.setHeureDebut(heureDebutSQL.toLocalTime());
	            }

	            java.sql.Time heureFinSQL = rs.getTime("heureFin");
	            if (heureFinSQL != null) {
	                vente.setHeureFin(heureFinSQL.toLocalTime());
	            }

	            vente.setLieu(rs.getString("lieu"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return vente;
	}
	
	//Modifier Vente
	public void modifierVente(Vente vente) {
	    String requete = "UPDATE VENTE SET titre = ?, dateVente = ?, heureDebut = ?, heureFin = ?, lieu = ? WHERE idVente = ?";

	    try (PreparedStatement ps = connexion.prepareStatement(requete)) {
	        ps.setString(1, vente.getTitre());

	        LocalDate dateVente = vente.getDateVente();
	        ps.setDate(2, dateVente != null ? java.sql.Date.valueOf(dateVente) : null);

	        LocalTime heureDebut = vente.getHeureDebut();
	        ps.setTime(3, heureDebut != null ? java.sql.Time.valueOf(heureDebut) : null);

	        LocalTime heureFin = vente.getHeureFin();
	        ps.setTime(4, heureFin != null ? java.sql.Time.valueOf(heureFin) : null);

	        ps.setString(5, vente.getLieu());
	        ps.setInt(6, vente.getIdVente());

	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	//get vente
	public ArrayList<Vente> getLesVentes() {
	    ArrayList<Vente> lesVentes = new ArrayList<>();
	    String requete = "SELECT * FROM VENTE ORDER BY dateVente, heureDebut";

	    try (Statement stmt = connexion.createStatement();
	         ResultSet rs = stmt.executeQuery(requete)) {

	        while (rs.next()) {
	            int idVente = rs.getInt("idVente");
	            String titre = rs.getString("titre");

	            Date dateSQL = rs.getDate("dateVente");
	            LocalDate dateVente = dateSQL != null ? dateSQL.toLocalDate() : null;

	            Time debutSQL = rs.getTime("heureDebut");
	            LocalTime heureDebut = debutSQL != null ? debutSQL.toLocalTime() : null;

	            Time finSQL = rs.getTime("heureFin");
	            LocalTime heureFin = finSQL != null ? finSQL.toLocalTime() : null;

	            String lieu = rs.getString("lieu");

	            Vente vente = new Vente(idVente, titre, dateVente, heureDebut, heureFin, lieu);
	            lesVentes.add(vente);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lesVentes;
	}
	
	//delete vente
	public void deleteVente(int idVente) {
	    String requete = "DELETE FROM VENTE WHERE idVente = ?";
	    try (PreparedStatement ps = connexion.prepareStatement(requete)) {
	        ps.setInt(1, idVente);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	//article à une vente
	public void ajouterArticleAVente(int idVente, int idArticle) {
	    String requete = "INSERT INTO VENTE_ARTICLE (idVente, idArticle) VALUES (?, ?)";
	    
	    try (PreparedStatement ps = connexion.prepareStatement(requete)) {
	        ps.setInt(1, idVente);
	        ps.setInt(2, idArticle);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public boolean articleDejaDansUneVente(int idArticle) {
	    String requete = "SELECT COUNT(*) FROM VENTE_ARTICLE WHERE idArticle = ?";
	    try (PreparedStatement ps = connexion.prepareStatement(requete)) {
	        ps.setInt(1, idArticle);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // true si l'article est déjà associé à une vente
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // par défaut, l'article n'est pas dans une vente
	}
	
	public Categorie getCategorieById(int idCategorie) {
	    try {
	        String sql = "SELECT * FROM CATEGORIE WHERE idCategorie = ?";
	        PreparedStatement ps = connexion.prepareStatement(sql);
	        ps.setInt(1, idCategorie);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new Categorie(rs.getInt("idCategorie"), rs.getString("libelle"));
	        }
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public Taille getTailleById(int idTaille) {
	    try {
	        String sql = "SELECT * FROM TAILLE WHERE idTaille = ?";
	        PreparedStatement ps = connexion.prepareStatement(sql);
	        ps.setInt(1, idTaille);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new Taille(rs.getInt("idTaille"), rs.getString("libelle"));
	        }
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public Couleur getCouleurById(int idCouleur) {
	    try {
	        String sql = "SELECT * FROM COULEUR WHERE idCouleur = ?";
	        PreparedStatement ps = connexion.prepareStatement(sql);
	        ps.setInt(1, idCouleur);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new Couleur(rs.getInt("idCouleur"), rs.getString("libelle"));
	        }
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public Genre getGenreById(int idGenre) {
	    try {
	        String sql = "SELECT * FROM GENRE WHERE idGenre = ?";
	        PreparedStatement ps = connexion.prepareStatement(sql);
	        ps.setInt(1, idGenre);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new Genre(rs.getInt("idGenre"), rs.getString("libelle"));
	        }
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public Etat getEtatById(int idEtat) {
	    try {
	        String sql = "SELECT * FROM ETAT WHERE idEtat = ?";
	        PreparedStatement ps = connexion.prepareStatement(sql);
	        ps.setInt(1, idEtat);
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return new Etat(rs.getInt("idEtat"), rs.getString("libelle"));
	        }
	        rs.close();
	        ps.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	
	public ArrayList<Article> getArticlesByVente(int idVente) {
	    ArrayList<Article> lesArticles = new ArrayList<>();
	    try {
	        String sql = "SELECT a.* FROM ARTICLE a " +
	                     "JOIN VENTE_ARTICLE va ON a.idArticle = va.idArticle " +
	                     "WHERE va.idVente = ?";
	        PreparedStatement ps = connexion.prepareStatement(sql);
	        ps.setInt(1, idVente);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            // Crée un Article avec les données récupérées
	            Article a = new Article(
	                rs.getInt("idArticle"),
	                rs.getString("description"),
	                rs.getFloat("prix"),
	                rs.getString("photo"),
	                rs.getString("statut"),
	                getCategorieById(rs.getInt("idCategorie")),
	                getTailleById(rs.getInt("idTaille")),
	                getCouleurById(rs.getInt("idCouleur")),
	                getGenreById(rs.getInt("idGenre")),
	                getEtatById(rs.getInt("idEtat"))
	            );
	            lesArticles.add(a);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return lesArticles;
	}
	
	
	public void supprimerArticleDuneVente(int idVente, int idArticle) {
	    String sql = "DELETE FROM VENTE_ARTICLE WHERE idVente = ? AND idArticle = ?";
	    try (PreparedStatement ps = connexion.prepareStatement(sql)) {
	        ps.setInt(1, idVente);
	        ps.setInt(2, idArticle);
	        int lignesSupprimees = ps.executeUpdate();
	        System.out.println("Articles supprimés de la vente : " + lignesSupprimees);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public Utilisateur connecter(String login, String mdp) {
	    Utilisateur user = null;

	    String sql = "SELECT * FROM UTILISATEUR WHERE login = ? AND mdp = ?";

	    try (PreparedStatement ps = connexion.prepareStatement(sql)) {
	        ps.setString(1, login);
	        ps.setString(2, mdp);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            int id = rs.getInt("idUser");
	            String role = rs.getString("role");

	            switch (role) {
	                case "Maire":
	                    user = new Maire(id, login);
	                    break;

	                case "Benevole":
	                    user = new Benevole(id, login);
	                    break;

	                case "Secrétaire":
	                    user = new Secretaire(id, login);
	                    break;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return user; // null si login/mdp incorrects
	}
	
	/**
	 * Ajoute un utilisateur dans la base de données.
	 * Si le rôle est "Benevole", l'ajoute également dans la table BENEVOLE.
	 */
	public boolean ajouterUtilisateur(int idUser, String login, String mdp, String role) {
	    boolean succes = false;
	    String sqlUser = "INSERT INTO UTILISATEUR (idUser, login, mdp, role) VALUES (?, ?, ?, ?)";
	    String sqlBenevole = "INSERT INTO BENEVOLE (idBenevole) VALUES (?)";

	    try {
	        
	        connexion.setAutoCommit(false); // Pour garantir que si la deuxième insertion échoue, 
	        								//la première est annulée (Rollback). Cela évite d'avoir un utilisateur "fantôme" qui n'est pas enregistré comme bénévole.

	        // 1. Insertion dans la table UTILISATEUR
	        try (PreparedStatement psUser = connexion.prepareStatement(sqlUser)) {
	            psUser.setInt(1, idUser);
	            psUser.setString(2, login);
	            psUser.setString(3, mdp);
	            psUser.setString(4, role);
	            psUser.executeUpdate();
	        }

	        // 2. Si c'est un bénévole, insertion dans la table BENEVOLE
	        if ("Benevole".equalsIgnoreCase(role)) {
	            try (PreparedStatement psBenevole = connexion.prepareStatement(sqlBenevole)) {
	                psBenevole.setInt(1, idUser);
	                psBenevole.executeUpdate();
	            }
	        }

	        // Valider la transaction
	        connexion.commit();
	        succes = true;
	        System.out.println("Utilisateur " + login + " ajouté avec succès.");

	    } catch (SQLException e) {
	        try {
	            if (connexion != null) connexion.rollback(); // Annuler en cas d'erreur
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        try {
	            connexion.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return succes;
	}

	/**
	 * Récupère la liste de tous les utilisateurs pour la vue de gestion.
	 */
	public ArrayList<Utilisateur> getLesComptes() {
	    ArrayList<Utilisateur> liste = new ArrayList<>();
	    String sql = "SELECT * FROM UTILISATEUR";
	    try (Statement statement = connexion.createStatement();
	         ResultSet resultSet = statement.executeQuery(sql)) {
	        
	        while (resultSet.next()) {
	            int id = resultSet.getInt("idUser");
	            String login = resultSet.getString("login");
	            String role = resultSet.getString("role");
	            
	            // On réutilise votre Switch existant pour créer les bons objets
	            switch (role) {
	                case "Maire": liste.add(new Maire(id, login)); break;
	                case "Benevole": liste.add(new Benevole(id, login)); break;
	                case "Secrétaire": liste.add(new Secretaire(id, login)); break;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return liste;
	}

	}

