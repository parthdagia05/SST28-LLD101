package com.example.pen;

public interface PenState {
    void start(Pen pen);
    void write(Pen pen, String text);
    void close(Pen pen);
    void refill(Pen pen);
}
