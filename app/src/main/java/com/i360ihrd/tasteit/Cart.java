package com.i360ihrd.tasteit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i360ihrd.tasteit.Common.Common;
import com.i360ihrd.tasteit.Common.Config;
import com.i360ihrd.tasteit.Database.Database;
import com.i360ihrd.tasteit.Model.Order;
import com.i360ihrd.tasteit.Model.Request;
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
    private static final int PAYPAL_REQUEST_CODE = 9999;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    String address;

    static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        database = FirebaseDatabase.getInstance();
        requests  = database.getReference("Requests");

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

                showAlertDialog();

            }
        });
        
        loadListFood();
        
    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Enter your address: ");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT

        );

        edtAddress.setLayoutParams(layoutParams);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                address = edtAddress.getText().toString();

                String amount = txtTotalPrice.getText().toString().replace("$","").replace(",","");
                PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(amount),"USD","Taste It App Order",PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent1 = new Intent(getApplicationContext(), PaymentActivity.class);
                intent1.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                intent1.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
                startActivityForResult(intent1,PAYPAL_REQUEST_CODE);



            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

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
                       Request request = new Request(
                               Common.currentUser.getPhone(),
                               Common.currentUser.getName(),
                               address,txtTotalPrice.getText().toString(),
                               "0",
                               jsonObject.getJSONObject("response").getString("state")
                               ,cart);
                       requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                       //delete cart
                       new Database(getBaseContext()).ClearCart();
                       Toast.makeText(Cart.this,"Thank you , Order Placed",Toast.LENGTH_SHORT);

                       finish();

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }


               }

           }else if(resultCode== Activity.RESULT_CANCELED){
               Toast.makeText(this, "Payment cancel", Toast.LENGTH_SHORT).show();
               
           }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
               Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
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
