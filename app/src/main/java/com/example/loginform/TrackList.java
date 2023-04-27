package com.example.loginform;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class TrackList extends ArrayAdapter<Track> {
    private Activity context;
    private List<Track> tracks;


    public TrackList(Activity context, List<Track> tracks) {
        super(context, R.layout.layout_track_list, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_track_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        Track track = tracks.get(position);

        textViewName.setText(track.getTrackName ());

        return listViewItem;
    }
}

