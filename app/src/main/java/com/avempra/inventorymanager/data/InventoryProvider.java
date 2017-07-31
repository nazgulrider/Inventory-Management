package com.avempra.inventorymanager.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.avempra.inventorymanager.data.InventoryContract.InventoryEntry;

/**
 * Created by shres on 7/27/2017.
 */

public class InventoryProvider extends ContentProvider {
    private InventoryHelper mInventoryHelper;

    private static final int INVENTORY=100;
    private static final int INVENTORY_ID=101;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY,INVENTORY);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY+"/#",INVENTORY_ID);
    }

    @Override
    public boolean onCreate() {
        mInventoryHelper=new InventoryHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase db=mInventoryHelper.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                cursor=db.query(InventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case INVENTORY_ID:
                selection= InventoryEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(InventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("cannot query unknown uri "+uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //insert returns id (datatype-long) for the item that was inserted to the database
        long newId=0;
        SQLiteDatabase db=mInventoryHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                newId=db.insert(InventoryEntry.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported for "+uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,newId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numberOfRowsDeleted;
        SQLiteDatabase db=mInventoryHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                numberOfRowsDeleted=db.delete(InventoryEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case INVENTORY_ID:
                selection=InventoryEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                numberOfRowsDeleted=db.delete(InventoryEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update Failed");
        }
        if(numberOfRowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return numberOfRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int id;
        SQLiteDatabase db=mInventoryHelper.getWritableDatabase();
        int match=sUriMatcher.match(uri);
        switch (match){
            case INVENTORY:
                id=db.update(InventoryEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case INVENTORY_ID:
                selection=InventoryEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                id=db.update(InventoryEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Update Failed");
        }
        if(id!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return id;
    }
}
