package tek.first.livingbetter.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import tek.first.livingbetter.wallet.model.ItemModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    public static String LIVING_BETTER_DATABASE_NAME = "livingbetter_database";

    public static int VERSION = 1;

    private Context context;

    public static final int SORTING_STANDARD_ASC = 0;
    public static final int SORTING_STANDARD_DESC = 1;
    public static final String SORT_BY_PRIORITY = "priority";
    public static final String SORT_BY_DEADLINE = "deadline";
    public static final String SORT_BY_TIME_ADDED = "timeAdded";
    public static final String SORT_BY_TITLE = "title";

    public static final String SORTING_ASC = " ASC";
    public static final String SORTING_DESC = " DESC";

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

        Log.v(LOG_TAG, "onCreate() executed.");
        String createDetailedToDoItemListTableQuery = "CREATE TABLE " +
                LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME + " (" +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE + " TEXT NOT NULL, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DESCRIPTION + " TEXT, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY + " INTEGER DEFAULT 1, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " TEXT, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " TEXT NOT NULL, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_CATEGORY + " TEXT, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE + " INTEGER DEFAULT 1, " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRICE + " REAL DEFAULT 0.0)";
        Log.v(LOG_TAG, "createDetailedToDoItemListTableQuery: " + createDetailedToDoItemListTableQuery);
        db.execSQL(createDetailedToDoItemListTableQuery);

        String createSimpleToDoItemListTableQuery = "CREATE TABLE " + LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME + " (" +
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE + " TEXT NOT NULL, " +
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_PRIORITY + " INTEGER DEFAULT 1, " +
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " TEXT NOT NULL, " +
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE + " INTEGER DEFAULT 1)";
        Log.v(LOG_TAG, "createSimpleToDoItemListTableQuery: " + createSimpleToDoItemListTableQuery);
        db.execSQL(createSimpleToDoItemListTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeDetailedToDoItemListTableQuery = "DROP TABLE IF EXISTS " + LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME;
        Log.v(LOG_TAG, "Upgrade " + LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME + " from " + oldVersion + " to " + newVersion);
        Log.v(LOG_TAG, "upgradeDetailedToDoItemListTableQuery: " + upgradeDetailedToDoItemListTableQuery);
        db.execSQL(upgradeDetailedToDoItemListTableQuery);

        String upgradeSimpleToDoItemListTableQuery = "DROP TABLE IF EXISTS " + LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME;
        Log.v(LOG_TAG, "Upgrade " + LivingBetterContract.SimpleToDoItemEntry.TABLE_NAME + " from " + oldVersion + " to " + newVersion);
        Log.v(LOG_TAG, "upgradeSimpleToDoItemListTableQuery: " + upgradeSimpleToDoItemListTableQuery);
        db.execSQL(upgradeSimpleToDoItemListTableQuery);
    }

    public ArrayList<ItemModel> getItemFilteredDate(String start, String end) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "SELECT * FROM " + LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME +
                " WHERE " + LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " >= ? AND " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " <= ? ";
        String[] selectionArgs = new String[]{start, end};
        Cursor res = db.rawQuery(selection, selectionArgs);
        ItemModel item;
        ArrayList<ItemModel> list = new ArrayList<>();
        if (res == null)
            return list;
        while (res.moveToNext()) {
            item = new ItemModel(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(4), res.getString(6), res.getString(8));
            list.add(item);
        }
        return list;
    }

    public ArrayList<ItemModel> getItemFilteredDate(long start, long end) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "SELECT * FROM " + LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME +
                " WHERE " + LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " >= ? AND " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " <= ? ";
        String[] selectionArgs = new String[]{Long.toString(start), Long.toString(end)};
        Cursor res = db.rawQuery(selection, selectionArgs);
        ItemModel item;
        ArrayList<ItemModel> list = new ArrayList<>();
        if (res == null)
            return list;
        while (res.moveToNext()) {
            item = new ItemModel(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(4), res.getString(6), res.getString(8));
            list.add(item);
        }
        return list;
    }

    public ArrayList<ItemModel> getMonthData(String start, String end) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "SELECT * FROM " + LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME +
                " WHERE " + LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " >= ? AND " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE + " <= ? ";
        String[] selectionArgs = new String[]{start, end};

        Cursor res = db.rawQuery(selection, selectionArgs);
        ItemModel item;
        ArrayList<ItemModel> list = new ArrayList<>();
        if (res == null)
            return list;
        while (res.moveToNext()) {
            item = new ItemModel(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(4), res.getString(6), res.getString(8));
            list.add(item);
        }
        return list;
    }

    public float getCurrentCost(int year, int month) {
        float current = 0;
        ArrayList<Float> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = "SELECT " + LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + ", " +
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRICE + " FROM " +
                LivingBetterContract.DetailedToDoItemEntry.TABLE_NAME +
                " WHERE " + LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE + " = ?";
        String[] selectionArgs = new String[]{"1"};
        Cursor res = db.rawQuery(selection, selectionArgs);

        while (res.moveToNext()) {
            String str2 = res.getString(0);
            if (Integer.parseInt(res.getString(0).substring(4, 6)) == (month + 1) && Integer.parseInt(res.getString(0).substring(0, 4)) == year) {
                list.add(res.getFloat(1));
                String s = String.valueOf(res.getFloat(1));
                Log.e("float", s);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            current += list.get(i);
        }
        return current;
    }
}
