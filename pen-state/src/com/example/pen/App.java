package com.example.pen;

public class App {

    public static void main(String[] args) {

        System.out.println("=== Pen State-Pattern Demo ===\n");

        Pen pen = new Pen("Parker");
        pen.status();
        System.out.println();

        // 1. Normal flow: start -> write -> close
        System.out.println("--- 1. Normal: start, write, close ---");
        pen.start();
        pen.write("Hello");
        pen.write(" World");
        pen.close();
        pen.status();
        System.out.println();

        // 2. Invalid transitions
        System.out.println("--- 2. Invalid: write while closed ---");
        pen.write("This should fail");
        System.out.println();

        // 3. Drain the ink
        System.out.println("--- 3. Drain ink until empty ---");
        pen.start();
        pen.write("This is a long sentence that will drain ink fast!!");
        pen.status();
        System.out.println();

        // 4. Try to write when empty
        System.out.println("--- 4. Write when empty ---");
        pen.write("more text");
        System.out.println();

        // 5. Refill and resume
        System.out.println("--- 5. Refill and resume writing ---");
        pen.refill();
        pen.status();
        pen.start();
        pen.write("Back in action!");
        pen.close();
        pen.status();
        System.out.println();

        // 6. Refill from idle (top-up)
        System.out.println("--- 6. Refill from idle (top-up) ---");
        pen.refill();
        pen.status();
    }
}
