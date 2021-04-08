package com.brozekm;

public class Block {

    private int resources;

    private boolean assigned;

    public Block(int resources) {
        this.resources = resources;
        this.assigned = false;
    }

    public int getResources() {
        return resources;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
