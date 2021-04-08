package com.brozekm;

import java.io.BufferedWriter;
import java.util.Random;

public class Worker implements Runnable {

    private int id;

    private int maxMiningTime;

    private int obtainedResources;

    private Block block;

    private CurrentLorry currentLorry;

    private Headman headman;

    private BufferedWriter bw;

    public Worker(int id, int maxMiningTime, CurrentLorry currLorry, Headman headman, BufferedWriter bw) {
        this.id = id;
        this.maxMiningTime = maxMiningTime;
        this.currentLorry = currLorry;
        this.headman = headman;
        this.bw = bw;
    }

    @Override
    public void run() {
        //TODO ask headman for work
        block = headman.getBlockForWorker();

        while (block != null) {
            Random rand = new Random();
            int sleep;
            for (int i = 0; i < block.getResources(); i++) {
                sleep = rand.nextInt(maxMiningTime) + 1;
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    System.out.println("Worker had heart attack while mining");
                }
                obtainedResources++;
            }

            for (int i = 0; i < obtainedResources; i++) {
                Lorry tmpLorry = currentLorry.getLorry();
                if (!tmpLorry.loadLorry()) {
                    i--;
                    continue;
                }
                if (tmpLorry.getCapacity() == tmpLorry.getLoadedResources()) {
                    Thread thread = new Thread(tmpLorry);
                    thread.start();
                    currentLorry.setLorry(new Lorry(tmpLorry.getId() + 1,
                            tmpLorry.getCapacity(),
                            tmpLorry.getMaxSpeed()));
                }
            }
            block = headman.getBlockForWorker();
        }
        //TODO Print logs
        System.out.println("Worker "+this.id+" chickened out of work and during work he obtained "+this.obtainedResources);

    }


}
