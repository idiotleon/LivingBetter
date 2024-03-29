package tek.first.livingbetter.todolist.helper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tek.first.livingbetter.provider.DatabaseHelper;
import tek.first.livingbetter.provider.LivingBetterContract;
import tek.first.livingbetter.todolist.model.Date;
import tek.first.livingbetter.todolist.model.DetailedToDoItem;
import tek.first.livingbetter.todolist.model.SimpleToDoItem;
import tek.first.livingbetter.todolist.model.Time;

/**
 * Created by Leon on 10/7/2015.
 */
public class GeneralHelper {

    private static String LOG_TAG = GeneralHelper.class.getSimpleName();

    public static Long dateAndTimeFormattedToLong(Date date, Time time) {
        if (date != null && time != null)
            return Long.parseLong(date.formatDateToString() + time.formatTimeToString());
        else {
            Log.e(LOG_TAG, "Date or time might be null");
            return 0L;
        }
    }

    /**
     * 0: not started
     * 1: incompleted
     * 2: completed
     */
    public enum CompletionStatus implements Parcelable {
        INCOMPLETE(1),
        COMPLETED(2);

        private int statusCode;

        CompletionStatus(int statusCode) {
            this.statusCode = statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
            dest.writeInt(statusCode);
        }

        // todo: what is this method used for? Possible explanation might be found at Parcelable class
        public static final Parcelable.Creator<CompletionStatus> CREATOR = new Parcelable.Creator<CompletionStatus>() {

            public CompletionStatus createFromParcel(Parcel in) {
                CompletionStatus completionStatus = CompletionStatus.values()[in.readInt()];
                completionStatus.setStatusCode(in.readInt());
                return completionStatus;
            }

            public CompletionStatus[] newArray(int size) {
                return new CompletionStatus[size];
            }
        };
    }

    public static int getCurrentTimeAndDate(int component) {

        String currentTimeAndDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

        int year = Integer.parseInt(currentTimeAndDate.substring(0, 4));
        int monthOfYear = Integer.parseInt(currentTimeAndDate.substring(4, 6));
        int dayOfMonth = Integer.parseInt(currentTimeAndDate.substring(6, 8));
        int hourOfDay = Integer.parseInt(currentTimeAndDate.substring(8, 10));
        int minute = Integer.parseInt(currentTimeAndDate.substring(10, 12));
        int second = Integer.parseInt(currentTimeAndDate.substring(12, 14));

        switch (component) {
            case 1:
                return year;
            case 2:
                return monthOfYear;
            case 3:
                return dayOfMonth;
            case 4:
                return hourOfDay;
            case 5:
                return minute;
            case 6:
                return second;
            default:
                return 0;
        }
    }

    public static CompletionStatus completionStatusCodeToCompletationStatus(int completionStatusCode) {
        switch (completionStatusCode) {
            case 2:
                return CompletionStatus.COMPLETED;
        }
        return CompletionStatus.INCOMPLETE;
    }

    public static String formatToString(String text) {
        if (text == null || text.length() == 0) {
            return "";
        } else
            return text;
    }

    public static String formatToString(Long text) {
        return Long.toString(text);
    }

    public static String formatToString(int text) {
        return Integer.toString(text);
    }

    public static String parseDateAndTimeToString(Long dateAndTime) {
        if (dateAndTime != 0L && dateAndTime != 1L) {
            String dateAndTimeString = formatToString(dateAndTime);
            String year = dateAndTimeString.substring(0, 4);
            String monthInString = dateAndTimeString.substring(4, 6);
            String day = dateAndTimeString.substring(6, 8);
            String hourOfDay = dateAndTimeString.substring(8, 10);
            String minuteOfHour = dateAndTimeString.substring(10, 12);
            int monthInInt = Integer.parseInt(monthInString);
            String[] monthOptions = {
                    "Jan ", "Feb ", "Mar ", "Apr ",
                    "May ", "Jun ", "Jul ", "Aug ",
                    "Sep ", "Oct ", "Nov ", "Dec "};
            String dateAndTimeFormatted = hourOfDay + ":" + minuteOfHour + ", "
                    + monthOptions[monthInInt - 1] + day + ", " + year;
            Log.v(LOG_TAG, "dateAndTimeFormatted, GeneralHelper: " + dateAndTimeFormatted);
            return dateAndTimeFormatted;
        } else if (dateAndTime == 1L) {
            return "";
        } else {
            return "0";
        }
    }

