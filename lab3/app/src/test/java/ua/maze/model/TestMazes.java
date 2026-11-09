package ua.maze.model;

/** Будує лабіринт з рядків: '#' — стіна, 'S' — старт, 'E' — вихід. */
final class TestMazes {

    private TestMazes() {
    }

    static Maze parse(String... lines) {
        boolean[][] walls = new boolean[lines.length][lines[0].length()];
        Position start = null;
        Position exit = null;
        for (int row = 0; row < lines.length; row++) {
            for (int col = 0; col < lines[row].length(); col++) {
                char symbol = lines[row].charAt(col);
                walls[row][col] = symbol == '#';
                if (symbol == 'S') {
                    start = new Position(row, col);
                } else if (symbol == 'E') {
                    exit = new Position(row, col);
                }
            }
        }
        return new Maze(walls, start, exit);
    }
}
