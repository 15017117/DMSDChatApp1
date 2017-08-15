package sg.edu.rp.c346.dmsdchatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    TextView tvForecast;
    EditText etMessage;
    Button btnAdd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference refUser, refMessage;
    private FirebaseAuth mAuth;
    ArrayAdapter<Chat> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        final ArrayList<Chat> chats = new ArrayList<Chat>();

        etMessage = (EditText) findViewById(R.id.editTextMessage);
        btnAdd = (Button) findViewById(R.id.buttonAdd);
        tvForecast = (TextView)findViewById(R.id.textViewForecast);
        final ListView lv = (ListView)findViewById(R.id.lvMessage);

        String area = null;
        String forecast = null;
        String url = "https://api.data.gov.sg/v1/environment/2-hour-weather-forecast";
        //TODO 2
        HttpRequest request = new HttpRequest(url);
        request.setMethod("GET");
        request.setAPIKey("api-key", "7dJJeg6uOb3QD7OxgXla3gkMXTX1Ai1q");

        //TODO 3
        request.execute();

        try {
            //TODO 4
            String jsonString = request.getResponse();
//            String jsonString = "[{\"category_id\":\"1\",\"name\":\"Sports\",\"description\":\"Sports photos\"}," +
//                    "{\"category_id\":\"2\",\"name\":\"Family\",\"description\":\"Family pho
// tos\"}]";
            System.out.println(">>" + jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray itemarray = (JSONArray) jsonObject.get("items");
            for (int i = 0; i < itemarray.length(); i++) {
                JSONObject jsonObj = itemarray.getJSONObject(i);
                JSONArray forecastArray = (JSONArray) jsonObj.get("forecasts");

                for (int items = 0; i < forecastArray.length(); i++) {
                    JSONObject jsonObj2 = forecastArray.getJSONObject(45);
                    area = jsonObj2.getString("area");
                    forecast = jsonObj2.getString("forecast");

                }

            }

            tvForecast.setText("Weather Forecast @ " + area + ": " + forecast);


        }catch(Exception e) {
            e.printStackTrace();
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        refUser = firebaseDatabase.getReference("/profiles");
        refMessage = firebaseDatabase.getReference("/messages");

        refMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Chat post = postSnapshot.getValue(Chat.class);
                    chats.add(post);
                }

                for (int i=0; i<chats.size(); i++) {
                    Log.d("Database content", i + ". " + chats.get(i));
                    aa = new MessageAdapter(HomeActivity.this,R.layout.row,chats);

                }
                lv.setAdapter(aa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = mAuth.getCurrentUser();
                final String message = etMessage.getText().toString();
                Date now = new Date();
                long time = now.getTime();

                Date now2 = new Date(time);
                final String strTime = String.valueOf(now2);

                refUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String profile = dataSnapshot.child(user.getUid()).getValue().toString();
                        Chat chat = new Chat(message, strTime, profile);
                        refMessage.push().setValue(chat);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
