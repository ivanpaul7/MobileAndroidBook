package com.example.ivanp.book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onServerCRUDClick(View view) {
        Intent intent = new Intent(this, ServerCRUDActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onLocalStorageCRUDClick(View view) {
        Intent intent = new Intent(this, LocalStorageCRUDActivity.class);
        startActivity(intent);
    }

    public void onMixedCRUDClick(View view) {
        Intent intent = new Intent(this, MixedCRUDActivity.class);
        startActivity(intent);
    }
}

