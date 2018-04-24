package com.i360ihrd.tasteit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView txtForgotPwd;

     FirebaseDatabase database;
     DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        txtForgotPwd = (TextView)findViewById(R.id.txtForgotPwd);


        txtForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View v) {
                showForgotPwdDialog();
            }
        });


        Paper.init(this);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        // init firebase

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

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

    private
    void showForgotPwdDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignIn.this);
        alertDialog.setTitle("Forgot Password");
        alertDialog.setMessage("Enter your secure code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password_layout,null);

        alertDialog.setView(forgot_view);
        alertDialog.setIcon(R.drawable.ic_security_black_24dp);

       final MaterialEditText edtPhone = (MaterialEditText) forgot_view.findViewById(R.id.editPhone);
       final MaterialEditText edtSecureCode = (MaterialEditText) forgot_view.findViewById(R.id.editSecureCode);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {
            // check if user avaiable
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()){
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);

                            if(user.getSecureCode().equals(edtSecureCode.getText().toString())){

                                Toast.makeText(SignIn.this, "Your password : "+user.getPassword(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(SignIn.this, "Wrong secure code ...!!!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignIn.this, "Wrong Phone Number ...!!!", Toast.LENGTH_SHORT).show();
                        }





                    }

                    @Override
                    public
                    void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public
            void onClick(DialogInterface dialog, int which) {



            }
        });

        alertDialog.show();

    }
}
