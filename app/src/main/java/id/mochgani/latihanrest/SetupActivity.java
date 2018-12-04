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
import id.mochgani.latihanrest.entity.User;

public class SetupActivity extends AppCompatActivity {

    private DatabaseHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mDB = new DatabaseHelper(this);

        final EditText txtURL = (EditText) findViewById(R.id.txtURL);
        txtURL.setText(mDB.getBaseUrl());

        Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDB.updateSetup("base_url", txtURL.getText().toString());

                Toast.makeText(getApplicationContext(),"Base URL Berhasil di Update!", Toast.LENGTH_LONG).show();

                startActivity(new Intent(SetupActivity.this, MainActivity.class));
                finish();
            }
        });

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetupActivity.this, MainActivity.class));
                finish();
            }
        });


    }
}
