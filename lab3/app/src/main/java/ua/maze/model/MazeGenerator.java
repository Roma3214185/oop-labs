package ua.maze.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Генерує випадковий лабіринт пошуком у глибину (recursive backtracker).
 * <p>
 * Клітинки лежать на непарних рядках і колонках, між ними — стіни.
 * Алгоритм іде до випадкового невідвіданого сусіда, «пробиваючи» стіну
 * між ними, а коли сусідів немає — повертається назад. Кожна клітинка
 * відвідується рівно раз, тому з будь-якої клітинки є шлях до виходу.
 */
public class MazeGenerator {

    private final Random random;

    public MazeGenerator(Random random) {
        this.random = random;
    }

    /** Лабіринт з size × size клітинок, тобто сітка (2·size+1) × (2·size+1). */
    public Maze generate(int size) {
        if (size < 2) {
            throw new IllegalArgumentException("size must be at least 2");
        }
        int length = 2 * size + 1;
        boolean[][] walls = new boolean[length][length];
        for (boolean[] row : walls) {
            Arrays.fill(row, true);
        }

        Position start = new Position(1, 1);
        walls[1][1] = false;
        Deque<Position> path = new ArrayDeque<>();
        path.push(start);

        while (!path.isEmpty()) {
            Position current = path.peek();
            List<Direction> closed = closedNeighbours(walls, current);
            if (closed.isEmpty()) {
                path.pop();
                continue;
            }
            Direction direction = closed.get(random.nextInt(closed.size()));
            Position between = current.next(direction);
            Position neighbour = between.next(direction);
            walls[between.row()][between.col()] = false;
            walls[neighbour.row()][neighbour.col()] = false;
            path.push(neighbour);
        }

        // Вихід — отвір у правій стіні біля нижнього кута.
        Position exit = new Position(length - 2, length - 1);
        walls[exit.row()][exit.col()] = false;
        return new Maze(walls, start, exit);
    }

    /** Напрямки до сусідніх клітинок (через одну), де ще суцільна стіна. */
    private static List<Direction> closedNeighbours(boolean[][] walls,
            Position cell) {
        List<Direction> result = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            Position neighbour = cell.next(direction).next(direction);
            boolean inside = neighbour.row() > 0
                    && neighbour.row() < walls.length - 1
                    && neighbour.col() > 0
                    && neighbour.col() < walls.length - 1;
            if (inside && walls[neighbour.row()][neighbour.col()]) {
                result.add(direction);
            }
        }
        return result;
    }
}
