<div class="mb-4">
    <a href="<?= site_url('secretaire') ?>" style="font-size: 0.875rem;">← Retour au panel</a>
    <h1 class="mt-2">Statistiques des Ventes</h1>
    <p class="text-muted">Aperçu global du Chiffre d'Affaires réalisé lors des ventes clôturées.</p>
</div>

<div class="grid" style="grid-template-columns: 1fr;">
    <!-- CA Global -->
    <div class="card" style="text-align: center; background-color: var(--accent-color); color: white; padding: 3rem 1rem;">
        <h2 style="font-size: 1.25rem; font-weight: 500; margin-bottom: 0.5rem; opacity: 0.9;">Chiffre d'Affaires Global</h2>
        <div style="font-size: 3.5rem; font-weight: 800; line-height: 1;">
            <?= number_format($totalCA ?? 0, 2, ',', ' ') ?> €
        </div>
    </div>
</div>

<h2 class="mt-4 mb-3" style="font-size: 1.5rem;">Détails par Vente (Clôturée)</h2>

<div class="card" style="padding: 0;">
    <?php if (empty($details)): ?>
        <div style="padding: 2rem; text-align: center; color: var(--text-muted);">
            Il n'y a aucune donnée à afficher pour le moment (aucune vente clôturée avec participation).
        </div>
    <?php else: ?>
        <table style="width: 100%; border-collapse: collapse; text-align: left;">
            <thead style="background-color: var(--bg-color); color: var(--text-muted); font-size: 0.875rem;">
                <tr>
                    <th style="padding: 1rem 1.5rem; font-weight: 500;">VENTE</th>
                    <th style="padding: 1rem 1.5rem; font-weight: 500; text-align: right;">CHIFFRE D'AFFAIRES</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($details as $enchere) { ?>
                    <tr style="border-bottom: 1px solid var(--border-color);">
                        <td style="padding: 1rem 1.5rem; font-weight: 600;">
                            <?= esc($enchere['titre']) ?>
                            <div class="text-muted" style="font-size: 0.75rem; font-weight: 400; margin-top: 0.25rem;">
                                Clôturée le <?= date('d/m/Y', strtotime($enchere['date_fin'])) ?>
                            </div>
                        </td>
                        <td style="padding: 1rem 1.5rem; font-weight: 700; text-align: right; color: var(--text-main); font-size: 1.125rem;">
                            <?= number_format($enchere['ca'] ?? 0, 2, ',', ' ') ?> €
                        </td>
                    </tr>
                <?php } ?>
            </tbody>
        </table>
    <?php endif; ?>
</div>
