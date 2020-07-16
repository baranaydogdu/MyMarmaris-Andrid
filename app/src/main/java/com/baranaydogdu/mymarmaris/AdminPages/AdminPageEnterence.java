package com.baranaydogdu.mymarmaris.AdminPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baranaydogdu.mymarmaris.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminPageEnterence extends AppCompatActivity {

    ImageView   auth_icon;
    TextView    auth_tv,forget_tv;
    EditText    email_edx,password_edx;
    Button      sign_in_button,notifications_button,sign_out_button;
    ProgressBar progressBar;

    FirebaseAuth mauth;
    AlertDialog.Builder builder;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page_enterence);

        activity=this;
        initview();
        builder=new AlertDialog.Builder(this);
        mauth=FirebaseAuth.getInstance();

        if (mauth.getCurrentUser()!=null){

            System.out.println("mauth.getCurrentUser().getUid() : "+mauth.getCurrentUser().getUid());
            setAuthirezed();

        }else {

            set_UnAuthorized();
        }


        forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetpassword();
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sing_in();
            }
        });

        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_out();
            }
        });

        notifications_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NotificationCreate.class);
                startActivity(intent);
            }
        });







    }





    private void initview() {

        auth_icon               = findViewById(R.id.auth_icon);
        auth_tv                 = findViewById(R.id.auth_tv);

        email_edx               = findViewById(R.id.email_edx);
        password_edx            = findViewById(R.id.password_edx);
        forget_tv               = findViewById(R.id.forget_tv);

        sign_in_button          = findViewById(R.id.sign_in_button);
        sign_out_button         = findViewById(R.id.sign_out_button);

        notifications_button    = findViewById(R.id.notification_button);

        progressBar             = findViewById(R.id.progressBar);
    }

    private void set_UnAuthorized() {

        auth_icon.setImageResource(R.drawable.error_icon);
        auth_tv.setText("No Authorization");
        email_edx.setVisibility(View.VISIBLE);
        password_edx.setVisibility(View.VISIBLE);
        forget_tv.setVisibility(View.VISIBLE);
        sign_in_button.setVisibility(View.VISIBLE);

        notifications_button.setVisibility(View.INVISIBLE);
        sign_out_button.setVisibility(View.INVISIBLE);



    }

    private void setAuthirezed() {

        auth_icon.setImageResource(R.drawable.checkicon);
        auth_tv.setText("Authorized");
        email_edx.setVisibility(View.INVISIBLE);
        password_edx.setVisibility(View.INVISIBLE);
        forget_tv.setVisibility(View.INVISIBLE);
        sign_in_button.setVisibility(View.INVISIBLE);

        notifications_button.setVisibility(View.VISIBLE);
        sign_out_button.setVisibility(View.VISIBLE);

    }

    private  void sing_in(){

        if (email_edx.getText().toString().equals("") || password_edx.getText().toString().equals("")){

           setfailuremessage("Email and password text can not be emty.");

        } else {

            startsmth();

            mauth.signInWithEmailAndPassword(email_edx.getText().toString(),password_edx.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    finishsmth();
                    setsuccessmessage("Authorization Success");
                    setAuthirezed();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setfailuremessage(e.getLocalizedMessage());
                }
            });

        }
    }

    private void sign_out() {
        mauth.signOut();
        set_UnAuthorized();
    }

    private void forgetpassword() {

        if (email_edx.getText().toString().equals("")){

            setfailuremessage("Please input your email adress...");

        } else {

            startsmth();
            mauth.sendPasswordResetEmail(email_edx.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    finishsmth();
                    setsuccessmessage("Email reset link sended your email adress.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    setfailuremessage(e.getLocalizedMessage());
                }
            });
        }
    }

    private void setfailuremessage(String error) {

        finishsmth();
        builder.setIcon(R.drawable.error_icon);
        builder.setTitle("ERROR");
        builder.setMessage("ERROR : "+error);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton(null,null);
        builder.setCancelable(true).show();
    }

    private void setsuccessmessage(String msg) {

        builder.setIcon(R.drawable.checkicon);
        builder.setTitle("SUCCESS");
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton(null,null);
        builder.setCancelable(true).show();
    }

    private void startsmth(){

        email_edx               .setEnabled(false);
        password_edx            .setEnabled(false);
        forget_tv               .setEnabled(false);
        sign_in_button          .setEnabled(false);
        sign_out_button         .setEnabled(false);
        notifications_button    .setEnabled(false);
        progressBar             .setVisibility(View.VISIBLE);


    }

    private void finishsmth(){

        email_edx               .setEnabled(true);
        password_edx            .setEnabled(true);
        forget_tv               .setEnabled(true);
        sign_in_button          .setEnabled(true);
        sign_out_button         .setEnabled(true);
        notifications_button    .setEnabled(true);
        progressBar             .setVisibility(View.INVISIBLE);


    }





}
