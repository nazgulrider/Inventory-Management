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

import com.avempra.inventorymanager.data.inventoryContract.inventoryEntry;
/**
 * Created by shres on 7/27/2017.
 */

public class InventoryProvider extends ContentProvider {
    private InventoryHelper mInventoryHelper;

    private static final int INVENTORY=100;
    private static final int INVENTORY_ID=101;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(inventoryContract.CONTENT_AUTHORITY,inventoryContract.PATH_INVENTORY,INVENTORY);
        sUriMatcher.addURI(inventoryContract.CONTENT_AUTHORITY,inventoryContract.PATH_INVENTORY+"/#",INVENTORY_ID);
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
                cursor=db.query(inventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case INVENTORY_ID:
                selection=inventoryEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(inventoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
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
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
