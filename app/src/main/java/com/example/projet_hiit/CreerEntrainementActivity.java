package com.example.projet_hiit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    //Donnees
    SparseArray<EditText> mapEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_entrainement);

        //Instantiation de la base de données
        db = DatabaseClient.getInstance(getApplicationContext());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        SparseArray<EditText> data = recupererDonnees();
        for (int i=0; i<=6; i++){
            outState.putString(String.valueOf(i),data.get(i).getText().toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        SparseArray<EditText> data = recupererDonnees();
        for (int i=0; i<=6; i++){
            data.get(i).setText(savedInstanceState.getString(String.valueOf(i)));
        }
    }



    public Seance save(View view) {
        mapEditText = recupererDonnees();
        if(!verifierDonneesValides()){
            return null;
        }
        Seance seance = creerNouvelleSeance(view);

        SaveSeance save = new SaveSeance(seance);
        save.execute();
        return seance;
    }

    public void enregistrer(View view){
        Seance seance = save(view);
        if (seance == null){
            return;
        }
        finish();
    }

    public void enregistrerEtJouer(View view) {
        Seance seance = save(view);
        if (seance == null){
            return;
        }
        Intent intent = new Intent(this,JouerEntrainementActivity.class);
        intent.putExtra("SEANCE",seance);
        startActivity(intent);
    }

    public void jouerSansEnregistrer(View view) {
        mapEditText = recupererDonnees();
        if (!verifierDonneesValides()){
            return;
        }
        Seance seance = creerNouvelleSeance(view);
        Intent intent = new Intent(this,JouerEntrainementActivity.class);
        intent.putExtra("SEANCE",seance);
        startActivity(intent);
    }

    public boolean verifierDonneesValides(){
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                      VERIFICATION DES DONNEES                                      //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////
        int i = 0;
        try {
            while(i<mapEditText.size()){
                if(mapEditText.valueAt(i).getText().toString().trim().isEmpty()){
                    throw new NumberFormatException("Champ requis");
                }else if(mapEditText.valueAt(i).getInputType() == InputType.TYPE_CLASS_NUMBER && Integer.valueOf(mapEditText.valueAt(i).getText().toString()) <= 0){
                    throw new NumberFormatException("Cette valeur doit être positive");
                }

                i++;
            }
        }catch (NumberFormatException e){
            mapEditText.valueAt(i).setError(e.getMessage());
            mapEditText.valueAt(i).requestFocus();
            return false; //Stoppe le traitement
        }
        return true;
    }

    public Seance creerNouvelleSeance(View view) {

        //Création de la séance pour l'enregistrement//
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

        return new Seance(sequence,
                Integer.parseInt(
                        mapEditText.valueAt(PREPARATION).getText().toString().trim()
                )
        );
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

    class SaveSeance extends AsyncTask<Void, Void, Seance>{

        private Seance seance;

        private SaveSeance(Seance seance) {
            this.seance = seance;
        }

        @Override
        protected Seance doInBackground(Void... voids) {

            db.getAppDatabase().seanceDao().insert(seance);

            return seance;
        }

        @Override
        protected void onPostExecute(Seance seance){
            Toast.makeText(getApplicationContext(), "Séance sauvegardée",Toast.LENGTH_LONG).show();
        }

    }
}
