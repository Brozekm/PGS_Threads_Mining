package com.brozekm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

public class Worker implements Runnable {

    private int id;

    private int maxMiningTime;

    private int obtainedResources = 0;

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
        block = headman.getBlockForWorker();

        while (block != null) {
            Random rand = new Random();
            int sleep;
            long blockTimeStart = System.currentTimeMillis();
            for (int i = 0; i < block.getResources(); i++) {
                sleep = rand.nextInt(maxMiningTime) + 1;
                long resourceTimeStart = System.currentTimeMillis();
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    System.out.println("Worker had heart attack while mining");
                }
                long resourceMiningTime = System.currentTimeMillis() - resourceTimeStart;
                try {
                    bw.write(System.currentTimeMillis() + "-Worker-" + Thread.currentThread().getId() + "-one resource mining time:" + resourceMiningTime + "\n");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                obtainedResources++;
            }
            long blockMiningTime = System.currentTimeMillis() - blockTimeStart;
            try {
                bw.write(System.currentTimeMillis() + "-Worker-" + Thread.currentThread().getId() + "-one block mining time:" + blockMiningTime + "\n");
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < block.getResources(); i++) {
                Lorry tmpLorry = currentLorry.getLorry();
                if (!tmpLorry.loadLorry()) {
                    i--;
                    currentLorry.sendLorry();
                    continue;
                }

            }
            block = headman.getBlockForWorker();
        }

        System.out.println("Worker " + this.id + ", mined " + this.obtainedResources + " resources");

    }


}
