package tek.first.livingbetter.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Leon on 9/30/2015.
 */
public class LivingBetterContract {

    public static final String CONTENT_AUTHORITY
            = "tek.first.livingbetter.LivingBetterProvider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class HabitInfoEntry implements BaseColumns {

        public static final String TABLE_NAME = "habit_table";

        public static final String COLUMN_ID = "habit_id";
        public static final String COLUMNE_NAME = "habit_name";
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
    }

    public static final class UserInfo implements BaseColumns {
    }

    public static final class SimpleToDoItemEntry implements BaseColumns {
    }

    public static final class DetailedToDoItemEntry implements BaseColumns {
    }

}
