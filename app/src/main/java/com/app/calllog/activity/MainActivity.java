package com.app.calllog.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.app.calllog.R;
import com.app.calllog.database.DBHelper;
import com.app.calllog.model.Contact;


public class MainActivity extends Activity {

    Contact contact,contact2,contact3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contact = new Contact(1,"Akash Soni","1234567890","akki9326@gmail.com");

        DBHelper.getInstance(getApplicationContext()).insert(DBHelper.TABLE_CONTACT,contact);
        contact2 = new Contact(2,"Akash Soni2","1234567890","akki9326@gmail2.com");
        DBHelper.getInstance(getApplicationContext()).insert(contact2);
        contact3 = new Contact(2,"Akash Soni","1234567890","akki9326@gmail2.com");
        DBHelper.getInstance(getApplicationContext()).insertOrUpdate(getApplicationContext(),contact3);

        setContentView(R.layout.activity_main);
        if(savedInstanceState==null)
        {
//            android.app.Fragment contactsFragment = new ContactsListFrag();
//            getFragmentManager().beginTransaction().add(R.id.container,contactsFragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
