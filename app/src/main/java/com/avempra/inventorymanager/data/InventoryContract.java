package com.avempra.inventorymanager.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shres on 7/26/2017.
 */

public class InventoryContract {
    private InventoryContract() {
    }
    public static final String SCHEME="content://";
    public static final String CONTENT_AUTHORITY="com.avempra.inventorymanager";
    public static final Uri BASE_CONTENT_URI=Uri.parse(SCHEME+CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY="inventory";

    public static final class InventoryEntry implements BaseColumns{

        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_INVENTORY);
        public static final String TABLE_NAME="inventory";
        public static final String COLUMN_NAME="item";
        public static final String COLUMN_DESC="description";
        public static final String COLUMN_COST="cost";
        public static final String COLUMN_MSRP="msrp";
        public static final String COLUMN_QTY="quantity";
        public static final String COLUMN_IMGLNK="imagelocation";

    }
}
