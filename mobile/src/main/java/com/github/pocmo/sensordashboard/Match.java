package com.github.pocmo.sensordashboard;

public class Match {
    private String betting;
    private String id;

    public Match() {}
    public Match(String betting, String id) {
        this.betting = betting;
        this.id = id;
    }

    public void setId() {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBetting() {
        this.betting = betting;
    }

    public String getBetting() {
        return betting;
    }
}
