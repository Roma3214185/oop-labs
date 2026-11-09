package ua.maze;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.lifecycle.ViewModelProvider;

import timber.log.Timber;
import ua.maze.model.Direction;
import ua.maze.model.Game;
import ua.maze.model.MoveResult;

/** Єдиний екран гри: лабіринт, статус, кнопки ходу і меню. */
public class MainActivity extends AppCompatActivity {

    private GameViewModel viewModel;
    private MazeView mazeView;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Timber.d("Activity створено, перестворення: %b",
                savedInstanceState != null);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        mazeView = findViewById(R.id.maze);
        status = findViewById(R.id.status);

        findViewById(R.id.button_up).setOnClickListener(v -> move(Direction.UP));
        findViewById(R.id.button_down).setOnClickListener(v -> move(Direction.DOWN));
        findViewById(R.id.button_left).setOnClickListener(v -> move(Direction.LEFT));
        findViewById(R.id.button_right).setOnClickListener(v -> move(Direction.RIGHT));

        showGame();
        if (viewModel.getGame().isFinished()) {
            showVictory();
        }
    }

    private void move(Direction direction) {
        MoveResult result = viewModel.move(direction);
        switch (result) {
            case MOVED -> showGame();
            case BLOCKED -> status.setText(R.string.wall_hit);
            case ESCAPED -> {
                showGame();
                showVictory();
            }
            case FINISHED -> { }
        }
    }

    private void showGame() {
        Game game = viewModel.getGame();
        mazeView.setGame(game);
        status.setText(getResources().getQuantityString(R.plurals.moves,
                game.getMoves(), game.getMoves()));
    }

    private void showVictory() {
        int moves = viewModel.getGame().getMoves();
        new AlertDialog.Builder(this)
                .setTitle(R.string.victory_title)
                .setMessage(getResources().getQuantityString(
                        R.plurals.victory_message, moves, moves))
                .setCancelable(false)
                .setPositiveButton(R.string.new_game, (dialog, which) -> startNewGame())
                .show();
    }

    private void startNewGame() {
        viewModel.newGame();
        showGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String current = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        int checked = switch (current) {
            case "en" -> R.id.language_en;
            case "uk" -> R.id.language_uk;
            default -> R.id.language_system;
        };
        menu.findItem(checked).setChecked(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.new_game) {
            startNewGame();
        } else if (id == R.id.language_system) {
            setLanguage("");
        } else if (id == R.id.language_en) {
            setLanguage("en");
        } else if (id == R.id.language_uk) {
            setLanguage("uk");
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /** Порожній рядок — мова системи. Activity перестворюється автоматично. */
    private void setLanguage(String languageTag) {
        Timber.i("Мова змінена на %s",
                languageTag.isEmpty() ? "системну" : languageTag);
        AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(languageTag));
    }
}
