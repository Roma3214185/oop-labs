package ua.maze;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;

/**
 * Запускається раніше за будь-яку Activity — тут налаштовується логер.
 * <p>
 * Debug-збірка: усі повідомлення йдуть у Logcat, тег — ім'я класу.
 * Release-збірка: лише попередження й помилки.
 */
public class MazeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(BuildConfig.DEBUG ? new Timber.DebugTree() : new ReleaseTree());
        Timber.i("Застосунок запущено, версія %s", BuildConfig.VERSION_NAME);
    }

    /** Дерево для release: пропускає тільки WARN і ERROR. */
    static class ReleaseTree extends Timber.Tree {

        @Override
        protected boolean isLoggable(String tag, int priority) {
            return priority >= Log.WARN;
        }

        @Override
        protected void log(int priority, String tag, String message,
                Throwable error) {
            Log.println(priority, tag == null ? "Maze" : tag, message);
        }
    }
}
