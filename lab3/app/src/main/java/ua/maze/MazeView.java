package ua.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import ua.maze.model.Game;
import ua.maze.model.Maze;
import ua.maze.model.Position;

/** Малює лабіринт: стіни, вихід і гравця. Завжди квадратна. */
public class MazeView extends View {

    private final Paint wallPaint = paint(R.color.wall);
    private final Paint pathPaint = paint(R.color.path);
    private final Paint exitPaint = paint(R.color.exit);
    private final Paint playerPaint = paint(R.color.player);

    private Game game;

    public MazeView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void setGame(Game game) {
        this.game = game;
        invalidate();
    }

    private Paint paint(int colorId) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(getContext(), colorId));
        return paint;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        int height = MeasureSpec.getSize(heightSpec);
        int size = MeasureSpec.getMode(heightSpec) == MeasureSpec.UNSPECIFIED
                ? width : Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (game == null) {
            return;
        }
        Maze maze = game.getMaze();
        float cell = (float) getWidth() / maze.getCols();

        canvas.drawRect(0, 0, getWidth(), getHeight(), pathPaint);
        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                if (maze.isWall(row, col)) {
                    drawCell(canvas, row, col, cell, wallPaint);
                }
            }
        }
        Position exit = maze.getExit();
        drawCell(canvas, exit.row(), exit.col(), cell, exitPaint);

        Position player = game.getPlayer();
        canvas.drawCircle((player.col() + 0.5f) * cell,
                (player.row() + 0.5f) * cell, cell * 0.4f, playerPaint);
    }

    private static void drawCell(Canvas canvas, int row, int col, float cell,
            Paint paint) {
        canvas.drawRect(col * cell, row * cell, (col + 1) * cell,
                (row + 1) * cell, paint);
    }
}
