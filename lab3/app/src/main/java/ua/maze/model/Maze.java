package ua.maze.model;

/** Поле лабіринту: стіни, старт і вихід. */
public class Maze {

    private final boolean[][] walls;
    private final Position start;
    private final Position exit;

    /** walls[row][col] == true означає стіну. */
    public Maze(boolean[][] walls, Position start, Position exit) {
        this.walls = walls;
        this.start = start;
        this.exit = exit;
    }

    public int getRows() {
        return walls.length;
    }

    public int getCols() {
        return walls[0].length;
    }

    public Position getStart() {
        return start;
    }

    public Position getExit() {
        return exit;
    }

    public boolean isInside(Position position) {
        return position.row() >= 0 && position.row() < getRows()
                && position.col() >= 0 && position.col() < getCols();
    }

    public boolean isWall(int row, int col) {
        return walls[row][col];
    }

    public boolean isWall(Position position) {
        return isWall(position.row(), position.col());
    }

    /** Чи можна стати на клітинку: вона в межах поля і не стіна. */
    public boolean isFree(Position position) {
        return isInside(position) && !isWall(position);
    }
}
