package com.example.fyp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreateContact extends AppCompatActivity {

    ListView contactList;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        //  Cursor cursor = getContacts();
        BottomNavigationView bottomNav = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setSelectedItemId(R.id.person);
        bottomNav.setSelectedItemId(R.id.settings);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Homepage.class));
                        return true;
                    case R.id.person:
                        startActivity(new Intent(getApplicationContext(), UserProfile.class));
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), Settings.class));
                        return true;
                }
                return false;
            }
        });
        contactList = (ListView) findViewById(R.id.contactList);
        getContacts();
       adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                 return getCursor(constraint.toString());

            }
        });

    }

    private /*void*/Cursor getContacts() {
        //check permissions
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            // create cursor and query the data
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            startManagingCursor(cursor);

            // data is a array of String type which is
            // used to store Number ,Names and id.
            String[] data = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID};
            int[] to = {android.R.id.text1, android.R.id.text2};
            // creation of adapter using SimpleCursorAdapter class
            adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, data, to);
            // Calling setAdaptor() method to set created adapter
            contactList.setAdapter(adapter);
            contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        }
      // return getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
      return getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},ContactsContract.Contacts._ID+"="+null,null,null);
     // return ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
       // return cursor;
        // Run query
       /* Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Phone.NUMBER, ContactsContract.Phone._ID};
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP+" = "+
                1  +" AND "+ ContactsContract.Contacts.HAS_PHONE_NUMBER +" = "+ 1;
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";
        return getContentResolver().query(uri, projection, selection, selectionArgs,
                sortOrder);*/
        //return getContentResolver().query();
    }

    private Cursor getCursor(String str) {
        if (str == null  ||  str.length () == 0)  {
            cursor = getContacts();
        }
       /* else {
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME,
                     ContactsContract.Contacts._ID};
            String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = " +
                    1 + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER + " = " + 1 + " AND " + ContactsContract.Contacts.DISPLAY_NAME + " like '" + str + "%'";
            String[] selectionArgs = null;
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                    + " COLLATE LOCALIZED ASC";
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    sortOrder);
        }*/
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
               startManagingCursor(cursor);
           }
       });

        // data is a array of String type which is
        // used to store Number ,Names and id.
        String[] data = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone._ID};
        int[] to = {android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, data, to);
        // creation of adapter using SimpleCursorAdapter class
        //adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, data, to);
        // Calling setAdaptor() method to set created adapter
        contactList.setAdapter(adapter);
        contactList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

        //search bar
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.search_menu, menu);
            MenuItem menuItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setQueryHint("Search Contacts");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                //called when user types in text and enters
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.getFilter().filter(query);
                    return true;
                }

                //called when user is just typing
                @Override
                public boolean onQueryTextChange(String newText) {

                    //not working
                    adapter.getFilter().filter(newText);
                    System.out.println("Typing");
                    return false;
                }
            });
            return super.onCreateOptionsMenu(menu);
        }
    }
