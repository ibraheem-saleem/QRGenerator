package pmru.covid19.qrCodes.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import pmru.covid19.qrCodes.Model.DataModel;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DataBaseVersion=1;
    private static final String  DataBaseName="PmruQrCodeSqliteDatabase";

    private static final String TABLE_USED_QR_CODES = "usedQrCodes";


    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_USED_QR_CODES = "used_qr_number";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DataBaseName, null, DataBaseVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USED_QR_CODES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_USED_QR_CODES + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USED_QR_CODES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
  public   void addUsedQrCodes(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, dataModel.getId()); // Qr Code  id
        values.put(KEY_NAME, dataModel.getName()); // Qr Code District Name
        values.put(KEY_USED_QR_CODES, dataModel.getUsed_qr_number()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_USED_QR_CODES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    public DataModel getDistById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USED_QR_CODES, new String[] { KEY_ID,
                        KEY_NAME, KEY_USED_QR_CODES }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DataModel contact = new DataModel((cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }
}
