package tek.first.livingbetter.helper;

import android.provider.BaseColumns;

/**
 * Created by stan on 8/18/15.
 */
public class UserContract {



    public static final String PATH_USER = "user";


    public static final class UserEntry implements BaseColumns {

        public static final String TABLE_NAME = "user";


        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_USERNAME = "username";
        public static final String COLUMN_USER_PASSWORD = "password";
        public static final String COLUMN_USER_AGE = "age";
        public static final String COLUMN_USER_STREET = "street";
        public static final String COLUMN_USER_COUNTRY = "country";
        public static final String COLUMN_USER_CITY = "city";


    }
}
