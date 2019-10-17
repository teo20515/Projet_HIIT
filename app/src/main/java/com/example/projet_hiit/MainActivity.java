package com.example.projet_hiit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Glide.with(getApplicationContext()).load(R.drawable.tapis_roulant).into((ImageView) findViewById(R.id.main_gif_container));
    }

    public void creerNouvelEntrainement(View view) {
        Intent intent = new Intent(this,CreerEntrainementActivity.class);
        startActivity(intent);
    }
}
