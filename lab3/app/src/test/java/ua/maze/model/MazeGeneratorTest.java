package ua.maze.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MazeGeneratorTest {

    private static Maze generate(long seed, int size) {
        return new MazeGenerator(new Random(seed)).generate(size);
    }

    /** Усі клітинки, до яких можна дійти від старту (пошук у ширину). */
    private static Set<Position> reachable(Maze maze) {
        Set<Position> visited = new HashSet<>();
        Deque<Position> queue = new ArrayDeque<>();
        queue.add(maze.getStart());
        visited.add(maze.getStart());
        while (!queue.isEmpty()) {
            Position current = queue.poll();
            for (Direction direction : Direction.values()) {
                Position next = current.next(direction);
                if (maze.isFree(next) && visited.add(next)) {
                    queue.add(next);
                }
            }
        }
        return visited;
    }

    private static Set<Position> freeCells(Maze maze) {
        Set<Position> free = new HashSet<>();
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                Position position = new Position(row, col);
                if (maze.isFree(position)) {
                    free.add(position);
                }
            }
        }
        return free;
    }

    /** Текстовий вигляд лабіринту, щоб порівнювати два лабіринти. */
    private static String draw(Maze maze) {
        StringBuilder text = new StringBuilder();
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                text.append(maze.isWall(new Position(row, col)) ? '#' : '.');
            }
            text.append('\n');
        }
        return text.toString();
    }

    @Test
    void gridHasWallsBetweenCells() {
        Maze maze = generate(1, 10);

        assertEquals(21, maze.getRows());
        assertEquals(21, maze.getCols());
    }

    @Test
    void startIsTopLeftAndExitIsInRightBorder() {
        Maze maze = generate(1, 10);

        assertEquals(new Position(1, 1), maze.getStart());
        assertEquals(new Position(19, 20), maze.getExit());
        assertTrue(maze.isFree(maze.getStart()));
        assertTrue(maze.isFree(maze.getExit()));
    }

    @Test
    void borderIsWallExceptExit() {
        Maze maze = generate(2, 6);
        int last = maze.getRows() - 1;

        for (int i = 0; i <= last; i++) {
            assertTrue(maze.isWall(new Position(0, i)));
            assertTrue(maze.isWall(new Position(last, i)));
            assertTrue(maze.isWall(new Position(i, 0)));
            Position right = new Position(i, last);
            assertEquals(!right.equals(maze.getExit()), maze.isWall(right));
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 42, 2024})
    void exitAndEveryFreeCellAreReachable(long seed) {
        Maze maze = generate(seed, 10);
        Set<Position> reachable = reachable(maze);

        assertTrue(reachable.contains(maze.getExit()));
        assertEquals(freeCells(maze), reachable);
    }

    @Test
    void everyCellIsOpened() {
        Maze maze = generate(5, 8);

        for (int row = 1; row < maze.getRows(); row += 2) {
            for (int col = 1; col < maze.getCols(); col += 2) {
                assertFalse(maze.isWall(new Position(row, col)));
            }
        }
    }

    @Test
    void sameSeedGivesSameMaze() {
        assertEquals(draw(generate(7, 10)), draw(generate(7, 10)));
    }

    @Test
    void differentSeedsGiveDifferentMazes() {
        assertNotEquals(draw(generate(7, 10)), draw(generate(8, 10)));
    }

    @Test
    void tooSmallSizeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> generate(1, 1));
    }
}
