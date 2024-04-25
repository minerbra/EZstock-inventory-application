package com.example.androidapplicationbradyminer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "InventoryDatabase";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "inventoryApp.db";

    // Singleton instance of the database helper
    private static DatabaseHelper sInstance;

    // Constructor is private to enforce singleton pattern
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Factory method to get the singleton instance
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Table and column names for the inventory table
    private static class InventoryTable {
        static final String TABLE_NAME = "inventory";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_QUANTITY = "quantity";
    }

    // Table and column names for the users table
    private static class UsersTable {
        static final String TABLE_NAME = "users";
        static final String COLUMN_ID = "_id";
        static final String COLUMN_USERNAME = "username";
        static final String COLUMN_PASSWORD = "password";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "Creating database");
        // Create the inventory table
        String createInventoryTable = "CREATE TABLE " + InventoryTable.TABLE_NAME + " (" +
                InventoryTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InventoryTable.COLUMN_NAME + " TEXT, " +
                InventoryTable.COLUMN_QUANTITY + " INTEGER)";
        db.execSQL(createInventoryTable);

        // Create the users table
        String createUsersTable = "CREATE TABLE " + UsersTable.TABLE_NAME + " (" +
                UsersTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UsersTable.COLUMN_USERNAME + " TEXT, " +
                UsersTable.COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + InventoryTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UsersTable.TABLE_NAME);
        // Recreate tables
        onCreate(db);
    }

    // CRUD operations for inventory items

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(InventoryTable.TABLE_NAME,
                null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(InventoryTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(InventoryTable.COLUMN_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(InventoryTable.COLUMN_QUANTITY));
                items.add(new Item(id, name, quantity));
            }
            cursor.close();
        }
        return items;
    }

    public boolean addItem(String name, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryTable.COLUMN_NAME, name);
        values.put(InventoryTable.COLUMN_QUANTITY, quantity);
        long newRowId = db.insert(InventoryTable.TABLE_NAME, null, values);
        return newRowId != -1;
    }

    public boolean updateItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryTable.COLUMN_NAME, item.getName());
        values.put(InventoryTable.COLUMN_QUANTITY, item.getQuantity());
        String selection = InventoryTable.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};
        int count = db.update(InventoryTable.TABLE_NAME, values, selection, selectionArgs);
        return count > 0;
    }

    public boolean deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = InventoryTable.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};
        int deletedRows = db.delete(InventoryTable.TABLE_NAME, selection, selectionArgs);
        return deletedRows > 0;
    }

    // User authentication methods

    public boolean addUser(String username, String password) {
        if (usernameExists(username)) {
            return false;
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UsersTable.COLUMN_USERNAME, username);
        values.put(UsersTable.COLUMN_PASSWORD, password);
        long newRowId = db.insert(UsersTable.TABLE_NAME, null, values);
        return newRowId != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = UsersTable.COLUMN_USERNAME + " = ? AND " +
                UsersTable.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(UsersTable.TABLE_NAME, null,
                selection, selectionArgs, null, null, null);
        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
    }

    public boolean usernameExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = UsersTable.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(UsersTable.TABLE_NAME, null,
                selection, selectionArgs, null, null, null);
        boolean usernameExists = cursor.getCount() > 0;
        cursor.close();
        return usernameExists;
    }
}

