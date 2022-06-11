package com.example.small_business_pos_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddInventory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
    }

    public void backButtonInventory(View v)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}