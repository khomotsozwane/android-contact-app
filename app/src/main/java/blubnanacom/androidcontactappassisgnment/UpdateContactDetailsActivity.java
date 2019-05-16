package blubnanacom.androidcontactappassisgnment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Random;

public class UpdateContactDetailsActivity extends AppCompatActivity {
    
    private ContactDatabaseOperations dbOperations;

    private EditText updateContactName;
    private EditText updateContactEmail;
    private EditText updateContactPhone;
    
    private Intent intent;
    
    private int intentExtra;

    private static final String DEBUG_TAG = "UpdateContactDActivity";

    private String name = "";
    private String email = "";
    private String phone = "";
    private String color = "";
    private String colorSelected = "";
    private int colorAsInt = 0;

    String[] colorSelection = {
            "red", "yellow", "blue", "green"
    };

    int colorSelectionIndex = 0;
    int[] imageSelector = {
            R.drawable.b1, R.drawable.b2,
            R.drawable.g1, R.drawable.g2
    };
    
    private Cursor mCursor;
    private Spinner updateContactColorSpinner;
    private Button updateContactImageSelectButton;
    private Button updateContactImageCpatureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateContactName = (EditText) findViewById(R.id.updateNameEditText);
        updateContactEmail = (EditText) findViewById(R.id.updateEmailEditText);
        updateContactPhone = (EditText) findViewById(R.id.updatePhoneEditText);

        updateContactColorSpinner = (Spinner) findViewById(R.id.updateColorSpinner);

        updateContactImageSelectButton = (Button) findViewById(R.id.updateImageSelectionButton);
        updateContactImageCpatureButton = (Button) findViewById(R.id.updateImageCaptureButton);

        intent = getIntent();

        dbOperations = new ContactDatabaseOperations(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, colorSelection);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        updateContactColorSpinner.setAdapter(spinnerAdapter);

        updateContactColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(), "You Selected " + colorSelection[i] , Toast.LENGTH_SHORT).show();
                getSpinnerColorSelected(colorSelection[i]);
                updateContactColorSpinner.setBackgroundColor(Color.parseColor(colorSelection[i]));
                colorSelectionIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        intentExtra = intent.getIntExtra(ContactDetailActivity.CONTACT_ID, 1);

        Log.d(DEBUG_TAG, "My Intent " + intentExtra);

        loadContactDetailsDataFromCursor(intentExtra);

        updateContactColorSpinner.setSelection(getColorFromIndex(colorSelection, color));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update
                applyChangesToContact();
                Snackbar.make(view, "Updating DB!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void applyChangesToContact(){
        int id = intentExtra;
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        String imagePath = String.valueOf(imageSelector[randomNumber]);
        name = updateContactName.getText().toString();
        email = updateContactEmail.getText().toString();
        phone = updateContactPhone.getText().toString();
        colorSelected = colorSelection[colorSelectionIndex];

        long update = dbOperations.updateContactById(dbOperations, id, name, email, phone, colorSelected, imagePath);

        Log.d(DEBUG_TAG, "Updating an item to DB ID: " + id + update);

        Intent startContactListIntent = new Intent(UpdateContactDetailsActivity.this, ContactDetailActivity.class);
        startContactListIntent.putExtra(ContactDetailActivity.CONTACT_ID, intentExtra);
        startActivity(startContactListIntent);

    }

    private void getSpinnerColorSelected(String color){
        colorSelected =  color;
    }

    private int getColorFromIndex(String[] colors, String inputColor){
        colors = colorSelection;
        int result = 0;
        for(int i = 0; i < colors.length; i++){
            if(colors[i] == inputColor){
                result = i;
            }
        }

        Log.d(DEBUG_TAG, "Index: " + result);
        return result;
    }

    public void loadContactDetailsDataFromCursor(int id){
        mCursor = dbOperations.getContactById(dbOperations,id);
        try{
            if(mCursor.moveToFirst()){
                name = mCursor.getString(mCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_NAME));
                email = mCursor.getString(mCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_EMAIL));
                phone = mCursor.getString(mCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_PHONE));
                color = mCursor.getString(mCursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_COLOR));
                colorAsInt = Color.parseColor(color);
            }else{
                Log.d(DEBUG_TAG, "Cursor Empty!");
            }
        }catch (Exception Ex){
            Log.d(DEBUG_TAG, "Exception: " + Ex.getMessage());
        }
        
        updateContactName.setText(name);
        updateContactEmail.setText(email);
        updateContactPhone.setText(phone);
        //updateContactColorSpinner.setBackgroundColor(colorAsInt);

        //updateContactColorSpinner.getItemAtPosition()
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateContactDetailsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
