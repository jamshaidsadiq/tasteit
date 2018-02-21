package com.i360ihrd.tasteit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i360ihrd.tasteit.Common.Common;
import com.i360ihrd.tasteit.Model.Request;
import com.i360ihrd.tasteit.ViewHolder.OrderViewHolder;

public class CartStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

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
                orderViewHolder.txtOrderStatus.setText(convertCode(request.getStatus()));

            }
        };

        recyclerView.setAdapter(adapter);
    }

    private String convertCode(String status) {
        if(status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "On my way";
        else
            return "Shipped";
    }


}
