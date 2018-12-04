package id.mochgani.latihanrest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import id.mochgani.latihanrest.database.DatabaseHelper;
import id.mochgani.latihanrest.entity.User;
import id.mochgani.latihanrest.model.KontakModel;
import id.mochgani.latihanrest.model.UserModel;
import id.mochgani.latihanrest.rest.ApiClient;
import id.mochgani.latihanrest.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertActivity extends AppCompatActivity {
    EditText edtNama, edtNomor;
    Button btInsert, btBack;
    ApiInterface mApiInterface;

    private DatabaseHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        mDB = new DatabaseHelper(this);

        edtNama = (EditText) findViewById(R.id.edtNama);
        edtNomor = (EditText) findViewById(R.id.edtNomor);
        mApiInterface = ApiClient.getClient(mDB.getBaseUrl()).create(ApiInterface.class);
        btInsert = (Button) findViewById(R.id.btInserting);
        btInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<KontakModel> postKontakCall = mApiInterface.postKontak(edtNama.getText().toString(), edtNomor.getText().toString());
                postKontakCall.enqueue(new Callback<KontakModel>() {
                    @Override
                    public void onResponse(Call<KontakModel> call, Response<KontakModel> response) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        if(status.equals("200")){
                            startActivity(new Intent(InsertActivity.this, ListActivity.class));
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
                startActivity(new Intent(InsertActivity.this, ListActivity.class));
                finish();
            }
        });
    }
}
