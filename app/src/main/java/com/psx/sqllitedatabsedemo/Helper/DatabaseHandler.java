package com.psx.sqllitedatabsedemo.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.psx.sqllitedatabsedemo.Model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav on 21-03-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // static values for the database
    // Database version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ContactsManager";
    // Database Table
    private static final String DATABASE_TABLE = "contacts";

    // table column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME_FIRST = "firstname";
    private static final String KEY_NAME_LAST = "lastname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NO = "phone_number";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public  DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME_FIRST + " TEXT, " + KEY_NAME_LAST + " TEXT, "
                + KEY_EMAIL + " TEXT, "
                + KEY_PHONE_NO + " TEXT " + ")";*/
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + DATABASE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME_FIRST + " TEXT, " + KEY_NAME_LAST + " TEXT, "+ KEY_EMAIL + " TEXT, "
                + KEY_PHONE_NO + " TEXT" + ");";

        // execute SQL
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(sqLiteDatabase);
    }

    //  THE CRUD OPERATIONS FOR THE DATABASE
    public void addContact (Contact contact){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_FIRST,contact.getFirst_name());
        values.put(KEY_NAME_LAST,contact.getLast_name());
        values.put(KEY_EMAIL,contact.getEmail_id());
        values.put(KEY_PHONE_NO,contact.getPhone_number());
        database.insert(DATABASE_TABLE,null,values);
        database.close();
    }
    public Contact getContact (int id){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(DATABASE_TABLE, new String[]{KEY_ID,KEY_NAME_FIRST,KEY_NAME_LAST,KEY_EMAIL,KEY_PHONE_NO}, KEY_ID +"=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4));
        return contact;
    }
    public List<Contact> getAllContacts (){
        List<Contact> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ DATABASE_TABLE;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        // loop through all the rows
        if (cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setFirst_name(cursor.getString(1));
                contact.setLast_name(cursor.getString(2));
                contact.setEmail_id(cursor.getString(3));
                contact.setPhone_number(cursor.getString(4));
                contactList.add(contact);
            }while (cursor.moveToNext());
        }
        return contactList;
    }
    public int getContactsCount ()
    {
        int count;
        String countQuery  = "SELECT * FROM "+ DATABASE_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery,null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int updateContact ()
    {
        return 0;
    }
    public void deleteContact (){
        return;
    }
}
