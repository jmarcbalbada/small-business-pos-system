package com.example.small_business_pos_system;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EditInventory extends AppCompatActivity {

    AlertDialog.Builder builder;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_inventory);
        setContentView(R.layout.edit_dialog_layout);
        builder = new AlertDialog.Builder(this);
        preferences = getSharedPreferences("activity",0);
        ((TextView)findViewById(R.id.editNameInput)).setText(preferences.getString("itemName","None"));
        ((TextView)findViewById(R.id.editPriceInput)).setText(Float.toString(preferences.getFloat("itemPrice",0)));
        ((TextView)findViewById(R.id.editQuantityInput)).setText(Integer.toString(preferences.getInt("itemQuantity",0)));
    }

    public void okButtonClicked(View v)
    {
        builder.setTitle("Save changes")
                .setMessage("Are you sure you want to save changes?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    public void cancelButtonClicked(View v)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}