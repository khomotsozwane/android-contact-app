package blubnanacom.androidcontactappassisgnment;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomListViewAdapter extends BaseAdapter {
    private Context mContext;
    private int mId;
    private String[] mContactNames;
    private String[] mContactNumbers;
    private int[] mImages;

    static final String TAG = "CustomAdapter";

    public CustomListViewAdapter(){}
    public CustomListViewAdapter(Context context, String[] contactNames, String[] contactNumbers){
        this.mContext = context;
        this.mContactNames = contactNames;
        this.mContactNumbers = contactNumbers;
    }

    public CustomListViewAdapter(Context context, int id, String[] contactNames, String[] contactNumbers, int[] images){
        this.mContext = context;
        this.mId = id;
        this.mContactNames = contactNames;
        this.mContactNumbers = contactNumbers;
        this.mImages = images;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        /*if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup, false);


        } */

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item, viewGroup, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.listItemImageView);
        TextView nameText = (TextView) view.findViewById(R.id.listItemNameTextView);
        TextView phoneText = (TextView) view.findViewById(R.id.listItemPhoneTextView);

        imageView.setImageResource(R.drawable.image);
        nameText.setText(mContactNames[i]);
        phoneText.setText(mContactNumbers[i]);

        Log.d(TAG, "Custom Adapter Complete");

        return view;

    }

}
