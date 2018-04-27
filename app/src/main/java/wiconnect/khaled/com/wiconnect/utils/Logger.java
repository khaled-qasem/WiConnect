package wiconnect.khaled.com.wiconnect.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import wiconnect.khaled.com.wiconnect.BuildConfig;

/**
 * Created by Khaled on 4/2 7/2018.
 * Assumptions
 * Descriptions
 */

public class Logger {

    private String LOG_TAG;

    private Logger(Class clazz) {
        this.LOG_TAG = clazz.getSimpleName();
    }

    @NonNull
    public static Logger createLogger(Class clazz) {
        return new Logger(clazz);
    }

    public void d(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message);
        }
    }

    public void d(String message, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message, throwable);
        }
    }

    public void i(String message) {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, message);
        }
    }

    public void e(String message) {
        Log.e(LOG_TAG, message);
    }

    public void e(String message, Throwable throwable) {
        Log.e(LOG_TAG, message, throwable);
    }
}
