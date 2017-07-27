package com.avempra.inventorymanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avempra.inventorymanager.data.InventoryHelper;
import com.avempra.inventorymanager.data.inventoryContract;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getData(){
        InventoryHelper inventoryHelper=new InventoryHelper(this);
        SQLiteDatabase db=inventoryHelper.getReadableDatabase();
        Cursor cursor=db.query(inventoryContract.inventoryEntry.TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();
        int idIndex=cursor.getColumnIndex(inventoryContract.inventoryEntry._ID);
        int idName=cursor.getColumnIndex(inventoryContract.inventoryEntry.COLUMN_NAME);
        int idDesc=cursor.getColumnIndex(inventoryContract.inventoryEntry.COLUMN_DESC);
        int idCost=cursor.getColumnIndex(inventoryContract.inventoryEntry.COLUMN_COST);
        int idMsrp=cursor.getColumnIndex(inventoryContract.inventoryEntry.COLUMN_MSRP);

        TextView mainView=(TextView)findViewById(R.id.hello_world_tv);
        mainView.setText(cursor.getString(idIndex)+cursor.getString(idName)+cursor.getString(idDesc)+cursor.getDouble(idCost)+cursor.getDouble(idMsrp));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(R.id.get_data==item.getItemId()){
            getData();
        }
        return true;
    }
}
