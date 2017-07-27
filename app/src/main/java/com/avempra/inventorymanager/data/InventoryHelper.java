package com.avempra.inventorymanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avempra.inventorymanager.data.inventoryContract.inventoryEntry;

/**
 * Created by shres on 7/26/2017.
 */

public class InventoryHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="inventory.db";
    private static final int DATABASE_VERSION=1;

    public InventoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INVENTORY_TABLE="CREATE TABLE "+inventoryEntry.TABLE_NAME+" (" +
                inventoryEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                inventoryEntry.COLUMN_NAME+" TEXT NOT NULL," +
                inventoryEntry.COLUMN_DESC+" TEXT NOT NULL, " +
                inventoryEntry.COLUMN_COST+" REAL NOT NULL, " +
                inventoryEntry.COLUMN_MSRP+" REAL NOT NULL)";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
