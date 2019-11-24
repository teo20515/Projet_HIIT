package com.example.projet_hiit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
    private MainActivity mainActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Affichage du gif animé
        Glide.with(getApplicationContext()).load(R.drawable.main_tapis_roulant).into((ImageView) findViewById(R.id.main_gif_container));

        //Preparation des données du RecyclerView
        recyclerView = findViewById(R.id.main_rvSeances);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GetSeancesEnregistrees recupererSeances = new GetSeancesEnregistrees();
        recupererSeances.execute();

    }

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

    public void creerNouvelEntrainement(View view) {
        Intent intent = new Intent(this,CreerEntrainementActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        //Jouer la séance selectionnée
    }

    @Override
    public void onLongItemClick(View view, int position) {
        //Permettre de supprimer la séance
    }

    private void populateListeSeances(final List<Seance> seances) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerViewListeSeancesAdapter adapter = new RecyclerViewListeSeancesAdapter(getApplicationContext(), seances);
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
            Toast.makeText(getApplicationContext(), seances.size()+" séances récupérées",Toast.LENGTH_LONG).show();
        }

    }
}
