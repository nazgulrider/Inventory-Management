package com.avempra.inventorymanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.avempra.inventorymanager.data.InventoryContract.InventoryEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mName;
    private EditText mDesc;
    private EditText mCost;
    private EditText mMsrp;
    private EditText mQty;
    private Uri mItemUri;
    private final int LOADER_ID=2;
    private final String TAG="DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Log.e(TAG, "onCreate: called" );

        mName=(EditText)findViewById(R.id.item_name_edit_text);
        mDesc=(EditText)findViewById(R.id.desc_edit_text);
        mCost=(EditText)findViewById(R.id.cost_edit_text);
        mMsrp=(EditText)findViewById(R.id.msrp_edit_text);
        mQty=(EditText)findViewById(R.id.quantity_edit_text);

        Intent intent=getIntent();
        mItemUri=intent.getData();

        if (mItemUri==null) {
            setTitle("Add Item");
        }else{
            setTitle("Edit Item");
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveItem();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;
    }

    private void saveItem(){
        String itemName=mName.getText().toString();
        String desc=mDesc.getText().toString();
        Double cost=Double.parseDouble(mCost.getText().toString());
        Double msrp=Double.parseDouble(mMsrp.getText().toString());
        Integer qty=Integer.parseInt(mQty.getText().toString());

        ContentValues cv=new ContentValues();
        cv.put(InventoryEntry.COLUMN_NAME,itemName);
        cv.put(InventoryEntry.COLUMN_DESC,desc);
        cv.put(InventoryEntry.COLUMN_COST,cost);
        cv.put(InventoryEntry.COLUMN_MSRP,msrp);
        cv.put(InventoryEntry.COLUMN_QTY,qty);

        Uri resultUri=getContentResolver().insert(InventoryEntry.CONTENT_URI,cv);
        long id= Long.parseLong(resultUri.getLastPathSegment());
        if(id==-1){
            Toast.makeText(this,"Error inserting item",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"item inserted with id "+id,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,mItemUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor==null || cursor.getCount()<1){
            return;
        }
        cursor.moveToFirst();

            int nameIndex=cursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
            int descIndex=cursor.getColumnIndex(InventoryEntry.COLUMN_DESC);
            int costIndex=cursor.getColumnIndex(InventoryEntry.COLUMN_COST);
            int msrpIndex=cursor.getColumnIndex(InventoryEntry.COLUMN_MSRP);
            int qtyIndex=cursor.getColumnIndex(InventoryEntry.COLUMN_QTY);

            mName.setText(cursor.getString(nameIndex));
            mDesc.setText(cursor.getString(descIndex));
            mCost.setText(String.valueOf(cursor.getDouble(costIndex)));
            mMsrp.setText(String.valueOf(cursor.getDouble(msrpIndex)));
            mQty.setText(String.valueOf(cursor.getInt(qtyIndex)));



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");
        mDesc.setText("");
        mCost.setText("");
        mMsrp.setText("");
        mQty.setText("");
    }


}
