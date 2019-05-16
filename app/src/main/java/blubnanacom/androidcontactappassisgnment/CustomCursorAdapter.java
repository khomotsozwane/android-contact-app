package blubnanacom.androidcontactappassisgnment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflator;

    public CustomCursorAdapter(Context context, Cursor cursor){
        super(context, cursor);
        mContext = context;
        mLayoutInflator = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = mLayoutInflator.inflate(R.layout.list_item, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_NAME));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_PHONE));

        //Optional
        String email = cursor.getString(cursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_EMAIL));
        String color = cursor.getString(cursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_COLOR));
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(TableData.TableInfo.CONTACT_IMAGE));

        ImageView imageView = (ImageView) view.findViewById(R.id.listItemImageView);
        TextView nameText = (TextView) view.findViewById(R.id.listItemNameTextView);
        TextView phoneText = (TextView) view.findViewById(R.id.listItemPhoneTextView);
        View colorView = (View) view.findViewById(R.id.listItemColorView);

        imageView.setImageResource(Integer.parseInt(imagePath));
        nameText.setText(name);
        phoneText.setText(phone);
        colorView.setBackgroundColor(Color.parseColor(color));

    }
}
