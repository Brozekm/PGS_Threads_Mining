package com.brozekm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

public class Worker implements Runnable {

    /**
     * Number of obtained resources by one worker during whole simulation
     */
    private int obtainedResources = 0;

    /**
     * Block with resources to mine
     */
    private Block block;

    private int id;

    private int maxMiningTime;

    private CurrentLorry currentLorry;

    private Headman headman;

    private BufferedWriter bw;

    /**
     * @param id workers unique id
     * @param maxMiningTime Maximum mining time, resource can be obtained from 1 - maxMiningTime
     * @param currLorry instance of current lorry to which worker will load
     * @param headman instance of headman - worker asks headman for block to mine
     * @param bw instance of BufferWriter for output logs
     */
    public Worker(int id, int maxMiningTime, CurrentLorry currLorry, Headman headman, BufferedWriter bw) {
        this.id = id;
        this.maxMiningTime = maxMiningTime;
        this.currentLorry = currLorry;
        this.headman = headman;
        this.bw = bw;
    }

    /**
     * In method worker repeatedly asks Headmen for block to mine resources from until there are no more blocks
     * Time of mining block depend on number of resources that block carries
     * After block is collected worker tries to load it onto lorry one resource at the time
     * Multiple workers can take turns during loading process
     */
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
