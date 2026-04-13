<div class="mb-4">
    <a href="<?= site_url('secretaire') ?>" style="font-size: 0.875rem;">← Retour au panel</a>
    <h1 class="mt-2 text-center">Modifier la Vente #<?= esc($enchere['id_enchere']) ?></h1>
</div>

<div style="max-width: 600px; margin: 0 auto;">
    <div class="card">
        <?php if (isset($validation)): ?>
            <div class="errors-list">
                <?= $validation->listErrors() ?>
            </div>
        <?php endif; ?>

        <?= form_open('secretaire/update') ?>
            <?= form_hidden('id_enchere', $enchere['id_enchere']) ?>

            <!-- Titre -->
            <div class="form-group">
                <?= form_label('Titre de la vente', 'titre', ['class' => 'form-label']) ?>
                <?= form_input([
                    'name'        => 'titre', 
                    'id'          => 'titre', 
                    'value'       => set_value('titre', $enchere['titre']), 
                    'class'       => 'form-control', 
                    'required'    => 'required'
                ]) ?>
            </div>

            <div class="grid" style="grid-template-columns: 1fr 1fr; gap: 1rem;">
                <!-- Date de début -->
                <div class="form-group mb-0">
                    <?= form_label('Date de début', 'date_debut', ['class' => 'form-label']) ?>
                    <?php 
                        // Formater pour input datetime-local form: YYYY-MM-DDThh:mm
                        $date_debut_fmt = date('Y-m-d\TH:i', strtotime($enchere['date_debut']));
                    ?>
                    <input type="datetime-local" name="date_debut" id="date_debut" class="form-control" value="<?= set_value('date_debut', $date_debut_fmt) ?>" required>
                </div>

                <!-- Date de fin -->
                <div class="form-group mb-0">
                    <?= form_label('Date de fin', 'date_fin', ['class' => 'form-label']) ?>
                    <?php 
                        $date_fin_fmt = date('Y-m-d\TH:i', strtotime($enchere['date_fin']));
                    ?>
                    <input type="datetime-local" name="date_fin" id="date_fin" class="form-control" value="<?= set_value('date_fin', $date_fin_fmt) ?>" required>
                </div>
            </div>

            <div class="mt-3 text-muted" style="font-size: 0.75rem;">
                <p>Note : L'état (À venir, En cours, Clôturée) sera mis à jour en fonction de ces nouvelles dates.</p>
            </div>

            <div class="text-center mt-4">
                <?= form_submit('submit', 'Enregistrer les modifications', ['class' => 'btn btn-primary', 'style' => 'width: 100%;']) ?>
            </div>

        <?= form_close() ?>
    </div>
</div>
