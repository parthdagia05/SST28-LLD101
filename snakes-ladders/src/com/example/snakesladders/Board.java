package com.example.snakesladders;

import java.util.*;

public class Board {

    private final int size;
    private final int maxPosition;
    private final List<Snake>  snakes;
    private final List<Ladder> ladders;
    private final Map<Integer, Integer> moveMap;

    public Board(int n, DifficultyLevel difficulty) {
        this.size        = n;
        this.maxPosition = n * n;
        this.snakes      = new ArrayList<>();
        this.ladders     = new ArrayList<>();
        this.moveMap     = new HashMap<>();
        generateSnakesAndLadders(n, difficulty);
    }

    private void generateSnakesAndLadders(int count, DifficultyLevel difficulty) {
        Random rand = new Random();
        Set<Integer> occupied = new HashSet<>();
        occupied.add(1);
        occupied.add(maxPosition);

        // Generate 'count' snakes
        for (int i = 0; i < count; i++) {
            int head, tail;
            int attempts = 0;
            do {
                head = rand.nextInt(maxPosition - 2) + 2;  // 2 .. maxPosition-1
                int gap;
                if (difficulty == DifficultyLevel.EASY) {
                    gap = rand.nextInt(Math.max(maxPosition / 4, 2)) + 1;
                } else {
                    gap = rand.nextInt(Math.max(maxPosition / 2, 2)) + maxPosition / 4 + 1;
                }
                tail = Math.max(1, head - gap);
                attempts++;
            } while ((occupied.contains(head) || occupied.contains(tail)
                       || head == tail || moveMap.containsKey(head) || moveMap.containsKey(tail)
                       || wouldCreateCycle(head, tail))
                     && attempts < 1000);

            if (attempts < 1000) {
                snakes.add(new Snake(head, tail));
                moveMap.put(head, tail);
                occupied.add(head);
                occupied.add(tail);
            }
        }

        // Generate 'count' ladders
        for (int i = 0; i < count; i++) {
            int start, end;
            int attempts = 0;
            do {
                start = rand.nextInt(maxPosition - 2) + 2;  // 2 .. maxPosition-1
                int gap;
                if (difficulty == DifficultyLevel.EASY) {
                    gap = rand.nextInt(Math.max(maxPosition / 2, 2)) + maxPosition / 4 + 1;
                } else {
                    gap = rand.nextInt(Math.max(maxPosition / 4, 2)) + 1;
                }
                end = Math.min(maxPosition - 1, start + gap);
                attempts++;
            } while ((occupied.contains(start) || occupied.contains(end)
                       || start == end || moveMap.containsKey(start) || moveMap.containsKey(end)
                       || wouldCreateCycle(start, end))
                     && attempts < 1000);

            if (attempts < 1000) {
                ladders.add(new Ladder(start, end));
                moveMap.put(start, end);
                occupied.add(start);
                occupied.add(end);
            }
        }
    }

    private boolean wouldCreateCycle(int from, int to) {
        Set<Integer> visited = new HashSet<>();
        int current = to;
        while (moveMap.containsKey(current)) {
            if (!visited.add(current)) return true;
            current = moveMap.get(current);
            if (current == from) return true;
        }
        return false;
    }

    public int getFinalPosition(int position) {
        return moveMap.getOrDefault(position, position);
    }

    public int getMaxPosition() {
        return maxPosition;
    }

    public int getSize() {
        return size;
    }

    public List<Snake>  getSnakes()  { return Collections.unmodifiableList(snakes); }
    public List<Ladder> getLadders() { return Collections.unmodifiableList(ladders); }

    public void printBoard() {
        System.out.println("\n--- Board (" + size + "x" + size + " = " + maxPosition + " cells) ---");
        System.out.println("Snakes  (" + snakes.size() + "):");
        for (Snake s : snakes) {
            System.out.println("  " + s);
        }
        System.out.println("Ladders (" + ladders.size() + "):");
        for (Ladder l : ladders) {
            System.out.println("  " + l);
        }
        System.out.println();
    }
}
