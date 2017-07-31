package com.avempra.inventorymanager;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.avempra.inventorymanager.data.InventoryAdapter;
import com.avempra.inventorymanager.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,InventoryAdapter.OnClickHandler {
    private InventoryAdapter mAdapter;
    private final int LOADER_ID=1;
    private RecyclerView mRecyclerView;
    private final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter=new InventoryAdapter(this, this);
        mRecyclerView=(RecyclerView)findViewById(R.id.main_rv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);


        getSupportLoaderManager().initLoader(LOADER_ID,null,this);

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, InventoryEntry.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        Log.e(TAG, "onLoadFinished called " );
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        switch (itemId){
            case R.id.delete_all_menu:
                showDeleteConfirmationDialog();
                return true;
            case R.id.get_data:
                Toast.makeText(this,"HA HA HA",Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setMessage("Are you out of your mind???")
                .setPositiveButton("Watch it bro!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAll();
                    }
                })
                .setNegativeButton("Let me re-think this..", new DialogInterface.OnClickListener() {
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

    private void deleteAll() {
        int numberOfRowsDeleted=getContentResolver().delete(InventoryEntry.CONTENT_URI,null,null);
        Toast.makeText(this,numberOfRowsDeleted+" rows deleted..hope you're happy..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int inventoryId) {
        Uri uri= ContentUris.withAppendedId(InventoryEntry.CONTENT_URI,inventoryId);

        Intent edit=new Intent(MainActivity.this,DetailActivity.class);
        edit.setData(uri);

        startActivity(edit);

    }

}
