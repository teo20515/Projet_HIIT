package com.example.projet_hiit.model;

public class Seance {

    private Sequence sequence;
    private Preparation preparation;

    public Seance(Sequence sequence, Preparation preparation) {
        this.sequence = sequence;
        this.preparation = preparation;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public Preparation getPreparation() {
        return preparation;
    }

    public void setPreparation(Preparation preparation) {
        this.preparation = preparation;
    }
}
