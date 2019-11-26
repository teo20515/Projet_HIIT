package com.example.projet_hiit.db.compteur;

import android.os.CountDownTimer;

/**
 * Created by fbm on 24/10/2017.
 */
public class Compteur extends UpdateSource {

    // DATA
    private long updatedTime;
    private CountDownTimer timer;   // https://developer.android.com/reference/android/os/CountDownTimer.html
    private long totalTime;
    private boolean paused = true;


    public Compteur(long tempsTotal) {
        totalTime = tempsTotal;
        updatedTime = totalTime;
    }

    // Lancer le compteur
    public void start() {

        if (timer == null) {    //Evite de créer plusieurs timers

            // Créer le CountDownTimer
            timer = new CountDownTimer(updatedTime, 10) {

                // Callback fired on regular interval
                public void onTick(long millisUntilFinished) {
                    updatedTime = millisUntilFinished;

                    // Mise à jour
                    update();
                }

                // Callback fired when the time is up
                public void onFinish() {
                    updatedTime = 0;

                    // Mise à jour
                    update();
                }

            }.start();   // Start the countdown
            setPaused(false);
        }

    }

    // Mettre en pause le compteur
    public void pause() {

        if (timer != null) {

            // Arreter le timer
            stop();

            // Mise à jour
            update();
        }
    }


    // Remettre à le compteur à la valeur initiale
    public void reset() {

        if (timer != null) {

            // Arreter le timer
            stop();
        }

        // Réinitialiser
        updatedTime = getTempsTotal();

        // Mise à jour
        update();

    }

    // Arrete l'objet CountDownTimer et l'efface
    private void stop() {
        timer.cancel();
        timer = null;
        setPaused(true);
    }

    public int getMinutes() {
        return getSecondes()/60;
    }

    public int getSecondes() {
        int secs = (int) (updatedTime / 1000);
        return secs % 60;
    }

    public int getMillisecondes() {
        return (int) (updatedTime % 1000);
    }

    public long getTempsTotal(){
        return this.totalTime;
    }

    public void setTempsTotal(long tempsTotal){
        this.totalTime = tempsTotal;
    }

    public boolean isPaused(){
        return this.paused;
    }

    public void setPaused(boolean status){
        this.paused = status;
    }

    public long getMilisecondesRestantes() {
        return updatedTime;
    }
}
