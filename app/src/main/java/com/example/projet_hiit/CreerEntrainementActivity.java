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

    //Initialisation de la base de données
    private DatabaseClient db;

    //Identification des éléments nécessaires pour créer la séance, utilisés comme clé du SparseArray mapEditText
    private static final int PREPARATION = 0;
    private static final int NOM_TRAVAIL = 1;
    private static final int DUREE_TRAVAIL = 2;
    private static final int DUREE_REPOS = 3;
    private static final int REPETITIONS_CYCLE = 4;
    private static final int DUREE_REPOS_LONG = 5;
    private static final int REPETITIONS_SEQUENCE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_entrainement);

        //Instantiation de la base de données
        db = DatabaseClient.getInstance(getApplicationContext());
    }

    //Recupere les données entrées par l'utilisateur
    private SparseArray<EditText> recupererDonnees(){
        SparseArray<EditText> array = new SparseArray<>();  //Optimal pour des clés de type Integer par rapport à une HashMap
        array.put(NOM_TRAVAIL,((EditText) findViewById(R.id.creerEntrainementNomTravail)));
        array.put(DUREE_TRAVAIL,((EditText)findViewById(R.id.creerEntrainementDureeTravail)));
        array.put(DUREE_REPOS,((EditText)findViewById(R.id.creerEntrainementDureeRepos)));
        array.put(REPETITIONS_CYCLE,((EditText)findViewById(R.id.creerEntrainementRepetitionsCycle)));
        array.put(DUREE_REPOS_LONG,((EditText)findViewById(R.id.creerEntrainementDureeReposLong)));
        array.put(REPETITIONS_SEQUENCE,((EditText)findViewById(R.id.creerEntrainementRepetitionsSequence)));
        array.put(PREPARATION,((EditText)findViewById(R.id.creerEntrainementPreparation)));

        return array;
    }

    public void creerNouvelleSeance(View view) {

        SparseArray<EditText> mapEditText = recupererDonnees(); //Met à jour la variable mapEditText avec les données de l'utilisateur

        //Vérification que tous les champs ont une valeur
        int i = 0; boolean Errors = false;
        while(i<mapEditText.size() && !Errors){
            Errors = mapEditText.valueAt(i).getText().toString().trim().isEmpty();
            i++;
        }

        //En cas d'erreur on affiche un message sur le champ problématique et on le séléctionne pour l'utilisateur
        if (Errors){
            mapEditText.valueAt(i-1).setError("Champ requis");
            mapEditText.valueAt(i-1).requestFocus();
            return; //Stoppe le traitement
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

}
