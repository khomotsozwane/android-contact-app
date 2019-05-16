package blubnanacom.androidcontactappassisgnment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.Toast;

public class ContactDatabaseOperations extends SQLiteOpenHelper {

    public static final int database_version = 1;
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String TAG = "DATABASE_OPERATIONS";

    //SQLite Create Query String
    public String CREATE_QUERY = "CREATE TABLE " + TableData.TableInfo.TABLE_NAME + "("
            + TableData.TableInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TableData.TableInfo.CONTACT_NAME + TEXT_TYPE + COMMA_SEP
            + TableData.TableInfo.CONTACT_EMAIL + TEXT_TYPE + COMMA_SEP
            + TableData.TableInfo.CONTACT_PHONE + TEXT_TYPE + COMMA_SEP
            + TableData.TableInfo.CONTACT_COLOR + TEXT_TYPE + COMMA_SEP
            + TableData.TableInfo.CONTACT_IMAGE + TEXT_TYPE  + ")";


    public ContactDatabaseOperations(Context context){
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        Log.d(TAG, "Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
        Log.d(TAG, "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableData.TableInfo.TABLE_NAME );
        onCreate(sqLiteDatabase);
    }

    public int countDbItems(ContactDatabaseOperations dbOperations){
        SQLiteDatabase db = dbOperations.getReadableDatabase();
        String SQL_QUERY = "SELECT * FROM " + TableData.TableInfo.TABLE_NAME;
        Cursor cursor = db.rawQuery(SQL_QUERY, null);
        int count = cursor.getCount();
        return count;
    }

    public long addContactToDB(ContactDatabaseOperations dbOperations, int id, String name, String email,
                            String phone, String color, String image){
        SQLiteDatabase db = dbOperations.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo._ID, id);
        cv.put(TableData.TableInfo.CONTACT_NAME, name);
        cv.put(TableData.TableInfo.CONTACT_EMAIL, email);
        cv.put(TableData.TableInfo.CONTACT_PHONE, phone);
        cv.put(TableData.TableInfo.CONTACT_COLOR,color);
        cv.put(TableData.TableInfo.CONTACT_IMAGE, image);

        long add = db.insert(TableData.TableInfo.TABLE_NAME, null, cv);

        Log.d(TAG, "Data Added to Table " + TableData.TableInfo.TABLE_NAME);

        return add;
    }


    public long removeContactFromDB(ContactDatabaseOperations dbOperations, int id){
        SQLiteDatabase db = dbOperations.getWritableDatabase();
        String SQL_QUERY =  TableData.TableInfo._ID  + "=" + "?";

        long delete = db.delete(TableData.TableInfo.TABLE_NAME, SQL_QUERY, new String[]{String.valueOf(id)});

        Log.d(TAG, "Data Removed from Table " + TableData.TableInfo.TABLE_NAME );

        return delete;
    }


    public long updateContactById(ContactDatabaseOperations dbOperations, int id, String name, String email,
                            String phone, String color, String image){
        SQLiteDatabase db = dbOperations.getWritableDatabase();
        String SQL_QUERY = TableData.TableInfo._ID + "=" + "?";

        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.CONTACT_NAME, name);
        cv.put(TableData.TableInfo.CONTACT_EMAIL, email);
        cv.put(TableData.TableInfo.CONTACT_PHONE, phone);
        cv.put(TableData.TableInfo.CONTACT_COLOR,color);
        cv.put(TableData.TableInfo.CONTACT_IMAGE, image);

        long update = db.update(TableData.TableInfo.TABLE_NAME, cv, SQL_QUERY, new String[]{String.valueOf(id)});
        Log.d(TAG, "Data Updated to Table " + TableData.TableInfo.TABLE_NAME);

        return update;
    }

    public void clearMockData(ContactDatabaseOperations dbOperations){
        SQLiteDatabase db = dbOperations.getWritableDatabase();
        String DELETE_SQL_QUERY = "DELETE FROM " + TableData.TableInfo.TABLE_NAME;
        db.rawQuery(DELETE_SQL_QUERY,null);
    }

    public void importNetworkData(ContactDatabaseOperations dbOperations, int id, String name, String email,
                            String phone, String color, String image){
        SQLiteDatabase db = dbOperations.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo._ID, id);
        cv.put(TableData.TableInfo.CONTACT_NAME, name);
        cv.put(TableData.TableInfo.CONTACT_EMAIL, email);
        cv.put(TableData.TableInfo.CONTACT_PHONE, phone);
        cv.put(TableData.TableInfo.CONTACT_COLOR,color);
        cv.put(TableData.TableInfo.CONTACT_IMAGE, image);

        long load = db.insert(TableData.TableInfo.TABLE_NAME, null, cv);
        Log.d(TAG, "Imported remote data to Table: " + TableData.TableInfo.TABLE_NAME);
    }

    public Cursor getAllContactData(ContactDatabaseOperations dbOperations){
        SQLiteDatabase db = dbOperations.getReadableDatabase();

        String[] projection = {
                TableData.TableInfo._ID,
                TableData.TableInfo.CONTACT_NAME,
                TableData.TableInfo.CONTACT_EMAIL,
                TableData.TableInfo.CONTACT_PHONE,
                TableData.TableInfo.CONTACT_COLOR,
                TableData.TableInfo.CONTACT_IMAGE
        };

        Cursor mCursor = db.query(TableData.TableInfo.TABLE_NAME, projection,
                null, null, null, null, null);

        if( mCursor!= null){
            mCursor.moveToNext();
        }

        return mCursor;

    }

    public Cursor getContactById(ContactDatabaseOperations dbOperations, int id){
        SQLiteDatabase db = dbOperations.getReadableDatabase();

        Log.d(TAG, "Get Contact By Id " + id);

        String sql_query = "SELECT * FROM "
                + TableData.TableInfo.TABLE_NAME + " WHERE "
                + TableData.TableInfo._ID + " =" + "?";

        Cursor mCursor = db.rawQuery(sql_query, new String[]{String.valueOf(id)});
        Log.d(TAG, "Cursor returned: " + mCursor);
        return mCursor;
    }

    public Cursor getContactByName(ContactDatabaseOperations dbOperations, String[] name){
        SQLiteDatabase db = dbOperations.getReadableDatabase();

        String sql_query = "SELECT * FROM "
                + TableData.TableInfo.TABLE_NAME + " WHERE "
                + TableData.TableInfo._ID + " = " + " ? ";

        Cursor mCursor = db.rawQuery(sql_query, name);
        Log.d(TAG, "Return Query from " + TableData.TableInfo.TABLE_NAME + " for " + name.toString() + " contact!");
        return mCursor;
    }

}
