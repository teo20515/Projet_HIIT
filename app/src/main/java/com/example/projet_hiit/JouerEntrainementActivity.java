package com.example.projet_hiit;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_hiit.db.compteur.Compteur;
import com.example.projet_hiit.db.compteur.OnUpdateListener;
import com.example.projet_hiit.db.model.Cycle;
import com.example.projet_hiit.db.model.Seance;
import com.example.projet_hiit.db.model.Sequence;
import com.example.projet_hiit.db.model.Travail;

public class JouerEntrainementActivity extends AppCompatActivity implements OnUpdateListener {

    //View
    private TextView timerValue;
    private TextView nomTravail;

    //DATA
    private Compteur compteur;
    private Seance seance;
    private int nbRepetitionsCycle = 0;
    private int nbRepetitionsSequence = 0;
    private boolean isRepos = true;
    private boolean isTermine = false;

    //SON
    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer_entrainement);

        //Initialisation
        this.timerValue = findViewById(R.id.timerValue);
        this.seance = getIntent().getParcelableExtra("SEANCE");
        this.nomTravail = findViewById(R.id.jouer_entrainement_nom_travail);


        if(savedInstanceState != null && savedInstanceState.getBoolean("ISTERMINE")){
            termine();  //Si on a une séance enregistrée comme étant terminée on affiche l'état terminé
        }
        else if(savedInstanceState != null && savedInstanceState.getParcelable("SEANCE") != null){

            //Restauration du contexte sauvegardé

            //SEANCE
            this.seance = savedInstanceState.getParcelable("SEANCE");
            //COMPTEUR
            this.compteur = new Compteur(savedInstanceState.getLong("TEMPSRESTANT"));
            this.compteur.addOnUpdateListener(this);
            this.timerValue.setText(compteur.getMinutes() + ":" + String.format("%02d", compteur.getSecondes()) + ":" + String.format("%03d", compteur.getMillisecondes()));
            //NOMTRAVAIL
            this.nomTravail.setText(savedInstanceState.getString("NOMTRAVAIL"));
            //ISREPOS
            this.isRepos = savedInstanceState.getBoolean("ISREPOS");
            //NBREPETITIONSCYCLE
            this.nbRepetitionsCycle = savedInstanceState.getInt("NBREPETITIONSCYCLE");
            //NBRERETITIONSSEQUENCE
            this.nbRepetitionsSequence = savedInstanceState.getInt("NBREPETITIONSSEQUENCE");
            //MISE A JOUR DE L'INTERFACE
            updateInterface((this.compteur.getMilisecondesRestantes()/1000),this.nomTravail.getText().toString(),savedInstanceState.getInt("COULEUR"));
            //IS PAUSED
            if(savedInstanceState.getBoolean("ISPAUSED")){
                compteur.pause();
            }else{
                compteur.start();
            }
        } else if (seance != null){
            //Si aucun contexte sauvegardé n'est trouvé on lance un nouvel entrainement
            this.compteur = new Compteur(seance.getPreparation()*1000);   //Met le compteur sur le temps de préparation initial

            compteur.addOnUpdateListener(this);

            nomTravail.setText("Preparation");

            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sifflet_debut);
            mediaPlayer.start();

            compteur.start();
        }else{
            Toast.makeText(getApplicationContext(),"Aucune séance n\'a été trouvée",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Sauvegarde des éléments nécessaires à la restauration de la séance d'entrainement
        if (isTermine()){
            outState.putBoolean("ISTERMINE",true);
        }else {
            outState.putParcelable("SEANCE", this.seance);
            outState.putLong("TEMPSRESTANT", this.compteur.getMilisecondesRestantes());
            outState.putString("NOMTRAVAIL", this.nomTravail.getText().toString());
            outState.putBoolean("ISREPOS", this.isRepos);
            outState.putInt("NBREPETITIONSCYCLE", this.nbRepetitionsCycle);
            outState.putInt("NBREPETITIONSSEQUENCE", this.nbRepetitionsSequence);
            outState.putInt("COULEUR", this.nomTravail.getCurrentTextColor());
            outState.putBoolean("ISPAUSED", this.compteur.isPaused());
        }
    }

    @Override
    public void onBackPressed() {
        //Libère les ressources du mediaPlayer
        if(mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;

        //Retour direct à la MainActivity
        Intent intent = new Intent(JouerEntrainementActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Libère les ressources du mediaPlayer
        if (mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
    }

    public void pausePlay(View view) {
        //Change l'état du compteur
        if (compteur.isPaused()){
            compteur.start();
        }else{
            compteur.pause();
        }
    }

    @Override
    public void onUpdate() {
        //Si il reste du temps à passer, on pet à jour le texte du compteur, sinon on change d'étape
        if (compteur.getMilisecondesRestantes() != 0){
            timerValue.setText(compteur.getMinutes() + ":" + String.format("%02d", compteur.getSecondes()) + ":" + String.format("%03d", compteur.getMillisecondes()));
        }else{
            miseAJourEtape();
            compteur.start();
        }
    }

    private void miseAJourEtape() {
        Sequence sequence = seance.getSequence();
        Cycle cycle = sequence.getCycle();
        Travail travail = cycle.getTravail();


        if (sequence.getRepetitions() > nbRepetitionsSequence){ //Si il reste des répétitions de séquence
            if (cycle.getRepetitions() > nbRepetitionsCycle){  //Si il reste des répétitions au cycle en cours

                if (isRepos){   //Si on était en état "repos"
                    updateInterface(travail.getDuree(),travail.getNom(), Color.RED);    //Mise à jour de l'interface avec le travail
                    isRepos = false;
                }else{
                    updateInterface(cycle.getRepos(),"Repos",Color.CYAN);   //Mise à jour de l'interface en mode "repos"
                    isRepos = true;
                    nbRepetitionsCycle++;   //Augmentation du nombre de cycle passés
                }
                compteur.reset();

            }else{
                nbRepetitionsCycle = 0; //Si on a fait le nombre de répétition du cycle, on le réinitialise
                nbRepetitionsSequence++;    //Et on incrémente le nombre de séquences

                updateInterface(sequence.getReposLong(),"Repos long", Color.BLUE);  //Mise à jour de l'interface en mode "repos long"
                isRepos = true;
                compteur.reset();
            }
        }else{
            termine();  //Si le nombre de répétitions de la séquence est atteint, on termine l'entrainement
        }
    }

    private void updateInterface(long temps, String travail, int couleurFond){
        compteur.setTempsTotal(temps*1000);
        nomTravail.setText(travail);
        nomTravail.setTextColor(couleurFond);
    }

    private void termine(){
        this.isTermine = true;
        nomTravail.setText("Terminé !");
        timerValue.setText("0:00:00");
        nomTravail.setTextColor(Color.GREEN);
        findViewById(R.id.bouton_pauseplay).setEnabled(false);  //On désactive le bouton de controle du timer une fois terminé
    }

    public boolean isTermine(){
        return this.isTermine;
    }
}
