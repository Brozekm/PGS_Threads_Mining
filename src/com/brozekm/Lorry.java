package com.brozekm;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

public class Lorry implements Runnable {

    private int id;

    private int capacity;

    private int loadedResources = 0;

    private int maxSpeed;

    private Ferry ferry;

    private long waitingTime;

    private BufferedWriter bw;


    public Lorry(int id, int capacity, int maxSpeed, Ferry ferry, BufferedWriter bw) {
        this.id = id;
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
        this.ferry = ferry;
        this.bw = bw;
        waitingTime = System.currentTimeMillis();
    }

    public synchronized boolean loadLorry() {
        if (loadedResources == capacity) {
            return false;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadedResources++;
        return true;
    }

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

    private void transport() {
        Random rand = new Random();
        int sleepTime = rand.nextInt(maxSpeed) + 1;
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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