    public static Long parseDateAndTimeModelToLong(Date date, Time time) {
        if (time == null) {
            /**
             * For purpose of differentiate
             * a simple new todoItem (deadline = 0L)
             * and a detailed todoItem without a deadline (deadline = 1L)
             */
            return 1L;
        } else {
            String dateInStringType;
            String timeInStringType;
            String dateAndTimeInStringType;
            if (date == null) {
                int year = getCurrentTimeAndDate(GeneralConstants.YEAR_OPTION);
                int monthOfYear = getCurrentTimeAndDate(GeneralConstants.MONTH_OF_YEAR_OPTION);
                int dayOfMonth = getCurrentTimeAndDate(GeneralConstants.DAY_OF_MONTH_OPTION);
                dateInStringType = Integer.toString(year) + String.format("%02d", monthOfYear) + String.format("%02d", dayOfMonth);
            } else {
                dateInStringType = date.formatDateToString();
            }
            timeInStringType = time.formatTimeToString();
            dateAndTimeInStringType = dateInStringType + timeInStringType;
            return Long.parseLong(dateAndTimeInStringType);
        }
    }

    public static String parseDateAndTimeToString(String dateAndTimeString) {
        String year = dateAndTimeString.substring(0, 4);
        String monthInString = dateAndTimeString.substring(4, 6);
        String day = dateAndTimeString.substring(6, 8);
        String hourOfDay = dateAndTimeString.substring(8, 10);
        String minuteOfHour = dateAndTimeString.substring(10, 12);
        int monthInInt = Integer.parseInt(monthInString);
        String[] monthOptions = {
                "Jan ", "Feb ", "Mar ", "Apr ",
                "May ", "Jun ", "Jul ", "Aug ",
                "Sep ", "Oct ", "Nov ", "Dec "};
        String dateAndTimeFormatted = hourOfDay + ":" + minuteOfHour + ", "
                + monthOptions[monthInInt - 1] + day + ", " + year;
        Log.v(LOG_TAG, "dateAndTimeFormatted, GeneralHelper: " + dateAndTimeFormatted);
        return dateAndTimeFormatted;
    }

    public static void displayTitleOfAllToDoItemsInAnArrayList(ArrayList<DetailedToDoItem> toDoItemsArrayList, String hint) {
        if (toDoItemsArrayList.isEmpty()) {
            Log.v(LOG_TAG, hint + ": is an empty ArrayList.");
        } else {
            for (int i = 0; i < toDoItemsArrayList.size(); i++) {
                Log.v(LOG_TAG, hint + ": " + toDoItemsArrayList.get(i).getTitle());
            }
        }
    }

