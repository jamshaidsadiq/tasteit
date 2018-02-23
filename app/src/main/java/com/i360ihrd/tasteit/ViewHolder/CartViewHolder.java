package com.i360ihrd.tasteit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.i360ihrd.tasteit.R;

/**
 * Created by Jamshaid on 22-02-2018.
 */

public class CartViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_cart_name,txt_price;
    public ImageView img_cart_count;




    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_price);
        img_cart_count = (ImageView) itemView.findViewById(R.id.cart_item_count);

    }
}
