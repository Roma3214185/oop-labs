package ua.maze.model;

/** Клітинка поля: рядок і колонка. */
public record Position(int row, int col) {

    /** Сусідня клітинка в заданому напрямку. */
    public Position next(Direction direction) {
        return new Position(row + direction.getRowStep(),
                col + direction.getColStep());
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