    /**
     * Helper method to decide whether on which the app is runned is a tablet.
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration()
                .screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void sortStandard(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPreferences.edit().putInt(key, 1).commit();
    }

    public static void hideFragment(Activity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(fragment)
                .commit();
    }

    public static void showFragment(Activity activity, Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fragment)
                .commit();
    }

    public interface ToDoItemStatusChangeListener {
        void onStatusChanged();
    }

    public static void hideSoftKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static Uri insertToDoListItem(Context context, DetailedToDoItem detailedToDoListItem) {
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE, detailedToDoListItem.getTitle());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DESCRIPTION, detailedToDoListItem.getDetailDescription());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY, detailedToDoListItem.getPriority());
        Long toDoItemDateAndTimeCreatedLongType = detailedToDoListItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType, inserted by DatabaseHelper: " + toDoItemDateAndTimeCreatedLongType);
        String toDoItemDateAndTimeCreated = Long.toString(toDoItemDateAndTimeCreatedLongType);
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE, toDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, inserted by DatabaseHelper: " + toDoItemDateAndTimeCreated);
        String deadline = Long.toString(detailedToDoListItem.getToDoItemDeadline());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE, deadline);
        Log.v(LOG_TAG, "deadline, inserted by DatabaseHelper: " + deadline);
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_CATEGORY, detailedToDoListItem.getCategory());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE, detailedToDoListItem.getCompletionStatus().getStatusCode());

        Uri insertedId = contentResolver.insert(LivingBetterContract.DetailedToDoItemEntry.CONTENT_URI, contentValues);

        return insertedId;
    }

    public static Uri insertToDoListItem(Context context, SimpleToDoItem simpleToDoItemModel) {
        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE, simpleToDoItemModel.getTitle());
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_PRIORITY, 1);
        Long simpleToDoItemCreatedDateAndTimeInLongType = simpleToDoItemModel.getItemCreatedDateAndTime();
        String simpleToDoItemCreatedDateAndTime = Long.toString(simpleToDoItemCreatedDateAndTimeInLongType);
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE, simpleToDoItemCreatedDateAndTime);
        Log.v(LOG_TAG, "simpleToDoItemDateAndTimeCreated, inserted by insertToDoListItem(Context context, SimpleToDoItem simpleToDoItemModel), GeneralHelper: " + simpleToDoItemCreatedDateAndTime);
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE, simpleToDoItemModel.getCompletionStatus().getStatusCode());

        Uri insertedId = contentResolver.insert(LivingBetterContract.SimpleToDoItemEntry.CONTENT_URI, contentValues);
        Log.v(LOG_TAG, "insertToDoListItem(Context context, SimpleToDoItem simpleToDoItemModel), GeneralHelper: " + insertedId.toString());
        return insertedId;
    }

    public static int updateToDoListItem(Context context, DetailedToDoItem detailedToDoItem) {
        Log.v(LOG_TAG, "updateToDoListItem(Context context, DetailedToDoItem detailedToDoItem) executed. ");

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE, detailedToDoItem.getTitle());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DESCRIPTION, detailedToDoItem.getDetailDescription());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY, detailedToDoItem.getPriority());
        Long toDoItemDateAndTimeCreatedLongType = detailedToDoItem.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "itemCreatedDateAndTimeLongType, updated by DatabaseHelper: " + toDoItemDateAndTimeCreatedLongType);
        String toDoItemDateAndTimeCreated = Long.toString(toDoItemDateAndTimeCreatedLongType);
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE, toDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "itemCreatedDateAndTime, updated by DatabaseHelper: " + toDoItemDateAndTimeCreated);
        String deadline = Long.toString(detailedToDoItem.getToDoItemDeadline());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE, deadline);
        Log.v(LOG_TAG, "deadline, updated by DatabaseHelper: " + deadline);
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_CATEGORY, detailedToDoItem.getCategory());
        contentValues.put(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE, detailedToDoItem.getCompletionStatus().getStatusCode());
        Log.v(LOG_TAG, "completionStatusCode, updated by DatabaseHelper: " + detailedToDoItem.getCompletionStatus().getStatusCode());

        String selection = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " = ?";
        String[] selectionArgs = new String[]{Long.toString(detailedToDoItem.getItemCreatedDateAndTime())};

        int updateRowsCount = contentResolver.update(LivingBetterContract.DetailedToDoItemEntry.CONTENT_URI,
                contentValues, selection, selectionArgs);

        return updateRowsCount;
    }

    public static int updateToDoListItem(Context context, SimpleToDoItem simpleToDoItemModel) {
        Log.v(LOG_TAG, "updateToDoListItem(Context context, SimpleToDoItem simpleToDoItemModel) executed. ");

        ContentResolver contentResolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE, simpleToDoItemModel.getTitle());
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_PRIORITY, simpleToDoItemModel.getPriority());
        Long simpleToDoItemDateAndTimeCreatedInLongType = simpleToDoItemModel.getItemCreatedDateAndTime();
        Log.v(LOG_TAG, "simpleToDoItemDateAndTimeCreatedLongType, updated by updateToDoListItem(Context context, SimpleToDoItem simpleToDoItemModel), GeneralHelper: " + simpleToDoItemDateAndTimeCreatedInLongType);
        String simpleToDoItemDateAndTimeCreated = Long.toString(simpleToDoItemDateAndTimeCreatedInLongType);
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE, simpleToDoItemDateAndTimeCreated);
        Log.v(LOG_TAG, "simpleToDoItemDateAndTimeCreated, updated by updateToDoListItem(Context context, SimpleToDoItem simpleToDoItemModel), GeneralHelper: " + simpleToDoItemDateAndTimeCreated);
        contentValues.put(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE, simpleToDoItemModel.getCompletionStatus().getStatusCode());

        String selection = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " = ?";
        String[] selectionArgs = new String[]{Long.toString(simpleToDoItemModel.getItemCreatedDateAndTime())};

        int updateRowsCount = contentResolver.update(LivingBetterContract.SimpleToDoItemEntry.CONTENT_URI,
                contentValues, selection, selectionArgs);

        return updateRowsCount;
    }

    public static int deleteToDoItem(Context context, DetailedToDoItem detailedToDoItem) {
        Log.v(LOG_TAG, "deleteToDoItem(Context context, DetailedToDoItem detailedToDoItem), GeneralHelper executed.");
        ContentResolver contentResolver = context.getContentResolver();

        String selection = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " = ?";
        String[] selectionArgs = new String[]{Long.toString(detailedToDoItem.getItemCreatedDateAndTime())};
        int deleteRowsCount = contentResolver.delete(LivingBetterContract.DetailedToDoItemEntry.CONTENT_URI, selection, selectionArgs);
        Log.v(LOG_TAG, "deleteRowCount, deleteToDoItem(Context context, DetailedToDoItem detailedToDoItem): " + deleteRowsCount);

        return deleteRowsCount;
    }

    public static int deleteToDoItem(Context context, SimpleToDoItem simpleToDoItemModel) {
        Log.v(LOG_TAG, "deleteToDoItem(Context context, SimpleToDoItem simpleToDoItemModel), GeneralHelper executed.");
        ContentResolver contentResolver = context.getContentResolver();

        String selection = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE + " = ?";
        String[] selectionArgs = new String[]{Long.toString(simpleToDoItemModel.getItemCreatedDateAndTime())};
        Log.v(LOG_TAG, "selectionArgs: " + Long.toString(simpleToDoItemModel.getItemCreatedDateAndTime()));
        int deleteRowsCount = contentResolver.delete(LivingBetterContract.SimpleToDoItemEntry.CONTENT_URI, selection, selectionArgs);
        Log.v(LOG_TAG, "deleteRowCount, deleteToDoItem(Context context, SimpleToDoItem simpleToDoItemModel): " + deleteRowsCount);

        return deleteRowsCount;
    }

    public static ArrayList<DetailedToDoItem> getSortedDetailedToDoItemsAsArrayList(Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingStandard
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER,
                DatabaseHelper.SORT_BY_DEADLINE);
        int sortAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<DetailedToDoItem> toDoItemsArrayListSorted = new ArrayList<>();

        String projection[] = null;

        String selection = null;
        String selectionArgs[] = null;
        String orderBy;
        switch (sortingStandard) {
            case DatabaseHelper.SORT_BY_PRIORITY:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY;
                break;
            case DatabaseHelper.SORT_BY_TIME_ADDED:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            case DatabaseHelper.SORT_BY_TITLE:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE;
                break;
            default:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE;
                break;
        }

        switch (sortAscOrDesc) {
            case DatabaseHelper.SORTING_STANDARD_ASC:
                orderBy += DatabaseHelper.SORTING_ASC;
                break;
            default:
                orderBy += DatabaseHelper.SORTING_DESC;
                break;
        }

        Cursor cursor = contentResolver.query
                (LivingBetterContract.DetailedToDoItemEntry.CONTENT_URI,
                        projection, selection, selectionArgs, orderBy);

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE));
                    int priority = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY));
                    long deadline = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE));
                    long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE));
                    String detailDescription = cursor.getString(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DESCRIPTION));
                    int category = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_CATEGORY));
                    int completionStatusCode = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE));
                    DetailedToDoItem toDoListItem = new DetailedToDoItem(title, priority, detailDescription,
                            itemDateAndTimeCreated, deadline, category, completionStatusCode);
                    toDoItemsArrayListSorted.add(toDoListItem);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }

        String hint = "ArrayList is sorted by " + orderBy + ": ";
        GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(toDoItemsArrayListSorted, hint);

        return toDoItemsArrayListSorted;
    }

    public static ArrayList<DetailedToDoItem> getSortedIncompleteDetailedToDoItemsAsArrayList(Context context) {
        Log.v(LOG_TAG, "getSortedIncompleteDetailedToDoItemsAsArrayList(Context context) executed.");

        ContentResolver contentResolver = context.getContentResolver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingByColumnNameOption
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER,
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<DetailedToDoItem> incompleteDetailedToDoItemsArrayList = new ArrayList<>();

        String selection = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE + " = ?";
        String[] selectionArgs = new String[]{"1"};
        String orderBy;
        String projection[] = null;

        switch (sortingByColumnNameOption) {
            case DatabaseHelper.SORT_BY_DEADLINE:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE;
                break;
            case DatabaseHelper.SORT_BY_TIME_ADDED:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            case DatabaseHelper.SORT_BY_TITLE:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE;
                break;
            default:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY;
                break;
        }

        switch (sortingAscOrDesc) {
            case DatabaseHelper.SORTING_STANDARD_ASC:
                orderBy += DatabaseHelper.SORTING_ASC;
                break;
            default:
                orderBy += DatabaseHelper.SORTING_DESC;
                break;
        }

        Cursor cursor = contentResolver.query(LivingBetterContract.DetailedToDoItemEntry.CONTENT_URI, projection, selection, selectionArgs, orderBy);

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE));
                    int priority = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY));
                    long deadline = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE));
                    long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE));
                    String detailDescription = cursor.getString(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DESCRIPTION));
                    int category = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_CATEGORY));
                    int completionStatusCode = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE));
                    DetailedToDoItem detailedToDoListItem = new DetailedToDoItem(title, priority, detailDescription, itemDateAndTimeCreated, deadline, category, completionStatusCode);
                    incompleteDetailedToDoItemsArrayList.add(detailedToDoListItem);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(incompleteDetailedToDoItemsArrayList, "getSortedIncompleteDetailedToDoItemsAsArrayList(), GeneralHelper");

        return incompleteDetailedToDoItemsArrayList;
    }

    public static ArrayList<DetailedToDoItem> getSortedCompletedDetailedToDoItemsAsArrayList(Context context) {
        Log.v(LOG_TAG, "getSortedCompletedDetailedToDoItemsAsArrayList(Context context) executed.");
        ContentResolver contentResolver = context.getContentResolver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingByColumnNameOption
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER,
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<DetailedToDoItem> completedDetailedToDoItemsArrayList = new ArrayList<>();

        String selection = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE + " = ?";
        String[] selectionArgs = new String[]{"2"};
        String orderBy;

        String projection[] = null;

        switch (sortingByColumnNameOption) {
            case DatabaseHelper.SORT_BY_DEADLINE:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE;
                break;
            case DatabaseHelper.SORT_BY_TIME_ADDED:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            case DatabaseHelper.SORT_BY_TITLE:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE;
                break;
            default:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY;
                break;
        }

        switch (sortingAscOrDesc) {
            case DatabaseHelper.SORTING_STANDARD_ASC:
                orderBy += DatabaseHelper.SORTING_ASC;
                break;
            default:
                orderBy += DatabaseHelper.SORTING_DESC;
                break;
        }

        Cursor cursor = contentResolver.query(LivingBetterContract.DetailedToDoItemEntry.CONTENT_URI, projection, selection, selectionArgs, orderBy);

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE));
                    int priority = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_PRIORITY));
                    long deadline = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE));
                    long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE));
                    String detailDescription = cursor.getString(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DESCRIPTION));
                    int category = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_CATEGORY));
                    int completionStatusCode = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE));
                    DetailedToDoItem detailedToDoListItem = new DetailedToDoItem(title, priority, detailDescription, itemDateAndTimeCreated, deadline, category, completionStatusCode);
                    completedDetailedToDoItemsArrayList.add(detailedToDoListItem);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        GeneralHelper.displayTitleOfAllToDoItemsInAnArrayList(completedDetailedToDoItemsArrayList, "getSortedCompletedDetailedToDoItemsAsArrayList(), GeneralHelper");

        return completedDetailedToDoItemsArrayList;
    }

    public static ArrayList<SimpleToDoItem> getSortedCompletedSimpleToDoItemsAsArrayList(Context context) {
        Log.v(LOG_TAG, "getSortedCompletedSimpleToDoItemsAsArrayList(Context context) executed.");

        ContentResolver contentResolver = context.getContentResolver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingByColumnNameOption
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER,
                LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_DEADLINE);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<SimpleToDoItem> completedSimpleToDoItemsArrayList = new ArrayList<>();

        String selection = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE + " = ?";
        String[] selectionArgs = new String[]{"2"};
        String orderBy;

        String projection[] = null;

        switch (sortingByColumnNameOption) {
            case DatabaseHelper.SORT_BY_DEADLINE:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            case DatabaseHelper.SORT_BY_TIME_ADDED:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            case DatabaseHelper.SORT_BY_TITLE:
                orderBy = LivingBetterContract.DetailedToDoItemEntry.DETAILED_TODO_COLUMN_TITLE;
                break;
            default:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
        }

        switch (sortingAscOrDesc) {
            case DatabaseHelper.SORTING_STANDARD_ASC:
                orderBy += DatabaseHelper.SORTING_ASC;
                break;
            default:
                orderBy += DatabaseHelper.SORTING_DESC;
                break;
        }

        Cursor cursor = contentResolver.query(LivingBetterContract.SimpleToDoItemEntry.CONTENT_URI, projection, selection, selectionArgs, orderBy);

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE));
                    long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE));
                    int completionStatusCode = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE));
                    SimpleToDoItem simpleToDoItem = new SimpleToDoItem(title, itemDateAndTimeCreated, completionStatusCode);
                    completedSimpleToDoItemsArrayList.add(simpleToDoItem);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }

        return completedSimpleToDoItemsArrayList;
    }

    public static ArrayList<SimpleToDoItem> getSortedIncompleteSimpleToDoItemsAsArrayList(Context context) {
        Log.v(LOG_TAG, "getSortedIncompleteSimpleToDoItemsAsArrayList(Context context) executed");

        ContentResolver contentResolver = context.getContentResolver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingWay
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER,
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<SimpleToDoItem> incompleteSimpleToDoItemsArrayListModel = new ArrayList<>();

        String selection = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE + " = ?";
        String[] selectionArgs = new String[]{"1"};

        String[] projection = null;

        String orderBy;
        switch (sortingWay) {
            case DatabaseHelper.SORT_BY_TITLE:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            default:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
        }

        switch (sortingAscOrDesc) {
            case DatabaseHelper.SORTING_STANDARD_ASC:
                orderBy += DatabaseHelper.SORTING_ASC;
                break;
            default:
                orderBy += DatabaseHelper.SORTING_DESC;
                break;
        }

        Cursor cursor = contentResolver.query(LivingBetterContract.SimpleToDoItemEntry.CONTENT_URI, projection, selection, selectionArgs, orderBy);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE));
                    Log.v(LOG_TAG, "title, getSortedIncompleteSimpleToDoItemsAsArrayList(Context context): " + title);
                    long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE));
                    int completionStatusCode = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE));
                    SimpleToDoItem simpleToDoListItem = new SimpleToDoItem(title, itemDateAndTimeCreated, completionStatusCode);
                    incompleteSimpleToDoItemsArrayListModel.add(simpleToDoListItem);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }

        return incompleteSimpleToDoItemsArrayListModel;
    }

    /*public static ArrayList<SimpleToDoItem> getIncompleteSortedSimpleToDoItemsAsArrayList(Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingWay
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER,
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<SimpleToDoItem> incompleteSimpleToDoItemsArrayListModel = new ArrayList<>();

        String selection = ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE;
        String[] selectionArgs = new String[]{"1"};

        String[] projection = {
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE,
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE,
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_PRIORITY,
                ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE
        };

        String orderBy;
        switch (sortingWay) {
            case DatabaseHelper.SORT_BY_PRIORITY:
                orderBy = ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_PRIORITY;
                break;
            case DatabaseHelper.SORT_BY_TITLE:
                orderBy = ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            default:
                orderBy = ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
        }

        switch (sortingAscOrDesc) {
            case DatabaseHelper.SORTING_STANDARD_ASC:
                orderBy += DatabaseHelper.SORTING_ASC;
                break;
            default:
                orderBy += DatabaseHelper.SORTING_DESC;
                break;
        }

        Cursor cursor = contentResolver.query(ToDoListProviderContract.SimpleToDoItemEntry.CONTENT_URI, projection, selection, selectionArgs, orderBy);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex(ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE));
                    long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE));
                    int completionStatusCode = cursor.getInt(cursor.getColumnIndex(ToDoListProviderContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE));
                    SimpleToDoItem simpleToDoListItem = new SimpleToDoItem(title, itemDateAndTimeCreated, completionStatusCode);
                    incompleteSimpleToDoItemsArrayListModel.add(simpleToDoListItem);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }

        return incompleteSimpleToDoItemsArrayListModel;
    }*/

