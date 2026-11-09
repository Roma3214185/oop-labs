package ua.maze;

import java.util.Random;

import androidx.lifecycle.ViewModel;

import timber.log.Timber;
import ua.maze.model.Direction;
import ua.maze.model.Game;
import ua.maze.model.MazeGenerator;
import ua.maze.model.MoveResult;

/**
 * Тримає поточну гру. ViewModel переживає перестворення Activity
 * (поворот екрана, зміна мови), тому лабіринт і позиція не губляться.
 */
public class GameViewModel extends ViewModel {

    /** Кількість клітинок по стороні: сітка 21 × 21 разом зі стінами. */
    static final int SIZE = 10;

    private Game game;

    public Game getGame() {
        if (game == null) {
            newGame();
        }
        return game;
    }

    public void newGame() {
        long seed = new Random().nextLong();
        game = new Game(new MazeGenerator(new Random(seed)).generate(SIZE));
        Timber.i("Нова гра %dx%d, seed=%d", game.getMaze().getRows(),
                game.getMaze().getCols(), seed);
    }

    public MoveResult move(Direction direction) {
        MoveResult result = getGame().move(direction);
        switch (result) {
            case MOVED -> Timber.d("%s → %s", direction, game.getPlayer());
            case BLOCKED -> Timber.d("%s заблоковано стіною %s", direction,
                    game.getPlayer());
            case ESCAPED -> Timber.i("Вихід знайдено за %d кроків",
                    game.getMoves());
            case FINISHED -> Timber.w("Хід %s після завершення гри", direction);
        }
        return result;
    }
}
