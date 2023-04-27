package com.example.loginform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AddTrackActivity extends AppCompatActivity {
    Button btnAddTrack;
    EditText editTextTrackName;
    TextView textViewArtistName;
    ListView listViewTracks;
    ImageView imageView3;
    EditText editTextYoutubeLink;
    DatabaseReference databaseTracks;
    DatabaseReference mDatabaseRef;
    TextView textViewArtistLocation;
    private Uri imageUri;
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    WebView youtubeWebView;
    String youtubeLink;
    public static final String TRACK_NAME = "trackname";
    public static final String TRACK_ID = "trackid";
    List<Track> tracks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("uploads");
        btnAddTrack = (Button) findViewById(R.id.btnAddTrack);
        editTextTrackName = (EditText) findViewById(R.id.editTrackName);
        textViewArtistName = (TextView) findViewById(R.id.textViewArtistName);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        listViewTracks = (ListView) findViewById(R.id.listViewTracks);
        editTextYoutubeLink = (EditText) findViewById(R.id.editYoutubeLink);
        textViewArtistLocation = (TextView) findViewById(R.id.textViewArtistLocation);

        Intent intent = getIntent();

        tracks=new ArrayList<>();
        String id = intent.getStringExtra(ViewArtist.ARTIST_ID);
        String name = intent.getStringExtra(ViewArtist.ARTIST_NAME);
        String location = intent.getStringExtra(ViewArtist.ARTIST_LOCATION);
        textViewArtistName.setText(name);
        textViewArtistLocation.setText(location);




        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        btnAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTrack();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent= new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,2);
            }
        });
        listViewTracks.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Track track =tracks.get(i);
                getYoutubeVideo(track.getTrackName(),track.getYoutubeLink());
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageView3);



        }
    }

    
    @Override
    protected void onStart(){
        super.onStart();

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotsnapshot) {
                tracks.clear();
                for (DataSnapshot trackSnapshot : dataSnapshotsnapshot.getChildren()){
                    Track track = trackSnapshot .getValue(Track.class);
                    tracks.add(track);
                }

                TrackList trackListAdapter = new TrackList(AddTrackActivity.this,tracks);
                listViewTracks.setAdapter(trackListAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveTrack() {
        String trackName = editTextTrackName.getText().toString().trim();
        String youtubeLink = editTextYoutubeLink.getText().toString().trim();
        if (imageUri != null){
            uploadtoFirebase(imageUri);

        }else {
            Toast.makeText(AddTrackActivity.this ,"Please select an Image", Toast.LENGTH_LONG);
        }


        if (!TextUtils.isEmpty(trackName)) {
            String id = databaseTracks.push().getKey();
            Track track = new Track(id, trackName, youtubeLink);
            databaseTracks.child(id).setValue(track);
            Toast.makeText(this, "Track saved", Toast.LENGTH_LONG).show();
            editTextTrackName.setText("");
            editTextYoutubeLink.setText("");
        } else {
            Toast.makeText(this, "Please enter track name", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadtoFirebase(Uri uri){

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "."+ getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Model model= new Model(uri.toString());
                        Toast.makeText(AddTrackActivity.this, "Image Uploaded Sucessfully", Toast.LENGTH_SHORT);

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddTrackActivity.this, "Uploading Failed", Toast.LENGTH_SHORT);
            }
        });

    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr =getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
    private void getYoutubeVideo(String trackName, String youtubeLink){
        String artistname = textViewArtistName.getText().toString();
        String artistlocation = textViewArtistLocation.getText().toString();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(trackName);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView=inflater.inflate(R.layout.view_video, null);
        dialogBuilder.setView(dialogView);
        final WebView ytLink = (WebView) dialogView.findViewById(R.id.WebViewYoutube);
        final WebView Gmaps = (WebView) dialogView.findViewById(R.id.WebViewGMaps);
        final TextView trackname = (TextView) dialogView.findViewById(R.id.textViewTrackName);
        final TextView artistLocation = (TextView) dialogView.findViewById(R.id.textViewArtistLoc);
        dialogBuilder.setTitle("Now Playing: "+ trackName);
        trackname.setText(trackName +"\n By: " + artistname);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        ytLink.setWebViewClient(new WebViewClient(){
            @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });
        WebSettings webSettings = ytLink.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(false);

        ytLink.loadUrl("https://www.youtube.com/embed/" + youtubeLink);

        Gmaps.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
        });
        WebSettings GmapSettings = Gmaps.getSettings();
        GmapSettings.setJavaScriptEnabled(true);
        GmapSettings.setLoadWithOverviewMode(true);
        GmapSettings.setUseWideViewPort(true);
        Gmaps.loadUrl("https://www.google.com/maps/place/" + artistlocation);
    }

}