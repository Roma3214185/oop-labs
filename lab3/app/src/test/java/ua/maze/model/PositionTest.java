package ua.maze.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PositionTest {

    @ParameterizedTest
    @CsvSource({
            "UP, 4, 5",
            "DOWN, 6, 5",
            "LEFT, 5, 4",
            "RIGHT, 5, 6"})
    void nextMovesOneCellInDirection(Direction direction, int row, int col) {
        assertEquals(new Position(row, col),
                new Position(5, 5).next(direction));
    }

    @ParameterizedTest
    @CsvSource({"UP, DOWN", "LEFT, RIGHT"})
    void oppositeDirectionsCancelOut(Direction there, Direction back) {
        Position position = new Position(2, 3);

        assertEquals(position, position.next(there).next(back));
    }

    @Test
    void toStringShowsRowAndColumn() {
        assertEquals("(2,3)", new Position(2, 3).toString());
    }
}
