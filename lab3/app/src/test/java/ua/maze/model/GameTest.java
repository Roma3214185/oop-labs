package ua.maze.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class GameTest {

    /** Гравець у центрі хрестика, вихід праворуч. */
    private static Game crossGame() {
        return new Game(TestMazes.parse(
                "##.##",
                "##.##",
                "..S.E",
                "##.##",
                "#####"));
    }

    @Test
    void startsAtStartWithZeroMoves() {
        Game game = crossGame();

        assertEquals(new Position(2, 2), game.getPlayer());
        assertEquals(0, game.getMoves());
        assertFalse(game.isFinished());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void movesToFreeCellInAnyDirection(Direction direction) {
        Game game = crossGame();

        assertEquals(MoveResult.MOVED, game.move(direction));
        assertEquals(new Position(2, 2).next(direction), game.getPlayer());
        assertEquals(1, game.getMoves());
    }

    @Test
    void wallBlocksMove() {
        Game game = crossGame();
        game.move(Direction.DOWN);

        assertEquals(MoveResult.BLOCKED, game.move(Direction.DOWN));
        assertEquals(MoveResult.BLOCKED, game.move(Direction.LEFT));
        assertEquals(new Position(3, 2), game.getPlayer());
        assertEquals(1, game.getMoves());
    }

    @Test
    void edgeOfFieldBlocksMove() {
        Game game = crossGame();
        game.move(Direction.UP);
        game.move(Direction.UP);

        assertEquals(MoveResult.BLOCKED, game.move(Direction.UP));
        assertEquals(new Position(0, 2), game.getPlayer());
        assertEquals(2, game.getMoves());
    }

    @Test
    void reachingExitWinsGame() {
        Game game = crossGame();

        assertEquals(MoveResult.MOVED, game.move(Direction.RIGHT));
        assertEquals(MoveResult.ESCAPED, game.move(Direction.RIGHT));
        assertTrue(game.isFinished());
        assertEquals(2, game.getMoves());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void noMovesAfterEscape(Direction direction) {
        Game game = crossGame();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);

        assertEquals(MoveResult.FINISHED, game.move(direction));
        assertEquals(new Position(2, 4), game.getPlayer());
        assertEquals(2, game.getMoves());
    }
}
