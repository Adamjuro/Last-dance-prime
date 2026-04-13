<?php

namespace App\Models;

use CodeIgniter\Model;

class EnchereModel extends Model
{
    public $table      = 'enchere';
    public $primaryKey = 'id_enchere';

    public $useAutoIncrement = true;
    public $returnType     = 'array';

    public $allowedFields = ['titre', 'date_debut', 'date_fin', 'status'];

    public function getAllEncheres()
    {
        $this->updateStatuses();
        
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Trie ordonné : classer les enchères par leur date de début dans un ordre ascendant (les plus proches en premier)
        $builder->orderBy('date_debut', 'ASC');
        $query = $builder->get();

        return $query->getResultArray();
    }

    /**
     * Récupère les enchères actives (pour la page d'accueil)
     */
    public function getActiveEncheres()
    {
        $this->updateStatuses();
        
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Filtre la sélection : garde uniquement les enchères qui ont un statut figurant dans le tableau (a_venir ou en_cours)
        $builder->whereIn('status', ['a_venir', 'en_cours']);
        // Trie par ordre chronologique
        $builder->orderBy('date_debut', 'ASC');
        $query = $builder->get();

        return $query->getResultArray();
    }

    /**
     * Récupère une enchère spécifique
     */
    public function getEnchere(int $id)
    {
        $this->updateStatuses();

        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        // Cible la requête sur l'ID de l'enchère demandée.
        $builder->where('id_enchere', $id);
        $query = $builder->get();

        return $query->getRowArray();
    }

    /**
     * Crée une nouvelle enchère
     */
    public function createEnchere(array $data)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        $builder->insert($data);
        return $db->insertID();
    }

    /**
     * Met à jour dynamiquement le statut des enchères en fonction de la date actuelle
     */
    public function updateStatuses()
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        $now = \CodeIgniter\I18n\Time::now()->toDateTimeString();

        // Mettre à jour en 'a_venir'
        // Condition 1 : La date de début de l'enchère est strictement supérieure à "maintenant" !
        $builder->where('date_debut >', $now);
        // Condition 2 : Pour éviter des requêtes inutiles on ne met à jour que si ce n'est pas DEJA 'a_venir'
        $builder->where('status !=', 'a_venir');
        $builder->update(['status' => 'a_venir']);

        // Mettre à jour en 'en_cours'
        $builder = $db->table($this->table);
        // Condition 1 : La date de début de l'enchère a été dépassée (est inférieure ou égale à maintenant)
        $builder->where('date_debut <=', $now);
        // Condition 2 : ET la date de fin n'est pas encore dépassée !
        $builder->where('date_fin >', $now);
        // Condition 3 : On filtre ceux qui ne sont pas déjà à l'état 'en_cours'
        $builder->where('status !=', 'en_cours');
        $builder->update(['status' => 'en_cours']);

        // Mettre à jour en 'cloturee'
        // Si l'instant T dépasse date_fin, alors c'est clôturé automatiquement.
        // C'est également compatible avec la clôture manuelle qui remplace la date_fin par l'instant T.
        $builder = $db->table($this->table);
        // Condition 1 : La date de fin de la vente a été atteinte ou dépassée
        $builder->where('date_fin <=', $now);
        // Condition 2 : Pareil, on évite la redondance
        $builder->where('status !=', 'cloturee');
        $builder->update(['status' => 'cloturee']);
    }
}
