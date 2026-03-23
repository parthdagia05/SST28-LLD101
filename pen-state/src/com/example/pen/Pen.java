package com.example.pen;

public class Pen {

    public static final int MAX_INK = 50;

    private final String brand;
    private PenState state;
    private int inkLevel;

    public Pen(String brand) {
        this.brand = brand;
        this.inkLevel = MAX_INK;
        this.state = IdleState.getInstance();
        System.out.println("[Pen] Created \"" + brand + "\" pen  [ink: " + inkLevel + ", state: " + state + "]");
    }

    public void start()            { state.start(this); }
    public void write(String text) { state.write(this, text); }
    public void close()            { state.close(this); }
    public void refill()           { state.refill(this); }

    public PenState getState()              { return state; }
    public void     setState(PenState s)    { this.state = s; }
    public int      getInkLevel()           { return inkLevel; }
    public void     setInkLevel(int level)  { this.inkLevel = level; }
    public String   getBrand()              { return brand; }

    public void status() {
        System.out.println("[Status] " + brand + " — ink: " + inkLevel + ", state: " + state);
    }
}
