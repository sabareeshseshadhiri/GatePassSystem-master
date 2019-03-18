package com.seven.zion.gatepass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    Button loginB;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference meReference;
    ProgressDialog progressDialog;
    memberModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginB = findViewById(R.id.Login);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        firebaseUser = mAuth.getCurrentUser();
        meReference = FirebaseDatabase.getInstance().getReference().child("Users");
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressDialog.setTitle("Loading");
                mAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                validate();
                                progressDialog.dismiss();
                            }
                        });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        validate();
    }

    private void validate() {
        firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser !=null) {
            String userEmail = firebaseUser.getEmail().replace(".", "dot");
            meReference.child(userEmail).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    model = new memberModel();
                    model = dataSnapshot.getValue(memberModel.class);
                    Toast.makeText(MainActivity.this, model.getMemberType(), Toast.LENGTH_LONG).show();
                    if(model.getMemberType().equals("hod")||model.getMemberType().equals("staff")) {
                        startActivity(new Intent(MainActivity.this, approvalActivity.class));
                        MainActivity.this.finish();
                    }
                    else {
                        startActivity(new Intent(MainActivity.this, gatePassCreation.class));
                        MainActivity.this.finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
