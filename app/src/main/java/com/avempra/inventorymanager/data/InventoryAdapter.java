package com.avempra.inventorymanager.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avempra.inventorymanager.R;
import com.avempra.inventorymanager.data.InventoryContract.InventoryEntry;
import com.bumptech.glide.Glide;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by shres on 7/27/2017.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryHolder> {

    private Cursor mCursor;
    Context mContext;
    private OnClickHandler mOnClickHandler;


    public InventoryAdapter(Context context,OnClickHandler onClickHandler) {
        this.mContext=context;
        this.mOnClickHandler=onClickHandler;
    }

    @Override
    public InventoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_layout_rv,parent,false);
        return new InventoryHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryHolder holder, int position) {
        mCursor.moveToPosition(position);
        int itemNameIndex=mCursor.getColumnIndex(InventoryEntry.COLUMN_NAME);
        int msrpIndex=mCursor.getColumnIndex(InventoryEntry.COLUMN_MSRP);
        int quantityIndex=mCursor.getColumnIndex(InventoryEntry.COLUMN_QTY);
        int imgLinkIndex=mCursor.getColumnIndex(InventoryEntry.COLUMN_IMGLNK);
        String path=mCursor.getString(imgLinkIndex);
        if (path!=null) {
            Glide.with(mContext)
                    .load(path)
                    .into(holder.thumbnailView);
        } else {
            Glide.with(mContext).clear(holder.thumbnailView);
            holder.thumbnailView.setImageDrawable(null);
        }

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

    class InventoryHolder extends ViewHolder implements View.OnClickListener{
       // ImageView imageView;
        TextView itemNameView;
        TextView priceView;
        TextView quantityView;
        ImageView thumbnailView;


        public InventoryHolder(View view) {
            super(view);
            itemNameView=(TextView)view.findViewById(R.id.item_name_tv);
            priceView=(TextView)view.findViewById(R.id.msrp_tv);
            quantityView=(TextView)view.findViewById(R.id.quantity_tv);
            thumbnailView=(ImageView)view.findViewById(R.id.item_iv);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            int inventoryId;
                mCursor.moveToPosition(position);
                int idIndex=mCursor.getColumnIndex(InventoryEntry._ID);
                inventoryId = mCursor.getInt(idIndex);
            mOnClickHandler.onClick(inventoryId);
        }
    }
    public interface OnClickHandler{
        void onClick(int inventoryId);

    }
}
