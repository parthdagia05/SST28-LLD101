State Pattern — Pen (start, write, close, refill)
---------------------------------------------------

## Narrative

A digital Pen object behaves differently depending on its internal state.
- A new pen starts in an **Idle** state.
- Calling `start()` transitions it to the **Writing** state.
- In the Writing state, `write(text)` outputs ink and reduces the ink level.
- Calling `close()` moves the pen back to Idle (cap on).
- When ink runs out, the pen enters an **Empty** state and cannot write.
- Calling `refill()` restores ink and returns the pen to Idle.
- Invalid transitions (e.g., writing when closed) produce a warning.

This is a classic application of the **State** design pattern.

## Class Diagram (UML — text)

```
        +---------------------+
        |    <<interface>>    |
        |      PenState       |
        +---------------------+
        | + start(Pen)        |
        | + write(Pen, String)|
        | + close(Pen)        |
        | + refill(Pen)       |
        +---------------------+
              ^   ^   ^   ^
              |   |   |   |
   +----------+   |   |   +------------+
   |              |   |                |
+--------+  +----------+  +--------+  +---------+
| Idle   |  | Writing  |  | Empty  |  | Closed  |
| State  |  | State    |  | State  |  | State   |
+--------+  +----------+  +--------+  +---------+

        +---------------------+
        |        Pen          |
        +---------------------+
        | - state: PenState   |
        | - inkLevel: int     |
        | - brand: String     |
        +---------------------+
        | + start()           |
        | + write(String)     |
        | + close()           |
        | + refill()          |
        | + setState(PenState)|
        | + getInkLevel()     |
        | + setInkLevel(int)  |
        +---------------------+
```

## Design Decisions
- Each state is a **singleton** (stateless) — no per-pen allocation.
- `Pen` is the context; it delegates every action to its current `PenState`.
- Ink consumption makes the Empty state reachable naturally during writing.
- `refill()` only works in the Empty or Idle states.

## Build & Run

```bash
cd pen-state/src
javac com/example/pen/*.java
java com.example.pen.App
```
