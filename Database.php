<?php

namespace Config;

use CodeIgniter\Database\Config;

/**
 * Configuration de la Base de Données
 */
class Database extends Config
{
    /**
     * Le répertoire qui contient les fichiers 
     * de Migrations et de Seeds (Jeux d'essai).
     */
    public string $filesPath = APPPATH . 'Database' . DIRECTORY_SEPARATOR;

    /**
     * Vous permet de choisir quel groupe de connexion 
     * utiliser si aucun autre n'est spécifié.
     */
    public string $defaultGroup = 'default';

    /**
     * Paramètres de la connexion à la base de données principale (par défaut).
     */
    public array $default = [
        'DSN'          => '',
        'hostname'     => '172.16.203.108',
        'username'     => 'sio',
        'password'     => 'slam',
        'database'     => 'enchereaporter',
        'DBDriver'     => 'MySQLi',
        'DBPrefix'     => '',
        'pConnect'     => false,
        'DBDebug'      => true,
        'charset'      => 'utf8mb4',
        'DBCollat'     => 'utf8mb4_general_ci',
        'swapPre'      => '',
        'encrypt'      => false,
        'compress'     => false,
        'strictOn'     => false,
        'failover'     => [],
        'port'         => 3306,
        'numberNative' => false,
        'dateFormat'   => [
            'date'     => 'Y-m-d',
            'datetime' => 'Y-m-d H:i:s',
            'time'     => 'H:i:s',
        ],
    ];

    /**
     * Cette connexion de base de données est utilisée uniquement
     * lors de l'exécution des tests automatisés PHPUnit.
     */
    public array $tests = [
        'DSN'         => '',
        'hostname'    => '127.0.0.1',
        'username'    => '',
        'password'    => '',
        'database'    => ':memory:',
        'DBDriver'    => 'SQLite3',
        'DBPrefix'    => 'db_',  // Needed to ensure we're working correctly with prefixes live. DO NOT REMOVE FOR CI DEVS
        'pConnect'    => false,
        'DBDebug'     => true,
        'charset'     => 'utf8',
        'DBCollat'    => 'utf8_general_ci',
        'swapPre'     => '',
        'encrypt'     => false,
        'compress'    => false,
        'strictOn'    => false,
        'failover'    => [],
        'port'        => 3306,
        'foreignKeys' => true,
        'busyTimeout' => 1000,
    ];

    public function __construct()
    {
        parent::__construct();

        // Ce code garantit que le groupe 'tests' sera toujours utilisé
        // en environnement d'intégration continue/test automatisé, afin de
        // ne jamais écraser la base de données de production par accident.
        if (ENVIRONMENT === 'testing') {
            $this->defaultGroup = 'tests';
        }
    }
}
