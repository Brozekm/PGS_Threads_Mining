package com.brozekm;

import java.io.BufferedWriter;
import java.io.IOException;

public class Ferry {

    private int capacity;

    /**
     * How many lorries are already loaded
     */
    private int lorriesLoaded;

    /**
     * How many resources is transported during the whole simulation
     */
    private int overallTransported;

    /**
     * Timestamp is used to determine how long ferry waited to set sail
     */
    private long startWaiting;

    /**
     * How many resources is expected to be transported during simulation
     */
    private int allResources = 0;

    /**
     * Boolean that shows if ferry is loaded
     * Used in case that some thread wakes up unexpectedly
     */
    private boolean loaded = false;

    /**
     * BufferedWriter that is used for writing log in output file
     */
    private BufferedWriter bw;

    /**
     * Ferry constructor
     * @param capacity Ferry capacity [lorries]
     * @param bw instance of BufferWriter for output logs
     */
    public Ferry(int capacity, BufferedWriter bw) {
        this.capacity = capacity;
        this.bw = bw;
        lorriesLoaded = 0;
        overallTransported = 0;
        startWaiting = System.currentTimeMillis();
    }

    /**
     * Synchronized method that is loading lorries onto ferry
     * @param load number of resources
     */
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
