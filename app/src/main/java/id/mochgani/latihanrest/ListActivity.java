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

import id.mochgani.latihanrest.database.DatabaseHelper;
import id.mochgani.latihanrest.entity.User;

public class ListActivity extends AppCompatActivity {

    private DatabaseHelper mDB;

    private String[] pilihan_menu = { "Edit Data", "Hapus Data" };

    private String[] kontak,idKontak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDB = new DatabaseHelper(this);

        User sessionUser = mDB.getSessionUser();

        TextView lblNama = (TextView) findViewById(R.id.lblNama);
        TextView lblTelp = (TextView) findViewById(R.id.lblTelp);

        lblNama.setText(sessionUser.getNama());
        lblTelp.setText(sessionUser.getTelepon());

//        idKontak = mDB.getDataAll(0);
//        kontak = mDB.getDataAll(1);

//        Cursor cursor;
        MatrixCursor matrixCursor = new MatrixCursor(new String[] { "nama", "telp" });
        matrixCursor.addRow(new Object[] { "Abdi", "9989999" });

        String[] columns = new String[] { "nama", "telp" };
        int[] to = new int[] { R.id.txtNama, R.id.txtNomor };
//
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.list_view, matrixCursor, columns, to, 0);
//        ListView listview =(ListView) findViewById(R.id.listKontak);
//        listview.setAdapter(adapter);
//        registerForContextMenu(listview);

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

//    public void onCreateContextMenu(ContextMenu menu, View tampil, ContextMenu.ContextMenuInfo menuInfo) {
//        if (tampil.getId() == R.id.listKontak) {
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//            menu.setHeaderTitle(kontak[info.position]);
//            for (int i = 0; i < pilihan_menu.length; i++) {
//                menu.add(Menu.NONE, i, i, pilihan_menu[i]);
//            }
//        }
//    }
//
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        String aksi = pilihan_menu[item.getItemId()];
//        int id = Integer.parseInt(idKontak[info.position]);
//
//        if(aksi.equals("Edit Data")){
//            Intent i = new Intent(ListActivity.this, EditActivity.class);
//
//            i.putExtra("idWord", id);
//            startActivity(i);
//        } else {
////            mDB.delete(id);
//
//            Toast.makeText(getApplicationContext(),
//                    "Data Berhasil di Hapus!",
//                    Toast.LENGTH_LONG).show();
//
//            Intent i = new Intent(ListActivity.this, MainActivity.class);
//            startActivity(i);
//        }
//
//        return true;
//    }

}
