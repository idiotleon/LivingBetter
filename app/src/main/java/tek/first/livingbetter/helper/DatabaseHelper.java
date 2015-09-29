package tek.first.livingbetter.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tek.first.livingbetter.todolist.toDoItem;
import tek.first.livingbetter.wallet.Item;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = DatabaseHelper.class.getSimpleName();

    private Context context;


    public static String TODOLIST_DATABASE_NAME = "todolist_database";
    public static String TODOLIST_TABLE_NAME = "todolist_table";
    public static String TODOLIST_ITEM_COLUMN_ID = "todolist_id";
    public static String TODOLIST_ITEM_COLUMN_TITLE = "todolist_title";
    public static String TODOLIST_ITEM_COLUMN_DESCRIPTION = "todolist_description";
    public static String TODOLIST_ITEM_COLUMN_PRIORITY = "todolist_priority";
    public static String TODOLIST_ITEM_COLUMN_DEADLINE = "todolist_time_date_set";
    public static String TODOLIST_ITEM_COLUMN_TIME_DATE_CREATED = "todolist_time_date_created";
    public static String TODOLIST_ITEM_COLUMN_CATEGORY = "todolist_category";
    public static String TODOLIST_ITEM_COLUMN_PRICE = "todolist_price";
    public static String TODOLIST_ITEM_COLUMN_COMPLETE_STATUS_CODE = "todolist_complete_status_code";

    public static int VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, TODOLIST_DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TODOLIST_TABLE_NAME + " (" +
                TODOLIST_ITEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TODOLIST_ITEM_COLUMN_TITLE + " TEXT NOT NULL, " +
                TODOLIST_ITEM_COLUMN_DESCRIPTION + " TEXT, " +
                TODOLIST_ITEM_COLUMN_PRIORITY + " INTEGER DEFAULT 1, " +
                TODOLIST_ITEM_COLUMN_DEADLINE + " TEXT, " +
                TODOLIST_ITEM_COLUMN_TIME_DATE_CREATED + " TEXT, " +
                TODOLIST_ITEM_COLUMN_CATEGORY + " TEXT, " +
                TODOLIST_ITEM_COLUMN_COMPLETE_STATUS_CODE + " INTEGER DEFAULT 0," +
                TODOLIST_ITEM_COLUMN_PRICE + " REAL DEFAULT 0.0)";
        Log.v(LOG_TAG, "createTableQuery: " + createTableQuery);

        Log.v(LOG_TAG, "onCreate(), DatabaseHelper");

        db.execSQL(createTableQuery);

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " ("
                + UserContract.UserEntry.COLUMN_USER_ID + " INTEGER PRIMARY KEY,"
                + UserContract.UserEntry.COLUMN_USER_USERNAME + " TEXT UNIQUE NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
                + UserContract.UserEntry.COLUMN_USER_AGE + " INTEGER, "
                + UserContract.UserEntry.COLUMN_USER_COUNTRY + " TEXT, "
                + UserContract.UserEntry.COLUMN_USER_CITY + " TEXT, "
                + UserContract.UserEntry.COLUMN_USER_STREET + " TEXT);";

        db.execSQL(SQL_CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgradeTableQuery = "DROP TABLE IF EXISTS " + TODOLIST_TABLE_NAME;
        db.execSQL(upgradeTableQuery);
        db.execSQL("DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME);

    }


    public boolean insertUser(ContentValues values) {

        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);
        if (res == -1)
            return false;
        return true;
    }

    public int validateLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select password from user where username = ?", new String[]{username});
        if (!cursor.moveToFirst())
            return -1;
        if (cursor.getString(0).equals(password))
            return 1;
        else
            return 0;
    }

    public int getExpense(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select todolist_category,todolist_price from todolist_table where todolist_category =" + "'" + category + "'", null);
        int total = 0;
        while (res.moveToNext()) {
            total += Integer.parseInt(res.getString(8));
        }
        return total;
    }

    public ArrayList<String> getName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select todolist_category from todolist_table", null);
        ArrayList<String> list = new ArrayList<>();
        String name = null;
        while (res.moveToNext()) {
            name = res.getString(0);
            if (list.contains(name)) {
                continue;
            } else {
                list.add(name);
            }
        }
        return list;
    }

    public ArrayList<Item> getItemfiltedDate(Long start, Long end) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from todolist_table where todolist_time_date_set >= ? and todolist_time_date_set <= ? ", new String[]{start.toString(), end.toString()});
        Item item;
        ArrayList<Item> list = new ArrayList<>();
        if (res == null)
            return list;
        while (res.moveToNext()) {
            item = new Item(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(4), res.getString(6), res.getString(8));
            list.add(item);
        }
        return list;
    }

    public ArrayList<Item> getItemfiltedDate(String start, String end) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from todolist_table where todolist_time_date_set >= ? and todolist_time_date_set <= ? ", new String[]{start, end});
        Item item;
        ArrayList<Item> list = new ArrayList<>();
        if (res == null)
            return list;
        while (res.moveToNext()) {
            item = new Item(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(4), res.getString(6), res.getString(8));
            list.add(item);
        }
        return list;
    }

    public ArrayList<Item> getMonthData(String start, String end) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from todolist_table where todolist_time_date_set >= ? and todolist_time_date_set <= ? ", new String[]{start, end});
        Item item;
        ArrayList<Item> list = new ArrayList<>();
        if (res == null)
            return list;
        while (res.moveToNext()) {
            item = new Item(Integer.parseInt(res.getString(0)), res.getString(1), res.getString(4), res.getString(6), res.getString(8));
            list.add(item);
        }
        return list;
    }

    public float getCurrentCost(int year, int month) {
        float current = 0;
        ArrayList<Float> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select todolist_time_date_set,todolist_price from todolist_table where todolist_complete_status_code = 1 ", null);

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

    public boolean insertData(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TODOLIST_TABLE_NAME, null, values);
        return true;
    }

    public ArrayList<Item> getUnPaidActivity() {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Item> res = new ArrayList<Item>();
        Cursor cursor = db.query(TODOLIST_TABLE_NAME,
                new String[]{TODOLIST_ITEM_COLUMN_ID, TODOLIST_ITEM_COLUMN_TITLE, TODOLIST_ITEM_COLUMN_DEADLINE, TODOLIST_ITEM_COLUMN_CATEGORY},
                TODOLIST_TABLE_NAME + "." + TODOLIST_ITEM_COLUMN_PRICE + " = ? ",
                new String[]{"0"},
                null,
                null,
                null);

        if (!cursor.moveToFirst()) {
            if (db != null)
                db.close();
            return res;

        }

        Item item = new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        res.add(item);
        while (cursor.moveToNext()) {
            item = new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            res.add(item);
        }

        if (db != null)
            db.close();

        return res;
    }

    public void inputPrice(String id, String price) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TODOLIST_ITEM_COLUMN_PRICE, price);
        db.update(TODOLIST_TABLE_NAME,
                values,
                TODOLIST_TABLE_NAME + "." + TODOLIST_ITEM_COLUMN_ID + " = ? ",
                new String[]{id});

        if (db != null)
            db.close();
    }

    public void deleteActivity(int id) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TODOLIST_TABLE_NAME,
                TODOLIST_TABLE_NAME + "." + TODOLIST_ITEM_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(id)});
    }

    public boolean insertToDoListItem(toDoItem toDoListItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_COLUMN_TITLE, toDoListItem.getTitle());
        contentValues.put(TODOLIST_ITEM_COLUMN_PRIORITY, toDoListItem.getPriority());
        String createtime = toDoListItem.getCreatetime();
        contentValues.put(TODOLIST_ITEM_COLUMN_TIME_DATE_CREATED, createtime);
        contentValues.put(TODOLIST_ITEM_COLUMN_CATEGORY, toDoListItem.getCategory());
        contentValues.put(TODOLIST_ITEM_COLUMN_COMPLETE_STATUS_CODE, 0);
        db.insert(TODOLIST_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateToDoListItem(toDoItem toDoItem,float expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_COLUMN_TITLE, toDoItem.getTitle());
        contentValues.put(TODOLIST_ITEM_COLUMN_PRIORITY, toDoItem.getPriority());

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String str = format.format(new Date());

        contentValues.put(TODOLIST_ITEM_COLUMN_DEADLINE, str);
        contentValues.put(TODOLIST_ITEM_COLUMN_CATEGORY, toDoItem.getCategory());
        contentValues.put(TODOLIST_ITEM_COLUMN_COMPLETE_STATUS_CODE, 1);
        contentValues.put(TODOLIST_ITEM_COLUMN_PRICE,expense);
        db.update(TODOLIST_TABLE_NAME, contentValues,
                TODOLIST_ITEM_COLUMN_TITLE + " = ? AND " + TODOLIST_ITEM_COLUMN_TIME_DATE_CREATED + " = ? ",
                new String[]{toDoItem.getTitle(), toDoItem.getCreatetime()});
        return true;
    }
    public boolean updateToDoListItem(toDoItem old,toDoItem last) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODOLIST_ITEM_COLUMN_TITLE, last.getTitle());
        contentValues.put(TODOLIST_ITEM_COLUMN_PRIORITY, last.getPriority());
        String createtime = last.getCreatetime();
        contentValues.put(TODOLIST_ITEM_COLUMN_TIME_DATE_CREATED, createtime);
        contentValues.put(TODOLIST_ITEM_COLUMN_CATEGORY, last.getCategory());
        contentValues.put(TODOLIST_ITEM_COLUMN_COMPLETE_STATUS_CODE, 0);
        db.update(TODOLIST_TABLE_NAME, contentValues,
                TODOLIST_ITEM_COLUMN_TITLE + " = ? AND " + TODOLIST_ITEM_COLUMN_DEADLINE + " = ? ",
                new String[]{old.getTitle(), old.getCreatetime()});
        return true;
    }

    public ArrayList<toDoItem> getincomplete(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<toDoItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TODOLIST_TABLE_NAME+" WHERE " + TODOLIST_ITEM_COLUMN_COMPLETE_STATUS_CODE+" = "+ "0",null);
        while(cursor.moveToNext()){
            toDoItem item = new toDoItem(0,cursor.getString(1),cursor.getString(5), Integer.parseInt(cursor.getString(3)),cursor.getString(6));
            list.add(item);
        }
        return  list;
    }
    public ArrayList<toDoItem> getcompleted(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<toDoItem> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from  todolist_table where todolist_complete_status_code = "+1+"",null);
        while(cursor.moveToNext()){
            toDoItem item = new toDoItem(1,cursor.getString(1),cursor.getString(5), Integer.parseInt(cursor.getString(3)),cursor.getString(6));
            list.add(item);
        }
        return  list;
    }

    public boolean deleteToDoItem(final toDoItem toDoItem) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODOLIST_TABLE_NAME,
                TODOLIST_ITEM_COLUMN_TIME_DATE_CREATED + " = ?",
                new String[]{toDoItem.getCreatetime()});
        return true;
    }
}
