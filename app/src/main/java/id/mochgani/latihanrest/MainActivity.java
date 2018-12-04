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
import id.mochgani.latihanrest.model.UserModel;
import id.mochgani.latihanrest.rest.ApiClient;
import id.mochgani.latihanrest.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ApiInterface mApiInterface;

    private DatabaseHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new DatabaseHelper(this);
        mDB.getWritableDatabase();

        if(mDB.cekSession() > 0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

        mApiInterface = ApiClient.getClient(mDB.getBaseUrl()).create(ApiInterface.class);

        final EditText txtUsername = (EditText) findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<UserModel> postKontakCall = mApiInterface.postLogin(txtUsername.getText().toString(), txtPassword.getText().toString());
                postKontakCall.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        String status = response.body().getStatus();
                        String message = response.body().getMessage();

                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        if(status.equals("200")){
                            List<User> UserList = response.body().getListDataUser();

                            for (User dataUser: UserList) {
                                mDB.insertUser(dataUser);
                            }

                            startActivity(new Intent(MainActivity.this, ListActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.e("Retrofit Get", t.toString());
                    }
                });

            }
        });

        Button btnSetup = (Button) findViewById(R.id.btnSetup);
        btnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                finish();
            }
        });
    }

}