package blubnanacom.androidcontactappassisgnment;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MockContactData {
    public List<ContactData> contactDataList = new ArrayList<>();
    ContactDatabaseOperations dbOperations;

    int image = R.drawable.b1;

    String imagePath = String.valueOf(image);

    public MockContactData(Context context){
        dbOperations = new ContactDatabaseOperations(context);

        dbOperations.addContactToDB(dbOperations, 1, "Simon Barr", "sbarr@internet.com", "0659166578", "red", imagePath);
    }


}
