package com.example.pen;

public class EmptyState implements PenState {

    private static final EmptyState INSTANCE = new EmptyState();

    private EmptyState() {}

    public static EmptyState getInstance() { return INSTANCE; }

    @Override
    public void start(Pen pen) {
        System.out.println("[Pen] Cannot start — ink is empty. Please refill.");
    }

    @Override
    public void write(Pen pen, String text) {
        System.out.println("[Pen] Cannot write — ink is empty. Please refill.");
    }

    @Override
    public void close(Pen pen) {
        System.out.println("[Pen] Closing empty pen. Cap on.");
        pen.setState(ClosedState.getInstance());
    }

    @Override
    public void refill(Pen pen) {
        System.out.println("[Pen] Refilling ink to full.");
        pen.setInkLevel(Pen.MAX_INK);
        pen.setState(IdleState.getInstance());
        System.out.println("[Pen] Pen is ready again (idle).");
    }

    @Override
    public String toString() { return "EMPTY"; }
}
