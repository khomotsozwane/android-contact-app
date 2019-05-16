package blubnanacom.androidcontactappassisgnment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ContactDetailActivity extends AppCompatActivity {

    private Intent myIntent;

    private ContactDatabaseOperations dbOperations;
    private Cursor contactCursor;

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private View colorView;
    private ImageView imageView;


    private int intentExtra;
    private static final String DEBUG_TAG = "ContactDetailsActivity";
    public static final String CONTACT_ID = "";

    private String name = "";
    private String email = "";
    private String phone = "";
    private String color = "";
    private String image = "";

    private int colorAsInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(DEBUG_TAG, "Starting Activity ");

        nameTextView = (TextView) findViewById(R.id.contactNameTextView);
        emailTextView = (TextView) findViewById(R.id.contactEmailTextView);
        phoneTextView = (TextView) findViewById(R.id.contactPhoneTextView);
        colorView = (View) findViewById(R.id.contactColorView);
        imageView = (ImageView) findViewById(R.id.contactProfileImageView);

        myIntent = getIntent();

        intentExtra = myIntent.getIntExtra(MainActivity.CONTACT_ID, 1);

        Log.d(DEBUG_TAG, "Got Intent Extra: " + intentExtra);

        populateContactDetails(intentExtra);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmailMessage();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            long delete =  dbOperations.removeContactFromDB(dbOperations, intentExtra);
            Log.d(DEBUG_TAG, "Deleted item at " + delete);
            //customCursorAdapter.notifyDataSetChanged();
            Intent startContactListIntent = new Intent(ContactDetailActivity.this, MainActivity.class);
            startActivity(startContactListIntent);
            return true;
        }else if(id == R.id.action_edit){
            Intent startContactEditIntent = new Intent(ContactDetailActivity.this, UpdateContactDetailsActivity.class);
            startContactEditIntent.putExtra(CONTACT_ID, intentExtra);
            startActivity(startContactEditIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmailMessage(){
        String name = contactCursor.getString(contactCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_NAME));
        String email = contactCursor.getString(contactCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_EMAIL));

        String subject = "Email from " + name;
        String[] address = { email };

        Log.d("My Email Address", address.toString());

        Intent startEmailService = new Intent(Intent.ACTION_SENDTO);
        startEmailService.setData(Uri.parse("mailto:"));
        startEmailService.putExtra(Intent.EXTRA_EMAIL, address);
        startEmailService.putExtra(Intent.EXTRA_SUBJECT, subject);

        startActivity(startEmailService);
    }

    private void populateContactDetails(int id){
        Log.d(DEBUG_TAG, "Start populating dating into variables");
        contactCursor = loadContactDetailsData(id);


        try{
            if(contactCursor.moveToFirst()){
                name = contactCursor.getString(contactCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_NAME));
                email = contactCursor.getString(contactCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_EMAIL));
                phone = contactCursor.getString(contactCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_PHONE));
                color = contactCursor.getString(contactCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_COLOR));
                image = contactCursor.getString(contactCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_IMAGE));
                colorAsInt = Color.parseColor(color);
                Log.d(DEBUG_TAG, "Cursor Results: " + name + email + phone + color);
            }else{

                Log.d(DEBUG_TAG, "Cursor Empty!");
            }
        }catch (Exception Ex){
            Log.d(DEBUG_TAG, "Exception: " + Ex.getMessage());
        }

        nameTextView.setText(name);
        emailTextView.setText(email);
        phoneTextView.setText(phone);
        colorView.setBackgroundColor(colorAsInt);
        imageView.setImageResource(Integer.parseInt(image));
    }

    private Cursor loadContactDetailsData(int id){
        dbOperations = new ContactDatabaseOperations(this);
        contactCursor = dbOperations.getContactById(dbOperations, id);
        Log.d(DEBUG_TAG, "ID: " + id + " Cursor Size: " + contactCursor.getCount());
        return contactCursor;
    }
}
