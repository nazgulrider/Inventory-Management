package com.avempra.inventorymanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avempra.inventorymanager.data.InventoryContract.InventoryEntry;

/**
 * Created by shres on 7/26/2017.
 */

public class InventoryHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="inventory.db";
    private static final int DATABASE_VERSION=4;

    public InventoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_INVENTORY_TABLE="CREATE TABLE "+ InventoryEntry.TABLE_NAME+" (" +
                InventoryEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InventoryEntry.COLUMN_NAME+" TEXT NOT NULL," +
                InventoryEntry.COLUMN_DESC+" TEXT NOT NULL, " +
                InventoryEntry.COLUMN_COST+" REAL NOT NULL, " +
                InventoryEntry.COLUMN_MSRP+" REAL NOT NULL, " +
                InventoryEntry.COLUMN_QTY+" INTEGER NOT NULL, " +
                InventoryEntry.COLUMN_IMGLNK+" TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ InventoryEntry.TABLE_NAME);
        onCreate(db);
    }
}
