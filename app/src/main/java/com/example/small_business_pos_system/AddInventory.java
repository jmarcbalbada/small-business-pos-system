package com.example.small_business_pos_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddInventory extends AppCompatActivity {

    private Connect conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inventory);
        conn = new Connect(AddInventory.this);
    }

    public void backButtonInventory(View v)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void addItem(View v) {

        EditText fieldItemName = findViewById(R.id.fieldItemName);
        EditText fieldQuantity = findViewById(R.id.fieldQuantity);
        EditText fieldPrice = findViewById(R.id.fieldPrice);
        boolean proceed = true;

        if (TextUtils.isEmpty(fieldItemName.getText().toString().trim())){
            fieldItemName.setError("Please enter item name!");
            proceed = false;
        }

        if(conn.isItemNameExist(new Item(fieldItemName.getText().toString().trim()), -1,false))
        {
            fieldItemName.setError("This item already exist!");
            proceed = false;
        }

        if (TextUtils.isEmpty(fieldQuantity.getText().toString().trim())){
            fieldQuantity.setError("Please enter quantity!");
            proceed = false;
        }
        if (TextUtils.isEmpty(fieldPrice.getText().toString().trim())){
            fieldPrice.setError("Please enter price!");
            proceed = false;
        }

        try{
            if (proceed) {
                Item item = new Item(fieldItemName.getText().toString(), Float.parseFloat(String.valueOf(fieldPrice.getText())));
                Inventory inventory = new Inventory(item,Integer.parseInt(String.valueOf(fieldQuantity.getText())));

                boolean flag = conn.addInventory(inventory);
                if(flag) {
                    Toast.makeText(this, "Item added successfully!", Toast.LENGTH_LONG).show();
                }

                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
            }
        }catch(Exception e)
        {
            Toast.makeText(this, "Please enter a valid input!", Toast.LENGTH_LONG).show();
        }

    }
}