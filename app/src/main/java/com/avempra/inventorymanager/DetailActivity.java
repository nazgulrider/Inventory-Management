package com.avempra.inventorymanager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avempra.inventorymanager.data.InventoryContract.InventoryEntry;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int LOADER_ID=2;
    private final String TAG="DetailActivity";
    private EditText mName;
    private EditText mDesc;
    private EditText mCost;
    private EditText mMsrp;
    private EditText mQty;
    private Uri mItemUri;
    private boolean mItemHasChanged=false;
    private View.OnTouchListener mTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mItemHasChanged=true;
            return false;
        }
    };

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

        mName.setOnTouchListener(mTouchListener);
        mDesc.setOnTouchListener(mTouchListener);
        mCost.setOnTouchListener(mTouchListener);
        mMsrp.setOnTouchListener(mTouchListener);
        mQty.setOnTouchListener(mTouchListener);

        Intent intent=getIntent();
        mItemUri=intent.getData();

        if (mItemUri==null) {
            setTitle("Add Item");
            invalidateOptionsMenu();
        }else{
            setTitle("Edit Item");
            getSupportLoaderManager().initLoader(LOADER_ID,null,this);

        }

    }
    private void showUnsavedChangesDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Discard the changes and quit editing?")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dialog!=null)
                            dialog.dismiss();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(!mItemHasChanged){
            super.onBackPressed();
            return;
        }
        showUnsavedChangesDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(mItemUri==null){
            MenuItem menuItem = menu.findItem(R.id.delete_item_menu);
            menuItem.setVisible(false);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveItem();

                return true;
            case R.id.delete_item_menu:
                deleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if(!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                }else{
                    showUnsavedChangesDialog();
                }

                return true;
        }
        return true;
    }
    private void deleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete the item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem();
                        finish();
                    }
                })
                .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dialog!=null){
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    private void deleteItem() {
        if (mItemUri!=null) {
            int numberOfRowsDeleted=getContentResolver().delete(mItemUri,null,null);
            Toast.makeText(this,numberOfRowsDeleted+" rows deleted",Toast.LENGTH_SHORT).show();
        }

    }

    private void saveItem(){
        if(!mItemHasChanged){
            Toast.makeText(this,"Nothing to save",Toast.LENGTH_SHORT).show();
            return;
        }
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

        if(mItemUri==null){
            Uri resultUri=getContentResolver().insert(InventoryEntry.CONTENT_URI,cv);
            long id= Long.parseLong(resultUri.getLastPathSegment());
            if(id==-1){
                Toast.makeText(this,"Error inserting item",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"item inserted with id "+id,Toast.LENGTH_SHORT).show();
            }
        }else{
            long id=getContentResolver().update(mItemUri,cv,null,null);
            if(id==-1){
                Toast.makeText(this,"Error inserting item",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"item updated with id "+id,Toast.LENGTH_SHORT).show();
            }
        }
        finish();
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
