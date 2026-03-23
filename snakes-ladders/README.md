Snakes & Ladders — LLD Implementation
--------------------------------------

## Narrative

A console-based Snakes & Ladders game that supports:
- Configurable board size (n x n)
- Multiple players (x players, turn-based)
- Difficulty levels (EASY / HARD)
- n snakes and n ladders placed randomly (no cycles)
- Six-sided dice with random rolls
- Game continues until only 1 player remains (all others have won)

### Difficulty Levels
- **EASY**: Snakes are shorter (head-tail gap is small), ladders are longer (climb more).
- **HARD**: Snakes are longer (drop further down), ladders are shorter (climb less).

## Class Diagram (UML — text)

```
+------------------+        +------------------+
| <<enum>>         |        |      Dice        |
| DifficultyLevel  |        +------------------+
+------------------+        | - faces: int     |
| EASY             |        +------------------+
| HARD             |        | + roll(): int    |
+------------------+        +------------------+

+------------------+        +------------------+
|     Snake        |        |     Ladder       |
+------------------+        +------------------+
| - head: int      |        | - start: int     |
| - tail: int      |        | - end: int       |
+------------------+        +------------------+
| + getHead(): int |        | + getStart(): int|
| + getTail(): int |        | + getEnd(): int  |
+------------------+        +------------------+

+-------------------------------+
|            Board              |
+-------------------------------+
| - size: int                   |
| - snakes: List<Snake>         |
| - ladders: List<Ladder>       |
| - moveMap: Map<Int, Int>      |
+-------------------------------+
| + Board(n, difficulty)        |
| + getFinalPosition(pos): int  |
| + getMaxPosition(): int       |
| + printBoard(): void          |
+-------------------------------+

+---------------------------+
|         Player            |
+---------------------------+
| - name: String            |
| - position: int           |
| - hasWon: boolean         |
+---------------------------+
| + getName(): String       |
| + getPosition(): int      |
| + setPosition(int): void  |
| + hasWon(): boolean       |
| + setWon(): void          |
+---------------------------+

+----------------------------------+
|             Game                  |
+----------------------------------+
| - board: Board                   |
| - players: List<Player>          |
| - dice: Dice                     |
| - activePlayers: int             |
+----------------------------------+
| + Game(board, players)           |
| + play(): void                   |
| - playTurn(Player): void         |
| - isGameOver(): boolean          |
+----------------------------------+

+----------------------------------+
|              App                 |
+----------------------------------+
| + main(String[]): void           |
+----------------------------------+
        (takes user input:
         n, x, difficulty_level)

Relationships:
  Game  ───>  Board   (has-a)
  Game  ───>  Player  (has-many)
  Game  ───>  Dice    (has-a)
  Board ───>  Snake   (has-many)
  Board ───>  Ladder  (has-many)
  Board ───>  DifficultyLevel (uses)
```

## Build & Run

```bash
cd snakes-ladders/src
javac com/example/snakesladders/*.java
java com.example.snakesladders.App
```

Then enter board size (n), number of players (x), and difficulty (easy/hard) when prompted.
