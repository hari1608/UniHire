package com.example.unihire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.unihire.ui.ViewUniversityDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewJobInfoPage extends AppCompatActivity {

    Button applyBtn;
    TextView jobTitle,UnivName,Dept,Spec,jd;
    FirebaseAuth fAuth;
    DatabaseReference reff;
    String UnivId="";
    TextView applied;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_info_page);

        String JOBID = getIntent().getStringExtra("JOBID");
        applied=findViewById(R.id.appliedTextView);
        applied.setVisibility(View.INVISIBLE);
        applyBtn=findViewById(R.id.applyJobBtn);
        applyBtn.setVisibility(View.INVISIBLE);
        jobTitle=findViewById(R.id.jobTitleDisp);
        UnivName=findViewById(R.id.univDisp);
        Dept=findViewById(R.id.deptDisp);
        Spec=findViewById(R.id.specDisp);
        jd=findViewById(R.id.jdTextBox);
        fAuth=FirebaseAuth.getInstance();
        reff= FirebaseDatabase.getInstance().getReference();
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobTitle.setText(snapshot.child("Job").child(JOBID).child("JobTitle").getValue().toString());
                UnivId= snapshot.child("Job").child(JOBID).child("UnivId").getValue().toString();
                UnivName.setText(snapshot.child("University").child(UnivId).child("univName").getValue().toString());
                Dept.setText(snapshot.child("Job").child(JOBID).child("Department").getValue().toString());
                Spec.setText(snapshot.child("Job").child(JOBID).child("Specialization").getValue().toString());
                jd.setText(snapshot.child("Job").child(JOBID).child("JobDescription").getValue().toString());
                boolean found=false;
                for (DataSnapshot dataSnapshot : snapshot.child("Application").getChildren()){
                    if (dataSnapshot.child("ApplicantID").getValue().toString().equals(fAuth.getUid())
                    && dataSnapshot.child("JobID").getValue().toString().equals(JOBID)
                    ){
                        found=true;
                        applyBtn.setVisibility(View.VISIBLE);
                        applyBtn.setVisibility(View.GONE);
                        applied.setVisibility(View.VISIBLE);
                        break;
                    }
                }
                if(!found)
                    applyBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewJobInfoPage.this, ApplyJobFormPage.class);
                intent.putExtra("JOBID", JOBID);
                startActivity(intent);
            }
        });
        UnivName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewJobInfoPage.this, ViewUniversityDetails.class);
                intent.putExtra("UNIVID", UnivId);
                startActivity(intent);
            }
        });



    }
}