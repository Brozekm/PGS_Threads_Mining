package com.brozekm;

public class CurrentLorry {

    private Lorry lorry;

    public Lorry getLorry() {
        return lorry;
    }

    public void setLorry(Lorry lorry) {
        this.lorry = lorry;
    }

    public CurrentLorry(Lorry lorry) {
        this.lorry = lorry;
    }
}
