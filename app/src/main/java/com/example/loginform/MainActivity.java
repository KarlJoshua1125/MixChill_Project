package com.example.loginform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    EditText txtuser;
    EditText txtpass;
    Button btnlogin;
    TextView battery;


    private BroadcastReceiver battinfo= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level=intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            battery.setText(String.valueOf(level)+" %");
        }
    };



    DatabaseReference databaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        battery = (TextView) findViewById(R.id.txtbatt);
        this.registerReceiver(this.battinfo, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        databaseUser= FirebaseDatabase.getInstance().getReference("users");

        txtuser = (EditText) findViewById(R.id.txtuser);
        txtpass  = (EditText) findViewById(R.id.txtpass);
        btnlogin = (Button) findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addUser();
            }
        });
    }

    private void addUser() {

        String Username = txtuser.getText().toString().trim();
        String Password = txtpass.getText().toString().trim();


        if (!TextUtils.isEmpty(Username)) {


            String id = databaseUser.push().getKey();

            //creating an Artist Object
            User user = new User(id, Username, Password);

            //Saving the Artist
            databaseUser.child(id).setValue(user);

            //setting edittext to blank again
            txtuser.setText("");
            txtpass.setText("");

            //displaying a success toast
            Toast.makeText(this, "User added", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Please enter a username", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, AddArtist.class);
        startActivity(intent);

    }
}