    public static ArrayList<SimpleToDoItem> getCompletedSortedSimpleToDoItemsAsArrayList(Context context) {

        ContentResolver contentResolver = context.getContentResolver();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortingWay
                = sharedPreferences.getString(GeneralConstants.TODOITEMS_SORTING_STANDARD_SHAREDPREFERENCE_IDENTIFIER,
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE);
        int sortingAscOrDesc
                = sharedPreferences.getInt(GeneralConstants.TODOITEMS_SORTING_ASC_OR_DESC_SHAREDPREFERNECE_IDENTIFIER,
                DatabaseHelper.SORTING_STANDARD_DESC);

        ArrayList<SimpleToDoItem> completedSimpleToDoItemsArrayListModel = new ArrayList<>();

        String selection = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE;
        String[] selectionArgs = new String[]{"2"};

        String[] projection = {
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE,
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE,
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_PRIORITY,
                LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE
        };

        String orderBy;
        switch (sortingWay) {
            case DatabaseHelper.SORT_BY_PRIORITY:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_PRIORITY;
                break;
            case DatabaseHelper.SORT_BY_TITLE:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
            default:
                orderBy = LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE;
                break;
        }

        switch (sortingAscOrDesc) {
            case DatabaseHelper.SORTING_STANDARD_ASC:
                orderBy += DatabaseHelper.SORTING_ASC;
                break;
            default:
                orderBy += DatabaseHelper.SORTING_DESC;
                break;
        }

        Cursor cursor = contentResolver.query(LivingBetterContract.SimpleToDoItemEntry.CONTENT_URI, projection, selection, selectionArgs, orderBy);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_TITLE));
                    long itemDateAndTimeCreated = cursor.getLong(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE));
                    int completionStatusCode = cursor.getInt(cursor.getColumnIndex(LivingBetterContract.SimpleToDoItemEntry.SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE));
                    SimpleToDoItem simpleToDoListItem = new SimpleToDoItem(title, itemDateAndTimeCreated, completionStatusCode);
                    completedSimpleToDoItemsArrayListModel.add(simpleToDoListItem);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }

        return completedSimpleToDoItemsArrayListModel;
    }


    public static boolean isNetworkAvailable(Context context) {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }
}
