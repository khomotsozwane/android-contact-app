package blubnanacom.androidcontactappassisgnment;

import android.provider.BaseColumns;

public class TableData {
    public TableData(){}

    public static abstract class TableInfo implements BaseColumns{

        public static final String CONTACT_NAME = "contact_table";
        public static final String CONTACT_EMAIL = "contact_email";
        public static final String CONTACT_PHONE = "contact_phone";
        public static final String CONTACT_COLOR = "contact_color";
        public static final String CONTACT_IMAGE = "contact_image";

        public static final String TABLE_NAME = "contact_table";
        public static final String DATABASE_NAME = "contacts_db";
    }
}
