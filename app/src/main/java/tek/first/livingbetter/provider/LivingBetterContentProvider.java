package tek.first.livingbetter.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Leon on 9/30/2015.
 */
public class LivingBetterContentProvider extends ContentProvider {

    private static final String LOG_TAG = LivingBetterContract.class.getSimpleName();

    private static final int HABIT = 100;
    private static final int HABITS = 101;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.HabitInfoEntry.TABLE_NAME, HABITS);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.HabitInfoEntry.TABLE_NAME + "/#", HABIT);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        database = databaseHelper.getReadableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case HABITS:
                sqLiteQueryBuilder.setTables(LivingBetterContract.HabitInfoEntry.TABLE_NAME);
                break;
            case HABIT:
                String habitName = uri.getPathSegments().get(1);
                sqLiteQueryBuilder.setTables(LivingBetterContract.HabitInfoEntry.TABLE_NAME);
                sqLiteQueryBuilder.appendWhere(LivingBetterContract.HabitInfoEntry.COLUMN_NAME + " = " + habitName);
                break;
        }

        Cursor cursor = sqLiteQueryBuilder.query(database, projection, selection, selectionArgs, groupBy, having, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case HABITS:
                return LivingBetterContract.HabitInfoEntry.CONTENT_TYPE;
            case HABIT:
                return LivingBetterContract.HabitInfoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unspported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        database = databaseHelper.getWritableDatabase();

        String nullColumnHack = null;

        long id;
        Uri insertedId;
        switch (uriMatcher.match(uri)) {
            case HABITS:
                id = database.insert(LivingBetterContract.HabitInfoEntry.TABLE_NAME, nullColumnHack, values);
                if (id > -1) {
                    insertedId = ContentUris.withAppendedId(LivingBetterContract.HabitInfoEntry.CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(insertedId, null);
                    return insertedId;
                }
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        database = databaseHelper.getWritableDatabase();

        String name;
        String distance;
        int deletedCount = 0;

        switch (uriMatcher.match(uri)) {
            case HABITS:
                name = uri.getPathSegments().get(1);
                distance = uri.getPathSegments().get(2);
                selection = LivingBetterContract.HabitInfoEntry.COLUMN_NAME + " = ?&&"
                        + LivingBetterContract.HabitInfoEntry.COLUMN_DISTANCE + " = ?";
                selectionArgs = new String[]{name, distance};
                if (selection == null) selection = "1";
                deletedCount = database.delete(LivingBetterContract.HabitInfoEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
        }

        return deletedCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        database = databaseHelper.getWritableDatabase();

        String name;
        String distance;

        int updateCount = 0;

        switch (uriMatcher.match(uri)) {
            case HABITS:
                updateCount = database.update(LivingBetterContract.HabitInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case HABIT:
                name = uri.getPathSegments().get(1);
                distance = uri.getPathSegments().get(2);
                selection = LivingBetterContract.HabitInfoEntry.COLUMN_NAME + "=?&&" +
                        LivingBetterContract.HabitInfoEntry.COLUMN_DISTANCE + "=?";
                selectionArgs = new String[]{name, distance};
                updateCount = database.update(LivingBetterContract.HabitInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }
}
