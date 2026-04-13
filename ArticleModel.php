<?php

namespace App\Models;

use CodeIgniter\Model;

class ArticleModel extends Model
{
    public $table      = 'article';
    public $primaryKey = 'id_article';

    public $useAutoIncrement = true;
    public $returnType     = 'array';

    public $allowedFields = ['nom_article', 'description', 'taille', 'prix_origine', 'prix_depart', 'prix_actuel', 'photo', 'id_enchere'];

    /**
     * Récupère tous les articles sans enchère (pour la vue de sélection bénévole)
     */
    public function getArticlesSansEnchere()
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Filtre la requête : SELECTIONNE UNIQUEMENT les articles où la colonne 'id_enchere' est vide (NULL)
        $builder->where('id_enchere IS NULL');
        $query = $builder->get();

        return $query->getResultArray();
    }

    /**
     * Associe des articles à une enchère spécifique
     */
    public function affecterAEnchere(array $article_ids, int $id_enchere)
    {
        if (empty($article_ids)) {
            return false;
        }
        
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Filtre la requête : SELECTIONNE les articles dont l'identifiant ('id_article') se trouve dans le tableau fourni ($article_ids)
        $builder->whereIn('id_article', $article_ids);
        return $builder->update(['id_enchere' => $id_enchere]);
    }

    /**
     * Récupère les articles pour une enchère spécifique
     */
    public function getArticlesByEnchere(int $id_enchere)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Filtre la requête : SELECTIONNE tous les articles appartenant à l'enchère ciblée ($id_enchere)
        $builder->where('id_enchere', $id_enchere);
        $query = $builder->get();

        return $query->getResultArray();
    }

    /**
     * Récupère un article spécifique et les détails de son enchère
     */
    public function getArticleDetails(int $id_article)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Déclare les colonnes à récupérer : toutes celles de l'article, plus le 'status' et la 'date_fin' de l'enchère
        $builder->select('article.*, enchere.status, enchere.date_fin');
        // Effectue une jointure à gauche (LEFT JOIN) avec la table 'enchere' basé sur 'id_enchere' pour rapatrier ses données associées
        $builder->join('enchere', 'enchere.id_enchere = article.id_enchere', 'left');
        // Filtre la requête pour récupérer uniquement les détails de l'article ciblé ($id_article)
        $builder->where('article.id_article', $id_article);
        $query = $builder->get();

        return $query->getRowArray();
    }

    /**
     * Met à jour le prix actuel d'un article
     */
    public function updatePrixActuel(int $id_article, float $nouveau_prix)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Cible spécifiquement l'article dont on modifie le prix.
        $builder->where('id_article', $id_article);
        return $builder->update(['prix_actuel' => $nouveau_prix]);
    }

    /**
     * Récupère tous les articles associés à une enchère qui est "cloturee".
     */
    public function getArticlesVendus()
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // On sélectionne toutes les infos de l'article + le titre et la date_fin de son enchère
        $builder->select('article.*, enchere.titre, enchere.date_fin');
        // Jointure interne (INNER JOIN) : l'article DOIT être lié à une enchère existante
        $builder->join('enchere', 'enchere.id_enchere = article.id_enchere');
        // Filtre : uniquement les articles liés à une enchère qui a le statut 'cloturee'
        $builder->where('enchere.status', 'cloturee');
        // Trie ordonné : par date de fin d'enchère descendante (du plus récent au plus ancien)
        $builder->orderBy('enchere.date_fin', 'DESC');
        $query = $builder->get();

        return $query->getResultArray();
    }
}
