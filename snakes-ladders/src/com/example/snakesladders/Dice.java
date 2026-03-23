package com.example.snakesladders;

import java.util.Random;

public class Dice {

    private final int faces;
    private final Random random;

    public Dice(int faces) {
        this.faces  = faces;
        this.random = new Random();
    }

    public Dice() {
        this(6);
    }

    public int roll() {
        return random.nextInt(faces) + 1;
    }
}
