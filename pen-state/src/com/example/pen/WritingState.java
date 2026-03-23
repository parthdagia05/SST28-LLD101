package com.example.pen;

public class WritingState implements PenState {

    private static final WritingState INSTANCE = new WritingState();

    private WritingState() {}

    public static WritingState getInstance() { return INSTANCE; }

    @Override
    public void start(Pen pen) {
        System.out.println("[Pen] Already writing — pen is uncapped.");
    }

    @Override
    public void write(Pen pen, String text) {
        int cost = text.length();
        if (pen.getInkLevel() <= 0) {
            System.out.println("[Pen] Ink empty! Cannot write. Please refill.");
            pen.setState(EmptyState.getInstance());
            return;
        }
        if (pen.getInkLevel() < cost) {
            String partial = text.substring(0, pen.getInkLevel());
            System.out.println("[Pen] Writing: \"" + partial + "\" (ink ran out mid-word!)");
            pen.setInkLevel(0);
            pen.setState(EmptyState.getInstance());
            System.out.println("[Pen] Ink exhausted — please refill.");
        } else {
            pen.setInkLevel(pen.getInkLevel() - cost);
            System.out.println("[Pen] Writing: \"" + text + "\"  [ink left: " + pen.getInkLevel() + "]");
        }
    }

    @Override
    public void close(Pen pen) {
        System.out.println("[Pen] Cap on — pen closed.");
        pen.setState(IdleState.getInstance());
    }

    @Override
    public void refill(Pen pen) {
        System.out.println("[Pen] Cannot refill while writing. Close the pen first.");
    }

    @Override
    public String toString() { return "WRITING"; }
}
