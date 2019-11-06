package com.example.projet_hiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projet_hiit.db.DatabaseClient;

public class MainActivity extends AppCompatActivity {

    private DatabaseClient db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Affichage du gif animé
        Glide.with(getApplicationContext()).load(R.drawable.main_tapis_roulant).into((ImageView) findViewById(R.id.main_gif_container));

        //Instantiation de la base de données
        db = DatabaseClient.getInstance(getApplicationContext());

    }

    public void creerNouvelEntrainement(View view) {
        Intent intent = new Intent(this,CreerEntrainementActivity.class);
        startActivity(intent);
    }
}
