package com.example.projet_hiit.model;

public class Sequence {

    private Cycle cycle;
    private ReposLong reposLong;
    private int repetitions;

    public Sequence(Cycle cycle, ReposLong reposLong, int repetitions) {
        this.cycle = cycle;
        this.reposLong = reposLong;
        this.repetitions = repetitions;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public ReposLong getReposLong() {
        return reposLong;
    }

    public void setReposLong(ReposLong reposLong) {
        this.reposLong = reposLong;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
