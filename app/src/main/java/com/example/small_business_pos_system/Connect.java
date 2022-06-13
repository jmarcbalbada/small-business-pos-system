package com.example.small_business_pos_system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
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
        String createTransactionTableStatement = "CREATE TABLE TRANSACTIONS (REF_NO INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, QUANTITY INTEGER, TOTAL_PRICE REAL, DATE TEXT)";
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

    public boolean deleteInventory(Item item)
    {
        boolean valid = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + INVENTORY_TABLE + " WHERE IT_ID = " + item.getIt_id();

        Cursor cursor = db.rawQuery(sql, null);
        if(!cursor.moveToFirst())
        {
            valid = true;
            deleteItem(item);
        }


        return valid;
    }



    public void deleteItem(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + ITEM_TABLE + " WHERE IT_ID = " + item.getIt_id();
        Log.e("IT_ID",item.getIt_id() + "");
        db.execSQL(sql);
    }

    public Inventory getFirstInstanceInventory(Item item)
    {
        Inventory result = null;
        String sql = "SELECT * FROM " + INVENTORY_TABLE + " WHERE IT_ID = " + item.getIt_id();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            result = new Inventory(cursor.getInt(0),item,cursor.getInt(2));
        }
        return result;
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

    public boolean addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String name = transaction.getItem().getName();

        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_QUANTITY,transaction.getQuantity());
        cv.put(COLUMN_TOTAL_PRICE,transaction.getTotalPrice());
        cv.put(COLUMN_DATEOFPURCHASE,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()));

        long insert = db.insert(TRANSACTION_TABLE, null, cv);
        if(insert == -1)
        {
            return false;
        }

        return true;
    }

    public boolean setAllInventoryField(Inventory inventory)
    {
//        String sql = "UPDATE " + INVENTORY_TABLE + " SET QUANTITY = " + quantity + " WHERE IT_ID = " + it_id;
        editInventoryItem(inventory);

        String sql = "UPDATE " + ITEM_TABLE + " SET NAME = '" + inventory.getItem().getName() + "', PRICE = " + inventory.getItem().getPrice() + " WHERE IT_ID = " + inventory.getItem().getIt_id();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.execSQL(sql);

        return true;
    }


    public void editInventoryItem(Inventory inventory) {

        String sql = "UPDATE " + INVENTORY_TABLE + " SET QUANTITY = " + inventory.getQuantity() + " WHERE it_id = " + inventory.getItem().getIt_id();
        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_QUANTITY, inventory.getQuantity());

        db.execSQL(sql);
    }


    public boolean editPrice(int it_id ,float price)
    {
        boolean valid = false;
        if(price < 0)
        {
            String sql = "UPDATE " + ITEM_TABLE + " SET PRICE = " + price + " WHERE IT_ID = " + it_id;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst())
            {
                valid = true;
            }
            cursor.close();
            db.close();
        }

        return valid;
    }

    public boolean editItemName(int it_id ,String name)
    {
        boolean valid = false;

        try{
            String sql = "UPDATE " + ITEM_TABLE + " SET NAME = " + name + " WHERE IT_ID = " + it_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst())
            {
                valid = true;
            }
            cursor.close();
            db.close();
        }catch(Exception e)
        {
        }

        return valid;
    }

    public boolean editQuantity(int quantity, int in_id)
    {
        boolean valid = false;

        try{
            String sql = "UPDATE " + INVENTORY_TABLE + " SET QUANTITY = " + quantity + " WHERE IN_ID = " + in_id;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst())
            {
                valid = true;
            }
            cursor.close();
            db.close();
        }catch(Exception e)
        {
        }

        return valid;
    }

    public List<Transaction> searchTransaction(String search, String category)
    {
        List<Transaction> result = new ArrayList<>();
        String sql = "";
        switch(category)
        {
            case "Product": sql = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE NAME = '" + search + "' COLLATE NOCASE";
                break;
            case "Ref_No": sql = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE REF_NO = " + Integer.parseInt(search);
                break;
            case "Date":
                sql = "SELECT * FROM " + TRANSACTION_TABLE + " WHERE DATE LIKE '" + search + "%'";
                break;
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do {
                Transaction transaction = new Transaction(cursor.getInt(0),cursor.getString(1),
                        cursor.getInt(2),cursor.getFloat(3),
                        cursor.getString(4));

                result.add(transaction);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<Transaction> firstFiveInTransaction()
    {
        List<Transaction> result = new ArrayList<>();
        String sql = "SELECT * FROM " + TRANSACTION_TABLE + " ORDER BY DATE DESC LIMIT 5";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            do {
                Transaction transaction = new Transaction(cursor.getInt(0),cursor.getString(1),
                        cursor.getInt(2),cursor.getFloat(3),
                        cursor.getString(4));

                result.add(transaction);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean isItemNameExist(Item item, int orig_it_id, boolean edit)
    {
        boolean flag = false;
        List<Inventory> list = getAllInventory();

        for(Inventory inventory: list)
        {
            if(inventory.getItem().getName().equals(item.getName()))
            {
                if(edit) {
                    if(Integer.toString(inventory.getItem().getIt_id()).equals(Integer.toString(item.getIt_id()))) {
                        if (!Integer.toString(orig_it_id).equals(Integer.toString(item.getIt_id()))) {
                            flag = true;
                            break;
                        }
                        else {
                        }
                    }
                }
                else {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

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

    public Item getFirstInstanceItem(String name)
    {
        Item result = null;
        String sql = "SELECT * FROM " + ITEM_TABLE + " WHERE NAME = '" + name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            result = new Item(cursor.getInt(0),cursor.getString(1),cursor.getFloat(2));
        }
        return result;
    }

    public Inventory getInventoryByItId(int it_id) {
        Inventory result = null;
        String sql = "SELECT * FROM " + INVENTORY_TABLE + " WHERE IT_ID = " + it_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            result = new Inventory(cursor.getInt(0),getItem(cursor.getInt(1)),cursor.getInt(2));
        }
        return result;
    }

    public Item getItemByName(String name)
    {
        Item result = null;
        String sql = "SELECT * FROM " + ITEM_TABLE + " WHERE NAME = '" + name + "'";
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
