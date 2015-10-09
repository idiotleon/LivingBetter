package tek.first.livingbetter.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Leon on 9/30/2015.
 */
public class LivingBetterContentProvider extends ContentProvider {

    private static final String LOG_TAG = LivingBetterContract.class.getSimpleName();

    private static final int HABIT = 100;
    private static final int HABITS = 101;
    private static final int SIMPLE_TODO_LIST = 200;
    private static final int SIMPLE_TODO_ITEM = 201;
    private static final int DETAILED_TODO_LIST = 300;
    private static final int DETAILED_TODO_ITEM = 301;
    private static final int USERS = 400;
    private static final int USER_NAME = 401;


    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.HabitInfoEntry.TABLE_NAME, HABITS);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.HabitInfoEntry.TABLE_NAME + "/#", HABIT);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME, SIMPLE_TODO_LIST);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME + "/#", SIMPLE_TODO_ITEM);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME, DETAILED_TODO_LIST);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME + "/#", DETAILED_TODO_ITEM);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.UserEntry.TABLE_NAME, USERS);
        uriMatcher.addURI(LivingBetterContract.CONTENT_AUTHORITY,
                LivingBetterContract.UserEntry.TABLE_NAME + "/#", USER_NAME);
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

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        String todoItemCreatedDateAndTime = "0";

        switch (uriMatcher.match(uri)) {
            case HABITS:
                queryBuilder.setTables(LivingBetterContract.HabitInfoEntry.TABLE_NAME);
                break;
            case HABIT:
                String habitName = uri.getPathSegments().get(1);
                queryBuilder.setTables(LivingBetterContract.HabitInfoEntry.TABLE_NAME);
                queryBuilder.appendWhere(LivingBetterContract.HabitInfoEntry.COLUMN_NAME + " = " + habitName);
                break;
            case SIMPLE_TODO_LIST:
                queryBuilder.setTables(LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME);
                break;
            case SIMPLE_TODO_ITEM:
                todoItemCreatedDateAndTime = uri.getPathSegments().get(1);
                queryBuilder.setTables(LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME);
                queryBuilder.appendWhere(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_ID + " = " + todoItemCreatedDateAndTime);
                break;
            case DETAILED_TODO_LIST:
                queryBuilder.setTables(LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME);
                break;
            case DETAILED_TODO_ITEM:
                todoItemCreatedDateAndTime = uri.getPathSegments().get(1);
                queryBuilder.setTables(LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME);
                queryBuilder.appendWhere(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_ID + " = " + todoItemCreatedDateAndTime);
                break;
            case USERS:
                queryBuilder.setTables(LivingBetterContract.UserEntry.TABLE_NAME);
                break;
            case USER_NAME:
                queryBuilder.setTables(LivingBetterContract.UserEntry.TABLE_NAME);
                queryBuilder.appendWhere(LivingBetterContract.UserEntry.COLUMN_USER_USERNAME + " = " + uri.getPathSegments().get(1));
                break;
        }

        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, groupBy, having, sortOrder);

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
            case SIMPLE_TODO_LIST:
                return LivingBetterContract.SimpleToDoItemEntry.CONTENT_TYPE;
            case SIMPLE_TODO_ITEM:
                return LivingBetterContract.SimpleToDoItemEntry.CONTNET_ITEM_TYPE;
            case DETAILED_TODO_LIST:
                return LivingBetterContract.DetailedToDoItemEntry.CONTENT_TYPE;
            case DETAILED_TODO_ITEM:
                return LivingBetterContract.DetailedToDoItemEntry.CONTENT_ITEM_TYPE;
            case USERS:
                return LivingBetterContract.UserEntry.CONTENT_TYPE;
            case USER_NAME:
                return LivingBetterContract.UserEntry.CONTNET_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unspported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(LOG_TAG, "insert(Uri uri, ContentValues values), ToDoListProvider executed");

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
            case SIMPLE_TODO_LIST:
                id = database.insert(LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME, nullColumnHack, values);
                if (id > -1) {
                    insertedId = ContentUris.withAppendedId(LivingBetterContract.SimpleToDoItemEntry.CONTENT_URI, id);
                    Log.v(LOG_TAG, "A new simple ToDoItem, insert(Uri uri, ContentValues values), ToDoListProvider: " + insertedId.toString());
                    getContext().getContentResolver().notifyChange(insertedId, null);
                    return insertedId;
                }
            case DETAILED_TODO_LIST:
                id = database.insert(LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME, nullColumnHack, values);
                if (id > -1) {
                    insertedId = ContentUris.withAppendedId(LivingBetterContract.DetailedToDoItemEntry.CONTENT_URI, id);
                    Log.v(LOG_TAG, "A new detailed ToDoItem, insert(Uri uri, ContentValues values), ToDoListProvider: " + insertedId.toString());
                    getContext().getContentResolver().notifyChange(insertedId, null);
                    return insertedId;
                }
            case USERS:
                id = database.insert(LivingBetterContract.UserEntry.TABLE_NAME, nullColumnHack, values);
                if (id > -1) {
                    insertedId = ContentUris.withAppendedId(LivingBetterContract.UserEntry.CONTENT_URI, id);
                    Log.v(LOG_TAG, "A new user, insert(Uri uri, ContentValues values): " + insertedId.toString());
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

        String todoItemCreatedDateAndTime;
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
            case SIMPLE_TODO_ITEM:
                todoItemCreatedDateAndTime = uri.getPathSegments().get(1);
                selection = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE
                        + " = ?";
                selectionArgs = new String[]{todoItemCreatedDateAndTime};
                deletedCount = database.delete(LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case SIMPLE_TODO_LIST:
                deletedCount = database.delete(LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME, selection, selectionArgs);
                Log.v(LOG_TAG, "deleteCount, SIMPLE_TODO_LIST: " + deletedCount);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case DETAILED_TODO_ITEM:
                todoItemCreatedDateAndTime = uri.getPathSegments().get(1);
                selection = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE
                        + " = ?";
                selectionArgs = new String[]{todoItemCreatedDateAndTime};
                deletedCount = database.delete(LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case DETAILED_TODO_LIST:
                deletedCount = database.delete(LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME, selection, selectionArgs);
                Log.v(LOG_TAG, "deleteCount, DETAILED_TODO_LIST: " + deletedCount);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case USER_NAME:
                String username = uri.getPathSegments().get(1);
                selection = LivingBetterContract.UserEntry.COLUMN_USER_USERNAME
                        + " = ?";
                selectionArgs = new String[]{username};
                deletedCount = database.delete(LivingBetterContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                break;
            case USERS:
                deletedCount = database.delete(LivingBetterContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                Log.v(LOG_TAG, "deletedCount, USERS: " + deletedCount);
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
        String todoItemCreatedDateAndTime;

        int updatedCount = 0;

        switch (uriMatcher.match(uri)) {
            case HABITS:
                updatedCount = database.update(LivingBetterContract.HabitInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case HABIT:
                name = uri.getPathSegments().get(1);
                distance = uri.getPathSegments().get(2);
                selection = LivingBetterContract.HabitInfoEntry.COLUMN_NAME + "=?&&" +
                        LivingBetterContract.HabitInfoEntry.COLUMN_DISTANCE + "=?";
                selectionArgs = new String[]{name, distance};
                updatedCount = database.update(LivingBetterContract.HabitInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SIMPLE_TODO_ITEM:
                todoItemCreatedDateAndTime = uri.getPathSegments().get(1);
                selection = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_ID +
                        " = ?";
                selectionArgs = new String[]{todoItemCreatedDateAndTime};
                updatedCount = database.update(LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SIMPLE_TODO_LIST:
                updatedCount = database.update(LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DETAILED_TODO_ITEM:
                todoItemCreatedDateAndTime = uri.getPathSegments().get(1);
                selection = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_ID +
                        " = ?";
                selectionArgs = new String[]{todoItemCreatedDateAndTime};
                updatedCount = database.update(LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case DETAILED_TODO_LIST:
                updatedCount = database.update(LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USER_NAME:
                String username = uri.getPathSegments().get(1);
                selection = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_ID +
                        " = ?";
                selectionArgs = new String[]{username};
                updatedCount = database.update(LivingBetterContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case USERS:
                updatedCount = database.update(LivingBetterContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return updatedCount;
    }
}
