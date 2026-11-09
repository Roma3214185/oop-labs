package ua.maze.model;

/** Напрямок ходу: на скільки змінюються рядок і колонка. */
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);

    private final int rowStep;
    private final int colStep;

    Direction(int rowStep, int colStep) {
        this.rowStep = rowStep;
        this.colStep = colStep;
    }

    public int getRowStep() {
        return rowStep;
    }

    public int getColStep() {
        return colStep;
    }
}
