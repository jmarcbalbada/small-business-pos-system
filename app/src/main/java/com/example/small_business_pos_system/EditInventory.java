package com.example.small_business_pos_system;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditInventory extends AppCompatActivity {

    AlertDialog.Builder builder;
    AlertDialog.Builder builderDelete;
    SharedPreferences preferences;
    Connect conn;
    EditText nameInput;
    EditText priceInput;
    EditText quantityInput;
    boolean state;
    InventoryModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_inventory);
        setContentView(R.layout.edit_dialog_layout);
        builder = new AlertDialog.Builder(this);
        builderDelete = new AlertDialog.Builder(this);
        preferences = getSharedPreferences("activity",0);
        conn = new Connect(EditInventory.this);
        nameInput = findViewById(R.id.editNameInput);
        priceInput = findViewById(R.id.editPriceInput);
        quantityInput = findViewById(R.id.editQuantityInput);
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        state = true;
        nameInput.setText(preferences.getString("itemName","None"));
        priceInput.setText(Float.toString(preferences.getFloat("itemPrice",0)));
        quantityInput.setText(Integer.toString(preferences.getInt("itemQuantity",0)));
    }

    public void okButtonClicked(View v)
    {
        checkFields();

        if(state)
        {
            goThrough(state);
        }

    }

    public void checkFields()
    {
        state = true;
        if(TextUtils.isEmpty(nameInput.getText().toString().trim()))
        {
            nameInput.setError("Field is required!");
            state = false;
        } else {
            Item item = conn.getItemByName(nameInput.getText().toString().trim());
            if (item == null) {
                state = true;
            } else if (conn.isItemNameExist(item,conn.getItemByName(preferences.getString("itemName","None")).getIt_id(),true)) {
                nameInput.setError("This item already exist!");
                state = false;
            }
            else {
            }
        }

        if(TextUtils.isEmpty(priceInput.getText().toString().trim()))
        {
            priceInput.setError("Field is required!");
            state = false;
        }

        if(TextUtils.isEmpty(quantityInput.getText().toString().trim()))
        {
            quantityInput.setError("Field is required!");
            state = false;
        }
    }

    public void goThrough(boolean state)
    {
        if(state == true)
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
                            try
                            {
                                String itemName = nameInput.getText().toString();
                                float price = Float.parseFloat(priceInput.getText().toString());
                                int quantity = Integer.parseInt(quantityInput.getText().toString());
                                Item item = conn.getItemByName(preferences.getString("itemName",null));
                                int it_id = item.getIt_id();
//                                Inventory inventory = new Inventory(item,quantity);
                                Inventory inventory = conn.getInventoryByItId(item.getIt_id());
                                inventory.getItem().setName(itemName);
                                inventory.getItem().setPrice(price);
                                inventory.setQuantity(quantity);
//                            Toast.makeText(EditInventory.this, item.getIt_id()+ " =  " + item.toString(), Toast.LENGTH_SHORT).show();
                                boolean success = conn.setAllInventoryField(inventory);
                                if(success)
                                {
                                    Toast.makeText(EditInventory.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
                                }
//                                else
//                                {
//                                    throw new Exception();
//                                }
//                        Toast.makeText(EditInventory.this, "Okay!", Toast.LENGTH_SHORT).show();
                            }catch(Exception e)
                            {
                                Toast.makeText(EditInventory.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                                Log.e("EXCEPTION DIALOG", e.getMessage());
                            }finally
                            {
                                Intent i = new Intent(EditInventory.this,MainActivity.class);
                                startActivity(i);
                            }

                        }
                    })
                    .show();
        }
    }

    public void deleteButtonClicked(View v)
    {
//        checkFields();
//        if(state)
//        {
            builderDelete.setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
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
                            try
                            {
                                String toDeleteName = preferences.getString("itemName",null);
                                Item item = conn.getItemByName(toDeleteName);
                                Inventory inventory = conn.getFirstInstanceInventory(item);
                                if(inventory != null)
                                {
                                    boolean isDeleteInventoryValid = conn.deleteInventory(item);
                                    if(isDeleteInventoryValid)
                                    {
//                                        conn.deleteItem(item);
                                        Toast.makeText(EditInventory.this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }catch(Exception e)
                            {
                                Toast.makeText(EditInventory.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }finally
                            {
                                Intent i = new Intent(EditInventory.this,MainActivity.class);
                                startActivity(i);
                            }
                        }
                    })
                    .show();
//        }
    }

    public void cancelButtonClicked(View v)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}