package com.brozekm;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Headman {

    private Queue<Block> blocks;

    private int workerCount;

    private CurrentLorry currentLorry;

    public Headman(File inputFile, int workerCount, CurrentLorry currentLorry) throws IOException {
        blocks = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(inputFile.getName()));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.toLowerCase();
//            System.out.println(line);
            int blockSize = 0;
            for (int index = 0; index < line.length(); index++) {
                if (line.charAt(index) == 'x') {
                    blockSize++;
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
        }
        this.workerCount = workerCount;
        this.currentLorry = currentLorry;
//        System.out.println("Blocks count: " + blocks.size());
//        for (int i = 0; i < blocks.size(); i++) {
//            System.out.println("Number: " + i + ", size: " + blocks.get(i).getResources());
//        }
    }

    public synchronized Block getBlockForWorker() {
        Block block = blocks.poll();
        if (block == null) {
            workerCount--;
        }
        if (workerCount == 0) {
            Lorry tmpLorry = currentLorry.getLorry();
            if (tmpLorry.getLoadedResources()>0){
                Thread thread = new Thread(tmpLorry);
                thread.start();
            }
        }
        return blocks.poll();
    }

    public Queue<Block> getBlocks() {
        return blocks;
    }
}
