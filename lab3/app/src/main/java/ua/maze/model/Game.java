package ua.maze.model;

/** Одна партія: лабіринт, положення гравця і кількість кроків. */
public class Game {

    private final Maze maze;
    private Position player;
    private int moves;

    public Game(Maze maze) {
        this.maze = maze;
        this.player = maze.getStart();
    }

    /** Робить хід, якщо він можливий. Тут уся валідація кроку. */
    public MoveResult move(Direction direction) {
        if (isFinished()) {
            return MoveResult.FINISHED;
        }
        Position next = player.next(direction);
        if (!maze.isFree(next)) {
            return MoveResult.BLOCKED;
        }
        player = next;
        moves++;
        return isFinished() ? MoveResult.ESCAPED : MoveResult.MOVED;
    }

    public boolean isFinished() {
        return player.equals(maze.getExit());
    }

    public Maze getMaze() {
        return maze;
    }

    public Position getPlayer() {
        return player;
    }

    public int getMoves() {
        return moves;
    }
}
