package com.seven.zion.gatepass;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApproveDialog extends DialogFragment {

    TextView Dfrom,Dto,Dname,Dreason,Ddept;
    Button approveB,declineB;
    DatabaseReference mReference;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    GatePassModel gatePassModel;
    private String UserType,ref;
    memberModel  model;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mReference = FirebaseDatabase.getInstance().getReference().child("GatePassRequests")
        .child(getArguments().getString("reference"));
        View view = inflater.inflate(R.layout.approve_dialog,container);
        Dfrom = view.findViewById(R.id.Dfrom);
        Dto = view.findViewById(R.id.DTo);
        Dname = view.findViewById(R.id.DstudentName);
        Ddept = view.findViewById(R.id.DstudentDept);
        Dreason = view.findViewById(R.id.DReason);
        approveB = view.findViewById(R.id.appoveB);
        declineB = view.findViewById(R.id.declineB);

       // ref=getArguments().getString("reference");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gatePassModel = new GatePassModel();
                gatePassModel = dataSnapshot.getValue(GatePassModel.class);
                Dfrom.setText(gatePassModel.getFrom());
                Dto.setText(gatePassModel.getTo());
                Dreason.setText(gatePassModel.getReason());
                Dname.setText(gatePassModel.getName());
                Ddept.setText(gatePassModel.getDept());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Log.i("Model",mReference.toString());
        approveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equals("hod"))
                {
                    gatePassModel.setHodApproved(true);
                    gatePassModel.setHodDeclined(false);
                    mReference.setValue(gatePassModel);
                    dismiss();
                }
                else
                {
                    gatePassModel.setStaffApproved(true);
                    gatePassModel.setStaffDeclined(false);
                    mReference.setValue(gatePassModel);
                    dismiss();
                }
            }

        });
        declineB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserType.equals("hod"))
                {
                    gatePassModel.setHodApproved(false);
                    gatePassModel.setHodDeclined(true);
                    mReference.setValue(gatePassModel);
                    dismiss();
                }
                else
                {
                    gatePassModel.setStaffDeclined(true);
                    gatePassModel.setStaffApproved(false);
                    mReference.setValue(gatePassModel);
                    dismiss();
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        String userEmail = firebaseUser.getEmail().replace(".", "dot");
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userEmail);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model = new memberModel();
                model = dataSnapshot.getValue(memberModel.class);
                UserType = model.memberType;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
