package com.i360ihrd.tasteit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.i360ihrd.tasteit.Common.Common;
import com.i360ihrd.tasteit.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    EditText editPhone,editPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        Paper.init(this);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        // init firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait....!!!");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       mDialog.dismiss();
                       //Check If user exists in database

                         if(dataSnapshot.child(editPhone.getText().toString()).exists()){
                            // get User Information
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            user.setPhone(editPhone.getText().toString());

                            if(user.getPassword().equals(editPassword.getText().toString())){



                                Paper.book().write(Common.USER_KEY,editPhone.getText().toString());
                                Paper.book().write(Common.PWD_KEY,editPassword.getText().toString());


                                Intent intent = new Intent(SignIn.this,Home.class);
                                Common.currentUser = user;
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignIn.this,"Sign in failed",Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(SignIn.this,"User Not Exist",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        final DatabaseReference table_user = database.getReference("User");
//
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
//                mDialog.setMessage("Please Wait.......");
//                mDialog.show();
//
//
//                table_user.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        mDialog.dismiss();
//                        // check if user not exists in database
//
//                        if(dataSnapshot.child(editPhone.getText().toString()).exists()){
//
//                        // get user information
//
//                        User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
//
//                        if(user.getPassword().equals(editPassword.getText().toString())){
//
//                            Toast.makeText(SignIn.this,"Sign in successfully",Toast.LENGTH_SHORT).show();
//
//                        }else{
//                            Toast.makeText(SignIn.this,"Sign in failed",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }else{
//                            Toast.makeText(SignIn.this,"User Not Exist",Toast.LENGTH_SHORT).show();
//                        }}
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });

    }
}
