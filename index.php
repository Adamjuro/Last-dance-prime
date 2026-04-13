<div class="mb-4" style="display: flex; justify-content: space-between; align-items: center;">
    <div>
        <h1 class="mb-1">Panel Secrétaire</h1>
        <p class="text-muted">Gérez les ventes, les dates et consultez les statistiques.</p>
    </div>
    <div>
        <a href="<?= site_url('secretaire/create') ?>" class="btn btn-primary">+ Créer une Vente</a>
        <a href="<?= site_url('secretaire/stats') ?>" class="btn btn-outline" style="margin-left: 0.5rem;">Statistiques</a>
    </div>
</div>

<div class="card mb-4" style="padding: 0;">
    <div style="padding: 1.5rem; border-bottom: 1px solid var(--border-color);">
        <h2 style="font-size: 1.25rem; margin: 0;">Toutes les Ventes</h2>
    </div>
    
    <?php if (!empty($encheres)): ?>
        <table style="width: 100%; border-collapse: collapse; text-align: left;">
            <thead style="background-color: var(--bg-color); color: var(--text-muted); font-size: 0.875rem;">
                <tr>
                    <th style="padding: 1rem 1.5rem; font-weight: 500;">TITRE</th>
                    <th style="padding: 1rem 1.5rem; font-weight: 500;">DATES</th>
                    <th style="padding: 1rem 1.5rem; font-weight: 500;">STATUT</th>
                    <th style="padding: 1rem 1.5rem; font-weight: 500; text-align: right;">ACTIONS</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($encheres as $enchere) { ?>
                    <tr style="border-bottom: 1px solid var(--border-color);">
                        <td style="padding: 1rem 1.5rem; font-weight: 600;">
                            <?= esc($enchere['titre']) ?>
                        </td>
                        <td style="padding: 1rem 1.5rem; font-size: 0.875rem;">
                            <?= date('d/m/Y H:i', strtotime($enchere['date_debut'])) ?> <br>
                            <span class="text-muted">au <?= date('d/m/Y H:i', strtotime($enchere['date_fin'])) ?></span>
                        </td>
                        <td style="padding: 1rem 1.5rem;">
                            <span class="badge badge-<?= $enchere['status'] ?>">
                                <?= str_replace('_', ' ', ucfirst($enchere['status'])) ?>
                            </span>
                        </td>
                        <td style="padding: 1rem 1.5rem; text-align: right;">
                            <a href="<?= site_url('enchere/details/' . $enchere['id_enchere']) ?>" title="Consulter" style="margin-right: 1rem;">👀 Voir</a>
                            
                            <?php if ($enchere['status'] === 'a_venir'): ?>
                                <a href="<?= site_url('secretaire/edit/' . $enchere['id_enchere']) ?>" title="Modifier" style="margin-right: 1rem;">✏️ Modifier</a>
                            <?php endif; ?>

                            <?php if ($enchere['status'] !== 'cloturee'): ?>
                                <a href="<?= site_url('secretaire/cloturer/' . $enchere['id_enchere']) ?>" title="Clôturer immédiatement" style="margin-right: 1rem; color: var(--danger-color);" onclick="return confirm('Êtes-vous sûr de vouloir clôturer cette vente prématurément ?');">🛑 Clôturer</a>
                            <?php endif; ?>

                            <a href="<?= site_url('secretaire/supprimerEnchere/' . $enchere['id_enchere']) ?>" title="Supprimer la vente" style="margin-right: 1rem; color: #dc3545;" onclick="return confirm('Attention, action irréversible. Voulez-vous vraiment supprimer cette vente ? (Les articles liés retourneront au catalogue)');">❌ Supprimer</a>
                        </td>
                    </tr>
                <?php } ?>
            </tbody>
        </table>
    <?php else: ?>
        <div style="padding: 2rem; text-align: center; color: var(--text-muted);">
            Aucune vente n'a encore été créée.
        </div>
    <?php endif; ?>
</div>
