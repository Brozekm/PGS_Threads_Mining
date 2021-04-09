package com.brozekm;

import java.io.BufferedWriter;
import java.io.IOException;

public class Ferry {

    private int capacity;

    private int lorriesLoaded;

    private int overallTransported;

    private long startWaiting;

    private int allResources = 0;

    private boolean loaded = false;

    private BufferedWriter bw;

    public Ferry(int capacity, BufferedWriter bw) {
        this.capacity = capacity;
        this.bw = bw;
        lorriesLoaded = 0;
        overallTransported = 0;
        startWaiting = System.currentTimeMillis();
    }

    public synchronized void loadIntoFerry(int load) {
        lorriesLoaded++;
        overallTransported += load;
        if (capacity == lorriesLoaded) {
            loaded = true;
            lorriesLoaded = 0;
            long waitingTime = System.currentTimeMillis() - startWaiting;
            startWaiting = System.currentTimeMillis();
            try {
                bw.write(System.currentTimeMillis()+"-Ferry-"+Thread.currentThread().getId()+"-sets sail:"+waitingTime+"\n");
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            loaded = false;
            notifyAll();
            System.out.println("Ferry is sailing away");

            if (overallTransported == allResources) {
                System.out.println("Number of resources that reached final destination: " + overallTransported);
            }
        } else {
            while (!loaded){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    public void setAllResources(int allResources) {
        this.allResources = allResources;
    }
}
