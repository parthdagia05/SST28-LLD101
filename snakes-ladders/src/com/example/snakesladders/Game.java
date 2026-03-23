package com.example.snakesladders;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final Board board;
    private final List<Player> players;
    private final Dice dice;
    private int finishCounter;

    public Game(Board board, List<Player> players) {
        this.board   = board;
        this.players = players;
        this.dice    = new Dice();
        this.finishCounter = 0;
    }

    public void play() {
        board.printBoard();
        System.out.println("=== Game Start ===\n");

        while (!isGameOver()) {
            for (Player player : players) {
                if (player.hasWon()) continue;
                playTurn(player);
                if (isGameOver()) break;
            }
        }

        printResults();
    }

    private void playTurn(Player player) {
        int roll = dice.roll();
        int oldPos = player.getPosition();
        int newPos = oldPos + roll;

        System.out.print(player.getName() + " rolled " + roll
                         + "  [" + oldPos + " -> ");

        if (newPos > board.getMaxPosition()) {
            System.out.println(oldPos + "] (exceeds " + board.getMaxPosition()
                               + ", stays put)");
            return;
        }

        int finalPos = board.getFinalPosition(newPos);

        if (finalPos < newPos) {
            System.out.print(newPos + "] Bitten by snake! Down to " + finalPos);
        } else if (finalPos > newPos) {
            System.out.print(newPos + "] Climbed a ladder! Up to " + finalPos);
        } else {
            System.out.print(newPos + "]");
        }

        player.setPosition(finalPos);

        if (finalPos == board.getMaxPosition()) {
            finishCounter++;
            player.setWon(finishCounter);
            System.out.print("  *** " + player.getName()
                             + " wins! (finished #" + finishCounter + ") ***");
        }

        System.out.println();
    }

    private boolean isGameOver() {
        int remaining = 0;
        for (Player p : players) {
            if (!p.hasWon()) remaining++;
        }
        return remaining <= 1;
    }

    private void printResults() {
        System.out.println("\n=== Game Over ===");
        System.out.println("Final standings:");

        List<Player> winners = new ArrayList<>();
        Player lastPlayer = null;

        for (Player p : players) {
            if (p.hasWon()) {
                winners.add(p);
            } else {
                lastPlayer = p;
            }
        }

        winners.sort((a, b) -> a.getFinishOrder() - b.getFinishOrder());

        for (Player w : winners) {
            System.out.println("  #" + w.getFinishOrder() + " " + w.getName());
        }
        if (lastPlayer != null) {
            System.out.println("  #" + (finishCounter + 1) + " " + lastPlayer.getName()
                               + " (last remaining at position " + lastPlayer.getPosition() + ")");
        }
    }
}
