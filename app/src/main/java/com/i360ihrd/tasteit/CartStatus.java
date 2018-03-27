package com.i360ihrd.tasteit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i360ihrd.tasteit.Common.Common;
import com.i360ihrd.tasteit.Interface.ItemClickListener;
import com.i360ihrd.tasteit.Model.Request;
import com.i360ihrd.tasteit.ViewHolder.OrderViewHolder;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class CartStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_status);


        //init firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        loadOrders();
        
    }

    private void loadOrders() {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,R.layout.order_layout,OrderViewHolder.class,requests.orderByChild("phone").equalTo(Common.currentUser.getPhone())) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {
                orderViewHolder.txtOderId.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderAddress.setText((request.getAddress()));
                orderViewHolder.txtOrderPhone.setText(request.getPhone());
                orderViewHolder.txtOrderStatus.setText(Common.convertCode(request.getStatus()));

                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public
                    void onClick(View view, int position, boolean isLongClick) {

                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public
    boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){

        }
        return super.onContextItemSelected(item);
    }

    private
    void showUpdateDialog(final String key, final Request item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please Choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        View update_order_layout = inflater.inflate(R.layout.update_order_layout,null);

        spinner = (MaterialSpinner)update_order_layout.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On my way","Shipped");

        alertDialog.setView(update_order_layout);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requests.child(key).setValue(item);

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

alertDialog.show();
    }




}
