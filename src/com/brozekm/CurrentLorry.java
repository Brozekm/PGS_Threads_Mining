package com.brozekm;

import java.io.IOException;

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

    public synchronized void sendLorry(){
        if (lorry.getLoadedResources() == lorry.getCapacity()) {
            long lorryWaitingTime = System.currentTimeMillis() - lorry.getWaitingTime();
            try {
                lorry.getBw().write(System.currentTimeMillis()+"-Lorry-"+Thread.currentThread().getId()+"-full after:"+ lorryWaitingTime+"\n");
                lorry.getBw().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(lorry);
            thread.start();
            this.setLorry(new Lorry(lorry.getId() + 1,
                    lorry.getCapacity(),
                    lorry.getMaxSpeed(), lorry.getFerry(), lorry.getBw()));
//            System.out.println("Creating new lorry, id: " + lorry.getId());
        }
    }
}
