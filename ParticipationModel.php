<?php

namespace App\Models;

use CodeIgniter\Model;

class ParticipationModel extends Model
{
    public $table      = 'participation';
    public $primaryKey = 'id_participation';

    public $useAutoIncrement = true;
    public $returnType     = 'array';

    public $allowedFields = ['id_util', 'id_article', 'montant_mise', 'date_mise', 'annulee', 'gagnant'];

    /**
     * Obtenir l'historique des participations d'un utilisateur
     */
    public function getHistoriqueUtilisateur(int $id_util)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // On sélectionne toutes les infos de la participation, plus le nom/photo de l'article, et le titre/statut de l'enchère
        $builder->select('participation.*, article.nom_article, article.photo, enchere.titre, enchere.status');
        // Jointure avec l'article lié à la participation
        $builder->join('article', 'article.id_article = participation.id_article');
        // Jointure avec l'enchère liée à l'article
        $builder->join('enchere', 'enchere.id_enchere = article.id_enchere');
        // Filtre : Uniquement les participations de cet utilisateur précis
        $builder->where('participation.id_util', $id_util);
        // Trie par ordre décroissant de date de mise (la plus récente d'abord)
        $builder->orderBy('participation.date_mise', 'DESC');
        $query = $builder->get();

        return $query->getResultArray();
    }

    /**
     * Vérifier si un utilisateur a déjà une mise active sur un article
     */
    public function getMiseActiveUtilisateur(int $id_util, int $id_article)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Cherche une mise pour l'utilisateur ciblé...
        $builder->where('id_util', $id_util);
        // ... sur cet article précis...
        $builder->where('id_article', $id_article);
        // ... avec pour condition qu'elle ne soit pas annulée.
        $builder->where('annulee', 0);
        $query = $builder->get();

        return $query->getRowArray();
    }

    /**
     * Placer une enchère (Transaction)
     */
    public function placerEnchere(int $id_util, int $id_article, float $montant)
    {
        $db = \Config\Database::connect();
        
        $db->transStart();

        // 1. Annuler l'ancienne mise de cet utilisateur s'il y en a une
        $builder = $db->table($this->table);
        // Cible la mise précédente de l'utilisateur sur cet article précis
        $builder->where('id_util', $id_util);
        $builder->where('id_article', $id_article);
        // Met à jour son statut pour la marquer en tant qu'annulée
        $builder->update(['annulee' => 1]);

        // 2. Insérer la nouvelle mise
        $data = [
            'id_util' => $id_util,
            'id_article' => $id_article,
            'montant_mise' => $montant,
            'date_mise' => date('Y-m-d H:i:s'),
            'annulee' => 0,
            'gagnant' => 0
        ];
        $builder = $db->table($this->table);
        $builder->insert($data);

        // 3. Mettre à jour le prix actuel de l'article
        $builderArticle = $db->table('article');
        // Cible l'article concerné par la mise
        $builderArticle->where('id_article', $id_article);
        // Met à jour le "prix actuel" directement sur l'article
        $builderArticle->update(['prix_actuel' => $montant]);

        $db->transComplete();

        return $db->transStatus();
    }

    /**
     * Annuler une participation spécifique
     */
    public function annulerParticipation(int $id_participation, int $id_util)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Identifie la participation exacte via son ID
        $builder->where('id_participation', $id_participation);
        // Sécurité additionnelle : vérifie que c'est bien l'utilisateur qui l'a créée
        $builder->where('id_util', $id_util);
        return $builder->update(['annulee' => 1]);
    }

    /**
     * Calcule la somme des ventes (CA) pour les enchères clôturées
     */
    public function getStatsCA()
    {
        $db = \Config\Database::connect();
        // Option 1 : on somme les mises de tout le monde qui a "gagné"
        // Si la gestion gagnant n'est pas encore faite, on prend la dernière mise pour chaque article lié à une enchère clôturée.
        // Ici, on supposera qu'on somme simplement les participations où gagnant = 1 pour simplifier, ou on somme simplement le max en sous-requête.
        // Puisque la consigne indique selectSum('montant_mise') très simplement :
        $builder = $db->table($this->table);
        // Somme de la colonne 'montant_mise', stockée sous l'alias 'total_ca'
        $builder->selectSum('montant_mise', 'total_ca');
        // Liaison avec la table article
        $builder->join('article', 'article.id_article = participation.id_article');
        // Liaison avec la table enchere pour connaître le statut de l'enchère
        $builder->join('enchere', 'enchere.id_enchere = article.id_enchere');
        // Filtre 1 : L'enchère doit obligatoirement être terminée
        $builder->where('enchere.status', 'cloturee');
        // Filtre 2 : La mise ne doit pas être annulée
        $builder->where('participation.annulee', 0);
        $query = $builder->get();

        $result = $query->getRowArray();
        return $result['total_ca'] ?? 0;
    }

    /**
     * Calcule la somme des ventes (CA) pour une enchère spécifique
     */
    public function getStatsCAByEnchere(int $id_enchere)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Addition totale des montants
        $builder->selectSum('montant_mise', 'total_ca');
        // Jointure pour savoir dans quelle vente se trouve chaque mise via l'article
        $builder->join('article', 'article.id_article = participation.id_article');
        // Filtre restreignant le résultat à l'enchère spécifique passée en argument
        $builder->where('article.id_enchere', $id_enchere);
        // Exclut les offres annulées
        $builder->where('participation.annulee', 0);
        $query = $builder->get();

        $result = $query->getRowArray();
        return $result['total_ca'] ?? 0;
    }
}
