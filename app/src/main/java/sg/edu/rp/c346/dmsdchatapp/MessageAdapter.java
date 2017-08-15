package sg.edu.rp.c346.dmsdchatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by 15017420 on 15/8/2017.
 */

public class MessageAdapter extends ArrayAdapter<Chat> {

    Context context;
    ArrayList<Chat> items;
    int resource;
    TextView tvMessage, tvName, tvDate;
    ImageView iv;

    public MessageAdapter(Context context, int resource, ArrayList<Chat> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(resource, parent, false);

        //Match the UI components with Java variables
        tvMessage = (TextView)rowView.findViewById(R.id.textViewMessage);
        tvName = (TextView)rowView.findViewById(R.id.textViewName);
        tvDate = (TextView)rowView.findViewById(R.id.textViewDate);

        Chat item = items.get(position);

        tvMessage.setText(item.getText());
        tvName.setText(item.getUser());
        tvDate.setText(String.valueOf(item.getTime()));

        return rowView;
    }

}
