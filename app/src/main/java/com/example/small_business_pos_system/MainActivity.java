package com.example.small_business_pos_system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        conn = new Connect(MainActivity.this);
        lv_itemlist = findViewById(R.id.lv_items);
//        conn.dropItem();
//        conn.dropInventory();
        transform();
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
        Intent i = new Intent(this,AddInventory.class);
        startActivity(i);
    }

    public void buttonClicked(View v)
    {
//        boolean valid = conn.addInventory();
//        Toast.makeText(this, "Success=" + valid, Toast.LENGTH_SHORT).show();
//        displayItem();
    }

    public void displayItem()
    {
        List<Inventory> itemList = conn.getAllInventory();
//        itemArrayAdapter = new ArrayAdapter<Inventory>(MainActivity.this,android.R.layout.simple_list_item_1,itemList);
//        lv_itemlist.setAdapter(itemArrayAdapter);
    }

    public void transform()
    {
        List<Inventory> itemList = conn.getAllInventory();
        List<InventoryModel> modelList = new ArrayList<>();

        for(Inventory inventory: itemList)
        {
            InventoryModel model = new InventoryModel(inventory.getItem().getName(),inventory.getItem().getPrice(),inventory.getQuantity());
            modelList.add(model);
        }

        itemArrayAdapter = new ArrayAdapter<InventoryModel>(MainActivity.this,android.R.layout.simple_list_item_1,modelList);
        lv_itemlist.setAdapter(itemArrayAdapter);
    }


}