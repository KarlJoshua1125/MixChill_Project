package com.example.loginform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class ViewArtist extends AppCompatActivity {
    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";
    public static final String ARTIST_LOCATION = "artistlocation";
ListView listViewArtist;
List<Artist> artists;
DatabaseReference databaseArtist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artist);

        listViewArtist = (ListView) findViewById(R.id.listViewArtist);
        databaseArtist = FirebaseDatabase.getInstance().getReference("Artist");

        artists=new ArrayList<>();

        listViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist = artists.get(i);

                Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);

                intent.putExtra(ARTIST_ID, artist.getArtistId());
                intent.putExtra (ARTIST_NAME, artist.getArtistName ());
                intent.putExtra(ARTIST_LOCATION, artist.getArtistLocation());
                startActivity(intent);
            }


        });

        listViewArtist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){


            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist artist =artists.get(i);
                showUpdateDeleteDialog(artist.getArtistId(), artist.getArtistName());
                return false;
            }
        });
    }
    private void showUpdateDeleteDialog(String artistId, String artistName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView=inflater.inflate(R.layout.update_data, null);

        dialogBuilder.setView(dialogView);


        final EditText editTextName= (EditText) dialogView.findViewById(R.id.updateArtistName);
        final EditText editTextLocation= (EditText) dialogView.findViewById(R.id.updateArtistLocation);
        final Spinner spinnerGenre= (Spinner) dialogView.findViewById(R.id.spinnerGenres);
        final Button buttonUpdate= (Button) dialogView.findViewById(R.id.btnUpdateArtist);
        final Button buttonDelete= (Button) dialogView.findViewById(R.id.btnDeleteArtist);

        dialogBuilder.setTitle("Updating Artist "+ artistName);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();;

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String genre= spinnerGenre.getSelectedItem().toString();

                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Name is required");
                    return;
                }else if (TextUtils.isEmpty(location)){
                    editTextLocation.setError("Location is required");
                    return;
                }
                updateArtist(artistId, name,location, genre);
                alertDialog.dismiss();

            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteArtist(artistId);
                alertDialog.dismiss();
            }

            private void deleteArtist(String artistId){
                DatabaseReference dR= FirebaseDatabase.getInstance().getReference("Artist").child(artistId);
                DatabaseReference drTracks= FirebaseDatabase.getInstance().getReference("tracks").child(artistId);

                dR.removeValue();
                drTracks.removeValue();

                Toast.makeText(getApplicationContext(),"Artist Deleted", Toast.LENGTH_LONG).show();
            }
        });




    }

    private boolean updateArtist(String id,String name,String location, String genre){

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Artist").child(id);

        Artist artist = new Artist(id,name,location, genre);
        dR.setValue(artist);
        Toast.makeText(getApplicationContext(),"Artist Updated", Toast.LENGTH_LONG).show();
        return true;


    }

    protected void onStart() {
        super.onStart();
        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artists.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    Artist artist = postSnapshot.getValue(Artist.class);
                    artists.add(artist);

                }

                ViewListArtist artistAdapter= new ViewListArtist(ViewArtist.this, artists);
                listViewArtist.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}