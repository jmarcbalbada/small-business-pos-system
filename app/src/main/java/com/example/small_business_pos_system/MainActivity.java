package com.example.small_business_pos_system;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.small_business_pos_system.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public Connect conn;
    ArrayAdapter itemArrayAdapter;
    ListView lv_itemlist;
    AppCompatButton yesButton, cancelButton;
    AlertDialog.Builder builderDialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                snackBarClicked(view);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        conn = new Connect(MainActivity.this);
        lv_itemlist = findViewById(R.id.lv_items);
        yesButton = (AppCompatButton)findViewById(R.id.yesButton);
        cancelButton = (AppCompatButton)findViewById(R.id.cancelButton);
//        lv_itemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                InventoryModel model = (InventoryModel) adapterView.getItemAtPosition(position);
//                Toast.makeText(MainActivity.this, model.toString(), Toast.LENGTH_SHORT).show();
//                showAlertDialog(R.layout.edit_dialog_layout,model.getName(),model.getPrice(),model.getQuantity());
//            }
//        });
//        lv_stock = findViewById(R.id.lv_items);;
//        lv_quantity = findViewById(R.id.lv_quantity);
//        conn.dropItem();
//        conn.dropInventory();
//        transform();
//        displayItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void snackBarClicked(View v)
    {
        Intent i = new Intent(this,AddTransaction.class);
        startActivity(i);
    }


    public void displayItem()
    {
        List<Inventory> itemList = conn.getAllInventory();
//        itemArrayAdapter = new ArrayAdapter<Inventory>(MainActivity.this,android.R.layout.simple_list_item_1,itemList);
//        lv_itemlist.setAdapter(itemArrayAdapter);
    }


//    public void cancelButtonClicked(View v)
//    {
//        alertDialog.cancel();
//    }
//
//    public void okButtonClicked(View v)
//    {
//        alertDialog.dismiss();
//    }
//
//    private void showAlertDialog(int myLayout,String name, float price, int quantity)
//    {
//        builderDialog = new AlertDialog.Builder(this);
//        View layoutView = getLayoutInflater().inflate(myLayout,null);
//        builderDialog.setView(layoutView);
//        alertDialog = builderDialog.create();
//        alertDialog.show();
//    }







}