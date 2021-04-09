package com.brozekm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

public class Lorry implements Runnable {

    /**
     * Loaded resources
     * to determine how many resources can lorry still load
     */
    private int loadedResources = 0;

    /**
     * Timestamp
     * Used to determine how long it took to load lorry
     */
    private long waitingTime;

    private int id;

    private int capacity;

    private BufferedWriter bw;

    private int maxSpeed;

    private Ferry ferry;

    /**
     * Lorry constructor
     * @param id lorry's unique id
     * @param capacity set from program's arguments
     * @param maxSpeed set from program's arguments
     * @param ferry instance of ferry
     * @param bw instance of BufferWriter for output logs
     */
    public Lorry(int id, int capacity, int maxSpeed, Ferry ferry, BufferedWriter bw) {
        this.id = id;
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
        this.ferry = ferry;
        this.bw = bw;
        waitingTime = System.currentTimeMillis();
    }

    /**
     * Synchronized method for loading resources from miner to lorry
     * Loading one resource takes 1 second
     * @return true - loading successful, false - lorry is full
     */
    public synchronized boolean loadLorry() {
        if (loadedResources == capacity) {
            return false;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadedResources++;
        return true;
    }

    /**
     * Simulates driving time to ferry and from ferry to final destination
     * Method runs in new thread -> implemented method from Runnable
     */
    @Override
    public void run() {
        long drivingStart = System.currentTimeMillis();
        transport();
        long drivingTime = System.currentTimeMillis() - drivingStart;
        try {
            bw.write(System.currentTimeMillis() + "-Lorry-" + Thread.currentThread().getId() + "-arrived at ferry:" + drivingTime + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ferry.loadIntoFerry(loadedResources);

        drivingStart = System.currentTimeMillis();
        transport();
        drivingTime = System.currentTimeMillis() - drivingStart;
        try {
            bw.write(System.currentTimeMillis() + "-Lorry-" + Thread.currentThread().getId() + "-arrived at final destination:" + drivingTime + "\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Simulate driving time for lorry
     * Time may differ from 1 to maximum set from arguments
     */
    private void transport() {
        Random rand = new Random();
        int sleepTime = rand.nextInt(maxSpeed) + 1;
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Getters */

    public Ferry getFerry() {
        return ferry;
    }

    public int getId() {
        return id;
    }

    public long getWaitingTime() {
        return waitingTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getLoadedResources() {
        return loadedResources;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public BufferedWriter getBw() {
        return bw;
    }
}
