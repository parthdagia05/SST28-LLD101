package com.example.snakesladders;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Snakes & Ladders ===\n");

        System.out.print("Enter board size n (board will be n x n): ");
        int n = scanner.nextInt();

        System.out.print("Enter number of players: ");
        int x = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter difficulty level (easy/hard): ");
        String diffInput = scanner.nextLine();
        DifficultyLevel difficulty = DifficultyLevel.fromString(diffInput);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= x; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }

        Board board = new Board(n, difficulty);
        Game  game  = new Game(board, players);

        game.play();

        scanner.close();
    }
}
