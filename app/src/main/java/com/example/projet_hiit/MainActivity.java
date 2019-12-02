package com.example.projet_hiit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projet_hiit.adapters.RecyclerViewListeSeancesAdapter;
import com.example.projet_hiit.db.DatabaseClient;
import com.example.projet_hiit.db.model.Seance;

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
        registerForContextMenu(recyclerView);
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
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getPosition();

        //Switch si on souhaite implémenter d'autres options par la suite
        switch (item.getItemId()){
            case R.id.main_menu_supprimer:
                new SupprimerSeance().execute(adapter.getItem(position));
                adapter.removeItem(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
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

    @Override
    public void onLongItemClick(View view, int position) {

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
            //Log.i("Seances recuperees",(seances.size())+" séances récupérées");
        }

    }

    class SupprimerSeance extends AsyncTask<Seance, Void, Void> {

        @Override
        protected Void doInBackground(Seance... seances) {

            DatabaseClient db = DatabaseClient.getInstance(getApplicationContext());
            Seance seance = seances[0];

            db.getAppDatabase().seanceDao().delete(seance);
            return null;
        }
    }
}
