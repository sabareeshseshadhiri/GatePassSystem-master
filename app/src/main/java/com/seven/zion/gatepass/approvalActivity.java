package com.seven.zion.gatepass;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class approvalActivity extends AppCompatActivity {

    private RecyclerView mFormList;
    DatabaseReference formReference;
    FirebaseUser user;
    FirebaseAuth mAuth;
    GatePassModel passModel;
    FirebaseRecyclerAdapter<GatePassModel,ItemViewHolder> recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        formReference = FirebaseDatabase.getInstance().getReference().child("GatePassRequests");
        mFormList = (RecyclerView)findViewById(R.id.formList);
        mFormList.setHasFixedSize(true);
        mFormList.setLayoutManager(new LinearLayoutManager(this));
        mFormList.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), mFormList, new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int pos) {

                         passModel = new GatePassModel();
                         final int position = pos;
                        recyclerAdapter.getRef(position).addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               passModel = dataSnapshot.getValue(GatePassModel.class);
                               Log.i("passModel",passModel.getName());
                               FragmentManager fragmentManager = getSupportFragmentManager();
                               Fragment fragment = fragmentManager.findFragmentByTag("Approve-dialog");
                               if (fragment !=null)
                                   fragmentManager.beginTransaction().remove(fragment).commit();
                               Log.i("ReferenceLul",recyclerAdapter.getRef(position).toString());
                               ApproveDialog approveDialog = new ApproveDialog();
                               Bundle data = new Bundle();
                               data.putString("reference",recyclerAdapter.getRef(position).toString().substring(56));
                               data.putString("name",passModel.getName());
                               data.putString("from",passModel.getFrom());
                               data.putString("to",passModel.getTo());
                               data.putString("dept",passModel.getDept());
                               data.putString("reason",passModel.getReason());
                               approveDialog.setArguments(data);
                               approveDialog.show(fragmentManager,"Approve-dialog");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
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
                startActivity(new Intent(approvalActivity.this,MainActivity.class));
                approvalActivity.this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        recyclerAdapter = new FirebaseRecyclerAdapter<GatePassModel, ItemViewHolder>(GatePassModel.class,
        R.layout.request_form_item,ItemViewHolder.class,formReference) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, GatePassModel model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setDept(model.getDept());
                viewHolder.setHodApproval(model.isHodApproved(),model.isHodDeclined());
                viewHolder.setStaffApproval(model.isStaffApproved(),model.isHodDeclined());

            }
        };
        mFormList.setAdapter(recyclerAdapter);
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View view;
        public ItemViewHolder(@NonNull View itemView)
        {
            super(itemView);
            view = itemView;
        }
        public void setName(String name)
        {
            TextView studentName = view.findViewById(R.id.studentName);
            studentName.setText(name);

        }
        public void setDept(String dept)
        {
            TextView department = view.findViewById(R.id.studentDept);
            department.setText(dept);
        }
        public void setStaffApproval(boolean staffApproval,boolean staffDeclined)
        {
            TextView staffApp = view.findViewById(R.id.FacultyApp);
            if (staffDeclined)
                staffApp.setTextColor(Color.RED);
           else if (staffApproval)
                staffApp.setTextColor(Color.GREEN);
        }
        public void setHodApproval(boolean hodApproval,boolean hodDeclined)
        {
            TextView hodApp = view.findViewById(R.id.hodApproval);
            if (hodDeclined)
                hodApp.setTextColor(Color.RED);
           else if (hodApproval)
                hodApp.setTextColor(Color.GREEN);
        }
    }
}
