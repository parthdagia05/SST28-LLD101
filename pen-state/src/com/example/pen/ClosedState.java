package com.example.pen;

public class ClosedState implements PenState {

    private static final ClosedState INSTANCE = new ClosedState();

    private ClosedState() {}

    public static ClosedState getInstance() { return INSTANCE; }

    @Override
    public void start(Pen pen) {
        if (pen.getInkLevel() <= 0) {
            System.out.println("[Pen] Cannot start — ink is empty. Please refill.");
            pen.setState(EmptyState.getInstance());
        } else {
            System.out.println("[Pen] Cap off — ready to write.");
            pen.setState(WritingState.getInstance());
        }
    }

    @Override
    public void write(Pen pen, String text) {
        System.out.println("[Pen] Cannot write — pen is closed. Call start() first.");
    }

    @Override
    public void close(Pen pen) {
        System.out.println("[Pen] Pen is already closed.");
    }

    @Override
    public void refill(Pen pen) {
        System.out.println("[Pen] Refilling ink to full.");
        pen.setInkLevel(Pen.MAX_INK);
        pen.setState(IdleState.getInstance());
        System.out.println("[Pen] Pen is ready again (idle).");
    }

    @Override
    public String toString() { return "CLOSED"; }
}
