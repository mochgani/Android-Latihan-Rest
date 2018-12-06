package id.mochgani.latihanrest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import id.mochgani.latihanrest.database.DatabaseHelper;
import id.mochgani.latihanrest.model.KontakModel;
import id.mochgani.latihanrest.rest.ApiClient;
import id.mochgani.latihanrest.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    EditText edtId, edtNama, edtNomor;
    Button btUpdate, btBack;
    ApiInterface mApiInterface;

    private DatabaseHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mDB = new DatabaseHelper(this);

        edtId = (EditText) findViewById(R.id.edtId);
        edtNama = (EditText) findViewById(R.id.edtNama);
        edtNomor = (EditText) findViewById(R.id.edtNomor);

        Intent mIntent = getIntent();
        edtId.setText(mIntent.getStringExtra("idKontak"));
        edtId.setTag(edtId.getKeyListener());
        edtId.setKeyListener(null);

        edtNama.setText(mIntent.getStringExtra("namaKontak"));
        edtNomor.setText(mIntent.getStringExtra("nomorKontak"));

        mApiInterface = ApiClient.getClient(mDB.getBaseUrl()).create(ApiInterface.class);

        btUpdate = (Button) findViewById(R.id.btUpdate2);
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<KontakModel> updateKontakCall = mApiInterface.putKontak(
                        edtId.getText().toString(),
                        edtNama.getText().toString(),
                        edtNomor.getText().toString());
                updateKontakCall.enqueue(new Callback<KontakModel>() {
                    @Override
                    public void onResponse(Call<KontakModel> call, Response<KontakModel> response) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        if(status.equals("200")){
                            startActivity(new Intent(EditActivity.this, ListActivity.class));
                            ListActivity.la.refresh();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<KontakModel> call, Throwable t) {
                        Log.e("Retrofit Get", t.toString());
                    }
                });
            }
        });
        btBack = (Button) findViewById(R.id.btBackGo);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListActivity.la.refresh();
                finish();
            }
        });
    }
}
