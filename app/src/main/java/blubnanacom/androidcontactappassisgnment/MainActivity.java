package blubnanacom.androidcontactappassisgnment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String IMPORT_STATE = "Import_State";
    public static final String SHARED_PREFERENCES = "SHEARED_PREFERENCES";
    private ListView contactsList;
    private String importContactUrl = "https://jsonplaceholder.typicode.com/users";

    private MockContactData mockContactData;
    private ContactDatabaseOperations dbOperations;
    private Cursor mCursor;

    private int itemPosition;
    private int itemLongClickPosition;

    public static final String CONTACT_ID = "";
    private static final String DEBUG_TAG = "MainActivty";

    public CustomCursorAdapter customCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactsList = (ListView) findViewById(R.id.fullContactsListView);

        dbOperations = new ContactDatabaseOperations(this);

        customCursorAdapter = new CustomCursorAdapter(this, loadRemoteData());

        if(customCursorAdapter.getCount() == 0) {
            //Load network data auto if empty
            performNetworkActions();
        }

        //customCursorAdapter = new CustomCursorAdapter(this, loadRemoteData());

        contactsList.setAdapter(customCursorAdapter);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPosition = mCursor.getInt(mCursor.getColumnIndexOrThrow(TableData.TableInfo._ID)); //Get ID of the clicked item

                Intent startContactDetailActivity = new Intent(MainActivity.this, ContactDetailActivity.class);
                startContactDetailActivity.putExtra(CONTACT_ID, itemPosition);
                startActivity(startContactDetailActivity);
            }
        });

        contactsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemLongClickPosition = mCursor.getInt(mCursor.getColumnIndexOrThrow(TableData.TableInfo._ID));
                long removeResult = dbOperations.removeContactFromDB(dbOperations, itemLongClickPosition);
                Log.d(DEBUG_TAG, "Deleted item at " + itemLongClickPosition + " items left in the Db " + mCursor.getCount() + " Stuff " + removeResult);
                customCursorAdapter.notifyDataSetChanged();
                Log.d(DEBUG_TAG, "Db Changed");
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(addContactIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        customCursorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        dbOperations.close();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        customCursorAdapter = new CustomCursorAdapter(this, mCursor);
        customCursorAdapter.notifyDataSetChanged();
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
        if (id == R.id.action_about) {
            return true;
        }else if(id == R.id.action_exit){
            //finish();
            Runtime.getRuntime().exit(0);
            return true;
        }else if(id == R.id.action_import){
            performNetworkActions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Cursor loadMockData(){
        mockContactData = new MockContactData(this);
        mCursor = dbOperations.getAllContactData(dbOperations);
        Log.d(DEBUG_TAG, "load mock cursor");
        return mCursor;
    }

    public Cursor loadRemoteData(){
        mCursor = dbOperations.getAllContactData(dbOperations);
        return mCursor;
    }

    public void performNetworkActions(){
        dbOperations.clearMockData(dbOperations);
        fetchContactData();
        restartActivityForLayout();
    }

    public void restartActivityForLayout(){
        Intent startActivity = new Intent(MainActivity.this, MainActivity.class);
        startActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startActivity);
    }
    public void fetchContactData(){
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String[] randomcolorGenerator = {
                "red","green","blue","yellow"
        };

        final int[] randomImageGenerator = {
                R.drawable.g1, R.drawable.b2,
                R.drawable.g2, R.drawable.b1
        };

        Log.d(DEBUG_TAG, "Data Fetch Started!");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, importContactUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONArray usersArray = response;
                        try{
                            for(int i = 0; i < usersArray.length(); i++){
                                JSONObject userObject = usersArray.getJSONObject(i);

                                String name = userObject.getString("name");
                                String email = userObject.getString("email");
                                String phone = userObject.getString("phone");

                                Random random = new Random();
                                int randomNumber = random.nextInt(4);

                                String color = randomcolorGenerator[randomNumber];
                                String imagePath = String.valueOf(randomImageGenerator[randomNumber]);

                                Log.d(DEBUG_TAG, "DB details" + name + " " + email + " " + phone);
                                long id = dbOperations.addContactToDB(dbOperations, i, name, email, phone, color, imagePath);
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

}
