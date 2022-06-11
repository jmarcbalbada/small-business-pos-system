package com.example.small_business_pos_system;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.small_business_pos_system.databinding.ActivityMainBinding;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public Connection conn;

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

    public void tableLayoutClicked(View v)
    {
        String str = retrieveItem();
        if(str.equals(""))
        {
            str = "sample";
        }
        TextView toAddText = new TextView(this);
        toAddText.setPadding(30,30,50,10);
        toAddText.setTextSize(18);
        TableRow row = new TableRow(this);
        toAddText.setText(str);
        TableLayout table = (TableLayout)findViewById(R.id.tableInventory);
        row.addView(toAddText);
        table.addView(row);
    }

    public void snackBarClicked(View v)
    {
        Intent i = new Intent(this,AddInventory.class);
        startActivity(i);
    }

    public String retrieveItem()
    {
        String res = "";
        try {
            conn = connectionclass();
            if(conn == null)
            {
                res = "Connection is null";
            }
            else
            {
                String sql = "select * from balbadadbazure.item";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if(rs.next())
                {
                    res = rs.getString(1) + rs.getFloat(2) + "";
                    conn.close();
                }
            }
        }catch(SQLException e)
        {
            res = "SQL Error";
        }catch(Exception e)
        {
            res = e.toString();
            Log.e("SQLException",e.getMessage());
        }

        return res;
    }

    @SuppressLint("NewApi")
    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String connectionURL = null;

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String url ="jdbc:mysql://balbada-db.mysql.database.azure.com:3306/balbadadbazure?useSSL=true&requireSSL=false;user=balbada@balbada-db;password=!marc0517";
            conn = DriverManager.getConnection(url);
        }catch(SQLException e)
        {

        }catch(ClassNotFoundException e)
        {

        }catch(Exception e)
        {
            Log.e("CONNECTION",e.getMessage());
        }

        return conn;
    }

//    @SuppressLint("NewApi")
//    public Connection connectionclass() throws Exception {
//
//        Properties properties = new Properties();
//        properties.load(MainActivity.class.getClassLoader().getResourceAsStream("application.properties"));
//        Connection conn = DriverManager.getConnection(properties.getProperty("url"),properties);
//        Scanner scanner = new Scanner(MainActivity.class.getClassLoader().getResourceAsStream("schema.sql"));
//        Statement statement = conn.createStatement();
//        while (scanner.hasNextLine()) {
//            statement.execute(scanner.nextLine());
//        }
//        conn.close();
//
//        return conn;
//    }

}