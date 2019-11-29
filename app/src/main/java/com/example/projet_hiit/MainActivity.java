package com.example.projet_hiit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.projet_hiit.adapters.RecyclerViewListeSeancesAdapter;
import com.example.projet_hiit.db.DatabaseClient;
import com.example.projet_hiit.db.model.Seance;

import java.io.Console;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewListeSeancesAdapter.ItemClickListener, RecyclerViewListeSeancesAdapter.ItemLongClickListener {

    private RecyclerView recyclerView;
    private RecyclerViewListeSeancesAdapter adapter;
    private MainActivity mainActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Preparation des données du RecyclerView
        recyclerView = findViewById(R.id.main_rvSeances);
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GetSeancesEnregistrees recupererSeances = new GetSeancesEnregistrees();
        recupererSeances.execute();

        //Affichage du gif animé
        Glide.with(getApplicationContext()).load(R.drawable.main_tapis_roulant).into((ImageView) findViewById(R.id.main_gif_container));
    }

    ///////////// CYCLE DE VIE ////////////////
    @Override
    protected void onResume() {
        super.onResume();
        new GetSeancesEnregistrees().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        new GetSeancesEnregistrees().execute();
    }

    ///////////// LANCEMENT ACTIVITES ////////////////
    public void creerNouvelEntrainement(View view) {
        Intent intent = new Intent(this,CreerEntrainementActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        //Jouer la séance selectionnée

        Intent intent = new Intent(getBaseContext(), JouerEntrainementActivity.class);
        intent.putExtra("SEANCE", adapter.getItem(position));
        startActivity(intent);
    }

    ///////////// ACTIONS DANS L'ACTIVITE ////////////////
    @Override
    public void onLongItemClick(View view, int position) {
        //Permettre de supprimer la séance


    }

    ///////////// MISE A JOUR DES DONNEES DE LA LISTE DE SEANCE ////////////////
    private void populateListeSeances(final List<Seance> seances) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new RecyclerViewListeSeancesAdapter(getApplicationContext(), seances);
                adapter.setClickListener(mainActivity);
                adapter.setLongClickListener(mainActivity);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    class GetSeancesEnregistrees extends AsyncTask<Void, Void, List<Seance>> {

        @Override
        protected List<Seance> doInBackground(Void... voids) {
            DatabaseClient db = DatabaseClient.getInstance(getApplicationContext());

            List<Seance> seances = db.getAppDatabase().seanceDao().getAll();
            populateListeSeances(seances);

            return seances;
        }

        @Override
        protected void onPostExecute(List<Seance> seances){
            Log.i("Seances recuperees",(seances.size())+" séances récupérées");
        }

    }
}
