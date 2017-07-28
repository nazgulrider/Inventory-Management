package com.avempra.inventorymanager.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avempra.inventorymanager.R;
import com.avempra.inventorymanager.data.inventoryContract.inventoryEntry;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by shres on 7/27/2017.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder> {

    private Cursor mCursor;
    Context mContext;

    public InventoryAdapter(Context context) {
        this.mContext=context;
    }

    @Override
    public InventoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_layout_rv,parent,false);
        return new InventoryHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryHolder holder, int position) {
        mCursor.moveToPosition(position);
        int itemNameIndex=mCursor.getColumnIndex(inventoryEntry.COLUMN_NAME);
        int msrpIndex=mCursor.getColumnIndex(inventoryEntry.COLUMN_MSRP);
        int quantityIndex=mCursor.getColumnIndex(inventoryEntry.COLUMN_QTY);

        holder.itemNameView.setText(mCursor.getString(itemNameIndex));
        holder.priceView.setText(mCursor.getString(msrpIndex));
        holder.quantityView.setText(mCursor.getString(quantityIndex));
    }

    @Override
    public int getItemCount() {
        if(mCursor==null){
            return 0;
        }
        else{
            return mCursor.getCount();
        }
    }

    public void swapCursor(Cursor newCursor){
        this.mCursor=newCursor;
        notifyDataSetChanged();
    }

    class InventoryHolder extends ViewHolder{
       // ImageView imageView;
        TextView itemNameView;
        TextView priceView;
        TextView quantityView;


        public InventoryHolder(View view) {
            super(view);
           // imageView=(ImageView) view.findViewById(R.id.item_iv);
            itemNameView=(TextView)view.findViewById(R.id.item_name_tv);
            priceView=(TextView)view.findViewById(R.id.msrp_tv);
            quantityView=(TextView)view.findViewById(R.id.quantity_tv);

        }
    }
}
