package com.avempra.inventorymanager.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shres on 7/26/2017.
 */

public class inventoryContract {
    private inventoryContract() {
    }
    public static final String SCHEME="content://";
    public static final String CONTENT_AUTHORITY="com.avempra.inventorymanager";
    public static final Uri BASE_CONTENT_URI=Uri.parse(SCHEME+CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY="inventory";

    public static final class inventoryEntry implements BaseColumns{
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_INVENTORY);



    }
}
