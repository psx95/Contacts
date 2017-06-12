package com.psx.sqllitedatabsedemo.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
        // execSQL is used for sql statements that do not return any data
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
    public void deleteContact (Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });
        db.close();
    }

    public void renameDatabaseColumns (String table_name, String old_column_name, String new_column_name){
        String alter_table_query = "ALTER TABLE "+table_name+" RENAME TO "+table_name+"temp";
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(alter_table_query);
        TableMetaData NewtableMetaData = createNewDatabaseTable(database,table_name,old_column_name,new_column_name);
        copyOldContentsToNewTable(table_name,NewtableMetaData);
        dropOldTable(database,table_name);
    }

    public void dropOldTable (SQLiteDatabase database, String table_name){
        String dropTable = "DROP TABLE "+table_name+"temp";
        database.execSQL(dropTable);
    }

    public void copyOldContentsToNewTable (String table_name, TableMetaData metaData){
        String copy_query = "INSERT INTO "+ table_name + "( ";
        String table_columns = "";
        for (int i = 0; i < metaData.fieldNames.size();i++){
            table_columns+=metaData.fieldNames.get(i);
            if (i!=metaData.fieldNames.size()-1)
                table_columns+=", ";
        }
        copy_query+=table_columns+" ) SELECT "+table_columns+" FROM "+table_name+"temp";
    }

    public TableMetaData createNewDatabaseTable (SQLiteDatabase database, String table_name, String old_column_name, String new_column_name){
        // get the info about current database table
        String table_meta_data_query = "PRAGMA table_info("+table_name+")";
        Cursor cursor = database.rawQuery(table_meta_data_query,null);
        TableMetaData metaData = extractTableInfo(cursor,old_column_name,new_column_name);
        String new_table_creation_query = "CREATE TABLE "+table_name+" ( ";
        String table_column_info= "";
        for (int i =0; i < metaData.getFieldNames().size(); i++){
            table_column_info+=metaData.getFieldNames().get(i)+" ";
            table_column_info+=metaData.getFieldtype().get(i)+" ";
            table_column_info+=metaData.getPrimaryKey().get(i)+" ";
            table_column_info+=metaData.getNotNull().get(i)+" ";
            table_column_info+=metaData.getDfltValue().get(i)+" ";
            if (!(i == metaData.getFieldNames().size()-1))
                table_column_info+=",";
            Log.d(TAG,"column info "+table_column_info);
        }
        new_table_creation_query += table_column_info + ")";
        database.execSQL(new_table_creation_query);
        return metaData;
    }

    public TableMetaData extractTableInfo (Cursor cursor, String old_column_name, String new_column_name) {
        TableMetaData tableMetaData = new TableMetaData();
        List<String>  fieldNames = new ArrayList<>();
        List<String> tableType = new ArrayList<>();
        List<String> tableNotNullValue = new ArrayList<>();
        List<String> tableDefaultValue = new ArrayList<>();
        List<String> tablePrimaryKey = new ArrayList<>();
        try{
            int field_nameIndex = cursor.getColumnIndexOrThrow("name");
            int variable_typeIndex = cursor.getColumnIndexOrThrow("type");
            int notNullValueIndex = cursor.getColumnIndexOrThrow("notnull");
            int defaultValueIndex = cursor.getColumnIndexOrThrow("dflt_value");
            int primaryKeyIndex = cursor.getColumnIndexOrThrow("pk");
            while (cursor.moveToNext()) {
                if (cursor.getString(field_nameIndex).equals(old_column_name))
                    fieldNames.add(new_column_name);
                else
                    fieldNames.add(cursor.getString(field_nameIndex));
                tableType.add(cursor.getString(variable_typeIndex));
                if (cursor.getString(notNullValueIndex).equals("1"))
                    tableNotNullValue.add("NOT NULL");
                else
                    tableNotNullValue.add("");
                if (!cursor.getString(defaultValueIndex).equals(""))
                    tableDefaultValue.add("DEFAULT "+cursor.getString(defaultValueIndex));
                if (cursor.getString(primaryKeyIndex).equals("1"))
                    tablePrimaryKey.add("PRIMARY KEY");
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        } finally {
            cursor.close();
            tableMetaData.setDfltValue(tableDefaultValue);
            tableMetaData.setNotNull(tableNotNullValue);
            tableMetaData.setFieldtype(tableType);
            tableMetaData.setPrimaryKey(tablePrimaryKey);
            tableMetaData.setFieldName(fieldNames);
            return tableMetaData;
        }
    }
}
