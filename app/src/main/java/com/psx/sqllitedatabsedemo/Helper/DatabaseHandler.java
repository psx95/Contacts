package com.psx.sqllitedatabsedemo.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.psx.sqllitedatabsedemo.Model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav on 21-03-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // static values for the database
    // Database version
    private static int DATABASE_VERSION = 2
            ;
    // Database Name
    public static final String DATABASE_NAME = "ContactsManager";
    // Database Table
    private static final String DATABASE_TABLE = "contacts";

    // table column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME_FIRST = "firstname";
    private static final String KEY_NAME_LAST = "lastname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NO = "phone_number";
    private static final String TAG = "DATABASE HANDLER";

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
            doOnUpgrade(sqLiteDatabase);

        // execSQL is used for sql statements that do not return any data
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        Log.d(TAG,"onUpgade");
        if (new_version > 1) {
            doOnUpgrade(sqLiteDatabase);
        }
        onCreate(sqLiteDatabase);
    }

    public void doOnUpgrade (SQLiteDatabase sqLiteDatabase) {
        TableMetaData tableMetaData = info(sqLiteDatabase);
        String alter_table_query = "ALTER TABLE " + DATABASE_TABLE + " RENAME TO temp";
        String create_table_query = "CREATE TABLE " + DATABASE_TABLE + " ("+ extractQueryFromMetaData(tableMetaData,"lastname","last")+")";
        Log.d(TAG,create_table_query+ " This is the query created");
        String columnNamesNew = generateColumnNamesFromMetaData(tableMetaData,"lastname","last");
        String columnNamesOld = generateColumnNamesFromMetaData(tableMetaData,"lastname","lastname");
        String copy_query = "INSERT INTO " + DATABASE_TABLE + "(" + columnNamesNew +") SELECT " + columnNamesOld + " FROM  temp";
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL(alter_table_query);
            Log.d(TAG,sqLiteDatabase.getPath());
            sqLiteDatabase.execSQL(create_table_query);
            sqLiteDatabase.execSQL(copy_query);
            sqLiteDatabase.execSQL("DROP TABLE temp");
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
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
    public void deleteContact (Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

    public TableMetaData info(SQLiteDatabase sqLiteDatabase){
        String table_meta_data_query = "PRAGMA table_info(" + DATABASE_TABLE + ")";
        Cursor cursor = sqLiteDatabase.rawQuery(table_meta_data_query, null);
        // cursor must have all the info about the data returned by the query
        List<String> columnNames = new ArrayList<>();
        List<String> columnDataTypes = new ArrayList<>();
        List<String> columnPrimaryKey = new ArrayList<>();
        List<String> columnDefaultValue = new ArrayList<>();
        List<String> columnNotNull = new ArrayList<>();
        TableMetaData tableMetaData = new TableMetaData();
        if (cursor.moveToFirst()){
            do{
                Log.d(TAG,cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4)+" "+cursor.getString(5));
                columnNames.add(cursor.getString(1));
                columnDataTypes.add(cursor.getString(2));
                columnNotNull.add(cursor.getString(3));
                columnDefaultValue.add(cursor.getString(4));
                columnPrimaryKey.add(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        tableMetaData.setColumnNames(columnNames);
        tableMetaData.setCoulumnDataTypes(columnDataTypes);
        tableMetaData.setColumnDefaultValue(columnDefaultValue);
        tableMetaData.setColumnNotNull(columnNotNull);
        tableMetaData.setColumnPrimaryKey(columnPrimaryKey);
        printMetaData(tableMetaData);
        return tableMetaData;
    }

    public  void iterateThroughAList (List<String> list) {
        for (String s : list) {
            Log.d(TAG+"DATA",s+"");
        }
    }

    public void printMetaData (TableMetaData metaData) {
        iterateThroughAList(metaData.columnNames);
        iterateThroughAList(metaData.coulumnDataTypes);
        iterateThroughAList(metaData.columnNotNull);
        iterateThroughAList(metaData.columnDefaultValue);
        iterateThroughAList(metaData.columnPrimaryKey);
    }

    public String extractQueryFromMetaData (TableMetaData metaData, String old_col_name, String new_col_name){
        int size = metaData.getColumnNames().size();
        Log.d(TAG, "size of notnull "+metaData.getColumnNotNull().size());
        Log.d(TAG, "size of names "+size);
        Log.d(TAG, "size of primary keys"+metaData.getColumnPrimaryKey().size());
        Log.d(TAG, "size of default values "+metaData.getColumnDefaultValue().size());
        Log.d(TAG, "size of data types "+metaData.getCoulumnDataTypes().size());
        String extracted_query = "";
        String curr_col_name;
        for (int i = 0; i<size; i++){
            curr_col_name = metaData.getColumnNames().get(i);
            if (curr_col_name.equals(old_col_name))
                curr_col_name = new_col_name;
            extracted_query+= curr_col_name+" "; // id
            extracted_query+= metaData.getCoulumnDataTypes().get(i)+" ";
            if (metaData.getColumnDefaultValue()!= null){
                if (metaData.getColumnDefaultValue().get(i) != null)
                    extracted_query+= "DEFAULT "+metaData.getColumnDefaultValue().get(i);
            }
            if (metaData.getColumnNotNull() != null) {
                if (Integer.parseInt(metaData.getColumnNotNull().get(i)) == 1)
                    extracted_query += "NOT NULL ";
            }
            if (Integer.parseInt(metaData.getColumnPrimaryKey().get(i)) == 1)
                extracted_query+= "PRIMARY KEY ";
            if (i != size-1)
                extracted_query+= ", ";
        }
        return extracted_query;
    }

    public String generateColumnNamesFromMetaData (TableMetaData metaData, String old_col_name, String new_col_name){
        String column_names = "";
        String curr_name;
        List<String> columnNames = metaData.getColumnNames();
        for (int i = 0; i < columnNames.size(); i++){
            if (columnNames.get(i).equals(old_col_name))
                curr_name = new_col_name;
            else
                curr_name = columnNames.get(i);
            if (i!=columnNames.size()-1)
                column_names+= curr_name+", ";
            else
                column_names+= curr_name;
        }
        return column_names;
    }

}
