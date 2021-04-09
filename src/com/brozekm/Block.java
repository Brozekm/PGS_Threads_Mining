package com.brozekm;

public class Block {

    /**
     * Number of resources in one block
     */
    private int resources;

    /**
     * Block with resources
     * @param resources resoures - 'X' in file
     */
    public Block(int resources) {
        this.resources = resources;
    }

    public int getResources() {
        return resources;
    }

}
