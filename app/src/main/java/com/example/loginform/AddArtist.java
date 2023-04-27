package com.example.loginform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddArtist extends AppCompatActivity {

    EditText txtArtist, artistloc;
    Spinner genre;
    Button addartist;
    Button viewartist;
    TextView txtlogout;


     DatabaseReference databaseArtist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_artist);

        databaseArtist = FirebaseDatabase.getInstance().getReference("Artist");

        txtArtist = (EditText) findViewById(R.id.txtArtist);
        artistloc =(EditText) findViewById(R.id.artistloc);
        genre = (Spinner) findViewById(R.id.genre);
        addartist = (Button) findViewById(R.id.addartist);
        viewartist = (Button) findViewById(R.id.viewartist);
        txtlogout = (TextView) findViewById(R.id.txtlogout);




        addartist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addArtist();
            }
        });

        viewartist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewArtist();
            }
        });

        txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


    }


    public void addArtist(){
        String ArtistName = txtArtist.getText().toString().trim();
        String ArtistLocation = artistloc.getText().toString().trim();
        String Genre = genre.getSelectedItem().toString();

        if (!TextUtils.isEmpty(ArtistName)) {

            String id = databaseArtist.push().getKey();


            Artist artist = new Artist(id, ArtistName,ArtistLocation, Genre);


            databaseArtist.child(id).setValue(artist);


            txtArtist.setText("");
            artistloc.setText("");


            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }


    public void viewArtist(){
        Intent intent = new Intent(this, ViewArtist.class);
        startActivity(intent);
    }
    public void logout(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}