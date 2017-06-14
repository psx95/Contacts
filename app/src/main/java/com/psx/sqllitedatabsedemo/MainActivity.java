package com.psx.sqllitedatabsedemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.psx.sqllitedatabsedemo.Adapters.AllContactsAdapter;
import com.psx.sqllitedatabsedemo.Helper.DatabaseHandler;
import com.psx.sqllitedatabsedemo.Helper.TableMetaData;
import com.psx.sqllitedatabsedemo.Model.Contact;

import java.util.ArrayList;
import java.util.List;

import static com.psx.sqllitedatabsedemo.Helper.DatabaseHandler.DATABASE_VERSION;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FrameLayout frameLayoutContainer;
    private Context context;
    private RecyclerView recyclerView_all_contacts;
    private AllContactsAdapter allContactsAdapter;
    private List<Contact> allContactList = new ArrayList<>();
    private DatabaseHandler databaseHandler;
    private FloatingActionButton floatingActionButton;
    private ActionBar actionBar;
    private int prevId = -99;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id  = item.getItemId();
            if (id != prevId) {
                prevId = id;
                switch (id) {
                    case R.id.navigation_home:
                        frameLayoutContainer.removeAllViews();
                        LayoutInflater.from(context).inflate(R.layout.all_contacts, frameLayoutContainer, true);
                        recyclerView_all_contacts = (RecyclerView) frameLayoutContainer.findViewById(R.id.contacts_holder);
                        floatingActionButton = (FloatingActionButton) frameLayoutContainer.findViewById(R.id.floatingActionButton);
                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(context, "Click Recieved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, AddNewContact.class);
                                startActivity(intent);
                            }
                        });
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView_all_contacts.setLayoutManager(linearLayoutManager);
                        populateList();
                        checkIfContactsPresent();
                        allContactsAdapter = new AllContactsAdapter(context, allContactList);
                        recyclerView_all_contacts.setAdapter(allContactsAdapter);
                        return true;
                    case R.id.navigation_dashboard:
                        frameLayoutContainer.removeAllViews();
                        LayoutInflater.from(context).inflate(R.layout.favourite_contacts, frameLayoutContainer, true);
                        return true;
                    case R.id.navigation_notifications:
                        frameLayoutContainer.removeAllViews();
                        LayoutInflater.from(context).inflate(R.layout.linked_contacts, frameLayoutContainer, true);
                        return true;
                }
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setTitle("Contacts");
        frameLayoutContainer = (FrameLayout) findViewById(R.id.content);
        LayoutInflater.from(context).inflate(R.layout.all_contacts,frameLayoutContainer,true);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // get the instance of DatabaseHandler for all the databse related options
        frameLayoutContainer.removeAllViews();
        databaseHandler = new DatabaseHandler(context,DatabaseHandler.DATABASE_NAME,null, DATABASE_VERSION);
        LayoutInflater.from(context).inflate(R.layout.all_contacts,frameLayoutContainer,true);
        recyclerView_all_contacts = (RecyclerView) frameLayoutContainer.findViewById(R.id.contacts_holder);
        floatingActionButton = (FloatingActionButton) frameLayoutContainer.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Click received",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,AddNewContact.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_all_contacts.setLayoutManager(linearLayoutManager);
        populateList();
        checkIfContactsPresent();
        allContactsAdapter = new AllContactsAdapter(context,allContactList);
        recyclerView_all_contacts.setAdapter(allContactsAdapter);
    }

    @Override
    protected void onResume() {
        Log.d("DEBUG","OnResume Called");
        populateList();
        allContactsAdapter = new AllContactsAdapter(context, allContactList);
        recyclerView_all_contacts.setAdapter(allContactsAdapter);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                //databaseHandler.renameDatabaseColumns("contacts","firstname","first"
                /*DatabaseHandler databaseHandler = new DatabaseHandler(context,DatabaseHandler.DATABASE_NAME,null,DatabaseHandler.DATABASE_VERSION++);
                databaseHandler.getReadableDatabase();*/
                DatabaseHandler databaseHandler = new DatabaseHandler(context,DatabaseHandler.DATABASE_NAME,null, DATABASE_VERSION);
                SQLiteDatabase db = databaseHandler.getWritableDatabase();
                Log.d("Main Activity ","clicked "+db.getVersion());
                String q = "PRAGMA user_version";
                Cursor cursor = db.rawQuery(q,null);
                if (cursor.moveToFirst()) {
                    while ( !cursor.isAfterLast() ) {
                        Log.d("Main Activity", cursor.getInt(0) + " this is the current user_ version after update " + DATABASE_VERSION + "This is the database version as defined");
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                db.close();
                TableMetaData tableMetaData = databaseHandler.info(databaseHandler.getReadableDatabase());
                databaseHandler.printMetaData(tableMetaData);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateList (){
        // fetch all the data from the database and add it to the list
        allContactList = databaseHandler.getAllContacts();
        //databaseHandler.addContact(new Contact("Pranav","Sharma","pranav.ps95@hotmail.com","8376003776"));
        for (Contact cn : allContactList) {
            String log = "Id: " + cn.getId() + " ,Name: " + cn.getFirst_name() + " ,Phone: " + cn.getPhone_number();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }

    private void checkIfContactsPresent (){
        if (allContactList.size() == 0){
            frameLayoutContainer.findViewById(R.id.no_contact_found).setVisibility(View.VISIBLE);
        }
        else {
            frameLayoutContainer.findViewById(R.id.no_contact_found).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        Log.d("DEBUG","onPause called");
        super.onPause();
    }
}
