package com.avempra.inventorymanager;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avempra.inventorymanager.data.InventoryContract.InventoryEntry;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int LOADER_ID=2;
    private static final int REQUEST_IMAGE_CAPTURE=1;
    private final String TAG="DetailActivity";
    private ImageView mItemImageView;
    private EditText mName;
    private EditText mDesc;
    private EditText mCost;
    private EditText mMsrp;
    private EditText mQty;
    private Uri mItemUri;
    private boolean mItemHasChanged=false;
    String mCurrentPhotoPath;

    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dispatchTakePictureIntent(v);
        }
    };
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

        mItemImageView=(ImageView)findViewById(R.id.image_item_iv);
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
        mItemImageView.setOnClickListener(mClickListener);

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


    private File createImageFile() throws IOException{
        //Create an image file name
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoUri= FileProvider.getUriForFile(this,
                        "com.avempra.inventorymanager.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = mItemImageView.getWidth();
        int targetH = mItemImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mItemImageView.setImageBitmap(bitmap);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mItemImageView.setImageBitmap(imageBitmap);*/
            setPic();
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
        boolean nameIsEmpty=mName.getText().toString().isEmpty();
        boolean descIsEmpty=mDesc.getText().toString().isEmpty();
        boolean costIsEmpty=mCost.getText().toString().isEmpty();
        boolean msrpIsEmpty=mMsrp.getText().toString().isEmpty();
        boolean qtyIsEmpty=mQty.getText().toString().isEmpty();
        if(nameIsEmpty
                | descIsEmpty
                | costIsEmpty
                | msrpIsEmpty
                | qtyIsEmpty){
            Toast.makeText(this,"Some fields are empty",Toast.LENGTH_SHORT).show();
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
