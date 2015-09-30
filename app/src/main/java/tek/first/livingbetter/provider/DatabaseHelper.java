package tek.first.livingbetter.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public static String LIVING_BETTER_DATABASE_NAME = "livingbetter_database";

    public static int VERSION = 1;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, LIVING_BETTER_DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG, "onCreate() executed.");
        final String createHabitTableQuery = "CREATE TABLE " + LivingBetterContract.HabitInfoEntry.TABLE_NAME + " (" +
                LivingBetterContract.HabitInfoEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_RATING + " REAL, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_NUMBER_OF_REVIEWS + " INTEGER, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_CATEGORY + " TEXT, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_IMAGE_URL + " TEXT, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_SNIPPET_TEXT + " TEXT, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_ADDRESS + " TEXT, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_PHONE_NUMBER + " TEXT, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_MOBILE_URL + " TEXT, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_LATITUDE + " REAL, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_LONGITUDE + " REAL, " +
                LivingBetterContract.HabitInfoEntry.COLUMN_DISTANCE + " REAL, " +
                " UNIQUE(" + LivingBetterContract.HabitInfoEntry.COLUMN_ADDRESS + ") ON CONFLICT REPLACE)";
        Log.v(LOG_TAG, "createHabitTableQuery: " + createHabitTableQuery);
        db.execSQL(createHabitTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
