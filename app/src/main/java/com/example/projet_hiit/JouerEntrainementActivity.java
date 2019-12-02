package com.example.projet_hiit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.SoundPool;
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

import java.util.HashMap;

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
    private static SoundPool soundPool;
    private static HashMap<Integer,SoundPool> soundPoolMap;
    private static final int SON_PERPARATION = 0;
    private static final int SON_TRAVAIL = 1;
    private static final int SON_REPOS = 2;
    private static final int SON_TERMINE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer_entrainement);

        //Initialisation
        this.timerValue = findViewById(R.id.timerValue);
        this.seance = getIntent().getParcelableExtra("SEANCE");
        this.nomTravail = findViewById(R.id.jouer_entrainement_nom_travail);
        initialiserSons(getApplicationContext());

        if(savedInstanceState != null && savedInstanceState.getBoolean("ISTERMINE")){
            termine();
        }
        else if(savedInstanceState != null && savedInstanceState.getParcelable("SEANCE") != null){
            //SEANCE (this.seance)
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
            this.compteur = new Compteur(seance.getPreparation()*1000);   //Met le compteur sur le temps de préparation initial
            compteur.addOnUpdateListener(this);
            nomTravail.setText("Preparation");

            compteur.start();
        }else{
            Toast.makeText(getApplicationContext(),"Aucune séance n\'a été trouvée",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



    private void initialiserSons(Context context) {
        soundPool = new SoundPool.Builder().build();
        soundPoolMap.put(SON_PERPARATION, soundPool.load());
        soundPoolMap.put(SON_TRAVAIL, soundPool.load());
        soundPoolMap.put(SON_REPOS, soundPool.load());
        soundPoolMap.put(SON_TERMINE, soundPool.load());
    }

    public void pausePlay(View view) {
        if (compteur.isPaused()){
            compteur.start();
        }else{
            compteur.pause();
        }
    }

    public void playSound(Context context, int soundId){
        //TODO
    }

    @Override
    public void onUpdate() {
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


        if (sequence.getRepetitions() > nbRepetitionsSequence){
            if (cycle.getRepetitions() > nbRepetitionsCycle){

                if (isRepos){
                    updateInterface(travail.getDuree(),travail.getNom(), Color.RED);
                    isRepos = false;
                }else{
                    updateInterface(cycle.getRepos(),"Repos",Color.CYAN);
                    isRepos = true;
                    nbRepetitionsCycle++;
                }
                compteur.reset();

            }else{
                nbRepetitionsCycle = 0;
                nbRepetitionsSequence++;

                updateInterface(sequence.getReposLong(),"Repos long", Color.BLUE);
                isRepos = true;
                compteur.reset();
            }
        }else{
            termine();
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
        findViewById(R.id.bouton_pauseplay).setEnabled(false);
        //TODO ajouter du son
    }

    public boolean isTermine(){
        return this.isTermine;
    }
}
