package com.example.small_business_pos_system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Connect extends SQLiteOpenHelper {
    public static final String INVENTORY_TABLE = "INVENTORY";
    public static final String TRANSACTION_TABLE = "TRANSACTIONS";
    public static final String ITEM_TABLE = "ITEM";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_PRICE = "PRICE";
    public static final String COLUMN_ITEMS = "ITEMS";
    public static final String COLUMN_QUANTITY = "QUANTITY";
    public static final String COLUMN_IT_ID = "IT_ID";
    public static final String COLUMN_REF_NO = "REF_NO";
    public static final String COLUMN_TOTAL_PRICE = "TOTAL_PRICE";
    public static final String COLUMN_DATEOFPURCHASE = "DATE";

    public Connect(@Nullable Context context) {
        super(context, "small_business_pos_system.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createItemTableStatement = "CREATE TABLE ITEM (IT_ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PRICE REAL)";
        String createInventoryTableStatement = "CREATE TABLE INVENTORY (IN_ID INTEGER PRIMARY KEY AUTOINCREMENT, IT_ID INTEGER, QUANTITY INTEGER, FOREIGN KEY(IT_ID) REFERENCES ITEM(IT_ID))";
        String createTransactionTableStatement = "CREATE TABLE TRANSACTIONS (REF_NO INTEGER PRIMARY KEY AUTOINCREMENT, IT_ID INTEGER, QUANTITY INTEGER, TOTAL_PRICE REAL, DATE TEXT, FOREIGN KEY(IT_ID) REFERENCES ITEM(IT_ID))";
        db.execSQL(createItemTableStatement);
        db.execSQL(createInventoryTableStatement);
        db.execSQL(createTransactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
//        db.execSQL("DROP TABLE IF EXISTS INVENTORY");
//        db.execSQL("DROP TABLE IF EXISTS TRANSACTIONS");
//        onCreate(db);
    }

    public long addItem(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME,item.getName());
        cv.put(COLUMN_PRICE,item.getPrice());

        long insert = db.insert(ITEM_TABLE, null, cv);
        return insert;
    }

    public boolean deleteTransactionContents()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TRANSACTION_TABLE;

        Cursor cursor = db.rawQuery(sql, null);
        if(!cursor.moveToFirst())
        {
            return true;
        }

        return false;
    }

    public boolean addInventory(Inventory inventory)
    {
        long rowId = addItem(inventory.getItem());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IT_ID,Integer.parseInt(rowId+""));
        cv.put(COLUMN_QUANTITY,inventory.getQuantity());

        long insert = db.insert(INVENTORY_TABLE, null, cv);
        if(insert == -1)
        {
            return false;
        }

        return true;
    }

//    public boolean addTransaction()
//    {
//        long rowId = addItem();
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        int it_id = (int) rowId;
//        Item item = getItem(it_id);
//
//        cv.put(COLUMN_IT_ID,it_id);
//        cv.put(COLUMN_QUANTITY,45);
//        cv.put(COLUMN_TOTAL_PRICE,500.00);
//        cv.put(COLUMN_DATEOFPURCHASE,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));
////        cv.put(COLUMN_DATEOFPURCHASE,"0000/00/00");
//        long insert = db.insert(TRANSACTION_TABLE, null, cv);
//        if(insert == -1)
//        {
//            return false;
//        }
//
//        return true;
//    }

    public List<Inventory> getAllInventory()
    {
        List<Inventory> result = new ArrayList<>();
        String sql = "SELECT * FROM " + INVENTORY_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do {
                Item item = getItem(cursor.getInt(1));
                Inventory inventory = new Inventory(cursor.getInt(0),item,cursor.getInt(2));
                result.add(inventory);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return result;
    }

    public List<Integer> getAllInventoryQuantity()
    {
        List<Integer> result = new ArrayList<>();
        String sql = "SELECT QUANTITY FROM " + INVENTORY_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do {
                Integer i = new Integer(cursor.getInt(0));
                result.add(i);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return result;
    }

    public List<String> getAllInventoryName()
    {
        List<String> result = new ArrayList<>();
        String sql = "SELECT IT_ID FROM " + INVENTORY_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do {
                Item item = getItem(cursor.getInt(0));
                String str = item.getName();
                result.add(str);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return result;
    }

    public void dropItem()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DROP TABLE " + ITEM_TABLE;
        db.rawQuery(sql,null);
    }

    public Item getItem(int it_id)
    {
        Item result = null;
        String sql = "SELECT * FROM " + ITEM_TABLE + " WHERE it_id = " + it_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            result = new Item(cursor.getInt(0),cursor.getString(1),cursor.getFloat(2));
        }
        return result;
    }

    public void dropInventory()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DROP TABLE " + INVENTORY_TABLE;
        db.rawQuery(sql,null);
    }


}
