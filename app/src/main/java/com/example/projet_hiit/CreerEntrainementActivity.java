package com.example.projet_hiit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projet_hiit.db.DatabaseClient;
import com.example.projet_hiit.db.model.Cycle;
import com.example.projet_hiit.db.model.Seance;
import com.example.projet_hiit.db.model.Sequence;
import com.example.projet_hiit.db.model.Travail;

public class CreerEntrainementActivity extends AppCompatActivity {

    private DatabaseClient db;

    private static final int PREPARATION = 0;
    private static final int NOM_TRAVAIL = 1;
    private static final int DUREE_TRAVAIL = 2;
    private static final int DUREE_REPOS = 3;
    private static final int REPETITIONS_CYCLE = 4;
    private static final int DUREE_REPOS_LONG = 5;
    private static final int REPETITIONS_SEQUENCE = 6;

    private SparseArray<EditText> mapEditText = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_entrainement);

        db = DatabaseClient.getInstance(getApplicationContext());
    }

    public void creerNouvelleSeance(View view) {

        recupererDonnees(); //Met à jour la variable mapEdittext avec les données de l'utilisateur

        int i = 0; boolean noErrors = true;
        while(i<mapEditText.size() && noErrors){
            noErrors = !mapEditText.valueAt(i).getText().toString().trim().isEmpty();
            i++;
        }

        if (!noErrors){
            mapEditText.valueAt(i-1).setError("Champs requis");
            mapEditText.valueAt(i-1).requestFocus();
            return;
        }

        Travail travail = new Travail(
                mapEditText.valueAt(NOM_TRAVAIL).getText().toString().trim(),
                Integer.parseInt(
                        mapEditText.valueAt(DUREE_TRAVAIL).getText().toString().trim()
                )
        );

        Cycle cycle = new Cycle(travail,
                Integer.parseInt(
                        mapEditText.valueAt(DUREE_REPOS).getText().toString().trim()
                ),
                Integer.parseInt(
                        mapEditText.valueAt(REPETITIONS_CYCLE).getText().toString().trim()
                )
        );

        Sequence sequence = new Sequence(cycle,
                Integer.parseInt(
                        mapEditText.valueAt(DUREE_REPOS_LONG).getText().toString().trim()
                ),
                Integer.parseInt(
                        mapEditText.valueAt(REPETITIONS_SEQUENCE).getText().toString().trim()
                )
        );

        final Seance seance = new Seance(sequence,
                Integer.parseInt(
                        mapEditText.valueAt(PREPARATION).getText().toString().trim()
                )
        );


        class SaveSeance extends AsyncTask<Void, Void, Seance>{

            @Override
            protected Seance doInBackground(Void... voids) {

                db.getAppDatabase().seanceDao().insert(seance);

                return seance;
            }

            @Override
            protected void onPostExecute(Seance seance){
                finish();
                Toast.makeText(getApplicationContext(), "Séance sauvegardée",Toast.LENGTH_LONG).show();
            }

        }


        SaveSeance save = new SaveSeance();
        save.execute();

    }

    private void recupererDonnees(){
        mapEditText.put(NOM_TRAVAIL,((EditText) findViewById(R.id.creerEntrainementNomTravail)));
        mapEditText.put(DUREE_TRAVAIL,((EditText)findViewById(R.id.creerEntrainementDureeTravail)));
        mapEditText.put(DUREE_REPOS,((EditText)findViewById(R.id.creerEntrainementDureeRepos)));
        mapEditText.put(REPETITIONS_CYCLE,((EditText)findViewById(R.id.creerEntrainementRepetitionsCycle)));
        mapEditText.put(DUREE_REPOS_LONG,((EditText)findViewById(R.id.creerEntrainementDureeReposLong)));
        mapEditText.put(REPETITIONS_SEQUENCE,((EditText)findViewById(R.id.creerEntrainementRepetitionsSequence)));
        mapEditText.put(PREPARATION,((EditText)findViewById(R.id.creerEntrainementPreparation)));
    }

}
