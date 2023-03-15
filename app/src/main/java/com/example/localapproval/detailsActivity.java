package com.example.localapproval;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localapproval.prevalent.constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class detailsActivity extends AppCompatActivity {

    private Button addbtn,giveapproval;
    private EditText newamount;
    private TextView totalearnings,e_balance;
    private String deliveryID;
    private DatabaseReference delivery_account;
    private Double totEarings , eBal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        addbtn = findViewById(R.id.addbtn);
        giveapproval = findViewById(R.id.give_approval);
        newamount = findViewById(R.id.new_amount);
        totalearnings = findViewById(R.id.total_earnings);
        e_balance = findViewById(R.id.e_balance);

        deliveryID = getIntent().getStringExtra("deliveryId");

        delivery_account = FirebaseDatabase.getInstance().getReference().child("delivery_account");
        giveapproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(detailsActivity.this);
                builder.setPositiveButton("give approval", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delivery_account.child(deliveryID).child("approval").setValue("approved");

                    }
                });
                builder.setNegativeButton("cancel approval", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delivery_account.child(deliveryID).child("approval").setValue("not approved");
                    }
                });
                builder.show();
            }
        });

        delivery_account.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totEarings = Double.parseDouble(dataSnapshot.child(deliveryID).child("earnings").getValue().toString());
                eBal = Double.parseDouble(dataSnapshot.child(deliveryID).child("wallet").getValue().toString());
                totalearnings.setText("total earnings : "+ constants.notation+totEarings);
                e_balance.setText("e-wallet : "+constants.notation+eBal);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newamount.getText().toString().trim().isEmpty()){
                    Toast.makeText(detailsActivity.this,"enter recharge amount",Toast.LENGTH_SHORT).show();
                }else {
                    savechangestoDataBase();
                }
            }
        });

    }

    private void savechangestoDataBase() {
        Double finalwallet = eBal+Double.parseDouble(newamount.getText().toString().trim());
        delivery_account.child(deliveryID).child("wallet").setValue(String.valueOf(finalwallet));
        Toast.makeText(detailsActivity.this,"added",Toast.LENGTH_SHORT).show();
    }
}
