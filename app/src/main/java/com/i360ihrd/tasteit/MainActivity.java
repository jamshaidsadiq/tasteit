package com.i360ihrd.tasteit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.i360ihrd.tasteit.Common.Common;
import com.i360ihrd.tasteit.Model.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnSignUp;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

       // printkeyHash();


        Paper.init(this);


        String username = Paper.book().read(Common.USER_KEY);
        String Password = Paper.book().read(Common.PWD_KEY);

        if(username !=null && Password !=null){
            if(!username.isEmpty() && !Password.isEmpty()){

                login(username,Password);
            }
        }


        // set onclick listener

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent siginin = new Intent(MainActivity.this,SignIn.class);
                startActivity(siginin);


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siginup = new Intent(MainActivity.this,SignUp.class);
                startActivity(siginup);
            }
        });
    }

    private
    void printkeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.i360ihrd.tasteit",PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private
    void login(final String username, final String password) {
        
        if(Common.isConnectTointernet(getBaseContext())){

        FirebaseApp.initializeApp(MainActivity.this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please Wait....!!!");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                //Check If user exists in database

                if(dataSnapshot.child(username).exists()){
                    // get User Information
                    User user = dataSnapshot.child(username).getValue(User.class);
                    user.setPhone(username);

                    if(user.getPassword().equals(password)){




                        Intent intent = new Intent(MainActivity.this,Home.class);
                        Common.currentUser = user;
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this,"Sign in failed",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this,"User Not Exist",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }else{
            Toast.makeText(this, "Please Check your Connection..!!!", Toast.LENGTH_SHORT).show();
        }
    
    
    }
}
