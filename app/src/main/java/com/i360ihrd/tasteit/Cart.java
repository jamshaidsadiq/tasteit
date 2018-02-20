package com.i360ihrd.tasteit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i360ihrd.tasteit.Common.Config;
import com.i360ihrd.tasteit.Database.Database;
import com.i360ihrd.tasteit.Model.Order;
import com.i360ihrd.tasteit.ViewHolder.CartAdapter;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {
    private static final int PAYPAL_REQUEST_CODE = 9998;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


//        database = FirebaseDatabase.getInstance();
//        request  = database.getReference("Requests");

        //init paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);


        //init view

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace =  (FButton)findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = txtTotalPrice.getText().toString().replace("$","").replace(",","");
                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount),"USD","Taste It App Order",PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent1 = new Intent(getApplicationContext(), PaymentActivity.class);
                intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                intent1.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
                startActivityForResult(intent1,PAYPAL_REQUEST_CODE);

            }
        });
        
        loadListFood();
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == PAYPAL_REQUEST_CODE){
           if (resultCode == RESULT_OK){


               PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
               if(confirmation !=null){

                   try{
                       String paymentDetail = confirmation.toJSONObject().toString(4);
                       JSONObject jsonObject = new JSONObject(paymentDetail);
                       System.out.println(jsonObject);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }


               }

           }


       }
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //Calculate total price
        int total = 0;
        for(Order order:cart)
            total += (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));

    }
}
