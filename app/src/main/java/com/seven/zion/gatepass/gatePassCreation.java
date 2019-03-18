package com.seven.zion.gatepass;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class gatePassCreation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int DATE_PICKER = 101 ;
    int year,month,day,pickerId;
    EditText Name,Dept,From,To,Reason;
    Button submitButton;
    FirebaseUser user;
    FirebaseAuth mAuth;
    GatePassModel gatePassModel;
    DatabaseReference mReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gate_pass_creation);
        progressDialog = new ProgressDialog(this);
        Name = findViewById(R.id.Name);
        Dept = findViewById(R.id.Dept);
        From = findViewById(R.id.from);
        To = findViewById(R.id.to);
        Reason = findViewById(R.id.reason);
        submitButton = findViewById(R.id.SubmitB);
        mReference = FirebaseDatabase.getInstance().getReference().child("GatePassRequests");
        From.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    selectDate(R.id.from);
            }
        });
        To.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    selectDate(R.id.to);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Loading");
                progressDialog.show();
                gatePassModel = new GatePassModel(Name.getText().toString(),Dept.getText().toString(),
                        From.getText().toString(),To.getText().toString(),Reason.getText().toString()
                ,false,false,false,false);
                mReference.push().setValue(gatePassModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(gatePassCreation.this,"Success",Toast.LENGTH_LONG).show();
                        //gatePassCreation.this.finish();
                    }
                });
            }
        });
    }

    public void selectDate(int id){
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(DATE_PICKER);
        pickerId = id;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case DATE_PICKER :
                return new DatePickerDialog(this,dateSetListener,year,month,day);
        }
        return super.onCreateDialog(id);
    }


    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            switch (pickerId) {
                case R.id.from:
                From.setText(new StringBuilder().append(dayOfMonth).append("-").append(month).append("-")
                        .append(year));
                     break;
                case R.id.to:
                    To.setText(new StringBuilder().append(dayOfMonth).append("-").append(month).append("-")
                            .append(year));
                    break;
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutB)
        {
            if (user!=null)
            {
             mAuth.signOut();
             startActivity(new Intent(gatePassCreation.this,MainActivity.class));
             gatePassCreation.this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gatePassCreation.this.finish();
    }
}
