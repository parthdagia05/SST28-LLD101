package com.example.pen;

public class IdleState implements PenState {

    private static final IdleState INSTANCE = new IdleState();

    private IdleState() {}

    public static IdleState getInstance() { return INSTANCE; }

    @Override
    public void start(Pen pen) {
        System.out.println("[Pen] Cap off — ready to write.");
        pen.setState(WritingState.getInstance());
    }

    @Override
    public void write(Pen pen, String text) {
        System.out.println("[Pen] Cannot write — pen is capped. Call start() first.");
    }

    @Override
    public void close(Pen pen) {
        System.out.println("[Pen] Pen is already capped (idle).");
    }

    @Override
    public void refill(Pen pen) {
        System.out.println("[Pen] Refilling ink to full.");
        pen.setInkLevel(Pen.MAX_INK);
    }

    @Override
    public String toString() { return "IDLE"; }
}
