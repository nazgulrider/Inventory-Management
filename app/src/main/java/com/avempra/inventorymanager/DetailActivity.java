package com.avempra.inventorymanager;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.avempra.inventorymanager.data.inventoryContract.inventoryEntry;

public class DetailActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mDesc;
    private EditText mCost;
    private EditText mMsrp;
    private EditText mQty;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mName=(EditText)findViewById(R.id.item_name_edit_text);
        mDesc=(EditText)findViewById(R.id.desc_edit_text);
        mCost=(EditText)findViewById(R.id.cost_edit_text);
        mMsrp=(EditText)findViewById(R.id.msrp_edit_text);
        mQty=(EditText)findViewById(R.id.quantity_edit_text);

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
            Integer qty=Integer.parseInt(mQty.getText().toString());

            ContentValues cv=new ContentValues();
            cv.put(inventoryEntry.COLUMN_NAME,itemName);
            cv.put(inventoryEntry.COLUMN_DESC,desc);
            cv.put(inventoryEntry.COLUMN_COST,cost);
            cv.put(inventoryEntry.COLUMN_MSRP,msrp);
            cv.put(inventoryEntry.COLUMN_QTY,qty);

            Uri resultUri=getContentResolver().insert(inventoryEntry.CONTENT_URI,cv);
            long id= Long.parseLong(resultUri.getLastPathSegment());
            if(id==-1){
                Toast.makeText(this,"Error inserting item",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"item inserted with id "+id,Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        return true;
    }
}
