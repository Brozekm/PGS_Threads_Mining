package com.brozekm;

import java.util.Random;

public class Lorry implements Runnable{

    private int id;

    private int capacity;

    private int loadedResources = 0;

    private int maxSpeed;



    public Lorry(int id, int capacity, int maxSpeed) {
        this.id = id;
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
    }

    public synchronized boolean loadLorry(){
//        while (loading){
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        if (loadedResources == capacity){
            return false;
        }
//        loading = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadedResources++;
//        loading = false;
//        notify();
        return true;
    }

    @Override
    public void run() {
        transport();
        //TODO load ferry
        transport();
        System.out.println("Lorry "+this.id+" reached its final destination");
    }

    private void transport(){
        Random rand = new Random();
        int sleepTime = rand.nextInt(maxSpeed)+1;
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
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
}
