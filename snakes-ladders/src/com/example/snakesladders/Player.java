package com.example.snakesladders;

public class Player {

    private final String name;
    private int position;
    private boolean won;
    private int finishOrder;

    public Player(String name) {
        this.name     = name;
        this.position = 0;
        this.won      = false;
    }

    public String  getName()        { return name; }
    public int     getPosition()    { return position; }
    public boolean hasWon()         { return won; }
    public int     getFinishOrder() { return finishOrder; }

    public void setPosition(int position)      { this.position = position; }
    public void setWon(int order)              { this.won = true; this.finishOrder = order; }

    @Override
    public String toString() {
        return name + " (pos=" + position + ")";
    }
}
