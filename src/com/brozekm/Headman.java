package com.brozekm;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Headman {

    /**
     * Queue of Blocks that needs to be mined
     */
    private Queue<Block> blocks;

    private int workerCount;

    private CurrentLorry currentLorry;

    private Ferry ferry;

    private BufferedWriter bw;

    /**
     * Headman constuctor
     * obtains block with resources based on input file
     * @param inputFile input file
     * @param workerCount number of workers set from parameters
     * @param currentLorry instance of current lorry that being loaded
     * @param ferry instance of ferry
     * @param bw instance of BufferWriter for output logs
     * @throws IOException Throws exception when obtaining resources from file is unsuccessful
     */
    public Headman(File inputFile, int workerCount, CurrentLorry currentLorry, Ferry ferry, BufferedWriter bw) throws IOException {
        blocks = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(inputFile.getName()));
        String line;
        int resourcesCount = 0;

        while ((line = br.readLine()) != null) {

            line = line.toLowerCase();
            int blockSize = 0;

            for (int index = 0; index < line.length(); index++) {
                if (line.charAt(index) == 'x') {
                    blockSize++;
                    resourcesCount++;
                } else if (line.charAt(index) == ' ') {
                    if (blockSize > 0) {
                        Block block = new Block(blockSize);
                        blocks.add(block);
                        blockSize = 0;
                    }
                } else {
                    throw new IOException();
                }
            }
            if (blockSize > 0) {
                Block block = new Block(blockSize);
                blocks.add(block);
            }
        }
        this.workerCount = workerCount;
        this.currentLorry = currentLorry;
        this.ferry = ferry;
        this.bw = bw;
        System.out.println("Blocks count: " + blocks.size());
        System.out.println("Resources count: " + resourcesCount);
        bw.write(System.currentTimeMillis()+"-Headmen-"+Thread.currentThread().getId()+"-blocks count:"+blocks.size()+"\n");
        bw.write(System.currentTimeMillis()+"-Headmen-"+Thread.currentThread().getId()+"-resources count:"+resourcesCount+"\n");
        bw.flush();
        this.ferry.setAllResources(resourcesCount);
    }

    /**
     * Synchronized method return block to worker when he asks
     * It also checks if there are some workers still working
     * if not -> send last lorry (with at least 1 resource)
     * @return block to worker for mining
     */
    public synchronized Block getBlockForWorker() {
        Block block = blocks.poll();
        if (block == null) {
            workerCount--;
        }
        if (workerCount == 0) {
            Lorry tmpLorry = currentLorry.getLorry();
            if (tmpLorry.getLoadedResources() > 0) {
                long lorryWaitingTime = System.currentTimeMillis() - tmpLorry.getWaitingTime();
                try {
                    bw.write(System.currentTimeMillis()+"-Lorry-"+Thread.currentThread().getId()+"-full after:"+ lorryWaitingTime+"\n");
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread thread = new Thread(tmpLorry);
                thread.start();
            }
        }
        return block;
    }

    public Queue<Block> getBlocks() {
        return blocks;
    }
}
