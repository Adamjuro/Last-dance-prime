<?php

namespace App\Models;

use CodeIgniter\Model;

class UtilisateurModel extends Model
{
    public $table      = 'utilisateur';
    public $primaryKey = 'id_util';

    public $useAutoIncrement = true;
    public $returnType     = 'array';

    public $allowedFields = ['nom', 'prenom', 'email', 'mot_de_passe', 'adresse', 'code_postal', 'ville', 'role'];

    /**
     * Authentifie l'utilisateur via son adresse email
     */
    public function getUserByEmail(string $email)
    {
        $db = \Config\Database::connect();
        $builder = $db->table($this->table);
        $builder->where('email', $email);
        $query = $builder->get();

        return $query->getRowArray();
    }
}
