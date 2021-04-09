package com.brozekm;

import java.io.IOException;

public class CurrentLorry {

    private Lorry lorry;

    /**
     * Class has lorry that is currently being loaded
     * Current lorry is wrapper for lorry so all instances such are workers
     * look at same instance
     * @param lorry
     */
    public CurrentLorry(Lorry lorry) {
        this.lorry = lorry;
    }

    /**
     * When lorry is full worker call this method that send lorry away
     * and replaces current lorry with new instance
     */
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
        }
    }

    public Lorry getLorry() {
        return lorry;
    }

    public void setLorry(Lorry lorry) {
        this.lorry = lorry;
    }
}
