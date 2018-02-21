package com.i360ihrd.tasteit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.i360ihrd.tasteit.Interface.ItemClickListener;
import com.i360ihrd.tasteit.R;

/**
 * Created by Jamshaid on 21-02-2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtOderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress = (TextView)itemView.findViewById(R.id.order_address);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOderId = (TextView)itemView.findViewById(R.id.order_id);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
          itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
