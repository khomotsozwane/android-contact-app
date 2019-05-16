package blubnanacom.androidcontactappassisgnment;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.Random;

public class AddContactActivity extends AppCompatActivity {

    public static final int GALLERY_IMAGE = -1;
    public static final int CAMERA_IMAGE = -2;
    private static final String DEBUG_TAG = "AddContactActivity";

    EditText newContactName;
    EditText newContactEmail;
    EditText newContactPhone;

    Spinner newContactColorSpinner;
    Button newContactImageSelectButton;
    Button newContactImageCpatureButton;

    ContactDatabaseOperations dbOperations;

    String[] colorSelection = {
            "red", "yellow", "blue", "green"
    };

    int[] imageSelector = {
            R.drawable.b1, R.drawable.b2,
            R.drawable.g1, R.drawable.g2
    };

    private String selectedColor;
    private String phone = "";
    private String email = "";
    private String name = "";
    private String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        newContactName = (EditText) findViewById(R.id.nameEditText);
        newContactEmail = (EditText) findViewById(R.id.emailEditText);
        newContactPhone = (EditText) findViewById(R.id.phoneEditText);

        newContactColorSpinner = (Spinner) findViewById(R.id.colorSpinner);

        newContactImageSelectButton = (Button) findViewById(R.id.imageSelectionButton);
        newContactImageCpatureButton = (Button) findViewById(R.id.imageCaptureButton);

        dbOperations = new ContactDatabaseOperations(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, colorSelection);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        newContactColorSpinner.setAdapter(spinnerAdapter);

        newContactColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "You Selected " + colorSelection[i] , Toast.LENGTH_SHORT).show();
                getSpinnerColorSelected(colorSelection[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        newContactImageCpatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performImageCaptureActivity();
            }
        });

        newContactImageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performImageSelection();
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add contact to local DB
                getContactDetails();
                int id = dbOperations.countDbItems(dbOperations) + 1;
                Log.d(DEBUG_TAG, "Adding an item to DB ID: " + id);
                Random random = new Random();
                int randomNumber = random.nextInt(4);
                imagePath = String.valueOf(imageSelector[randomNumber]);
                dbOperations.addContactToDB(dbOperations, id, name, email, phone, selectedColor, imagePath);
                Intent startContactListIntent = new Intent(AddContactActivity.this, MainActivity.class);
                startActivity(startContactListIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void getContactDetails(){
        name = newContactName.getText().toString();
        email = newContactEmail.getText().toString();
        phone = newContactPhone.getText().toString();
        /*
        if(name.length() != 0 && email.length() != 0 && phone.length() != 0){

        }else{
            Toast.makeText(this, "Please ensure all the textboxes are completed", Toast.LENGTH_LONG).show();
        }
        */
    }

    private void getSpinnerColorSelected(String color){
        selectedColor =  color;
    }

    public void performImageSelection(){
        Intent startImageSelectionFromGallery = new Intent(Intent.ACTION_GET_CONTENT);
        startImageSelectionFromGallery.setType("image/*");
        startActivityForResult(Intent.createChooser(startImageSelectionFromGallery, "Pick an Image"), GALLERY_IMAGE);
    }

    //TODO
    public void performImageCaptureActivity(){
        Intent startImageCaptureFromCamera = new Intent(Intent.ACTION_GET_CONTENT);
        startActivityForResult(startImageCaptureFromCamera, CAMERA_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode == GALLERY_IMAGE ){
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    Random random = new Random();
                    int randomNumber = random.nextInt(4);
                    imagePath = String.valueOf(imageSelector[randomNumber]);
                    Log.d(DEBUG_TAG, imagePath);
                }
            }else if(requestCode == CAMERA_IMAGE){
                //TODO
            }
        }catch (Exception ex){
            Log.d(DEBUG_TAG, "An Error:" + ex.getMessage());
        }

    }

}
