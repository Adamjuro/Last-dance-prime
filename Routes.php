<?php

use CodeIgniter\Router\RouteCollection;

/**
 * @var RouteCollection $routes
 */
/*
 * --------------------------------------------------------------------
 * Définition des Routes (Chemins URL)
 * --------------------------------------------------------------------
 */

// Spécifier la route par défaut offre un gain de performances 
// car CodeIgniter n'aura pas besoin d'analyser les répertoires.
$routes->get('/', 'Home::index');

// Routes d'authentification (Connexion, Inscription, Déconnexion)
$routes->get('login', 'Auth::login');
$routes->post('login', 'Auth::processLogin');
$routes->get('register', 'Auth::register');
$routes->post('register', 'Auth::processRegister');
$routes->get('logout', 'Auth::logout');

// Routes réservées au profil "Secrétaire" (Protégées par le filtre auth:secretaire)
$routes->group('secretaire', ['filter' => 'auth:secretaire'], static function ($routes) {
    $routes->get('/', 'Secretaire::index');
    $routes->get('create', 'Secretaire::create');
    $routes->post('store', 'Secretaire::store');
    $routes->get('edit/(:num)', 'Secretaire::edit/$1'); // Route pour modifier une vente
    $routes->post('update', 'Secretaire::update'); // Route pour valider la mise à jour d'une vente
    $routes->get('cloturer/(:num)', 'Secretaire::cloturer/$1'); // Route pour forcer la clôture manuelle
    $routes->get('supprimerEnchere/(:num)', 'Secretaire::supprimerEnchere/$1'); // Route de suppression radicale d'une vente
    $routes->get('stats', 'Secretaire::stats');
    $routes->get('qrcode/(:num)', 'Secretaire::qrcode/$1');
});

// Routes réservées au profil "Bénévole" (Protégées par le filtre auth:benevole)
$routes->group('benevole', ['filter' => 'auth:benevole'], static function ($routes) {
    $routes->get('/', 'Benevole::index');
    $routes->get('add', 'Benevole::add'); // Affichage du formulaire de création d'article
    $routes->post('store', 'Benevole::store'); // Traitement et sauvegarde de l'article en base
    $routes->get('selection', 'Benevole::selection');
    $routes->post('save_selection', 'Benevole::saveSelection');
    $routes->get('ventes', 'Benevole::ventes');
    $routes->get('deleteArticle/(:num)', 'Benevole::deleteArticle/$1');
});

// Routes réservées au profil "Habitant" (Protégées par le filtre auth:habitant)
$routes->group('habitant', ['filter' => 'auth:habitant'], static function ($routes) {
    $routes->get('/', 'Habitant::index');
    $routes->get('profil', 'Habitant::profil');
    $routes->post('update_profil', 'Habitant::updateProfil');
    $routes->get('historique', 'Habitant::historique');
});

// Routes publiques et d'actions liées aux actions d'enchères
$routes->get('enchere/details/(:num)', 'Enchere::details/$1');
$routes->post('enchere/participer', 'Enchere::participer', ['filter' => 'auth:habitant']);
$routes->post('enchere/annuler', 'Enchere::annuler', ['filter' => 'auth:habitant']);
$routes->post('enchere/inscriptionVente/(:num)', 'Enchere::inscriptionVente/$1', ['filter' => 'auth:habitant']);
