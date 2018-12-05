package id.mochgani.latihanrest;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.mochgani.latihanrest.adapter.KontakAdapter;
import id.mochgani.latihanrest.database.DatabaseHelper;
import id.mochgani.latihanrest.entity.Kontak;
import id.mochgani.latihanrest.entity.User;
import id.mochgani.latihanrest.model.KontakModel;
import id.mochgani.latihanrest.rest.ApiClient;
import id.mochgani.latihanrest.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {

    ApiInterface mApiInterface;

    private DatabaseHelper mDB;

    private String[] pilihan_menu = { "Edit Data", "Hapus Data" };

    private String[] listNama,listNomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDB = new DatabaseHelper(this);

        mApiInterface = ApiClient.getClient(mDB.getBaseUrl()).create(ApiInterface.class);

        User sessionUser = mDB.getSessionUser();

        TextView lblNama = (TextView) findViewById(R.id.lblNama);
        TextView lblTelp = (TextView) findViewById(R.id.lblTelp);

        lblNama.setText(sessionUser.getNama());
        lblTelp.setText(sessionUser.getTelepon());

        // Construct the data source
        final ArrayList<Kontak> arrayOfUsers = new ArrayList<>();

        Call<KontakModel> kontakCall = mApiInterface.getKontak();
        kontakCall.enqueue(new Callback<KontakModel>() {
            @Override
            public void onResponse(Call<KontakModel> call, Response<KontakModel>
                    response) {
                List<Kontak> kontakList = response.body().getListDataKontak();

                listNama = new String[kontakList.size()];
                listNomor = new String[kontakList.size()];

                int i = 0;
                for (Kontak dataKontak: kontakList) {
                    arrayOfUsers.add(dataKontak);
                    listNama[i] = dataKontak.getNama();
                    listNomor[i] = dataKontak.getNomor();
                    i++;
                }
            }

            @Override
            public void onFailure(Call<KontakModel> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });

        KontakAdapter adapter = new KontakAdapter(this, arrayOfUsers);
        ListView listView = (ListView) findViewById(R.id.listKontak);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        Button btnInput = (Button) findViewById(R.id.btnInput);
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, InsertActivity.class));
            }
        });

        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDB.deleteUser();
                startActivity(new Intent(ListActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View tampil, ContextMenu.ContextMenuInfo menuInfo) {
        if (tampil.getId() == R.id.listKontak) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(listNama[info.position]);
            for (int i = 0; i < pilihan_menu.length; i++) {
                menu.add(Menu.NONE, i, i, pilihan_menu[i]);
            }
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String aksi = pilihan_menu[item.getItemId()];
        int id = Integer.parseInt(listNomor[info.position]);

        if(aksi.equals("Edit Data")){
            Intent i = new Intent(ListActivity.this, EditActivity.class);

            i.putExtra("idWord", id);
            startActivity(i);
        } else {
//            mDB.delete(id);

            Toast.makeText(getApplicationContext(),
                    "Data Berhasil di Hapus!",
                    Toast.LENGTH_LONG).show();

            Intent i = new Intent(ListActivity.this, MainActivity.class);
            startActivity(i);
        }

        return true;
    }

}
