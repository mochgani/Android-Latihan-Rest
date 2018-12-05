package id.mochgani.latihanrest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.mochgani.latihanrest.R;
import id.mochgani.latihanrest.entity.Kontak;

/**
 * Created by mochgani on 05/12/18.
 */

public class KontakAdapter extends ArrayAdapter<Kontak> {
    public KontakAdapter(Context context, ArrayList<Kontak> kontaks) {
        super(context, 0, kontaks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Kontak kontak = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        // Lookup view for data population
        TextView txtNama = (TextView) convertView.findViewById(R.id.txtNama);
        TextView txtNomor = (TextView) convertView.findViewById(R.id.txtNomor);
        // Populate the data into the template view using the data object
        txtNama.setText(kontak.getNama());
        txtNomor.setText(kontak.getNomor());
        // Return the completed view to render on screen
        return convertView;
    }
}
