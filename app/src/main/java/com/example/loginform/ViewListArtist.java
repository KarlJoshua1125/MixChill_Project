package com.example.loginform;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ViewListArtist extends ArrayAdapter {

    private Activity context;
    private List<Artist> viewartistList;

    public ViewListArtist(Activity context, List<Artist> viewartistList){
        super(context, R.layout.activity_view_list_artist, viewartistList);
        this.context=context;
        this.viewartistList =viewartistList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_view_list_artist, null, true);

        TextView txtViewname= (TextView) listViewItem.findViewById(R.id.txtViewName);
        TextView txtViewgenre = (TextView) listViewItem.findViewById(R.id.txtViewGenre);

        Artist artist = viewartistList.get(position);

        txtViewname.setText(artist.getArtistName());
        txtViewgenre.setText(artist.getArtistGenre());

        return listViewItem;
    }
}
