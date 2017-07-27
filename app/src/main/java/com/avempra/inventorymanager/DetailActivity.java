package com.avempra.inventorymanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.avempra.inventorymanager.data.InventoryHelper;
import com.avempra.inventorymanager.data.inventoryContract;

public class DetailActivity extends AppCompatActivity {

    private InventoryHelper mInventoryHelper;
    private EditText mName;
    private EditText mDesc;
    private EditText mCost;
    private EditText mMsrp;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mInventoryHelper=new InventoryHelper(this);

        mName=(EditText)findViewById(R.id.item_name_edit_text);
        mDesc=(EditText)findViewById(R.id.desc_edit_text);
        mCost=(EditText)findViewById(R.id.cost_edit_text);
        mMsrp=(EditText)findViewById(R.id.msrp_edit_text);

        mDb=mInventoryHelper.getWritableDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save_menu){
            String itemName=mName.getText().toString();
            String desc=mDesc.getText().toString();
            Double cost=Double.parseDouble(mCost.getText().toString());
            Double msrp=Double.parseDouble(mMsrp.getText().toString());

            ContentValues cv=new ContentValues();
            cv.put(inventoryContract.inventoryEntry.COLUMN_NAME,itemName);
            cv.put(inventoryContract.inventoryEntry.COLUMN_DESC,desc);
            cv.put(inventoryContract.inventoryEntry.COLUMN_COST,cost);
            cv.put(inventoryContract.inventoryEntry.COLUMN_MSRP,msrp);

            mDb.insert(inventoryContract.inventoryEntry.TABLE_NAME,null,cv);
            finish();
        }
        return true;
    }
}
