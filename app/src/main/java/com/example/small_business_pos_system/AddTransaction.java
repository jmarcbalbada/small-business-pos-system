package com.example.small_business_pos_system;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddTransaction extends AppCompatActivity {

    private Connect conn;
    private ArrayAdapter itemArrayAdapter;
    private ListView add_transaction_lvItems;
    private String m_Quantity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        conn = new Connect(this);
        add_transaction_lvItems = findViewById(R.id.add_transaction_lvItems);

        add_transaction_lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                InventoryModel model = (InventoryModel) adapterView.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(AddTransaction.this);
                builder.setTitle("Input Quantity");
                builder.setMessage("You selected " + model.getName() +" @ "+model.getPrice()+" each\nIn stock: " + model.getQuantity());

                // Set up the input
                EditText input = new EditText(AddTransaction.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);


                // Set up the buttons
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    boolean proceed = true;
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Quantity = input.getText().toString();
                        Log.e("M_QUANTITY",m_Quantity);
                        if(Integer.parseInt(m_Quantity) > model.getQuantity()) {
                            input.setError("Insufficient stock!");
                            proceed = false;
                        }
                        try {
                            if(proceed) {
                                Item item = conn.getItemByName(model.getName());
                                Log.e("ITEM",item.toString());
                                Transaction transaction = new Transaction(item, Integer.parseInt(m_Quantity), model.getPrice() * Float.parseFloat(m_Quantity));
                                boolean successTransaction = conn.addTransaction(transaction);
                                if (successTransaction) {
                                    Inventory inventory = new Inventory(item,model.getQuantity()-Integer.parseInt(m_Quantity));
                                    conn.editInventoryItem(inventory);
                                    Toast.makeText(AddTransaction.this, "Purchase complete!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AddTransaction.this,MainActivity.class);
                                    startActivity(i);
                                }
                            }
                        } catch (Exception e) {
                            Log.e("ExceptionTransaction",e.toString());
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        transform();
    }

    public void backButtonInventory(View v)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void transform()
    {
        List<Inventory> itemList = conn.getAllInventory();
        List<InventoryModel> modelList = new ArrayList<>();

        for(Inventory inventory: itemList)
        {
            InventoryModel model = new InventoryModel(inventory.getItem().getName(),
                    inventory.getItem().getPrice(),
                    inventory.getQuantity());
            modelList.add(model);
        }

        itemArrayAdapter = new ArrayAdapter<InventoryModel>(this,
                android.R.layout.simple_list_item_1,
                modelList);
        add_transaction_lvItems.setAdapter(itemArrayAdapter);
    }
}