package com.example.projet_hiit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer_entrainement);

        //Initialisation
        this.timerValue = findViewById(R.id.timerValue);
        this.seance = getIntent().getParcelableExtra("SEANCE");
        this.nomTravail = findViewById(R.id.jouer_entrainement_nom_travail);


        if (seance != null){
            this.compteur = new Compteur(seance.getPreparation()*1000);   //Met le compteur sur le temps de préparation initial
            compteur.addOnUpdateListener(this);
            nomTravail.setText("Preparation");
            compteur.start();
        }else{
            Toast.makeText(getApplicationContext(), "Aucune séance n\'a été trouvée",Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public void pausePlay(View view) {
        if (compteur.isPaused()){
            compteur.start();
        }else{
            compteur.pause();
        }
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
            nomTravail.setText("Terminé !");
            timerValue.setText("0:00:00");
            nomTravail.setTextColor(Color.GREEN);
        }
    }

    private void updateInterface(int temps, String travail, int couleurFond){
        compteur.setTempsTotal(temps*1000);
        nomTravail.setText(travail);
        nomTravail.setTextColor(couleurFond);
    }
}
