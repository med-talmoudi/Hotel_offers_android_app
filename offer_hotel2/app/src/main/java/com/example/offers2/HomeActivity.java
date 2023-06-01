package com.example.offers2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ImageView login_icon = findViewById(R.id.login_icon);
        login_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        Button buttonVisitor =findViewById(R.id.buttonVisitor);
        buttonVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( HomeActivity.this, ClientInterfaceActivity.class);
                i.putExtra("clientId","visitor");
                startActivity(i);
            }
        });
    }
}