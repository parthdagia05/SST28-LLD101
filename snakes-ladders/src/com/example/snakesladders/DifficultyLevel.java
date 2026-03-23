package com.example.snakesladders;

public enum DifficultyLevel {
    EASY,
    HARD;

    public static DifficultyLevel fromString(String s) {
        return s.trim().equalsIgnoreCase("hard") ? HARD : EASY;
    }
}
