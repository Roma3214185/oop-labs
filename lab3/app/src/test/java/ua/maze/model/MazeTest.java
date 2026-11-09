package ua.maze.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MazeTest {

    private final Maze maze = TestMazes.parse(
            "####",
            "#S.E",
            "####");

    @Test
    void knowsSizeStartAndExit() {
        assertEquals(3, maze.getRows());
        assertEquals(4, maze.getCols());
        assertEquals(new Position(1, 1), maze.getStart());
        assertEquals(new Position(1, 3), maze.getExit());
    }

    @Test
    void freeCellIsInsideAndNotWall() {
        assertTrue(maze.isFree(new Position(1, 2)));
        assertFalse(maze.isFree(new Position(0, 0)));
    }

    @Test
    void cellsOutsideFieldAreNotFree() {
        assertFalse(maze.isFree(new Position(-1, 1)));
        assertFalse(maze.isFree(new Position(1, -1)));
        assertFalse(maze.isFree(new Position(3, 1)));
        assertFalse(maze.isFree(new Position(1, 4)));
    }
}
