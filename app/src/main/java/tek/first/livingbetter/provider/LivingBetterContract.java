package tek.first.livingbetter.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Leon on 9/30/2015.
 */
public class LivingBetterContract {

    public static final String CONTENT_AUTHORITY
            = "tek.first.livingbetter.LivingBetterProvider";
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class HabitInfoEntry implements BaseColumns {

        public static final String TABLE_NAME = "habit_table";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_ID = "habit_id";
        public static final String COLUMN_NAME = "habit_name";
        public static final String COLUMN_RATING = "habit_rating";
        public static final String COLUMN_NUMBER_OF_REVIEWS = "habit_number_of_reviews";
        public static final String COLUMN_CATEGORY = "habit_category";
        public static final String COLUMN_IMAGE_URL = "habit_image_url";
        public static final String COLUMN_SNIPPET_TEXT = "habit_snippet_text";
        public static final String COLUMN_ADDRESS = "habit_address";
        public static final String COLUMN_PHONE_NUMBER = "habit_phone_number";
        public static final String COLUMN_MOBILE_URL = "habit_mobile_url";
        public static final String COLUMN_LATITUDE = "habit_latitude";
        public static final String COLUMN_LONGITUDE = "habit_longitude";
        public static final String COLUMN_DISTANCE = "habit_distance";

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class UserEntry implements BaseColumns {

        public static final String TABLE_NAME = "user_table";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_USERNAME = "username";
        public static final String COLUMN_USER_PASSWORD = "password";
        public static final String COLUMN_USER_AGE = "age";
        public static final String COLUMN_USER_STREET = "street";
        public static final String COLUMN_USER_COUNTRY = "country";
        public static final String COLUMN_USER_CITY = "city";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTNET_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }


    public static final class SimpleToDoItemEntry implements BaseColumns {

        public static String TABLE_NAME = "simple_todolist_table";
        public static String SIMPLE_TODO_ITEM_COLUMN_ID = "simple_todolist_id";
        public static String SIMPLE_TODO_ITEM_COLUMN_TITLE = "simple_todolist_title";
        public static String SIMPLE_TODO_ITEM_COLUMN_PRIORITY = "simple_todo_item_priority";
        public static String SIMPLE_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE = "simple_todo_item_created_time_and_date";
        public static String SIMPLE_TODO_ITEM_COLUMN_COMPLETION_STATUS_CODE = "simple_todo_item_completion_status_code";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTNET_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class DetailedToDoItemEntry implements BaseColumns {
        public static String TABLE_NAME = "detailed_todolist_table";
        public static String DETAILED_TODO_ITEM_COLUMN_ID = "detailed_todo_item_id";
        public static String DETAILED_TODO_COLUMN_TITLE = "detailed_todo_item_title";
        public static String DETAILED_TODO_COLUMN_DESCRIPTION = "detailed_todo_tem_description";
        public static String DETAILED_TODO_COLUMN_PRIORITY = "detailed_todo_item_priority";
        public static String DETAILED_TODO_COLUMN_DEADLINE = "detailed_todo_item_deadline";
        public static String DETAILED_TODO_ITEM_COLUMN_CREATED_TIME_AND_DATE = "detailed_todo_item_created_time_and_date";
        public static String DETAILED_TODO_COLUMN_CATEGORY = "detailed_todo_item_category";
        public static String DETAILED_TODO_COLUMN_PRICE = "detailed_todo_item_price";
        public static String DETAILED_TODO_COLUMN_COMPLETION_STATUS_CODE = "detailed_todo_item_completion_status_code";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
    }
}
