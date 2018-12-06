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

    private String[] listNama,listId,listNomor;

    public static ListActivity la;

    public ArrayList<Kontak> arrayOfUsers;

    public KontakAdapter adapter;

    public ListView listView;

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

        Button btnInput = (Button) findViewById(R.id.btnInput);
        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, InsertActivity.class));
                finish();
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

        refresh();
        la = this;
    }

    public void refresh(){
        Call<KontakModel> kontakCall = mApiInterface.getKontak();
        kontakCall.enqueue(new Callback<KontakModel>() {
            @Override
            public void onResponse(Call<KontakModel> call, Response<KontakModel>
                    response) {
                List<Kontak> kontakList = response.body().getListDataKontak();

                arrayOfUsers = new ArrayList<>();
                listNama = new String[kontakList.size()];
                listNomor = new String[kontakList.size()];
                listId = new String[kontakList.size()];

                int i = 0;
                for (Kontak dataKontak: kontakList) {
                    arrayOfUsers.add(dataKontak);
                    listNama[i] = dataKontak.getNama();
                    listId[i] = dataKontak.getId();
                    listNomor[i] = dataKontak.getNomor();
                    i++;
                }

                adapter = new KontakAdapter(la, arrayOfUsers);
                listView = (ListView) findViewById(R.id.listKontak);
                listView.setAdapter(adapter);
                registerForContextMenu(listView);

            }

            @Override
            public void onFailure(Call<KontakModel> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
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
        int id = Integer.parseInt(listId[info.position]);

        if(aksi.equals("Edit Data")){
            Intent i = new Intent(ListActivity.this, EditActivity.class);

            i.putExtra("idKontak", listId[info.position]);
            i.putExtra("namaKontak", listNama[info.position]);
            i.putExtra("nomorKontak", listNomor[info.position]);
            startActivity(i);
            finish();
        } else {
            Call<KontakModel> deleteKontak = mApiInterface.deleteKontak(String.valueOf(id));
            deleteKontak.enqueue(new Callback<KontakModel>() {
                @Override
                public void onResponse(Call<KontakModel> call, Response<KontakModel> response) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                    if(status.equals("200")){
                        refresh();
                    }
                }

                @Override
                public void onFailure(Call<KontakModel> call, Throwable t) {
                    Log.e("Retrofit Get", t.toString());
                }
            });

            Toast.makeText(getApplicationContext(),
                    "Data Berhasil di Hapus!",
                    Toast.LENGTH_LONG).show();

            Intent i = new Intent(ListActivity.this, MainActivity.class);
            startActivity(i);
        }

        return true;
    }

}
